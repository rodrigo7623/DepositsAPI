package cavapy.api.py.continental.controller;

import cavapy.api.py.continental.service.AuthenticatorService;
import cavapy.api.py.continental.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final AuthenticatorService authenticatorService;

    private final UserProfileService userProfileService;

    public LoginController(AuthenticatorService authenticatorService, UserProfileService userProfileService) {
        this.authenticatorService = authenticatorService;
        this.userProfileService = userProfileService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping(value = "/login")
    public String postLogin(HttpServletRequest request) {
        boolean authenticated = authenticatorService.authenticate(request.getParameter("username"), request.getParameter("password"));
        boolean hasRequiredProfile = userProfileService.hasRequiredProfile(request.getParameter("username"));
        if (authenticated && hasRequiredProfile) {
            HttpSession session = request.getSession();
            session.setAttribute("authenticated", true);
            return "redirect:/home";
        } else if (authenticated) {
            return "redirect:/login?profileError";
        } else {
            return "redirect:/login?error";
        }
    }

}
