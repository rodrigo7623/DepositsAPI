package cavapy.api.py.continental.custom;

import cavapy.api.py.continental.enums.ApiMessage;
import cavapy.api.py.continental.service.UserProfileService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomPostAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserProfileService userProfileService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            boolean hasRequiredProfile = userProfileService.hasRequiredProfile(username);

            if (!hasRequiredProfile) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, ApiMessage.USER_DOES_NOT_HAVE_REQUIRED_PROFILE.getValue());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
