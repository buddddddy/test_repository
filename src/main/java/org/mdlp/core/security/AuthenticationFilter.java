package org.mdlp.core.security;

import com.google.common.base.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Created by ssuvorov on 03.04.2017.
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationFilter extends GenericFilterBean {
    public static final String TOKEN_KEY = "token";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_URL = "/logout";

    @Value("${web.indexpage}")
    @NotNull
    private String rootUrl;

    private final AuthenticationManager authenticationManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);

        Optional<String> username = Optional.fromNullable(httpRequest.getParameter("login"));
        Optional<String> password = Optional.fromNullable(httpRequest.getParameter("password"));
        Optional<Cookie> cookie = getSecurityCookie(httpRequest);
        Optional<String> token = Optional.fromNullable(cookie.isPresent() ? cookie.get().getValue() : null);

        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);

        try {
            if (postToLogin(httpRequest, resourcePath)) {
                login(httpResponse, username, password, cookie);
                return;
            }

            if (postToLogout(httpRequest, resourcePath)) {
                logout(httpResponse, cookie);
                return;
            }

            if (token.isPresent()) {
                try {
                    processTokenAuthentication(token);
                } catch (BadCredentialsException e) {
                    cookie.get().setMaxAge(0);
                    httpResponse.addCookie(cookie.get());
                }
            }
            chain.doFilter(request, response);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        }
    }

    private Optional<Cookie> getSecurityCookie(HttpServletRequest httpRequest) {
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TOKEN_KEY.equals(cookie.getName())) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.absent();
    }

    private HttpServletRequest asHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    private HttpServletResponse asHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

    private boolean postToLogin(HttpServletRequest httpRequest, String resourcePath) {
        return LOGIN_URL.equalsIgnoreCase(resourcePath) && httpRequest.getMethod().equals("POST");
    }

    private boolean postToLogout(HttpServletRequest httpRequest, String resourcePath) {
        return LOGOUT_URL.equalsIgnoreCase(resourcePath) && httpRequest.getMethod().equals("POST");
    }

    private void logout(HttpServletResponse httpResponse, Optional<Cookie> cookie) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(null);
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        if (cookie.isPresent()) {
            Cookie logoutCookie = cookie.get();
            logoutCookie.setMaxAge(0);
            httpResponse.addCookie(logoutCookie);
        }
        httpResponse.sendRedirect("/login.html");
    }

    private void login(HttpServletResponse httpResponse, Optional<String> username, Optional<String> password, Optional<Cookie> cookie) throws IOException {
        try {
            Authentication auth = tryToAuthenticateWithUsernameAndPassword(username, password);
            SecurityContextHolder.getContext().setAuthentication(auth);
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            Cookie newCookie = new Cookie(TOKEN_KEY, auth.getDetails().toString());
            httpResponse.addCookie(newCookie);
            httpResponse.sendRedirect(rootUrl);
        } catch (BadCredentialsException e) {
            if (cookie.isPresent()) {
                cookie.get().setMaxAge(0);
                httpResponse.addCookie(cookie.get());
            }
            httpResponse.sendRedirect("/login.html?error=1");
        }
    }

    private Authentication tryToAuthenticateWithUsernameAndPassword(Optional<String> username, Optional<String> password) {
        UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username, password);
        return tryToAuthenticate(requestAuthentication);
    }

    private void processTokenAuthentication(Optional<String> token) {
        Authentication resultOfAuthentication = tryToAuthenticateWithToken(token);
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
    }

    private Authentication tryToAuthenticateWithToken(Optional<String> token) {
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token, null);
        return tryToAuthenticate(requestAuthentication);
    }

    private Authentication tryToAuthenticate(Authentication requestAuthentication) {
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }
        return responseAuthentication;
    }
}