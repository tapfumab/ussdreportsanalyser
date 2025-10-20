package zw.co.netone.ussdreportsanalyser.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.netone.ussdreportsanalyser.dto.*;
import zw.co.netone.ussdreportsanalyser.model.User;
import zw.co.netone.ussdreportsanalyser.service.UserService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse<String>> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse<?>> registerUser(@RequestBody RegisterUserRequest registrationRequest) throws Exception {
        return ResponseEntity.ok(userService.registerUser(registrationRequest));
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }
    @PostMapping("/user/update/{id}")
    public ResponseEntity<AuthenticationResponse<?>> update(@RequestBody RegisterUserRequest request, @PathVariable(value = "id") Long id) throws Exception {
        return ResponseEntity.ok(userService.editUser(request, id));
    }

    @GetMapping("/user/{employee_no}")
    public ResponseEntity<ApiResponse<User>> getEmployeeByEmployeeNumber(@PathVariable(value = "employee_no") String employee_number){
        return ResponseEntity.ok(userService.findByEmployeeNumber(employee_number));
    }
    @GetMapping("/user/all")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }
    @GetMapping("/user/by-id/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(userService.findUserById(id));
    }
 }
