package com.example.demo;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro que se ejecuta en CADA petición HTTP antes del controlador.
 *
 * Flujo:
 *   Petición entrante
 *       │
 *       ▼
 *   JwtFilter  ──── ¿tiene token válido? ──── NO ──► 401 Unauthorized
 *       │
 *      SÍ
 *       │
 *       ▼
 *   SesionController (ejecuta el endpoint)
 */

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String ruta = request.getRequestURI();

        // Rutas públicas que NO necesitan token
        if (ruta.equals("/login") || ruta.startsWith("/index") || ruta.equals("/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Buscamos el token en el header "Authorization: Bearer <token>"
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("❌ Token requerido. Haz login primero.");
            return;
        }

        // Extraemos el token (quitamos el prefijo "Bearer ")
        String token = authHeader.substring(7);

        if (!jwtUtil.esValido(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("❌ Token inválido o expirado.");
            return;
        }

        // Token válido → dejamos pasar la petición al controlador
        filterChain.doFilter(request, response);
    }
}