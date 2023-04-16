package com.amd.backend.Global.Config;

import com.amd.backend.Domain.User.Repository.UserService;
import com.amd.backend.Global.Config.JWT.Token.TokenProvider;
import com.amd.backend.Global.Filter.AuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration      // 다른 Bean보다 우선순위가 위임
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;
    private TokenProvider tokenProvider;


    public SecurityConfig(Environment env,
                          UserService userService,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          TokenProvider tokenProvider
                          ){
        this.env = env;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.formLogin().disable();
        http.logout().disable();
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/health_check")
                //.hasIpAddress("127.0.0.1")
                .permitAll()
                .and()
                .addFilter(getAuthentionFilter());
        http.headers().frameOptions().disable();
    }

    // AuthentionFilter
    private AuthenticationFilter getAuthentionFilter() throws Exception{
        AuthenticationFilter authenticationFilter =  new AuthenticationFilter(authenticationManager(), userService, env, tokenProvider);

        return authenticationFilter;
    }

    // select pwd from users where email=?
    // db_pwd == input_pwd(encrypted_pwd)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }
}