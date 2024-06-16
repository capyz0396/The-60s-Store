package org.example.the60sstore.Security;

import org.example.the60sstore.Security.Oauth2.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/* Class SecurityConfig of Spring Security controls all access from client by role of them. */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }

    /* filterChain method set policy for customers.
    * When login successfully, redirect to home with param logged true.
    * Else, using failureHandler to show result. */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.requestCache(RequestCacheConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/img/**",
                                "/webfonts/**", "/home**", "/",
                                "/signup**", "/about", "/contact", "/sent-message",
                                "/oauth2/authorization/google").permitAll()
                        .requestMatchers("/confirm**", "/register-confirm**", "/login**",
                                "/forgot-password", "/check-email",
                                "/check-token-renew-password", "/reconfirm-password",
                                "/update-new-password").anonymous()
                        .requestMatchers("/add-product", "/save-product", "/store-price",
                                "/store-price-history/", "/edit-price",
                                "/edited-price", "/invoice").hasRole("OWNER")
                        .requestMatchers("/admin-signup", "/account-manager",
                                "change-status").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .defaultSuccessUrl("/home?logged=true"))
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/home?logged=true")
                        .failureHandler(authenticationFailureHandler())
                        .permitAll()).
                logout(Customizer.withDefaults()).exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler()))
                .build();
    }

    /* authenticationFailureHandler method returns CustomAuthenticationFailureHandler.
    * Which is in CustomAuthenticationFailureHandler.java. */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    /* accessDeniedHandler method returns CustomAccessDeniedHandler.
    * Which is in CustomAccessDeniedHandler.java. */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    /* webSecurityCustomizer will ignore url /error/
    * It is new bug of the newest Spring Security version. */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/error/**");
    }

    /* authenticationManager method is default of Spring Security. */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /* passwordEncoder helps password be decode and use it to check with database. */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
