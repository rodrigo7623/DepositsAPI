package cavapy.api.py.continental.service;

import cavapy.api.py.continental.enums.PermittedProfiles;
import cavapy.api.py.continental.model.UserProfile;
import cavapy.api.py.continental.util.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Service
@Configuration
public class AuthenticatorService {

    Logger logger = Logger.getLogger(AuthenticatorService.class.getName());

    private final LdapAuthenticatorService ldapAuthenticatorService;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthenticatorService(LdapAuthenticatorService ldapAuthenticatorService,
                                RestTemplate restTemplate) {
        this.ldapAuthenticatorService = ldapAuthenticatorService;
        this.restTemplate = restTemplate;
    }

    public boolean authenticate(String userName, String password) {
        ApiResponse<?> response = ldapAuthenticatorService.authenticate(userName, password);
        return response.isSuccess();
    }

}
