
package com.example.planaula.controllers;

import com.example.planaula.Dto.*;
import com.example.planaula.services.*;

import jakarta.transaction.TransactionalException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/horarios")
public class HorariosController {

    private final GuardiasService guardiasService;
    private final HorasService horasService;
    private final DiasService diasService;
    private final CursosService cursosService;
    private final ProfesoresService profesoresService;
    private final HorariosService horariosService;
    private final AsignaturasService asignaturasService;
    private final AulasService aulasService;

    public HorariosController(GuardiasService guardiasService, HorasService horasService, DiasService diasService, CursosService cursosService, ProfesoresService profesoresService, HorariosService horariosService, AsignaturasService asignaturasService, AulasService aulasService) {
        this.guardiasService = guardiasService;
        this.horasService = horasService;
        this.diasService = diasService;
        this.cursosService = cursosService;
        this.profesoresService = profesoresService;
        this.horariosService = horariosService;
        this.asignaturasService = asignaturasService;
        this.aulasService = aulasService;
    }

    @GetMapping("")
    public String findHorarios(@RequestParam(name="dia", required = false, defaultValue = "0") int dia,
    						   @RequestParam(name="hora", required = false, defaultValue = "0") int hora,
                               @RequestParam(name="turno", required = false, defaultValue = "0") String turno,
                               @RequestParam(name="curso", required = false, defaultValue = "0") int curso,
                               @RequestParam(name="asignatura", required = false, defaultValue = "0") int asignatura,
                               @RequestParam(name="profesor", required = false, defaultValue = "0") int profesor,
                               @RequestParam(name="aula", required = false, defaultValue = "0") int aula,
                               @RequestParam(name="tab", required = false, defaultValue = "lista") String tab,
                                         Model model) {
        model.addAttribute("tab", tab);

        model.addAttribute("diaFiltro", dia);
        model.addAttribute("horaFiltro", hora);
        model.addAttribute("turnoFiltro", turno);
        model.addAttribute("cursoFiltro", curso);
        model.addAttribute("profesorFiltro", profesor);
        model.addAttribute("aulaFiltro", aula);
        model.addAttribute("asignaturaFiltro", asignatura);

        List<DiaDTO> diaDTOList =  diasService.findAllDias();
        model.addAttribute("dias", diaDTOList);

        List<HoraDTO> horaDTOList =  horasService.findAllHoras();
        model.addAttribute("horas", horaDTOList);

        List<CursoDTO> cursoDTOList =  cursosService.findAllCursos();
        model.addAttribute("cursos", cursoDTOList);

        List<ProfesorDTO> profesorDTOList =  profesoresService.findAllProfesores();
        model.addAttribute("profesores", profesorDTOList);

        List<AulaDTO> aulaDTOList =  aulasService.findAllAulas();
        model.addAttribute("aulas", aulaDTOList);

        List<AsignaturaDTO> asignaturaDTOList =  asignaturasService.findAllAsignaturas();
        model.addAttribute("asignaturas", asignaturaDTOList);

        /*List<GuardiasDTO> guardiasDTOList = guardiasService.findAllGuardiasByDiaAndHoraAndProfesor(dia,hora,profesor);

        Map<String, String> mapaTurnos = Map.of(
                "G1", "guardia1", "G2", "guardia2", "G3", "guardia3",
                "L1", "libranza1", "L2", "libranza2", "L3", "libranza3",
                "R1", "recreo1", "R2", "recreo2", "R3", "recreo3"
        );
        model.addAttribute("turnos", mapaTurnos);

        if(!guardiasDTOList.isEmpty() && !Objects.equals(turno, "0")) {
            String campo = mapaTurnos.getOrDefault(turno, "");

            guardiasDTOList = guardiasDTOList.stream()
                    .filter(guardia -> !estaVacio(obtenerValorCampoDinamico(guardia, campo)))
                    .collect(Collectors.toList());
        }
        model.addAttribute("guardias",guardiasDTOList);*/

        List<HorarioDTO> horarioDTOList = horariosService.findAllHorariosByDiaAndHoraAndCurso(dia, hora, curso, asignatura, profesor, aula);
        model.addAttribute("page",horarioDTOList);
        
        int tableCurso = curso != 0 ? curso : 1;
        List<HorarioDTO> horariosCursoDTOlist = horariosService.findAllHorariosByDiaAndHoraAndCurso(0, 0, tableCurso, 0, 0, 0);
        model.addAttribute("tableCurso", tableCurso);
        model.addAttribute("horarioCurso", horariosCursoDTOlist);
        model.addAttribute("accionesHorario", new HorarioDTO());
        return "horarios";
    }
    
    @GetMapping("/{id}")
    @ResponseBody
    public HorarioDTO findHorarioById(@PathVariable(name="id") int id) {
    	return horariosService.findHorarioById(id);
    }
    
    @PostMapping("/accion")
    public String accionHorario(@RequestParam(name="search") String search,
    							@ModelAttribute HorarioDTO accionesHorario) {
    	try{
    		if(accionesHorario.getId() == 0) {
        		horariosService.anadirHorario(accionesHorario);
        	}else {
        		horariosService.editarHorario(accionesHorario);
        	}	
    	}catch(TransactionalException e){
    		e.printStackTrace();
    	}
    	return "redirect:/horarios" + search;
    }
    
    @PostMapping("/eliminar")
    public String eliminarHorario(@RequestParam(name="id") int id,
    							  @RequestParam(name="search") String search) {
	try {
		horariosService.eliminarHorario(id);	
    }catch(TransactionalException e){
		e.printStackTrace();
	}
    	return "redirect:/horarios" + search;
    }
}
