package org.mdlp.core.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

/**
 * Created by ssuvorov on 03.04.2017.
 */
public class AuthenticationWithToken extends PreAuthenticatedAuthenticationToken {
    public AuthenticationWithToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public AuthenticationWithToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public void setToken(String token) {
        setDetails(token);
    }

    public String getToken() {
        return (String)getDetails();
    }
}
