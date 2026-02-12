package com.example.planaula.controllers;

import com.example.planaula.dto.*;
import com.example.planaula.services.CursosService;
import com.example.planaula.services.HorariosService;
import com.example.planaula.services.JornadaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/{idCentro}/jornada")
public class JornadaController {

	private final JornadaService jornadaService; 
	private final CursosService cursosService;
	private final HorariosService horariosService;

	public JornadaController(JornadaService jornadaService, CursosService cursosService,
			HorariosService horariosService) {
		this.jornadaService = jornadaService;
		this.cursosService = cursosService;
		this.horariosService = horariosService;
	}
	LocalDate localDate = LocalDate.now();

	@GetMapping("")
	@PreAuthorize("@permisoService.tieneAccesoCentroFromPath(authentication, #this)")
	public String findJornada(@PathVariable(name = "idCentro") int idCentro,
							  @RequestParam(name = "page", required = false, defaultValue = "0") int page,
							  Model model) {
		model.addAttribute("anio", localDate.getYear());
		model.addAttribute("idCentro", idCentro);
		return "jornada";
	}

	@PostMapping("")
	public String saveJornada(@PathVariable(name = "idCentro") int idCentro,
								   @ModelAttribute JornadaDTO jornadaDTO) {
		return "redirect:/" + idCentro + "/profesores";
	}

}