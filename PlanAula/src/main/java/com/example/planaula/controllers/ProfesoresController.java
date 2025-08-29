package com.example.planaula.controllers;

import com.example.planaula.Dto.*;
import com.example.planaula.services.CursosService;
import com.example.planaula.services.GuardiasService;
import com.example.planaula.services.HorariosService;
import com.example.planaula.services.ProfesoresService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/profesores")
public class ProfesoresController {

    private final ProfesoresService profesoresService;
    private final CursosService cursosService;
    private final HorariosService horariosService;
    private final GuardiasService guardiasService;

    public ProfesoresController(ProfesoresService profesoresService, CursosService cursosService, HorariosService horariosService, GuardiasService guardiasService) {
        this.profesoresService = profesoresService;
        this.cursosService = cursosService;
        this.horariosService = horariosService;
        this.guardiasService = guardiasService;
    }

    @GetMapping("")
    public String findAllProfesores(@RequestParam(name="page", required = false, defaultValue = "0") int page,
                                    @RequestParam(name="size", required = false, defaultValue = "15") int size,
                                    Model model) {
        Page<ProfesorDTO> profesorDTOList = profesoresService.findPageProfesores(PageRequest.of(page, size));
        model.addAttribute("page", profesorDTOList);
        model.addAttribute("profesorForm", new ProfesorDTO());
        return "profesores";
    }

    @PostMapping("")
    public String accionProfesores(@ModelAttribute ProfesorDTO profesorDTO,
    							   @RequestParam(name="accion", required = false, defaultValue = "A") String accion) {
    	String[] apellidos = profesorDTO.getNombre().split(" ");
    	profesorDTO.setNombre(apellidos[0]);
    	for(int i = 1; i<apellidos.length; i++) {
    		profesorDTO.setApellidos(profesorDTO.getApellidos() + apellidos[i] + " ");
    	}
        switch (accion) {
            case "A":
                profesoresService.anadirProfesor(profesorDTO);
                break;
            case "M":
                profesoresService.modificarProfesor(profesorDTO);
                break;
            case "E":
                profesoresService.eliminarProfesorById(profesorDTO.getId());
                break;
        }
        return "redirect:/profesores";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ProfesorDTO findProfesorById(@PathVariable(name= "id") Integer id) {
        return profesoresService.findProfesorById(id);
    }

    @GetMapping("/tutores")
    public String findAllTutores(@RequestParam(name="curso", required = false, defaultValue = "0") int curso,
                                 @RequestParam(name="profesor", required = false, defaultValue = "0") int profesor,
                                 @RequestParam(name="page", required = false, defaultValue = "0") int page,
                                 @RequestParam(name="size", required = false, defaultValue = "15") int size,
                                 Model model) {
        model.addAttribute("cursoFiltro", curso);
        model.addAttribute("profesorFiltro", profesor);
        Page<TutorDTO> tutorDTOList;
        tutorDTOList = profesoresService.findPageTutoresByCursoAndProfesor(curso, profesor, PageRequest.of(page, size));
        model.addAttribute("page", tutorDTOList);

        List<ProfesorDTO> profesorDTOList = profesoresService.findAllProfesores();
        model.addAttribute("profesores", profesorDTOList);

        List<CursoDTO> cursoDTOList = cursosService.findAllCursos();
        model.addAttribute("cursos", cursoDTOList);
        return "tutores";
    }

    @GetMapping("/tutores/modificar/{id}")
    @ResponseBody
    public String modificarTutor(@PathVariable Integer id,
                                 @RequestParam String curso,
                                 @RequestParam String profesor) {
        /*TutorDTO tutorDTO = new TutorDTO(id, null, Objects.equals(curso, "2425") ? profesor : null,  Objects.equals(curso, "2324") ? profesor : null);*/
        /*profesoresService.modificarTutor(tutorDTO);*/
        return "tutores";
    }

    @GetMapping("/suplencias")
    public String suplenciaProfesor(@RequestParam(name="fecha", required = false, defaultValue = "") String fecha,
                                    @RequestParam(name="profesor", required = false, defaultValue = "0") int profesor,
                                    Model model) {
        model.addAttribute("fechaFiltro", fecha);
        model.addAttribute("profesorFiltro", profesor);

        List<ProfesorDTO> profesorDTOList = profesoresService.findAllProfesores();
        model.addAttribute("profesores", profesorDTOList);

        if (!fecha.isEmpty() && profesor != 0) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(fecha , formatter);
            model.addAttribute("dia", date.getDayOfWeek());
            List<HorarioDTO> horarioDTO = horariosService.findHorarioByProfesorAndDia(profesor, date.getDayOfWeek().getValue());
            model.addAttribute("horarios", horarioDTO);
        }
        return "suplencias";
    }

