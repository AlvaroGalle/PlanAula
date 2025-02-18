package com.example.planaula.controllers;

import com.example.planaula.Dto.CursoDTO;
import com.example.planaula.Dto.ProfesorDTO;
import com.example.planaula.Dto.TutorDTO;
import com.example.planaula.services.CursosService;
import com.example.planaula.services.ProfesoresService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String findAllProfesores(@RequestParam(required = false, defaultValue = "0") int page,
                                    @RequestParam(required = false, defaultValue = "15") int size,
                                    Model model) {
        Page<ProfesorDTO> profesorDTOList = profesoresService.findPageProfesores(PageRequest.of(page, size));
        model.addAttribute("page", profesorDTOList);
        model.addAttribute("profesorForm", new ProfesorDTO());
        return "profesores";
    }

    @PostMapping("")
    public String anadirProfesores(@ModelAttribute ProfesorDTO profesorDTO, @RequestParam(required = false, defaultValue = "A") String accion) {
        switch (accion) {
            case "A":
                profesoresService.anadirProfesor(profesorDTO);
                break;
                case "M":
        }
        return "redirect:/profesores";
    }


    @GetMapping("/tutores")
    public String findAllTutores(@RequestParam(required = false, defaultValue = "0") int curso,
                                 @RequestParam(required = false, defaultValue = "0") int profesor,
                                 @RequestParam(required = false, defaultValue = "0") int page,
                                 @RequestParam(required = false, defaultValue = "15") int size,
                                 Model model) {
        model.addAttribute("cursoFiltro", curso);
        model.addAttribute("profesorFiltro", profesor);
        Page<TutorDTO> tutorDTOList;
        tutorDTOList = profesoresService.findPageTutoresByCursoAndProfesor(curso, profesor, PageRequest.of(page, size));
        model.addAttribute("page", tutorDTOList);

        List<ProfesorDTO> profesorDTOList = profesoresService.findAllProfesores();
        model.addAttribute("profesores", profesorDTOList);

        List<CursoDTO> cursoDTOList = cursosService.findAllCursos();
        model.addAttribute("cursos", cursoDTOList);
        return "tutores";
    }
}
