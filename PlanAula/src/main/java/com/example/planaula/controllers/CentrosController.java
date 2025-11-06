package com.example.planaula.controllers;

import com.example.planaula.Dto.*;
import com.example.planaula.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/centros")
public class CentrosController {

    public final UserService userService;
    private final HorasService horasService;
    private final DiasService diasService;
    private final CursosService cursosService;
    private final ProfesoresService profesoresService;
    private final HorariosService horariosService;
    private final AsignaturasService asignaturasService;
    private final AulasService aulasService;

    public CentrosController(UserService userService, HorasService horasService, DiasService diasService, CursosService cursosService, ProfesoresService profesoresService, HorariosService horariosService, AsignaturasService asignaturasService, AulasService aulasService) {
        this.userService = userService;
        this.horasService = horasService;
        this.diasService = diasService;
        this.cursosService = cursosService;
        this.profesoresService = profesoresService;
        this.horariosService = horariosService;
        this.asignaturasService = asignaturasService;
        this.aulasService = aulasService;
    }

    @GetMapping("")
    public String centros(@RequestParam(name="curso", required = false, defaultValue = "0") int curso,
            		   @RequestParam(name="profesor", required = false, defaultValue = "0") int profesor, 
            		   Model model) {
        model.addAttribute("asignaturas", new ArrayList<>());
        return "centros";
    }
}
