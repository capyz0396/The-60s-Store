package org.example.the60sstore.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/* CustomAuthenticationFailureHandler gets error when logging and return to html by custom design. */
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    /* When Spring Security meet error when logging, it will return to /login with param error. */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if (exception.getMessage().equals("Bad credentials")) {
            super.setDefaultFailureUrl("/login?errorPassword=true");
        } else if (exception.getMessage().equals("User account is locked")) {
            super.setDefaultFailureUrl("/login?errorLocked=true");
        } else if (exception.getMessage().equals("User is disabled")) {
            super.setDefaultFailureUrl("/login?errorDisable=true");
        }
        else {
            super.setDefaultFailureUrl("/login?error=" + exception.getMessage());
        }
        super.onAuthenticationFailure(request, response, exception);
    }
}
