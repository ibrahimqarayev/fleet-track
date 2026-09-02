package az.fleettrack.config;

import az.fleettrack.security.CustomAccessDeniedHandler;
import az.fleettrack.security.JwtAuthenticationEntryPoint;
import az.fleettrack.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Authentication
                        .requestMatchers("/api/v1/auth/**")
                        .permitAll()

                        //WebSocket
                        .requestMatchers("/ws", "/ws/**")
                        .permitAll()

                        // Swagger / OpenAPI
                        .requestMatchers(
                                "/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // PDF Reports
                        .requestMatchers("/api/v1/reports/**")
                        .hasAnyRole("ADMIN", "FLEET_MANAGER")

                        // GET
                        .requestMatchers(HttpMethod.GET, "/api/v1/**")
                        .hasAnyRole("ADMIN", "FLEET_MANAGER")

                        // POST
                        .requestMatchers(HttpMethod.POST, "/api/v1/**")
                        .hasAnyRole("ADMIN", "FLEET_MANAGER")

                        // PUT
                        .requestMatchers(HttpMethod.PUT, "/api/v1/**")
                        .hasAnyRole("ADMIN", "FLEET_MANAGER")

                        // DELETE
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}