    @PostMapping("/suplencias/pdf")
    public ResponseEntity<byte[]> generarPdfHorario(@RequestParam(name="fecha") String fecha,
                                                    @RequestParam(name="profesor") int id_profesor,
                                                    @RequestBody Map<String, String> mapSuplencias)
    {
        ProfesorDTO profesor = profesoresService.findProfesorById(id_profesor);

        String titulo = "Suplencias " + profesor.getNombre() + " " + fecha;
        try (PDDocument doc = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PDPage pagina = new PDPage(PDRectangle.A4);
            doc.addPage(pagina);

            float pageWidth  = pagina.getMediaBox().getWidth();
            float pageHeight = pagina.getMediaBox().getHeight();

            float margin = 50f;
            float sectionSpacing = 24f;       // espacio entre secciones
            float sectionHeight = (pageHeight - (margin * 2) - sectionSpacing) / 2f;

            float startY1 = pageHeight - margin;               // inicio sección 1 (arriba)
            float startY2 = startY1 - sectionHeight - sectionSpacing; // inicio sección 2 (abajo)

            try (PDPageContentStream contenido = new PDPageContentStream(doc, pagina)) {
                // Dibuja ambas secciones con el mismo contenido
                drawSection(contenido, pageWidth, margin, startY1, titulo, mapSuplencias);
                drawSection(contenido, pageWidth, margin, startY2, titulo, mapSuplencias);

                // (Opcional) línea separadora entre secciones
                float sepY = startY1 - sectionHeight - (sectionSpacing / 2f);
                contenido.moveTo(margin, sepY);
                contenido.lineTo(pageWidth - margin, sepY);
                contenido.setLineWidth(0.5f);
                contenido.stroke();
            }

            doc.save(baos);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=suplencias.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(baos.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(("Error al generar PDF: " + e.getMessage()).getBytes());
        }
    }

    private void drawSection(PDPageContentStream cs,
                             float pageWidth,
                             float margin,
                             float startY,
                             String titulo,
                             Map<String, String> mapSuplencias) throws IOException {

        // Título
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
        cs.newLineAtOffset(margin, startY);
        cs.showText(titulo);
        cs.endText();

        // Cabecera "tipo tabla" (simple)
        float y = startY - 22f; // salto bajo el título
        float numColX = margin;
        float textoColX = margin + 40f; // segunda "columna" visual

        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
        cs.newLineAtOffset(textoColX, y);
        cs.showText("Horario");
        cs.endText();

        // Línea bajo cabecera
        y -= 6f;
        cs.moveTo(margin, y);
        cs.lineTo(pageWidth - margin, y);
        cs.setLineWidth(0.5f);
        cs.stroke();

        // Filas
        float lineHeight = 18f;
        y -= 12f;

        int i = 1;
        for(Map.Entry<String,String> e: mapSuplencias.entrySet()) {

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.newLineAtOffset(numColX, y);

            String[] key = e.getKey().split("_");
            ProfesorDTO profesorDTO = new ProfesorDTO();
            if(!Objects.equals(e.getValue(), "0")){
             profesorDTO = profesoresService.findProfesorById(Integer.parseInt(e.getValue()));
            }
            String text;
            if (key[0].equals("Clase")) {
                HorarioDTO horarioDTO = horariosService.findHorarioById(Integer.parseInt(key[1]));
                text = profesorDTO.getNombre() + " | " + horarioDTO.getCurso() + " | " + horarioDTO.getHora() + " | " + horarioDTO.getCurso() + " | " + horarioDTO.getAsignatura() + " | " + horarioDTO.getEspacio();
            } else {
                TurnoDTO turnoDTO = guardiasService.findTurnoByTipoAndId(key[0], Integer.parseInt(key[1]));
                text = profesorDTO.getNombre() + " | " + turnoDTO.getTipo() + " | " + turnoDTO.getHora();
            }
            cs.showText(text);
            cs.endText();

            float rowBottom = y - 4f;
            cs.moveTo(margin, rowBottom);
            cs.lineTo(pageWidth - margin, rowBottom);
            cs.setLineWidth(0.25f);
            cs.stroke();

            y -= lineHeight;
            i++;
        }
    }

}
