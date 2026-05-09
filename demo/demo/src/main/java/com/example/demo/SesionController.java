package com.example.demo;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

/**
 * Controlador REST que demuestra el uso de cookies de sesión en Spring Boot.
 *
 * Una sesión permite al servidor recordar información de un usuario
 * entre múltiples peticiones HTTP. La cookie "MI_SESION" actúa como
 * llave para identificar la sesión en el servidor.
 */
@RestController
public class SesionController {

    /**
     * Endpoint de login: crea una sesión nueva y guarda el nombre del usuario.
     * Spring Boot genera automáticamente una cookie MI_SESION en la respuesta.
     *
     * @param nombre  nombre del usuario recibido como parámetro en la URL
     * @param sesion  objeto HttpSession inyectado automáticamente por Spring
     * @return mensaje confirmando la sesión creada con su ID único
     */
    @PostMapping("/login")
    public String login(@RequestParam String nombre, HttpSession sesion) {
        // Guardamos el nombre en la sesión del servidor
        sesion.setAttribute("usuario", nombre);
        // Guardamos también la hora de inicio de sesión
        sesion.setAttribute("inicio", System.currentTimeMillis());

        return "Sesion creada para: " + nombre + " | ID: " + sesion.getId();
    }

    /**
     * Endpoint de perfil: lee los datos guardados en la sesión activa.
     * El servidor identifica al usuario gracias a la cookie MI_SESION
     * que el navegador envía automáticamente en cada petición.
     *
     * @param sesion  objeto HttpSession con los datos del usuario actual
     * @return datos del usuario si la sesión existe, mensaje de error si no
     */
    @GetMapping("/perfil")
    public String perfil(HttpSession sesion) {
        String usuario = (String) sesion.getAttribute("usuario");

        // Si no hay usuario en sesión, significa que no hizo login
        if (usuario == null) {
            return "No hay sesion activa. Haz login primero.";
        }

        // Calculamos cuántos segundos lleva activa la sesión
        long inicio = (long) sesion.getAttribute("inicio");
        long segundos = (System.currentTimeMillis() - inicio) / 1000;

        return "Bienvenido: " + usuario
             + " | Sesion ID: " + sesion.getId()
             + " | Tiempo activo: " + segundos + "s";
    }

    /**
     * Endpoint de logout: invalida la sesión y destruye la cookie.
     * Después de esto, el ID de sesión ya no es válido en el servidor.
     *
     * @param sesion  sesión activa que será destruida
     * @return mensaje de confirmación con el nombre del usuario desconectado
     */
    @DeleteMapping("/logout")
    public String logout(HttpSession sesion) {
        String usuario = (String) sesion.getAttribute("usuario");

        // invalidate() destruye la sesión y su cookie asociada
        sesion.invalidate();

        return "Sesion cerrada para: " + usuario;
    }
}