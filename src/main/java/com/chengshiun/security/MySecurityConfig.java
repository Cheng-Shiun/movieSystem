package com.chengshiun.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                //設定 Session 的創建機制
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )

                .csrf(csrf -> csrf.disable())

                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())

                .authorizeHttpRequests(request -> request
                        .requestMatchers("/register", "/error").permitAll()
                        .requestMatchers("/userLogin").authenticated()

                        //movie api
                        .requestMatchers("/getMovies").hasAnyRole("ADMIN", "NORMAL_MEMBER", "VIP_MEMBER", "MOVIE_MANAGER")
                        .requestMatchers("/watchFreeMovie").hasAnyRole("ADMIN", "NORMAL_MEMBER")
                        .requestMatchers("/watchVipMovie").hasAnyRole("ADMIN", "VIP_MEMBER")
                        .requestMatchers("/uploadMovie").hasAnyRole("ADMIN", "MOVIE_MANAGER")
                        .requestMatchers("/deleteMovie").hasAnyRole("ADMIN", "MOVIE_MANAGER")

                        //subscribe api
                        .requestMatchers("/subscribe", "/unsubscribe").authenticated()

                        .anyRequest().denyAll()
                )

                .build();
    }
}
