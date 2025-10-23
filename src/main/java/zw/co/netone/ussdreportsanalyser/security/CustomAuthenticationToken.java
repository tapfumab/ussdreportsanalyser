package zw.co.netone.ussdreportsanalyser.security;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String officeId;

    public CustomAuthenticationToken(Object principal, Object credentials,
                                     Collection<? extends GrantedAuthority> authorities,
                                     String officeId) {
        super(principal, credentials, authorities);
        this.officeId = officeId;
    }

    public CustomAuthenticationToken(Object principal, Object credentials, String officeId) {
        super(principal, credentials);
        this.officeId = officeId;
        super.setAuthenticated(false);
    }

    public String getOfficeId() {
        return officeId;
    }

}
