package com.example.pfkworkspace.security;

import com.example.pfkworkspace.service.impl.UserDetailsServiceImpl;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtility jwtUtility;
  private final UserDetailsServiceImpl userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    log.info("JWT FILTER TRIGGERED for URI: {}", request.getRequestURI());

    String token = resolveToken(request);

    if (token == null) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String username = jwtUtility.extractUsername(token);
      log.info("Extracted username from token: {}", username);

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtUtility.isTokenValid(token, userDetails)) {
          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authToken);

          log.info("JWT authenticated user: {}", username);
        }
      }

    } catch (Exception e) {
      log.error("JWT filter error: {}", e.getMessage(), e);
    }

    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    if (request.getCookies() == null) return null;

    for (var cookie : request.getCookies()) {
      if (cookie.getName().equals("refreshToken")) {
        return cookie.getValue();
      }
    }

    return null;
  }
}
