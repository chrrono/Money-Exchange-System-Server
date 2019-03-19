package com.money.exchange.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.SessionManagementFilter;

import com.money.exchange.config.CorsFilter;

//@Order(-1)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	 @Autowired
	 private UserDetailsService userDetailsService;
	 
	 @Autowired
	 private PasswordEncoder passwordEncoder; 
	
	 @Bean
	 CorsFilter corsFilter() {
	        CorsFilter filter = new CorsFilter();
	        return filter;
	    }
	 
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService)
        	.passwordEncoder(passwordEncoder);
    }
	
	@Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(corsFilter(), SessionManagementFilter.class)
            .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS,"/oauth/token").permitAll()
                .anyRequest().authenticated()
                .and()
            .httpBasic()
                .and()
            .csrf().disable();
        
//        .addFilterBefore(corsFilter(), SessionManagementFilter.class)
    }
    
    
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    } 
}
