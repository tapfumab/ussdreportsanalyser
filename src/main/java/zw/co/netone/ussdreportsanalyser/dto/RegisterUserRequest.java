package zw.co.netone.ussdreportsanalyser.dto;

public record RegisterUserRequest(
        String firstName,
        String lastName,
        String email,
        String username,
        Long Id,
        String cellNumber,
        String role
) { }
