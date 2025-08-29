package com.example.planaula.controllers;

import com.example.planaula.Dto.*;
import com.example.planaula.services.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/espacios")
public class EspaciosController {

    private final String RUTATEMPLATE = "espacios/";

    private final EspaciosService espaciosService;
    private final AsignaturasService asignaturasService;
    private final HorasService horasService;
    private final DiasService diasService;
    private final AulasService aulasService;
    private final ProfesoresService profesoresService;
    private final CursosService cursosService;
    private final HorariosService horariosService;

    public EspaciosController(EspaciosService espaciosService, AsignaturasService asignaturasService, HorasService horasService, DiasService diasService, AulasService aulasService, ProfesoresService profesoresService, CursosService cursosService, HorariosService horariosService) {
        this.espaciosService = espaciosService;
        this.asignaturasService = asignaturasService;
        this.horasService = horasService;
        this.diasService = diasService;
        this.aulasService = aulasService;
        this.profesoresService = profesoresService;
        this.cursosService = cursosService;
        this.horariosService = horariosService;
    }

    @GetMapping("")
    public String findAllEspacios(@RequestParam(name = "dia", required = false, defaultValue = "0") int dia,
            					  @RequestParam(name = "hora", required = false, defaultValue = "0") int hora,
            					  @RequestParam(name = "aula", required = false, defaultValue = "0") int aula,
            					  @RequestParam(name = "asignatura", required = false, defaultValue = "0") int asignatura,
                                  @RequestParam(name="page", required = false, defaultValue = "0") int page,
                                  @RequestParam(name="size", required = false, defaultValue = "15") int size,
            					  @RequestParam(name = "id", required = false, defaultValue = "0") String id,
                                  Model model) {
    	model.addAttribute("id", id);
    	
        model.addAttribute("diaFiltro", dia);
        model.addAttribute("horaFiltro", hora);
        model.addAttribute("aulaFiltro", aula);
        model.addAttribute("asignaturaFiltro", asignatura);

        List<DiaDTO> diaDTOList =  diasService.findAllDias();
        model.addAttribute("dias", diaDTOList);

        List<HoraDTO> horaDTOList =  horasService.findAllHoras();
        model.addAttribute("horas", horaDTOList);

        List<AulaDTO> aulaDTOList =  aulasService.findAllAulas();
        model.addAttribute("aulas", aulaDTOList);

        List<ProfesorDTO> profesorDTOList = profesoresService.findAllProfesores();
        model.addAttribute("profesores", profesorDTOList);

        List<CursoDTO> cursosDTOList = cursosService.findAllCursos();
        model.addAttribute("cursos", cursosDTOList);

        for(AulaDTO a : aulaDTOList){
            if(a.getId() == aula){
                model.addAttribute("aula", a);
            }
        }

        List<AsignaturaDTO> asignaturaDTOList =  asignaturasService.findAllAsignaturas();
        model.addAttribute("asignaturas", asignaturaDTOList);
        for(AsignaturaDTO a : asignaturaDTOList){
            if(a.getId() == asignatura){
                model.addAttribute("asignatura", a);
            }
        }

        Page<EspacioDTO> espacioDTOList = espaciosService.findAllEspaciosByDiaAndHoraAndAulaAndAsignatura(dia,hora,asignatura,aula, PageRequest.of(page, size));
        model.addAttribute("page", espacioDTOList);
        return "espacios";
    }
    
    @GetMapping("{id}")
    public String findEspacioaById(@PathVariable(name="id") int id, Model model) {
        try {
            EspacioDTO espacioDTO = espaciosService.findEspacioById(id);
            model.addAttribute("espacio", espacioDTO);

            List<AulaDTO> aulaDTOList =  aulasService.findAllAulas();
            model.addAttribute("aulas", aulaDTOList);
            
            List<AsignaturaDTO> asignaturaDTOList =  asignaturasService.findAllAsignaturas();
            model.addAttribute("asignaturas", asignaturaDTOList);

            List<ProfesorDTO> profesorDTOList = profesoresService.findAllProfesores();
            model.addAttribute("profesores", profesorDTOList);

            List<CursoDTO> cursosDTOList = cursosService.findAllCursos();
            model.addAttribute("cursos", cursosDTOList);
            
            return RUTATEMPLATE + "cardEspacios";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/anadir")
    public String addEspacio(@ModelAttribute HorarioDTO horarioDTO,
                                @RequestParam(name="params", required= false) String params) {
        horariosService.anadirHorario(horarioDTO);
        return "redirect:/espacios?" + params;
    }
}
