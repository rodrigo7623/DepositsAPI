package cavapy.api.py.continental.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfig {

    @Value("${cavapy.ldap.url}")
    private String ldapUrl;

    @Value("${cavapy.ldap.base}")
    private String base;

    @Value("${cavapy.ldap.dnPatterns}")
    private String dnPatterns;

    @Value("${cavapy.ldap.password}")
    private String password;

    @Bean
    public LdapContextSource ldapContextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapUrl);
        contextSource.setBase(base);
        contextSource.setUserDn(dnPatterns);
        contextSource.setPassword(password);
        return contextSource;
    }
}
