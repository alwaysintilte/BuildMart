package com.example.buildMart.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        try {
            String username = jwtService.extractUserName(token);
            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Authentication authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute("jwt_error", "Expired");
            request.setAttribute("jwt_error_message", e.getMessage());
        } catch (SignatureException e) {
            request.setAttribute("jwt_error", "Invalid_signature");
            request.setAttribute("jwt_error_message", e.getMessage());
        } catch (MalformedJwtException e) {
            request.setAttribute("jwt_error", "Malformed");
            request.setAttribute("jwt_error_message", e.getMessage());
        } catch (UsernameNotFoundException e) {
            request.setAttribute("jwt_error", "User_not_found");
            request.setAttribute("jwt_error_message", e.getMessage());
        } catch (Exception e) {
            request.setAttribute("jwt_error", "Unknown");
            request.setAttribute("jwt_error_message", e.getMessage());
        }
        chain.doFilter(request, response);
    }
}
