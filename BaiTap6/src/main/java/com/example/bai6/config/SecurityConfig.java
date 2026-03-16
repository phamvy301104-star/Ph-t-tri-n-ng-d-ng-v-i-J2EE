package com.example.bai6.config;

import com.example.bai6.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AccountService accountService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/products/add", "/products/save", "/products/edit/**",
                        "/products/delete/**")
                .hasRole("ADMIN")
                .requestMatchers("/order").hasRole("USER")
                .requestMatchers("/products").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated())
                .formLogin(form -> form.defaultSuccessUrl("/products", true))
                .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll())
                .exceptionHandling(ex -> ex.accessDeniedPage("/403"));
        return http.build();
    }
}
