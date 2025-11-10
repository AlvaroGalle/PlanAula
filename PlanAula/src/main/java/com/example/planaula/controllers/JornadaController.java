package com.example.planaula.controllers;

import com.example.planaula.Dto.*;
import com.example.planaula.services.CursosService;
import com.example.planaula.services.GuardiasService;
import com.example.planaula.services.HorariosService;
import com.example.planaula.services.JornadaService;
import com.example.planaula.services.ProfesoresService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

	@GetMapping("")
	@PreAuthorize("@permisoService.tieneAccesoCentroFromPath(authentication, #this)")
	public String findJornada(@PathVariable(name = "idCentro") int idCentro,
							  @RequestParam(name = "page", required = false, defaultValue = "0") int page,
							  Model model) {
		model.addAttribute("idCentro", idCentro);
		return "jornada";
	}

	@PostMapping("")
	public String saveJornada(@PathVariable(name = "idCentro") int idCentro,
								   @ModelAttribute JornadaDTO jornadaDTO) {
		return "redirect:/" + idCentro + "/profesores";
	}

}