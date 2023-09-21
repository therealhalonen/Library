package halonen.bookstore.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Customize the behavior based on user roles
        if (authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_TEMP"))) {
            // If the user has the TEMP role, redirect to /approval
            response.sendRedirect("/approval");
        } else {
            // Otherwise, redirect to the default page
            response.sendRedirect("/booklist");
        }
    }
}