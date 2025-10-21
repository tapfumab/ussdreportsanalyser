package zw.co.netone.ussdreportsanalyser.security;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String shopId;

    public CustomAuthenticationToken(Object principal, Object credentials, String shopId) {
        super(principal, credentials);
        this.shopId = shopId;
        super.setAuthenticated(false);
    }

    public CustomAuthenticationToken(Object principal, Object credentials, String shopId,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.shopId = shopId;
        super.setAuthenticated(true); // must use super, as we override
    }

    public String getShopId() {

        return this.shopId;
    }
}
