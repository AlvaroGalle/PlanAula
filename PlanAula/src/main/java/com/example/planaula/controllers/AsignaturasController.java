package com.example.planaula.controllers;

import com.example.planaula.services.AsignaturasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/asignaturas")
public class AsignaturasController {

    private final AsignaturasService asignaturasService;

    public AsignaturasController(AsignaturasService asignaturasService) {
        this.asignaturasService = asignaturasService;
    }

    @GetMapping("")
    public String findAllAsignaturas(Model model) {
        model.addAttribute("asignaturas", asignaturasService.findAllAsignaturas());
        return "asignaturas";
    }
}
