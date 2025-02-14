package com.example.planaula.controllers;

import com.example.planaula.Dto.DiaDTO;
import com.example.planaula.Dto.GuardiasDTO;
import com.example.planaula.Dto.HoraDTO;
import com.example.planaula.Dto.ProfesorDTO;
import com.example.planaula.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
                                                @RequestParam(required = false, defaultValue = "0") int profesor,
                                                Model model) {
        model.addAttribute("diaFiltro", dia);
        model.addAttribute("horaFiltro", hora);
        model.addAttribute("profesorFiltro", profesor);

        List<DiaDTO> diaDTOList =  diasService.findAllDias();
        model.addAttribute("dias", diaDTOList);

        List<HoraDTO> horaDTOList =  horasService.findAllHoras();
        model.addAttribute("horas", horaDTOList);

        List<ProfesorDTO> profesorDTOList =  profesoresService.findAllProfesores();
        model.addAttribute("profesores", profesorDTOList);

        List<GuardiasDTO> guardiasDTOList = guardiasService.findAllGuardiasByDiaAndHoraAndProfesor(dia,hora,profesor);
        model.addAttribute("guardias",guardiasDTOList);
        return "guardias";
    }
}
