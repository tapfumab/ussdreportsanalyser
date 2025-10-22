package zw.co.netone.ussdreportsanalyser.exception;

import lombok.Getter;

@Getter
public class LoginException extends  RuntimeException{

    private String responseCode = "500";

    public LoginException(String message, String responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public LoginException(String message) {
        super(message);
    }

}
