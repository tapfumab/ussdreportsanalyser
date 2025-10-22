package zw.co.netone.ussdreportsanalyser.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.netone.ussdreportsanalyser.dto.AuthenticationResponse;
import zw.co.netone.ussdreportsanalyser.dto.LoginRequest;
import zw.co.netone.ussdreportsanalyser.dto.RegisterUserRequest;
import zw.co.netone.ussdreportsanalyser.service.UserService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
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


}


