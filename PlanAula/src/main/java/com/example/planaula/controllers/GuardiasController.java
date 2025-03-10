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
@RequestMapping("/guardias")
public class GuardiasController {

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
    public String findAllGuardias(@RequestParam(required = false, defaultValue = "0") int dia,
                                                @RequestParam(required = false, defaultValue = "0") int hora,
                                                @RequestParam(required = false, defaultValue = "0") String turno,
                                                @RequestParam(required = false, defaultValue = "0") int profesor,
                                                Model model) {
        model.addAttribute("diaFiltro", dia);
        model.addAttribute("horaFiltro", hora);
        model.addAttribute("turnoFiltro", turno);
        model.addAttribute("profesorFiltro", profesor);

        List<DiaDTO> diaDTOList =  diasService.findAllDias();
        model.addAttribute("dias", diaDTOList);

        List<HoraDTO> horaDTOList =  horasService.findAllHoras();
        model.addAttribute("horas", horaDTOList);

        List<ProfesorDTO> profesorDTOList =  profesoresService.findAllProfesores();
        model.addAttribute("profesores", profesorDTOList);
        for(ProfesorDTO p : profesorDTOList){
            if(p.getId() == profesor){
                model.addAttribute("profesor", p);
            }
        }

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
        model.addAttribute("page",guardiasDTOList);
        return "guardias";
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
