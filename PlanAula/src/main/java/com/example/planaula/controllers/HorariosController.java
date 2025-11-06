package com.example.planaula.controllers;

import com.example.planaula.Dto.*;
import com.example.planaula.services.*;
import jakarta.transaction.TransactionalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/{idCentro}/horarios")
public class HorariosController {

    private final String RUTATEMPLATE = "horarios/";

    private final EspaciosService espaciosService;
    private final AsignaturasService asignaturasService;
    private final HorasService horasService;
    private final DiasService diasService;
    private final AulasService aulasService;
    private final ProfesoresService profesoresService;
    private final CursosService cursosService;
    private final HorariosService horariosService;

    public HorariosController(EspaciosService espaciosService, AsignaturasService asignaturasService, HorasService horasService, DiasService diasService, AulasService aulasService, ProfesoresService profesoresService, CursosService cursosService, HorariosService horariosService) {
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
    @PreAuthorize("@permisoService.tieneAccesoCentroFromPath(authentication, #this)")
    public String findAllHorarios(@PathVariable(name = "idCentro") int idCentro,
    							  @RequestParam(name = "dia", required = false, defaultValue = "0") int dia,
                                  @RequestParam(name = "hora", required = false, defaultValue = "0") int hora,
                                  @RequestParam(name = "aula", required = false, defaultValue = "0") int aula,
                                  @RequestParam(name = "asignatura", required = false, defaultValue = "0") int asignatura,
                                  @RequestParam(name = "profesor", required = false, defaultValue = "0") int profesor,
                                  @RequestParam(name = "curso", required = false, defaultValue = "0") int curso,
                                  @RequestParam(name="page", required = false, defaultValue = "0") int page,
                                  @RequestParam(name="size", required = false, defaultValue = "15") int size,
                                  @RequestParam(name = "id", required = false, defaultValue = "0") String id,
                                  Model model) {
        model.addAttribute("id", id);
        model.addAttribute("idCentro", idCentro);
        model.addAttribute("diaFiltro", dia);
        model.addAttribute("horaFiltro", hora);
        model.addAttribute("aulaFiltro", aula);
        model.addAttribute("asignaturaFiltro", asignatura);
        model.addAttribute("profesorFiltro", profesor);
        model.addAttribute("cursoFiltro", curso);

        List<DiaDTO> diaDTOList =  diasService.findAllDias(idCentro);
        model.addAttribute("dias", diaDTOList);

        List<HoraDTO> horaDTOList =  horasService.findAllHoras(idCentro);
        model.addAttribute("horas", horaDTOList);

        List<AulaDTO> aulaDTOList =  aulasService.findAllAulas(idCentro);
        model.addAttribute("aulas", aulaDTOList);
        for(AulaDTO a : aulaDTOList){
            if(a.getId() == aula){
                model.addAttribute("aula", a);
            }
        }
        
        List<ProfesorDTO> profesorDTOList = profesoresService.findAllProfesores(idCentro);
        model.addAttribute("profesores", profesorDTOList);
        for(ProfesorDTO p : profesorDTOList){
            if(p.getId() == profesor){
                model.addAttribute("profesor", p);
            }
        }
        
        List<CursoDTO> cursosDTOList = cursosService.findAllCursos(idCentro);
        model.addAttribute("cursos", cursosDTOList);
        for(CursoDTO c : cursosDTOList){
            if(c.getId() == curso){
                model.addAttribute("curso", c);
            }
        }
        

        List<AsignaturaDTO> asignaturaDTOList =  asignaturasService.findAllAsignaturas(idCentro);
        model.addAttribute("asignaturas", asignaturaDTOList);
        for(AsignaturaDTO a : asignaturaDTOList){
            if(a.getId() == asignatura){
                model.addAttribute("asignatura", a);
            }
        }

        Page<EspacioDTO> espacioDTOList = espaciosService.findAllEspaciosByDiaAndHoraAndAulaAndAsignatura(dia,hora,asignatura,aula,profesor,curso, PageRequest.of(page, size), idCentro);
        model.addAttribute("page", espacioDTOList);
        return "horarios";
    }

    @GetMapping("{id}")
    public String findHorarioById(@PathVariable(name = "idCentro") int idCentro,
    							  @PathVariable(name="id") int id, Model model) {
        try {

            EspacioDTO espacioDTO = espaciosService.findEspacioById(id);
            model.addAttribute("espacio", espacioDTO);

            List<DiaDTO> diaDTOList =  diasService.findAllDias(idCentro);
            model.addAttribute("dias", diaDTOList);

            List<HoraDTO> horaDTOList =  horasService.findAllHoras(idCentro);
            model.addAttribute("horas", horaDTOList);

            List<AulaDTO> aulaDTOList =  aulasService.findAllAulas(idCentro);
            model.addAttribute("aulas", aulaDTOList);

            List<AsignaturaDTO> asignaturaDTOList =  asignaturasService.findAllAsignaturas(idCentro);
            model.addAttribute("asignaturas", asignaturaDTOList);

            List<ProfesorDTO> profesorDTOList = profesoresService.findAllProfesores(idCentro);
            model.addAttribute("profesores", profesorDTOList);

            List<CursoDTO> cursosDTOList = cursosService.findAllCursos(idCentro);
            model.addAttribute("cursos", cursosDTOList);

            return RUTATEMPLATE + "cardHorarios";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/anadir")
    public String addHorario(@ModelAttribute HorarioDTO horarioDTO,
    						 @PathVariable(name = "idCentro") int idCentro,
                             @RequestParam(name="search", required = false, defaultValue = "") String search) {
        horariosService.anadirHorario(horarioDTO, idCentro);
        return "redirect:/horarios" + search;
    }

    @PostMapping("/accion")
    public String accionHorario(@PathVariable(name = "idCentro") int idCentro,
    							@RequestParam(name="search", required = false, defaultValue = "") String search,
                                @ModelAttribute HorarioDTO accionesHorario) {
        try{
            if(accionesHorario.getId() == 0) {
                horariosService.anadirHorario(accionesHorario, idCentro);
            }else {
                horariosService.editarHorario(accionesHorario);
            }
        }catch(TransactionalException e){
            e.printStackTrace();
        }
        return "redirect:/horarios" + search;
    }

    @PostMapping("/eliminar")
    public String eliminarHorario(@PathVariable(name = "idCentro") int idCentro,
    							  @RequestParam(name="id") int id,
                                  @RequestParam(name="search") String search) {
        try {
            horariosService.eliminarHorario(id);
        }catch(TransactionalException e){
            e.printStackTrace();
        }
        return "redirect:/horarios" + search;
    }
}
