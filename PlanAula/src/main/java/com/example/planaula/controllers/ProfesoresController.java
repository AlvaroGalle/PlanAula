package com.example.planaula.controllers;

import com.example.planaula.Dto.CursoDTO;
import com.example.planaula.Dto.ProfesorDTO;
import com.example.planaula.Dto.TutorDTO;
import com.example.planaula.services.CursosService;
import com.example.planaula.services.ProfesoresService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
    public String findAllProfesores(@RequestParam(name="page", required = false, defaultValue = "0") int page,
                                    @RequestParam(name="size", required = false, defaultValue = "15") int size,
                                    Model model) {
        Page<ProfesorDTO> profesorDTOList = profesoresService.findPageProfesores(PageRequest.of(page, size));
        model.addAttribute("page", profesorDTOList);
        model.addAttribute("profesorForm", new ProfesorDTO());
        return "profesores";
    }

    @PostMapping("")
    public String accionProfesores(@ModelAttribute ProfesorDTO profesorDTO,
    							   @RequestParam(name="accion", required = false, defaultValue = "A") String accion) {
    	String[] apellidos = profesorDTO.getNombre().split(" ");
    	profesorDTO.setNombre(apellidos[0]);
    	for(int i = 1; i<apellidos.length; i++) {
    		profesorDTO.setApellidos(profesorDTO.getApellidos() + apellidos[i] + " ");
    	}
        switch (accion) {
            case "A":
                profesoresService.anadirProfesor(profesorDTO);
                break;
            case "M":
                profesoresService.modificarProfesor(profesorDTO);
                break;
            case "E":
                profesoresService.eliminarProfesorById(profesorDTO.getId());
                break;
        }
        return "redirect:/profesores";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ProfesorDTO findProfesorById(@PathVariable(name= "id") Integer id) {
        return profesoresService.findProfesorById(id);
    }

    @GetMapping("/tutores")
    public String findAllTutores(@RequestParam(name="curso", required = false, defaultValue = "0") int curso,
                                 @RequestParam(name="profesor", required = false, defaultValue = "0") int profesor,
                                 @RequestParam(name="page", required = false, defaultValue = "0") int page,
                                 @RequestParam(name="size", required = false, defaultValue = "15") int size,
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

    @GetMapping("/tutores/modificar/{id}")
    @ResponseBody
    public String modificarTutor(@PathVariable Integer id,
                                 @RequestParam String curso,
                                 @RequestParam String profesor) {
        /*TutorDTO tutorDTO = new TutorDTO(id, null, Objects.equals(curso, "2425") ? profesor : null,  Objects.equals(curso, "2324") ? profesor : null);*/
        /*profesoresService.modificarTutor(tutorDTO);*/
        return "tutores";
    }
}
