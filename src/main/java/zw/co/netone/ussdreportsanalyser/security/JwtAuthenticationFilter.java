package zw.co.netone.ussdreportsanalyser.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import zw.co.netone.ussdreportsanalyser.model.Shop;
import zw.co.netone.ussdreportsanalyser.repository.ShopRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ShopRepository shopRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isValid(token, userDetails)) {
                String shopId = extractShopId(request);

                if (shopId != null && !shopId.isEmpty()) {
                    if (!validateUserShopAssociation(username, shopId)) {
                        log.error("User {} is not associated with shop {}", username, shopId);
                        response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                "User is not authorized for the specified shop");
                        return;
                    }
                }

                CustomAuthenticationToken authenticationToken = new CustomAuthenticationToken(
                        userDetails, null, shopId, userDetails.getAuthorities()
                );


                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractShopId(HttpServletRequest request) {

        String shopId = request.getHeader("X-Shop-Id");

        if (shopId == null || shopId.isEmpty()) {
            shopId = request.getParameter("shopId");
        }

        return shopId;
    }

    private boolean validateUserShopAssociation(String username, String shopId) {
        try {
            Shop shop = shopRepository.findByShopId(shopId);
            if (shop == null) {
                log.error("Shop with officeId {} not found", shopId);
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("Error validating user-shop association", e);
            return false;
        }
    }
}
