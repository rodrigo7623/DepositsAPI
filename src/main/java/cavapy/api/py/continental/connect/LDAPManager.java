package cavapy.api.py.continental.connect;

import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.ResultCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class LDAPManager {

    private static final Logger logger = Logger.getLogger(LDAPManager.class.getName());

    @Value("${cavapy.ldap.host}")
    private String host;

    @Value("${cavapy.ldap.port}")
    private Integer port;

    @Value("${cavapy.ldap.user}")
    private String ldapUser;

    public boolean ldapAuthenticator(String userName, String password) {
        logger.info("Login del usuario LDAP");
        LDAPConnection  ldapConnection = new LDAPConnection();
        try {
            ldapConnection.connect(host, port);
            BindResult bindResult = ldapConnection.bind("uid=" + userName + ldapUser, password);
            return bindResult.getResultCode().equals(ResultCode.SUCCESS);
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
        return false;
    }
}
