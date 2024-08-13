package cavapy.api.py.continental.config;

import cavapy.api.py.continental.custom.CustomPostAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    CustomPostAuthenticationFilter customPostAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());
        http.addFilterAfter(customPostAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.formLogin(login -> login.loginPage("/login").loginProcessingUrl("/perform_login").failureForwardUrl("/login?error")
                .defaultSuccessUrl("/home", true).permitAll());
        http.logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl("ldap://10.0.1.223:389");
        return ldapContextSource;
    }

    @Bean
    public AuthenticationManager authManager(BaseLdapPathContextSource source) {
        LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(source);
        factory.setUserDnPatterns("uid={0},ou=People,dc=cavapy,dc=com,dc=py");
        return factory.createAuthenticationManager();
    }

}
