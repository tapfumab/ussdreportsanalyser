package zw.co.netone.ussdreportsanalyser.exception;

public class LoginException extends  RuntimeException{

    private String responseCode = "500";

    public LoginException(String message, String responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public LoginException(String message) {
        super(message);
    }

    public String getResponseCode() {
        return responseCode;
    }
}
