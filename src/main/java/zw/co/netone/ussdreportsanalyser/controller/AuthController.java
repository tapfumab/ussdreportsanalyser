package zw.co.netone.ussdreportsanalyser.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.netone.ussdreportsanalyser.dto.*;
import zw.co.netone.ussdreportsanalyser.model.User;
import zw.co.netone.ussdreportsanalyser.service.UserService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/users")
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse<String>> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse<?>> registerUser(@RequestBody RegisterUserRequest registrationRequest) throws Exception {
        return ResponseEntity.ok(userService.registerUser(registrationRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<AuthenticationResponse<?>> update(@RequestBody RegisterUserRequest request,
                                                            @PathVariable(value = "id") Long id) throws Exception {
        return ResponseEntity.ok(userService.editUser(request, id));
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers( @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size){

        ApiResponse<List<UserResponse>> response = userService.findAllUsers();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable(value = "id") Long id){
        ApiResponse<UserResponse> response = userService.findUserById(id);
        HttpStatus status = response.isSuccess()
                ? HttpStatus.OK
                : HttpStatus.NOT_FOUND;

        return ResponseEntity.status(status).body(response);
    }

}


