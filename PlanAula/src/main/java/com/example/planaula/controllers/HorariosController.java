package com.example.planaula.controllers;

import com.example.planaula.Dto.*;
import com.example.planaula.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String findHorarios(@RequestParam(required = false, defaultValue = "0") int dia,
                                         @RequestParam(required = false, defaultValue = "0") int hora,
                                         @RequestParam(required = false, defaultValue = "0") String turno,
                                         @RequestParam(required = false, defaultValue = "0") int curso,
                                         @RequestParam(required = false, defaultValue = "0") int asignatura,
                                         @RequestParam(required = false, defaultValue = "0") int profesor,
                                         @RequestParam(required = false, defaultValue = "0") int aula,
                                         @RequestParam(required = false, defaultValue = "lista") String active,
                                         Model model) {
        model.addAttribute("active", active);

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

        List<GuardiasDTO> guardiasDTOList = guardiasService.findAllGuardiasByDiaAndHoraAndProfesor(dia,hora,profesor);

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
        model.addAttribute("guardias",guardiasDTOList);

        List<HorarioDTO> horarioDTOList = horariosService.findAllHorariosByDiaAndHoraAndCurso(dia, hora, curso, asignatura, profesor, aula);
        model.addAttribute("page",horarioDTOList);
        
        int tableCurso = curso != 0 ? curso : 1;
        List<HorarioDTO> horariosCursoDTOlist = horariosService.findAllHorariosByDiaAndHoraAndCurso(0, 0, tableCurso, 0, 0, 0);
        model.addAttribute("tableCurso", tableCurso);
        model.addAttribute("horarioCurso", horariosCursoDTOlist );
        return "horarios";
    }

        private String obtenerValorCampoDinamico(GuardiasDTO guardia, String nombreCampo) {
            try {
                Field campo = GuardiasDTO.class.getDeclaredField(nombreCampo);
                campo.setAccessible(true);
                Object valor = campo.get(guardia);
                return valor != null ? valor.toString() : null;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return null;
            }
        }

        private boolean estaVacio(String valor) {
            return valor == null || valor.trim().isEmpty();
        }
}
