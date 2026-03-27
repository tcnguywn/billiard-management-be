package com.backend.billiards_management.utils;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;


@Configuration
public class FilterConfig {

    @Bean
    public OncePerRequestFilter logFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain)
                    throws ServletException, IOException {

                System.out.println("REQUEST: " + request.getMethod() + " " + request.getRequestURI());

                filterChain.doFilter(request, response);
            }
        };
    }
}