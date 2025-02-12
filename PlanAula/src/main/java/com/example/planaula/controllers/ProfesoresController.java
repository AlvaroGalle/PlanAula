package com.example.planaula.controllers;

import com.example.planaula.services.ProfesoresService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profesores")
public class ProfesoresController {

    private final ProfesoresService profesoresService;

    public ProfesoresController(ProfesoresService profesoresService) {
        this.profesoresService = profesoresService;
    }

    @GetMapping("")
    public String findAllProfesores(Model model) {
        model.addAttribute("profesores", profesoresService.findAllProfesores());
        /*model.addAttribute("tutores", profesoresService.findAllTutores());*/
        return "profesores";
    }
}
