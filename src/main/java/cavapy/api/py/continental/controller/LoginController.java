package cavapy.api.py.continental.controller;

import cavapy.api.py.continental.connect.LDAPManager;
import cavapy.api.py.continental.util.Credentials;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final LDAPManager ldapManager;

    public LoginController(LDAPManager ldapManager) {
        this.ldapManager = ldapManager;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("credentials", new Credentials());
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(Credentials credential) {

        if (ldapManager.ldapAuthenticator(credential.getUserName().toUpperCase(), credential.getPassword())) {

            return "redirect:/home";
        } else {
            return "redirect:/login?error";
        }
    }
}
