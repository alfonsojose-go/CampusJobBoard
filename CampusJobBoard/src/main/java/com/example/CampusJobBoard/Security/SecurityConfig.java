package com.example.CampusJobBoard.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider authProvider) throws Exception {

        http.authenticationProvider(authProvider);

        http.authorizeHttpRequests(auth -> auth
                // public pages
                .requestMatchers("/login", "/register").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()

                // allow H2 console
                .requestMatchers("/h2-console/**").permitAll()

                // role protected pages
                .requestMatchers("/student/**").hasRole("STUDENT")
                .requestMatchers("/job/**").hasRole("EMPLOYER")
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // everything else requires login
                .anyRequest().authenticated()
        );

        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")

                // ✅ role-based redirect
                .successHandler((request, response, authentication) -> {
                    boolean isEmployer = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYER"));
                    boolean isStudent = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));

                    if (isEmployer) {
                        response.sendRedirect("/job");
                    } else if (isStudent) {
                        response.sendRedirect("/student");
                    } else {
                        response.sendRedirect("/admin/job-review");
                    }
                })

                .failureUrl("/login?error")
                .permitAll()
        );

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
        );

        // Needed for H2 console
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
