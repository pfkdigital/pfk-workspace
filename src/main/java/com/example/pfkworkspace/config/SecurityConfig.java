package com.example.pfkworkspace.config;

import com.example.pfkworkspace.handler.OauthSuccessHandler;
import com.example.pfkworkspace.security.JwtAuthenticationFilter;
import com.example.pfkworkspace.service.impl.CustomOauth2UserService;
import com.example.pfkworkspace.utility.CookieUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  private final CustomOauth2UserService customOauth2UserService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final OauthSuccessHandler oauthSuccessHandler;
  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  public SecurityConfig(
      CustomOauth2UserService customOauth2UserService,
      JwtAuthenticationFilter jwtAuthenticationFilter,
      OauthSuccessHandler oauthSuccessHandler,
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder) {
    this.customOauth2UserService = customOauth2UserService;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.oauthSuccessHandler = oauthSuccessHandler;
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            request ->
                request
                    .requestMatchers("/api/v1/auth/**", "/oauth2/authorization/google")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .authenticationProvider(authenticationProvider())
        .oauth2Login(
            oauthLogin ->
                oauthLogin
                    .authorizationEndpoint(
                        authorization -> authorization.baseUri("/oauth2/authorization"))
                    .userInfoEndpoint(userInfo -> userInfo.userService(customOauth2UserService))
                    .successHandler(oauthSuccessHandler)
                    .failureHandler(
                        (req, res, ex) -> {
                          ex.printStackTrace(); // or log.error("OAuth2 login failed", ex);
                          res.setStatus(HttpStatus.UNAUTHORIZED.value());
                          res.getWriter().write("OAuth2 failed: " + ex.getMessage());
                        }))
        .exceptionHandling(
            e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setPasswordEncoder(passwordEncoder);
    authenticationProvider.setUserDetailsService(userDetailsService);
    return authenticationProvider;
  }
}
