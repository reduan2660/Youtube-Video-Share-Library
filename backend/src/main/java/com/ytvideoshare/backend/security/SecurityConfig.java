package com.ytvideoshare.backend.security;

import com.ytvideoshare.backend.filter.CustomAuthenticationFilter;
import com.ytvideoshare.backend.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity @RequiredArgsConstructor @Slf4j
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.info("Security Filter Chain");

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(authenticationManager); // Custom Authentication Filter
        authenticationFilter.setFilterProcessesUrl("/user/login");

        http.cors().and().csrf().disable();
        http
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/swagger-ui.html", "/swagger-ui/**", "/api-docs/**").permitAll()

                .antMatchers(HttpMethod.GET,"/user/login/**", "/token/refresh/**", "/user/resendcode/**", "/video/explore/**", "/video/likes/**", "/video/dislikes/**").permitAll()
                .antMatchers(HttpMethod.POST, "/user/registration/**", "/user/verify/**").permitAll()

                .antMatchers(HttpMethod.GET,"/video/my/**", "/video/myreaction/**").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/video/upload/**", "video/like/**", "video/dislike/**").hasAuthority("ROLE_USER")
                .anyRequest().authenticated()
                .and().authenticationManager(authenticationManager)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilter(authenticationFilter);

        return http.build();
    }
}
