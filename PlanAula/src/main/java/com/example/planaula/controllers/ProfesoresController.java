package com.example.planaula.controllers;

import com.example.planaula.Dto.CursoDTO;
import com.example.planaula.Dto.ProfesorDTO;
import com.example.planaula.Dto.TutorDTO;
import com.example.planaula.services.CursosService;
import com.example.planaula.services.ProfesoresService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/profesores")
public class ProfesoresController {

    private final ProfesoresService profesoresService;
    private final CursosService cursosService;

    public ProfesoresController(ProfesoresService profesoresService, CursosService cursosService) {
        this.profesoresService = profesoresService;
        this.cursosService = cursosService;
    }

    @GetMapping("")
    public String findAllProfesores(Model model) {
        List<ProfesorDTO> profesorDTOList = profesoresService.findAllProfesores();
        model.addAttribute("profesores", profesorDTOList);
        return "profesores";
    }


    @GetMapping("/tutores")
    public String findAllTutores(@RequestParam(required = false, defaultValue = "0") int curso,
                                 @RequestParam(required = false, defaultValue = "0") int profesor, Model model) {
        model.addAttribute("cursoFiltro", curso);
        model.addAttribute("profesorFiltro", profesor);
        List<TutorDTO> tutorDTOList;
        if(curso == 0 && profesor == 0) {
             tutorDTOList = profesoresService.findAllTutores();
        }else{
            tutorDTOList = profesoresService.findAllTutoresByCursoAndProfesor(curso, profesor);
        }
        model.addAttribute("tutores", tutorDTOList);

        List<ProfesorDTO> profesorDTOList = profesoresService.findAllProfesores();
        model.addAttribute("profesores", profesorDTOList);

        List<CursoDTO> cursoDTOList = cursosService.findAllCursos();
        model.addAttribute("cursos", cursoDTOList);
        return "tutores";
    }
}
