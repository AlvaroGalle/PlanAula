package com.example.planaula.controllers;

import com.example.planaula.Dto.HorarioDTO;
import com.example.planaula.services.HorariosService;
import com.example.planaula.services.HorasService;
import com.example.planaula.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/menu")
public class MenuController {

    public final UserService userService;
    public final HorariosService horariosService;

    public MenuController(UserService userService, HorariosService horariosService) {
        this.userService = userService;
        this.horariosService = horariosService;
    }

    @GetMapping("")
    public String menu(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        /*UserDto user = userService.findUserByName(username);*/
        model.addAttribute("user", username);
        model.addAttribute("idUser", 1);

        List<HorarioDTO> horarioDTOList = horariosService.findAllHorariosByDiaAndHoraAndCurso(0, 0, 0, 0, 1, 0);
        List<HorarioDTO> future = horariosService.filtrarHorariosFuturos(horarioDTOList);
        List<HorarioDTO> tenTask = new ArrayList<>();
        for (int i = 0; i < 10 && i < future.size(); i++) {
            tenTask.add(future.get(i));
        }
        model.addAttribute("task", tenTask);

        List<String> colores = Arrays.asList("#ffeb99", "#ffe584", "#D3F8E2", "#ADD8E6", "#FFB6C1");
        model.addAttribute("colores", colores);

        LocalDate fechaActual = LocalDate.now();
        String diaSemana = fechaActual.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase();
        int dia = fechaActual.getDayOfMonth();
        String mes = fechaActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase();
        int anio = fechaActual.getYear();
        String fechaFormateada = String.format("%s, %d de %s de %d",
                diaSemana, dia, mes, anio);


        model.addAttribute("menuItems", new String[]{"Asignaturas", "Profesores", "Tutores", "Espacios", "Guardias", "Horarios"});
        model.addAttribute("fecha", fechaFormateada);
        return "menu";
    }
}
