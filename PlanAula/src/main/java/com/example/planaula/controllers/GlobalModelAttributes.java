package com.example.planaula.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("menuItems")
    public String[] menuItems() {
        return new String[]{"Asignaturas", "Profesores", "Horarios", "Turnos"};
    }

    @ModelAttribute("currentUrl")
    public String getCurrentUrl(HttpServletRequest request) {
        return request.getRequestURI();
    }
    @ModelAttribute("anio")
    public int anio() {
        return LocalDate.now().getYear();
    }
}