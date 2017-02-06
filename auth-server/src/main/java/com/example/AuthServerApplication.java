package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.Collections;

@SpringBootApplication
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}

@Configuration
@EnableAuthorizationServer
class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer)
            throws Exception {

        oauthServer.tokenKeyAccess("permitAll()");
        oauthServer.checkTokenAccess("permitAll()");
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> new User(username, username + "pass",
                Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_USER")
                ));
    }

    @Bean
    ClientDetailsService clientDetailsService() {
        return clientId -> {
            BaseClientDetails details = new BaseClientDetails(clientId, "", "openid", "authorization_code,password", "openid");
            details.setClientSecret(clientId + "secret");
            return details;
        };
    }
}
/*
 curl localhost:8080/oauth/token  \
          -u acme:acmesecret      \
          -d username=user        \
          -d password=userpass    \
          -d grant_type=password

*/