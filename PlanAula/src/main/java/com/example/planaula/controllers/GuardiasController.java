package com.example.planaula.controllers;

import com.example.planaula.Dto.GuardiasDTO;
import com.example.planaula.services.AsignaturasService;
import com.example.planaula.services.GuardiasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/guardias")
public class GuardiasController {

    private final GuardiasService guardiasService;

    public GuardiasController(GuardiasService guardiasService) {
        this.guardiasService = guardiasService;
    }

    @GetMapping("")
    public List<GuardiasDTO> findAllAsignaturas(Model model) {
        return guardiasService.findAllGuardiasByDiaAndHoraAndProfesor(1,1,1);
    }
}
