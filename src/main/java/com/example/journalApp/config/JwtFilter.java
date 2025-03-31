package com.example.journalApp.config;

import com.example.journalApp.service.JWTService;
import com.example.journalApp.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
     private JWTService jwtService;

    @Autowired
    ApplicationContext Context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("spring security called");
//        Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYXkiLCJpYXQiOjE3NDE2MDM1MTEsImV4cCI6MTc0MTYwMzYxOX0.2FW4TTHws5UYgoFh678M3PM2CpkJwAsjr39sDu5mpKs
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if(authHeader !=null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);

        }
        if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null ){
            //here we get the entire user details complete data
            UserDetails userDetails = Context.getBean(MyUserDetailsService.class).loadUserByUsername(username);

            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }


        }
        filterChain.doFilter(request, response);
    }
}
