package com.neosoft.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.neosoft.exception.AuthenticationProcessingException;
import com.neosoft.exception.InvalidJwtTokenException;
import com.neosoft.exception.JwtTokenExpiredException;
import com.neosoft.service.impl.CustomUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtil jwtUtil,
                         CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader(AUTH_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            if (!jwtUtil.isTokenValid(token)) {
                throw new InvalidJwtTokenException(
                        "JWT token is invalid",
                        HttpStatus.UNAUTHORIZED
                );
            }

            String username = jwtUtil.extractUsername(token);

            if (username == null || username.isBlank()) {
                throw new InvalidJwtTokenException(
                        "JWT token does not contain a valid username",
                        HttpStatus.UNAUTHORIZED
                );
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                var userDetails = userDetailsService.loadUserByUsername(username);

                var authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

		} catch (ExpiredJwtException ex) {
			throw new JwtTokenExpiredException("JWT token has expired", HttpStatus.UNAUTHORIZED);
		} catch (JwtException ex) {
			throw new InvalidJwtTokenException("JWT token validation failed", HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			throw new AuthenticationProcessingException("Unexpected authentication processing error",
					HttpStatus.UNAUTHORIZED);
		}
	}
}
