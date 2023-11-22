package cavapy.api.py.controller;

import cavapy.api.py.repository.LoginRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    final
    LoginRepository loginRepository;

    public MainController(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
}
