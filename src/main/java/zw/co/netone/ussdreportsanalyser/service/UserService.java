package zw.co.netone.ussdreportsanalyser.service;


import zw.co.netone.ussdreportsanalyser.dto.*;

import java.util.List;

public interface UserService {
    AuthenticationResponse<String> authenticateUser(LoginRequest loginRequest);

    AuthenticationResponse<?> registerUser(RegisterUserRequest registrationRequest) throws Exception;

    AuthenticationResponse<?> editUser(RegisterUserRequest registrationRequest, Long id) throws Exception;

    ApiResponse<String> deleteUser(Long id);

    ApiResponse<List<UserResponse>> findAllUsers();

    ApiResponse<UserResponse> findUserById(Long id);
}
