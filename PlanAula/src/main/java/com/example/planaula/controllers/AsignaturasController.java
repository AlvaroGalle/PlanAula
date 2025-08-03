package com.example.planaula.controllers;

import com.example.planaula.Dto.AsignaturaDTO;
import com.example.planaula.Dto.ProfesorDTO;
import com.example.planaula.services.AsignaturasService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String findAllAsignaturas(@RequestParam(name="page", required = false, defaultValue = "0") int page,
                                     @RequestParam(name="size", required = false, defaultValue = "15") int size,
                                     Model model) {
        Page<AsignaturaDTO> asignaturaDTOS = asignaturasService.findPageAsignaturas(PageRequest.of(page, size));
        model.addAttribute("page", asignaturaDTOS);
        model.addAttribute("params", "page=" + page + "&size=" + size);
        return "asignaturas";
    }
    
    @PostMapping("/anadir")
    public String addAsignatura(@RequestParam(name="nombre") String nombre, 
    							   @RequestParam(name="params", required= false) String params) {
    	asignaturasService.addAsignatura(nombre);
		return "redirect:/asignaturas?" + params;
    }
    
    @PostMapping("/eliminar")
    public String deleteAsignatura(@RequestParam(name="id") int id, 
    							   @RequestParam(name="params", required= false) String params) {
    	asignaturasService.deleteAsignatura(id);
		return "redirect:/asignaturas?" + params;
    }
}
