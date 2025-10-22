package zw.co.netone.ussdreportsanalyser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zw.co.netone.ussdreportsanalyser.dto.*;
import zw.co.netone.ussdreportsanalyser.model.Shop;
import zw.co.netone.ussdreportsanalyser.model.User;
import zw.co.netone.ussdreportsanalyser.repository.RoleRepository;
import zw.co.netone.ussdreportsanalyser.repository.ShopRepository;
import zw.co.netone.ussdreportsanalyser.repository.UserRepository;
import zw.co.netone.ussdreportsanalyser.security.CustomAuthenticationToken;
import zw.co.netone.ussdreportsanalyser.security.JwtService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String USER_ROLE = "USER";
    private static final String USER_SHOP = "SHOP";
    private static final String USER_NOT_FOUND = "User does not exist.";
    private static final String USER_DELETED = "User deleted successfully.";
    private static final String USER_UPDATED = "User updated successfully.";
    private static final String LOGIN_SUCCESSFUL = "Login successful.";
    private static final String LOGIN_FAILED = "Invalid username or password.";
    private static final String REGISTRATION_SUCCESSFUL = "User registered successfully.";
    private static final String USERNAME_EXISTS = "Username already exists.";
    private static final String EMAIL_EXISTS = "Email already exists.";
    private static final String CELL_EXISTS = "Cell number already exists.";
    private static final String USER_ROLE_REQUIRED = "User role cannot be empty.";
    private static final String USER_SHOP_REQUIRED = "User Shop cannot be empty.";
    private static final String NO_USERS_FOUND = "No users found.";


    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public AuthenticationResponse<String> authenticateUser(LoginRequest loginRequest) {
        try {
            log.info("Authentication attempt for user: {} with shopId: {}",
                    loginRequest.username(), loginRequest.shopId());

            Shop shop = null;
            if (loginRequest.shopId() != null && !loginRequest.shopId().isEmpty()) {
                shop = shopRepository.findByShopId(loginRequest.shopId());
                if (shop == null) {
                    log.error("Shop with officeId {} not found", loginRequest.shopId());
                    throw new RuntimeException("Invalid shop specified");
                }
            }

            if (shop != null) {
                Optional<User> user = userRepository.findByUsernameAndShop(loginRequest.username(), shop);
                if (user.isEmpty()) {
                    log.error("User {} not found for shop {}", loginRequest.username(), loginRequest.shopId());
                    throw new RuntimeException("Invalid credentials. User not authorized for specified shop");
                }
            } else {
                authenticationManager.authenticate(
                        new CustomAuthenticationToken(
                                loginRequest.username(),
                                loginRequest.password(),
                                loginRequest.shopId()
                        )
                );
            }

            User user = userRepository.findByUsername(loginRequest.username())
                    .orElseThrow(() -> new BadCredentialsException(LOGIN_FAILED));

            String token = jwtService.generateToken(user);
            log.info("Authentication successful for user: {}", loginRequest.username());

            return new AuthenticationResponse<>(true, LOGIN_SUCCESSFUL, token);
        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for user: {}", loginRequest.username());
            return new AuthenticationResponse<>(false, LOGIN_FAILED, null);
        }
    }


    @Override
    public AuthenticationResponse<?> registerUser(RegisterUserRequest registrationRequest) {
        AuthenticationResponse<?> validation = validateUser(registrationRequest);
        if (!validation.isSuccess()) {
            log.warn("User registration validation failed: {}", validation.getMessage());
            return validation;
        }

        try {

            User user = User.builder()
                    .firstName(registrationRequest.firstName())
                    .lastName(registrationRequest.lastName())
                    .email(registrationRequest.email())
                    .username(registrationRequest.username())
                    .shop(shopRepository.findByShopId(USER_SHOP))
                    .cellNumber(registrationRequest.cellNumber())
                    .role(roleRepository.findByName(USER_ROLE))
                    .activeStatus(true)
                    .build();

            userRepository.save(user);
            log.info("User registered successfully: {}", registrationRequest.username());

            return new AuthenticationResponse<>(true, REGISTRATION_SUCCESSFUL, null);

        } catch (Exception e) {
            log.error("Error registering user: {}", registrationRequest.username(), e);
            return new AuthenticationResponse<>(false, "Failed to register user. Please try again.", null);
        }
    }

    @Override
    public AuthenticationResponse<?> editUser(RegisterUserRequest registrationRequest, Long id) {
        if (registrationRequest.role() == null || registrationRequest.role().isBlank()) {
            return new AuthenticationResponse<>(false, USER_ROLE_REQUIRED, null);
        }
        if (registrationRequest.shop() == null || registrationRequest.shop().isBlank()) {
            return new AuthenticationResponse<>(false, USER_SHOP_REQUIRED, null);
        }

        try {

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));

            user.setFirstName(registrationRequest.firstName());
            user.setLastName(registrationRequest.lastName());
            user.setEmail(registrationRequest.email());
            user.setUsername(registrationRequest.username());
            user.setCellNumber(registrationRequest.cellNumber());
            user.setShop(shopRepository.findByShopId(registrationRequest.shop().trim().toUpperCase()));
            user.setRole(roleRepository.findByName(registrationRequest.role().trim().toUpperCase()));

            userRepository.save(user);
            log.info("User updated successfully: {}", id);

            return new AuthenticationResponse<>(true, USER_UPDATED, null);

        } catch (IllegalArgumentException e) {
            log.warn("Failed to update user - user not found: {}", id);
            return new AuthenticationResponse<>(false, USER_NOT_FOUND, null);
        } catch (Exception e) {
            log.error("Error updating user: {}", id, e);
            return new AuthenticationResponse<>(false, "Failed to update user. Please try again.", null);
        }
    }

    @Override
    public ApiResponse<String> deleteUser(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));

            user.setActiveStatus(false);
            userRepository.save(user);

            log.info("User deleted successfully: {}", id);
            return new ApiResponse<>(true, USER_DELETED, null);

        } catch (IllegalArgumentException e) {
            log.warn("Failed to delete user - user not found: {}", id);
            return new ApiResponse<>(false, USER_NOT_FOUND, null);
        } catch (Exception e) {
            log.error("Error deleting user: {}", id, e);
            return new ApiResponse<>(false, "Failed to delete user. Please try again.", null);
        }
    }

    @Override
    public ApiResponse<List<UserResponse>> findAllUsers() {
        try {
            List<User> users = userRepository.findAllByActiveStatusTrue();

            if (users.isEmpty()) {
                log.info("No active users found in the system.");
                return new ApiResponse<>(false, NO_USERS_FOUND, null);
            }

            List<UserResponse> userResponses = users.stream()
                    .map(this::mapToUserResponse)
                    .collect(Collectors.toList());

            log.info("Retrieved {} users from database.", userResponses.size());
            return new ApiResponse<>(true, "Users retrieved successfully.", userResponses);

        } catch (Exception e) {
            log.error("Error retrieving users.", e);
            return new ApiResponse<>(false, "Failed to retrieve users.", null);
        }
    }

    @Override
    public ApiResponse<UserResponse> findUserById(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));

            UserResponse userResponse = mapToUserResponse(user);
            log.info("User retrieved successfully: {}", id);

            return new ApiResponse<>(true, "User found.", userResponse);

        } catch (IllegalArgumentException e) {
            log.warn("User not found: {}", id);
            return new ApiResponse<>(false, USER_NOT_FOUND, null);
        } catch (Exception e) {
            log.error("Error retrieving user: {}", id, e);
            return new ApiResponse<>(false, "Failed to retrieve user.", null);
        }
    }

    private AuthenticationResponse<?> validateUser(RegisterUserRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            return new AuthenticationResponse<>(false, USERNAME_EXISTS, null);
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            return new AuthenticationResponse<>(false, EMAIL_EXISTS, null);
        }

        if (userRepository.findByCellNumber(request.cellNumber()).isPresent()) {
            return new AuthenticationResponse<>(false, CELL_EXISTS, null);
        }

        return new AuthenticationResponse<>(true, "", null);

    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .cellNumber(user.getCellNumber())
                .role(user.getRole())
                .activeStatus(user.isActiveStatus())
                .build();
    }


}
