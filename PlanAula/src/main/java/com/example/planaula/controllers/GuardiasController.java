package com.example.planaula.controllers;

import com.example.planaula.Dto.*;
import com.example.planaula.services.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/{idCentro}/guardias")
public class GuardiasController {

    private final String RUTATEMPLATE = "guardias/";

    private final GuardiasService guardiasService;
    private final HorasService horasService;
    private final DiasService diasService;
    private final ProfesoresService profesoresService;

    public GuardiasController(GuardiasService guardiasService, HorasService horasService, DiasService diasService, ProfesoresService profesoresService) {
        this.guardiasService = guardiasService;
        this.horasService = horasService;
        this.diasService = diasService;
        this.profesoresService = profesoresService;
    }

    @GetMapping("")
    @PreAuthorize("@permisoService.tieneAccesoCentroFromPath(authentication, #this)")
    public String findAllGuardias(@PathVariable(name = "idCentro") int idCentro,
    							  @RequestParam(name="dia", required = false, defaultValue = "0") int dia,
                                  @RequestParam(name="hora", required = false, defaultValue = "0") int hora,
                                  @RequestParam(name="turno", required = false, defaultValue = "T") String turno,
                                  @RequestParam(name="profesor", required = false, defaultValue = "0") int profesor,
                                  @RequestParam(name="page", required = false, defaultValue = "0") int page,
                                  @RequestParam(name="size", required = false, defaultValue = "15") int size,
                                  @RequestParam(name="id", required = false, defaultValue = "0") String id,
                                  Model model) {
    	model.addAttribute("id", id);
    	model.addAttribute("idCentro", idCentro);
        model.addAttribute("diaFiltro", dia);
        model.addAttribute("horaFiltro", hora);
        model.addAttribute("turnoFiltro", turno);
        model.addAttribute("profesorFiltro", profesor);

        List<DiaDTO> diaDTOList = diasService.findAllDias(idCentro);
        model.addAttribute("dias", diaDTOList);

        List<HoraDTO> horaDTOList = horasService.findAllHoras(idCentro);
        model.addAttribute("horas", horaDTOList);

        List<ProfesorDTO> profesorDTOList = profesoresService.findAllProfesores(idCentro);
        model.addAttribute("profesores", profesorDTOList);

        model.addAttribute("turnos", mapaTurnos());

        Page<TurnoDTO> turnos = guardiasService.findPageTurnosByDiaHoraProfesor(dia, hora, profesor, turno, PageRequest.of(page, size), idCentro);
        model.addAttribute("page", turnos);

        model.addAttribute("turnoForm", new TurnoDTO());
        return "guardias";
    }

    @GetMapping("{id}")
    public String findGuardiaById(@PathVariable(name = "idCentro") int idCentro,
    							  @PathVariable(name="id") String id, Model model) {
        try {
            String[] partes = id.split("-");
            TurnoDTO guardiasDTO = guardiasService.findTurnoByTipoAndId(partes[0], Integer.parseInt(partes[1]));
            model.addAttribute("guardia", guardiasDTO);

            List<ProfesorDTO> profesorDTOList = profesoresService.findAllProfesores(idCentro);
            model.addAttribute("profesores", profesorDTOList);

            Map<String, String> mapaTurnos = this.mapaTurnos();
            model.addAttribute("turnos", mapaTurnos);
            return RUTATEMPLATE + "cardGuardias";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/anadir")
    public String addEspacio(@ModelAttribute TurnoDTO turnoDTO,
    						 @PathVariable(name = "idCentro") int idCentro,
                             @RequestParam(name="params", required= false) String params) {
        guardiasService.anadirTurno(turnoDTO, idCentro);
        return "redirect:/guardias?" + params;
    }

    @GetMapping("accion")
    public String accionesGuardias(@PathVariable(name = "idCentro") int idCentro,
    		  					   @RequestParam(name="accion") String accion,
                                   @RequestParam(name="turno") String turno,
                                   @RequestParam(name="profesor") Integer profesor,
                                   @RequestParam(name="id") Integer id) {
        try {
            guardiasService.accionGuardias(accion, turno, profesor, id);
            return "redirect:/guardias?id=" + id;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private Map<String, String> mapaTurnos(){
       return Map.of("G", "Guardia",  "L", "Libranza", "R", "Recreo");
    }

    private String obtenerValorCampoDinamico(TurnoDTO guardia, String nombreCampo) {
        try {
            Field campo = TurnoDTO.class.getDeclaredField(nombreCampo);
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
