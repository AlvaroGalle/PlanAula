package com.example.planaula.controllers;

import com.example.planaula.Dto.AsignaturaDTO;
import com.example.planaula.Dto.AulaDTO;
import com.example.planaula.Dto.CursoDTO;
import com.example.planaula.Dto.DiaDTO;
import com.example.planaula.Dto.HoraDTO;
import com.example.planaula.Dto.HorarioDTO;
import com.example.planaula.Dto.ProfesorDTO;
import com.example.planaula.services.AsignaturasService;
import com.example.planaula.services.AulasService;
import com.example.planaula.services.CursosService;
import com.example.planaula.services.DiasService;
import com.example.planaula.services.HorariosService;
import com.example.planaula.services.HorasService;
import com.example.planaula.services.ProfesoresService;
import com.example.planaula.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    private final HorasService horasService;
    private final DiasService diasService;
    private final CursosService cursosService;
    private final ProfesoresService profesoresService;
    private final HorariosService horariosService;
    private final AsignaturasService asignaturasService;
    private final AulasService aulasService; 

    public MenuController(UserService userService, HorasService horasService, DiasService diasService, CursosService cursosService, ProfesoresService profesoresService, HorariosService horariosService, AsignaturasService asignaturasService, AulasService aulasService) {
        this.userService = userService;
        this.horasService = horasService;
        this.diasService = diasService;
        this.cursosService = cursosService;
        this.profesoresService = profesoresService;
        this.horariosService = horariosService;
        this.asignaturasService = asignaturasService;
        this.aulasService = aulasService;
    }

    @GetMapping("")
    public String menu(@RequestParam(name="curso", required = false, defaultValue = "0") int curso,
            		   @RequestParam(name="profesor", required = false, defaultValue = "0") int profesor, 
            		   Model model) {
    	/*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
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
        model.addAttribute("colores", colores);*/

        LocalDate fechaActual = LocalDate.now();
        String diaSemana = fechaActual.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase();
        int dia = fechaActual.getDayOfMonth();
        String mes = fechaActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase();
        int anio = fechaActual.getYear();
        String fechaFormateada = String.format("%s, %d de %s de %d",
                diaSemana, dia, mes, anio);


        model.addAttribute("menuItems", new String[]{"Asignaturas", "Profesores", "Tutores", "Espacios", "Guardias", "Horarios"});
        model.addAttribute("fecha", fechaFormateada);
        
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
        
        List<HorarioDTO> horariosCursoDTOlist = horariosService.findAllHorariosByDiaAndHoraAndCurso(0, 0, curso, 0, profesor, 0);
        model.addAttribute("horarioCurso", horariosCursoDTOlist);
        
        model.addAttribute("cursoFiltro", curso);
        model.addAttribute("profesorFiltro", profesor);
        return "menu";
    }
}
