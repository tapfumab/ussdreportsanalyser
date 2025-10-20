package zw.co.netone.ussdreportsanalyser.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
