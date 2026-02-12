package com.example.planaula.controllers;

import com.example.planaula.dto.AsignaturaDTO;
import com.example.planaula.services.AsignaturasService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/{idCentro}/asignaturas")
public class AsignaturasController {

    private final AsignaturasService asignaturasService;

    public AsignaturasController(AsignaturasService asignaturasService) {
        this.asignaturasService = asignaturasService;
    }
    LocalDate localDate = LocalDate.now();

    @GetMapping("")
    @PreAuthorize("@permisoService.tieneAccesoCentroFromPath(authentication, #this)")
    public String findAllAsignaturas(@PathVariable(name = "idCentro") int idCentro,
    								 @RequestParam(name="page", required = false, defaultValue = "0") int page,
                                     @RequestParam(name="size", required = false, defaultValue = "15") int size,
                                     Model model) {
    	model.addAttribute("idCentro", idCentro);
    	
        Page<AsignaturaDTO> asignaturaDTOS = asignaturasService.findPageAsignaturas(PageRequest.of(page, size), idCentro);
        model.addAttribute("page", asignaturaDTOS);
        model.addAttribute("asignaturaForm", new AsignaturaDTO());
        model.addAttribute("params", "page=" + page + "&size=" + size);
        model.addAttribute("anio", localDate.getYear());
        return "asignaturas";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public AsignaturaDTO findAsignaturaById(@PathVariable(name = "idCentro") int idCentro,
    										@PathVariable(name= "id") Integer id) {
        return asignaturasService.findAsignaturaById(id);
    }

    @PostMapping("")
    public String accionProfesores(@PathVariable(name = "idCentro") int idCentro,
    							   @ModelAttribute AsignaturaDTO asignaturaDTO,
                                   @RequestParam(name="accion", required = false, defaultValue = "A") String accion) {
        switch (accion) {
            case "A":
                asignaturasService.addAsignatura(asignaturaDTO.getNombre(), idCentro);
                break;
            case "M":
                asignaturasService.modifyAsignatura(asignaturaDTO);
                break;
            case "E":
                asignaturasService.deleteAsignatura(asignaturaDTO.getId());
                break;
        }
        return "redirect:/" + idCentro + "/asignaturas";
    }
    
    @PostMapping("/anadir")
    public String addAsignatura(@PathVariable(name = "idCentro") int idCentro,
    						    @RequestParam(name="nombre") String nombre, 
    							@RequestParam(name="params", required= false) String params) {
    	asignaturasService.addAsignatura(nombre, idCentro);
		return "redirect:/asignaturas?" + params;
    }
    
    @PostMapping("/eliminar")
    public String deleteAsignatura(@PathVariable(name = "idCentro") int idCentro,
    							   @RequestParam(name="id") int id, 
    							   @RequestParam(name="params", required= false) String params) {
    	asignaturasService.deleteAsignatura(id);
		return "redirect:/" + idCentro + "/asignaturas?" + params;
    }
}
