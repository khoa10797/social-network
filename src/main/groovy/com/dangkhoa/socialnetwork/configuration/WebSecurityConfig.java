package com.dangkhoa.socialnetwork.configuration;


import com.dangkhoa.socialnetwork.base.jwt.CustomAccessDeniedHandler;
import com.dangkhoa.socialnetwork.base.jwt.CustomAuthenticationEntryPoint;
import com.dangkhoa.socialnetwork.base.jwt.JwtAuthenticationTokenFilter;
import com.dangkhoa.socialnetwork.mongo.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception {
        JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter = new JwtAuthenticationTokenFilter();
        jwtAuthenticationTokenFilter.setAuthenticationManager(super.authenticationManager());
        return jwtAuthenticationTokenFilter;
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http.authorizeRequests().antMatchers("/user/login").permitAll();

        http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN");

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/post/**").permitAll()
                .antMatchers("/post/**").hasRole("POST");

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/comment/**").permitAll()
                .antMatchers("/comment/**").hasRole("COMMENT");

        http.authorizeRequests().antMatchers(HttpMethod.GET, "/topic/**").permitAll()
                .antMatchers("/topic/**").hasRole("TOPIC");

        http.authorizeRequests().antMatchers("/notification").authenticated();

        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        http.httpBasic()
                .authenticationEntryPoint(customAuthenticationEntryPoint())
                .and()
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }
}
