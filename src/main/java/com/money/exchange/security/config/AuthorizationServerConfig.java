package com.money.exchange.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        security
                .checkTokenAccess("isAuthenticated()");
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	 clients
	         .inMemory()
	         .withClient("user")
	         .authorizedGrantTypes("client_credentials", "password")
	         .authorities("ROLE_CLIENT","ROLE_TRUSTED_CLIENT")
	         .scopes("read","write")
	         .resourceIds("oauth2-resource")
	         .accessTokenValiditySeconds(5000)
	         .secret("$2a$10$VbRzgzs36/5CadrZ5jIbJub5EgMGWEkAUrWIu3SpUtPcVEdKe9FC.")
    	 .and()
	         .withClient("menadzer")
	         .authorizedGrantTypes("client_credentials", "password")
	         .authorities("ROLE_ADMIN")
	         .scopes("read","write","trust")
	         .resourceIds("oauth2-resource")
	         .accessTokenValiditySeconds(5000)
	         .secret("$2a$10$GbwSx0uSOzL3snRcgTvT1e8bKgFLZ9o.ClyjhTyz1ycEbPsjqTNRC")
	      .and()
	         .withClient("krynica")
	         .authorizedGrantTypes("client_credentials", "password")
	         .authorities("ROLE_CLIENT")
	         .scopes("read","write","trust")
	         .resourceIds("oauth2-resource")
	         .accessTokenValiditySeconds(5000)
	         .secret("$2a$10$FprCSH0xV79KJgVzdn4mfOWHScHVTumsBa7/mlz87LTk29b2sezTy");

    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.authenticationManager(authenticationManager);
    }
}
