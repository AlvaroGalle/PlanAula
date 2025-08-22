package com.example.planaula.controllers;

import com.example.planaula.Dto.AsignaturaDTO;
import com.example.planaula.Dto.ProfesorDTO;
import com.example.planaula.services.AsignaturasService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("asignaturaForm", new AsignaturaDTO());
        model.addAttribute("params", "page=" + page + "&size=" + size);
        return "asignaturas";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public AsignaturaDTO findAsignaturaById(@PathVariable(name= "id") Integer id) {
        return asignaturasService.findAsignaturaById(id);
    }

    @PostMapping("")
    public String accionProfesores(@ModelAttribute AsignaturaDTO asignaturaDTO,
                                   @RequestParam(name="accion", required = false, defaultValue = "A") String accion) {
        switch (accion) {
            case "A":
                asignaturasService.addAsignatura(asignaturaDTO.getNombre());
                break;
            case "M":
                asignaturasService.modifyAsignatura(asignaturaDTO);
                break;
            case "E":
                asignaturasService.deleteAsignatura(asignaturaDTO.getId());
                break;
        }
        return "redirect:/asignaturas";
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
