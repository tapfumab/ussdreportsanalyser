package zw.co.netone.ussdreportsanalyser.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.netone.ussdreportsanalyser.dto.ApiResponse;
import zw.co.netone.ussdreportsanalyser.dto.AuthenticationResponse;
import zw.co.netone.ussdreportsanalyser.dto.RegisterUserRequest;
import zw.co.netone.ussdreportsanalyser.dto.UserResponse;
import zw.co.netone.ussdreportsanalyser.service.UserService;

import java.util.List;

/**
 * REST Controller for user management operations
 * Handles CRUD operations for users
 *
 * @author btapfuma Oct2025
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Request to fetch all users - page: {}, size: {}", page, size);
        ApiResponse<List<UserResponse>> response = userService.findAllUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        log.info("Request to fetch user by ID: {}", id);
        ApiResponse<UserResponse> response = userService.findUserById(id);

        HttpStatus status = response.isSuccess()
                ? HttpStatus.OK
                : HttpStatus.NOT_FOUND;

        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AuthenticationResponse<?>> updateUser(
            @PathVariable Long id,
            @RequestBody RegisterUserRequest request) throws Exception {
        log.info("Request to update user with ID: {}", id);
        return ResponseEntity.ok(userService.editUser(request, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        log.info("Request to delete user with ID: {}", id);
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
