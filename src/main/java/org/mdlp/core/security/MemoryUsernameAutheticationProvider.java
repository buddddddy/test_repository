package org.mdlp.core.security;

import com.google.common.base.Optional;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 30.05.2017
 * @package org.mdlp.core.security
 */
public class MemoryUsernameAutheticationProvider implements AuthenticationProvider {

    private TokenService tokenService;
    private InMemoryUserDetailsManager manager;


    public MemoryUsernameAutheticationProvider(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.manager = (InMemoryUserDetailsManager) userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<String> username = (Optional) authentication.getPrincipal();
        Optional<String> password = (Optional) authentication.getCredentials();

        if (!username.isPresent() || !password.isPresent()) {
            throw new BadCredentialsException("Invalid Domain User Credentials");
        }


        try {
            UserDetails userDetails = manager.loadUserByUsername(username.get());
            if (!userDetails.getPassword().equals(password.get())) {
                throw new BadCredentialsException("User not found.");
            }
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("User not found.");
        }


        AuthenticationWithToken auth = new AuthenticationWithToken(username, password);

        String newToken = tokenService.generateNewToken();
        auth.setToken(newToken);
        auth.setAuthenticated(true);
        tokenService.store(newToken, auth);

        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}