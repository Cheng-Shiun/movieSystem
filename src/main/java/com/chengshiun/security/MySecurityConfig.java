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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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

                //設定 Csrf 保護
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(createCsrfHandler())
                        .ignoringRequestMatchers("/register", "/userLogin")

                )

                //設定 Cors 跨域
                .cors(cors -> cors
                        .configurationSource(createCorsConfig())
                )
                //添加自定義的 Filter
                .addFilterAfter(new UserLoginFilter(), BasicAuthenticationFilter.class)

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
    //自定義 createCorsConfig() 方法
    private CorsConfigurationSource createCorsConfig() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://example.com"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    //自定義 createCsrfHandler() 方法
    private CsrfTokenRequestAttributeHandler createCsrfHandler() {
        CsrfTokenRequestAttributeHandler csrfHandler = new CsrfTokenRequestAttributeHandler();
        csrfHandler.setCsrfRequestAttributeName(null);
        return csrfHandler;
    }
}
