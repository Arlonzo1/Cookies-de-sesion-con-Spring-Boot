package com.example.demo;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Utilidad para crear y validar tokens JWT.
 *
 * Un token JWT tiene 3 partes separadas por puntos:
 *   HEADER.PAYLOAD.FIRMA
 *
 * Ejemplo real:
 *   eyJhbGc...  →  Header  (algoritmo usado)
 *   eyJ1c3Vy... →  Payload (datos del usuario)
 *   SflKxwRJ... →  Firma   (garantiza que no fue modificado)
 */
@Component
public class JwtUtil {

    // Clave secreta con la que firmamos el token (mínimo 256 bits)
    private final Key clave = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tiempo de expiración: 10 minutos en milisegundos
    private final long EXPIRACION = 1000 * 60 * 10;

    /**
     * Genera un token JWT con el nombre del usuario adentro.
     * @param usuario nombre del usuario
     * @return token JWT como String
     */
    public String generarToken(String usuario) {
        return Jwts.builder()
                .setSubject(usuario)                          // quién es el usuario
                .setIssuedAt(new Date())                      // cuándo se creó
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRACION)) // cuándo expira
                .signWith(clave)                              // firmamos con nuestra clave
                .compact();                                   // lo convertimos a String
    }

    /**
     * Verifica que el token sea válido y extrae el nombre del usuario.
     * @param token el JWT recibido del cliente
     * @return nombre del usuario si el token es válido
     */
    public String extraerUsuario(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(clave)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Verifica si el token es válido (firma correcta y no expirado).
     * @param token el JWT a verificar
     * @return true si es válido, false si no
     */
    public boolean esValido(String token) {
        try {
            extraerUsuario(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // token inválido o expirado
        }
    }
}