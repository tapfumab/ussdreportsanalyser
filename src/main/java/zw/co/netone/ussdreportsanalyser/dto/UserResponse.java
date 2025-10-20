package zw.co.netone.ussdreportsanalyser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zw.co.netone.ussdreportsanalyser.model.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse{
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String cellNumber;
    private Role role;
    private boolean activeStatus;
}
