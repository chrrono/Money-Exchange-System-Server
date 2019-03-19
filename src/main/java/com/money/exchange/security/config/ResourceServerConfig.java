package com.money.exchange.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .headers()
                .frameOptions()
                .disable()
                .and()
            .authorizeRequests()
                .antMatchers("/","/home","/register","/login").permitAll()
                .antMatchers("/WorkPlace/info").hasAnyRole("EMPLOYEE","MENADZER","EMPLOYEE2")
        		.antMatchers("/socket2/**").hasAnyRole("EMPLOYEE","MENADZER","EMPLOYEE2");
//              .antMatchers("/currencyState/**").hasRole("MENADZER");
        //access("#oauth2.hasScope('trust')")
    }
	
}
