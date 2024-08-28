package cavapy.api.py.continental.controller;

import cavapy.api.py.continental.model.UserLoginRequest;
import cavapy.api.py.continental.service.LdapAuthenticatorService;
import cavapy.api.py.continental.util.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticatorController {

    private final LdapAuthenticatorService ldapAuthenticatorService;

    public AuthenticatorController(LdapAuthenticatorService ldapAuthenticatorService) {
        this.ldapAuthenticatorService = ldapAuthenticatorService;
    }

    @PostMapping("/ldap/login")
    public ApiResponse<?> login(@RequestBody UserLoginRequest loginRequest) {
        return ldapAuthenticatorService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
    }
}
