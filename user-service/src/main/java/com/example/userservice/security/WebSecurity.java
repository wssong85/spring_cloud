package com.example.userservice.security;

import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

  private final UserService userService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final Environment env;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
//    http.authorizeHttpRequests().antMatchers("/users/**").permitAll();
    http.authorizeRequests()
            .antMatchers("/actuator/**").permitAll()
            .antMatchers("/**").permitAll()
            // TODO: 아래.. 잘 안묵음 확인해야함
//            .hasIpAddress("127.0.0.1")
//            .hasIpAddress("127.0.0.1")
            .and()
            .addFilter(getAuthenticationFilter());
    
    // http open frame for h2 in memory database
    http.headers().frameOptions().disable();
  }

  private AuthenticationFilter getAuthenticationFilter() throws Exception {
    AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, env);
//    authenticationFilter.setAuthenticationManager(authenticationManager());

    return authenticationFilter;
  }

  // select pwd from users where email=?
  // db_pwd(encrypted) == input_pwd(encrypted)
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
  }
}
