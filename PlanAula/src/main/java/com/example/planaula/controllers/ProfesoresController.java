package com.example.planaula.controllers;

import com.example.planaula.Dto.ProfesorDTO;
import com.example.planaula.Dto.TutorDTO;
import com.example.planaula.services.ProfesoresService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/profesores")
public class ProfesoresController {

    private final ProfesoresService profesoresService;

    public ProfesoresController(ProfesoresService profesoresService) {
        this.profesoresService = profesoresService;
    }

    @GetMapping("")
    public String findAllProfesores(Model model) {
        List<ProfesorDTO> profesorDTOList = profesoresService.findAllProfesores();
        List<TutorDTO> tutorDTOList = profesoresService.findAllTutores();
        model.addAttribute("docentes", null);
        return "profesores";
    }
}
