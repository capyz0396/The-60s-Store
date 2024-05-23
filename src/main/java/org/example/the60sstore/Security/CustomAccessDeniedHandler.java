package org.example.the60sstore.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/* CustomAccessDeniedHandler class resolves access failed by custom design. */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /* When access is denied, redirect client to /home url. */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.sendRedirect("/home");
    }
}
