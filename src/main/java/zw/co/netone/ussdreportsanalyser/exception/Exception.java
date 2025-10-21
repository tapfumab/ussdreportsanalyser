package zw.co.netone.ussdreportsanalyser.exception;

public class Exception extends  RuntimeException{

    private String responseCode = "500";

    public Exception(String message, String responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public Exception(String message) {
        super(message);
    }

    public String getResponseCode() {
        return responseCode;
    }
}
