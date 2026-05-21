package com.example.demo;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador REST con autenticación basada en JWT.
 *
 * Flujo completo:
 *   1. Cliente hace POST /login  → recibe un token JWT
 *   2. Cliente guarda el token
 *   3. En cada petición envía: Authorization: Bearer <token>
 *   4. JwtFilter valida el token antes de llegar aquí
 */
@RestController
public class SesionController {

    private final JwtUtil jwtUtil;

    public SesionController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Login: recibe el nombre y devuelve un token JWT.
     * No necesita token previo (es la ruta pública).
     */
    @PostMapping("/login")
    public String login(@RequestParam String nombre) {
        String token = jwtUtil.generarToken(nombre);
        return "✅ Login exitoso!\n\n"
             + "Tu token JWT:\n"
             + token
             + "\n\nGuárdalo y úsalo en el header:\n"
             + "Authorization: Bearer " + token;
    }

    /**
     * Perfil: ruta protegida, solo accesible con token válido.
     * El JwtFilter ya validó el token antes de llegar aquí.
     */
    @GetMapping("/perfil")
    public String perfil(HttpServletRequest request) {
        // Extraemos el token del header y leemos el usuario
        String token = request.getHeader("Authorization").substring(7);
        String usuario = jwtUtil.extraerUsuario(token);
        return "👤 Bienvenido: " + usuario + "\n✅ Tu token JWT es válido.";
    }

    /**
     * Logout: con JWT el servidor no destruye nada.
     * El cliente es responsable de eliminar el token.
     */
    @DeleteMapping("/logout")
    public String logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String usuario = jwtUtil.extraerUsuario(token);
        return "👋 Hasta luego " + usuario + "!\n"
             + "ℹ️ Elimina el token en tu cliente para cerrar sesión.";
    }
}