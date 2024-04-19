package io.ussopm.UserApp.security;

import io.ussopm.UserApp.service.CustomerDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CustomerDetailsService customerDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {


        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(login -> login
                        .loginPage("/auth/login")
//                        .defaultSuccessUrl("/todo/tasks")
                        .failureUrl("/auth/login?error"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login")
                        .deleteCookies("JSESSIONID"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/api/login", "/auth/api/hello","/auth/api/registration",
                                "/auth/login", "/auth/logout", "/todo/tasks/**",
                                "/auth/registration").permitAll()
                        .requestMatchers("/todo/task/**").authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customerDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

}
//  .requestMatchers("/auth/api/hello","auth/api/registration", "/auth/api/login",
//                                "/auth/login", "/todo/tasks/hello", "/todo/tasks/**","/todo/tasks",
//                                "/auth/logout").permitAll()
//                        .requestMatchers("/auth/api/secured", "/todo/tasks", "/todo/tasks/new",
//                                "/todo/task/{taskId}","/todo/task/{taskId}","/todo/task/{taskId}/update",
//                                "/todo/task/{taskId}/delete", "todo/api/tasks", "todo/api/tasks/new",
//                                "todo/api/task/{id}","todo/api/task/{id}/delete", "todo/api/task/{id}/edit").authenticated())