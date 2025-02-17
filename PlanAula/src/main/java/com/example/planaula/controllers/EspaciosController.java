package com.example.planaula.controllers;

import com.example.planaula.Dto.*;
import com.example.planaula.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/espacios")
public class EspaciosController {

    private final EspaciosService espaciosService;
    private final AsignaturasService asignaturasService;
    private final HorasService horasService;
    private final DiasService diasService;
    private final AulasService aulasService;

    public EspaciosController(EspaciosService espaciosService, AsignaturasService asignaturasService, HorasService horasService, DiasService diasService, AulasService aulasService) {
        this.espaciosService = espaciosService;
        this.asignaturasService = asignaturasService;
        this.horasService = horasService;
        this.diasService = diasService;
        this.aulasService = aulasService;
    }

    @GetMapping("")
    public String findAllEspacios(@RequestParam(required = false, defaultValue = "0") int dia,
                                  @RequestParam(required = false, defaultValue = "0") int hora,
                                  @RequestParam(required = false, defaultValue = "0") int aula,
                                  @RequestParam(required = false, defaultValue = "0") int asignatura, Model model) {
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

        List<AsignaturaDTO> asignaturaDTOList =  asignaturasService.findAllAsignaturas();
        model.addAttribute("asignaturas", asignaturaDTOList);

        List<EspacioDTO> espacioDTOList = espaciosService.findAllEspaciosByDiaAndHoraAndAulaAndAsignatura(dia,hora,asignatura);


        if(!espacioDTOList.isEmpty() && aula != 0){
            AulaDTO aulaDTO = aulasService.findAulaByid(aula);
            String campo = normalizarNombreCampo(aulaDTO.getNombre());

            espacioDTOList = espacioDTOList.stream()
                    .filter(espacio -> !estaVacio(obtenerValorCampoDinamico(espacio, campo)))
                    .collect(Collectors.toList());
        }

        model.addAttribute("espacios", espacioDTOList);
        return "espacios";
    }

private String normalizarNombreCampo(String nombreAula) {
    return nombreAula.toLowerCase()
            .replaceAll("\\s+", "")
            .replaceAll("á", "a")
            .replaceAll("é", "e")
            .replaceAll("í", "i")
            .replaceAll("ó", "o")
            .replaceAll("ú", "u");
}

private String obtenerValorCampoDinamico(EspacioDTO espacio, String nombreCampo) {
    try {
        Field campo = EspacioDTO.class.getDeclaredField(nombreCampo);
        campo.setAccessible(true);
        Object valor = campo.get(espacio);
        return valor != null ? valor.toString() : null;
    } catch (NoSuchFieldException | IllegalAccessException e) {
        return null;
    }
}

private boolean estaVacio(String valor) {
    return valor == null || valor.trim().isEmpty();
}
}
