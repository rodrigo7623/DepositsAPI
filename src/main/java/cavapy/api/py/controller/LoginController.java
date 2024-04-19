package cavapy.api.py.controller;

import cavapy.api.py.util.Credentials;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("credentials", new Credentials());
        return "/login";
    }

    @PostMapping("/login")
    public String postLogin(Model model) {
        return "redirect:/home";
    }
}
