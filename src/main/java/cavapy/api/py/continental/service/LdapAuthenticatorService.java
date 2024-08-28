package cavapy.api.py.continental.service;

import cavapy.api.py.continental.enums.ApiMessage;
import cavapy.api.py.continental.util.ApiResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@Configuration
public class LdapAuthenticatorService {

    Logger logger = Logger.getLogger(LdapAuthenticatorService.class.getName());

    private final LdapTemplate ldapTemplate;

    public LdapAuthenticatorService(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public ApiResponse<?> authenticate(String username, String password) {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        try {
            ldapTemplate.authenticate(LdapQueryBuilder.query().where("uid").is(username), password);
            apiResponse.setSuccess(true);
            apiResponse.setCode(HttpStatus.OK.value());
            apiResponse.setMessage(ApiMessage.INF_LOGIN_SUCCESSFUL.getValue());
        } catch (Exception e) {
            logger.severe(e.getMessage());
            apiResponse.setSuccess(false);
            apiResponse.setCode(HttpStatus.BAD_REQUEST.value());
            apiResponse.setMessage(ApiMessage.ERR_LOGIN_FAILED.getValue());
        }
        return apiResponse;
    }

}
