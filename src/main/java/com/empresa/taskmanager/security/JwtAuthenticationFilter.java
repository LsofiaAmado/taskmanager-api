package com.empresa.taskmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Obtener el header Authorization
        String authHeader = request.getHeader("Authorization");

        // Verificar que exista y empiece por Bearer
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer el JWT
        String jwt = authHeader.substring(7);

        // Extraer el email almacenado dentro del JWT
        String username = jwtService.extractUsername(jwt);

        // Buscar el usuario en la base de datos
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Validar el token
        if(jwtService.isTokenValid(jwt, userDetails)){
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            // Guardar la uatenticación en el contexto de Spring Security
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // Continuar con la petición
        filterChain.doFilter(request, response);

    }
}
