package com.example.planaula.controllers;

import com.example.planaula.Dto.AsignaturaDTO;
import com.example.planaula.Dto.ProfesorDTO;
import com.example.planaula.services.AsignaturasService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/asignaturas")
public class AsignaturasController {

    private final AsignaturasService asignaturasService;

    public AsignaturasController(AsignaturasService asignaturasService) {
        this.asignaturasService = asignaturasService;
    }

    @GetMapping("")
    public String findAllAsignaturas(@RequestParam(required = false, defaultValue = "0") int page,
                                     @RequestParam(required = false, defaultValue = "15") int size,
                                     Model model) {
        Page<AsignaturaDTO> asignaturaDTOS = asignaturasService.findPageAsignaturas(PageRequest.of(page, size));
        model.addAttribute("page", asignaturaDTOS);
        return "asignaturas";
    }
}
