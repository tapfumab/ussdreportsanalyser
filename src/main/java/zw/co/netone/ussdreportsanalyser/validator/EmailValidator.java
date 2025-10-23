package zw.co.netone.ussdreportsanalyser.validator;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");


    public static boolean validate(String email, String requiredDomain) {

        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        if (requiredDomain == null || requiredDomain.trim().isEmpty()) {
            return false;
        }

        String cleanEmail = email.trim();

        if (!EMAIL_PATTERN.matcher(cleanEmail).matches()) {
            return false;
        }

        String[] parts = cleanEmail.split("@");
        if (parts.length != 2) {
            return false;
        }

        String emailDomain = parts[1].toLowerCase();
        String requiredDomainLower = requiredDomain.toLowerCase().trim();

        return emailDomain.equals(requiredDomainLower);
    }

    public static String validateWithMessage(String email, String requiredDomain) {
        if (email == null || email.trim().isEmpty()) {
            return "Email is required";
        }

        if (requiredDomain == null || requiredDomain.trim().isEmpty()) {
            return "Required domain is not specified";
        }

        String cleanEmail = email.trim();

        if (!EMAIL_PATTERN.matcher(cleanEmail).matches()) {
            return "Invalid email format";
        }

        String[] parts = cleanEmail.split("@");
        if (parts.length != 2) {
            return "Invalid email structure";
        }

        String emailDomain = parts[1].toLowerCase();
        String requiredDomainLower = requiredDomain.toLowerCase().trim();

        if (!emailDomain.equals(requiredDomainLower)) {
            return "Email must be from domain @" + requiredDomain;
        }

        return "Valid email";
    }

    public static boolean isValidFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static String extractDomain(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        String[] parts = email.trim().split("@");
        return parts.length == 2 ? parts[1].toLowerCase() : null;
    }

//    // Test examples
//    public static void main(String[] args) {
//        System.out.println("=== Email Validation Examples ===\n");
//
//        // Example 1: Valid email
//        String email1 = "btapfuma@netone.co.zw";
//        String domain = "netone.co.zw";
//        System.out.println("Email: " + email1);
//        System.out.println("Valid: " + validate(email1, domain));
//        System.out.println("Message: " + validateWithMessage(email1, domain));
//        System.out.println();
//
//        // Example 2: Wrong domain
//        String email2 = "user@gmail.com";
//        System.out.println("Email: " + email2);
//        System.out.println("Valid: " + validate(email2, domain));
//        System.out.println("Message: " + validateWithMessage(email2, domain));
//        System.out.println();
//
//        // Example 3: Invalid format
//        String email3 = "invalid-email";
//        System.out.println("Email: " + email3);
//        System.out.println("Valid: " + validate(email3, domain));
//        System.out.println("Message: " + validateWithMessage(email3, domain));
//        System.out.println();
//
//        // Example 4: Extract domain
//        System.out.println("Domain from 'btapfuma@netone.co.zw': " +
//                extractDomain("btapfuma@netone.co.zw"));
//    }
}