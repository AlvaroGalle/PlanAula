package com.example.planaula.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Controller
@RequestMapping("/menu")
public class MenuController {


    @GetMapping("")
    public String menu(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", authentication.getName());

        LocalDate fechaActual = LocalDate.now();
        String diaSemana = fechaActual.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase();
        int dia = fechaActual.getDayOfMonth();
        String mes = fechaActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase();
        int anio = fechaActual.getYear();
        String fechaFormateada = String.format("%s, %d de %s de %d",
                diaSemana, dia, mes, anio);

        model.addAttribute("fecha", fechaFormateada);
        return "menu";
    }
}
