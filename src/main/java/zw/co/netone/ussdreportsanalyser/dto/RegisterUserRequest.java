package zw.co.netone.ussdreportsanalyser.dto;

public record RegisterUserRequest(
        String firstName,
        String lastName,
        String email,
        String username,
        String shop,
        String cellNumber,
        String role
) { }
