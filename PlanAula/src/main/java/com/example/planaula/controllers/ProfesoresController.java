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
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/{idCentro}/profesores")
public class ProfesoresController {

	private final ProfesoresService profesoresService;
	private final CursosService cursosService;
	private final HorariosService horariosService;
	private final GuardiasService guardiasService;

	public ProfesoresController(ProfesoresService profesoresService, CursosService cursosService,
			HorariosService horariosService, GuardiasService guardiasService) {
		this.profesoresService = profesoresService;
		this.cursosService = cursosService;
		this.horariosService = horariosService;
		this.guardiasService = guardiasService;
	}

	@GetMapping("")
	@PreAuthorize("@permisoService.tieneAccesoCentroFromPath(authentication, #this)")
	public String findAllProfesores(@PathVariable(name = "idCentro") int idCentro,
								    @RequestParam(name = "page", required = false, defaultValue = "0") int page,
									@RequestParam(name = "size", required = false, defaultValue = "15") int size, Model model) {
		model.addAttribute("idCentro", idCentro);
		Page<ProfesorDTO> profesorDTOList = profesoresService.findPageProfesores(PageRequest.of(page, size), idCentro);
		model.addAttribute("page", profesorDTOList);
		model.addAttribute("profesorForm", new ProfesorDTO());
		return "profesores";
	}

	@PostMapping("")
	public String accionProfesores(@PathVariable(name = "idCentro") int idCentro,
								   @ModelAttribute ProfesorDTO profesorDTO,
								   @RequestParam(name = "accion", required = false, defaultValue = "A") String accion) {
		String[] apellidos = profesorDTO.getNombre().split(" ");
		profesorDTO.setNombre(apellidos[0]);
		for (int i = 1; i < apellidos.length; i++) {
			profesorDTO.setApellidos(profesorDTO.getApellidos() + apellidos[i] + " ");
		}
		switch (accion) {
		case "A":
			profesoresService.anadirProfesor(profesorDTO, idCentro);
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
	public ProfesorDTO findProfesorById(@PathVariable(name = "id") Integer id) {
		return profesoresService.findProfesorById(id);
	}

	@GetMapping("/tutores")
	@PreAuthorize("@permisoService.tieneAccesoCentroFromPath(authentication, #this)")
	public String findAllTutores(@PathVariable(name = "idCentro") int idCentro,
								 @RequestParam(name = "curso", required = false, defaultValue = "0") int curso,
								 @RequestParam(name = "profesor", required = false, defaultValue = "0") int profesor,
								 @RequestParam(name = "page", required = false, defaultValue = "0") int page,
								 @RequestParam(name = "size", required = false, defaultValue = "15") int size, Model model) {
		model.addAttribute("cursoFiltro", curso);
		model.addAttribute("profesorFiltro", profesor);
		model.addAttribute("idCentro", idCentro);
		Page<TutorDTO> tutorDTOList;
		tutorDTOList = profesoresService.findPageTutoresByCursoAndProfesor(curso, profesor, PageRequest.of(page, size), idCentro);
		model.addAttribute("page", tutorDTOList);

		List<ProfesorDTO> profesorDTOList = profesoresService.findAllProfesores(idCentro);
		model.addAttribute("profesores", profesorDTOList);

		List<CursoDTO> cursoDTOList = cursosService.findAllCursos(idCentro);
		model.addAttribute("cursos", cursoDTOList);
		return "tutores";
	}

	@GetMapping("/tutores/modificar/{id}")
	@ResponseBody
	public String modificarTutor(@PathVariable(name = "idCentro") int idCentro,
								 @PathVariable Integer id, 
								 @RequestParam String curso,
								 @RequestParam String profesor) {
		/*
		 * TutorDTO tutorDTO = new TutorDTO(id, null, Objects.equals(curso, "2425") ?
		 * profesor : null, Objects.equals(curso, "2324") ? profesor : null);
		 */
		/* profesoresService.modificarTutor(tutorDTO); */
		return "tutores";
	}

	@GetMapping("/suplencias")
	@PreAuthorize("@permisoService.tieneAccesoCentroFromPath(authentication, #this)")
	public String suplenciaProfesor(@PathVariable(name = "idCentro") int idCentro,
									@RequestParam(name = "fecha", required = false, defaultValue = "") String fecha,
									@RequestParam(name = "profesor", required = false, defaultValue = "0") int profesor, Model model) {
		model.addAttribute("fechaFiltro", fecha);
		model.addAttribute("profesorFiltro", profesor);
		model.addAttribute("idCentro", idCentro);
		List<ProfesorDTO> profesorDTOList = profesoresService.findAllProfesores(idCentro);
		model.addAttribute("profesores", profesorDTOList);

		if (!fecha.isEmpty() && profesor != 0) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate date = LocalDate.parse(fecha, formatter);
			model.addAttribute("dia", date.getDayOfWeek());
			List<HorarioDTO> horarioDTO = horariosService.findHorarioByProfesorAndDia(profesor,
					date.getDayOfWeek().getValue(), idCentro);
			model.addAttribute("horarios", horarioDTO);
			List<TurnoDTO> turnoDTO = guardiasService.findAllTurnosByDiaHoraProfesor(date.getDayOfWeek().getValue(), 0,
					0, "T", idCentro);
			model.addAttribute("turnos", turnoDTO);

			Map<String, String> turnoMap = new HashMap<>();
			for (TurnoDTO turno : turnoDTO) {
				String key = turno.getHora() + "_" + turno.getNombre();
				String tipo = turno.getTipo();
				String cssClass = switch (tipo) {
				case "Guardia" -> "text-danger";
				case "Libranza" -> "text-success";
				case "Recreo" -> "text-secondary";
				default -> "";
				};
				turnoMap.put(key, cssClass);
			}
			model.addAttribute("turnoMap", turnoMap);
		}
		return "suplencias";
	}

	@PostMapping("/suplencias/pdf")
	public ResponseEntity<byte[]> generarPdfHorario(@PathVariable(name = "idCentro") int idCentro,
													@RequestParam(name = "fecha") String fecha,
													@RequestParam(name = "profesor") int id_profesor,
													@RequestBody Map<String, Map<String, String>> horario) {
		ProfesorDTO profesor = profesoresService.findProfesorById(id_profesor);
		String titulo = "Suplencias " + profesor.getNombre() + " " + fecha;

		try (PDDocument doc = new PDDocument(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			PDPage pagina = new PDPage(PDRectangle.A4);
			doc.addPage(pagina);

			float pageWidth = pagina.getMediaBox().getWidth();
			float pageHeight = pagina.getMediaBox().getHeight();
			float margin = 40f;

			try (PDPageContentStream cs = new PDPageContentStream(doc, pagina)) {
				float gapEntreTablas = 40f;

				float y = pageHeight - margin;
				y = drawTable(cs, y, pageWidth, margin, titulo, horario);

				y -= gapEntreTablas;

				drawTable(cs, y, pageWidth, margin, titulo, horario);
			}

			doc.save(baos);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=suplencias.pdf")
					.contentType(MediaType.APPLICATION_PDF).body(baos.toByteArray());

		} catch (Exception ex) {
			return ResponseEntity.internalServerError().body(("Error al generar PDF: " + ex.getMessage()).getBytes());
		}
	}

	private float drawTable(PDPageContentStream cs, float y, float pageWidth, float margin, String titulo,
			Map<String, Map<String, String>> mapSuplencias) throws IOException {

		cs.beginText();
		cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
		cs.newLineAtOffset(margin, y);
		cs.showText(titulo);
		cs.endText();
		y -= 20f;

		String[] headers = { "Sustituto", "Curso", "Hora", "Asignatura", "Espacio", "Observaciones" };
		float tableWidth = pageWidth - 2 * margin;
		float[] colPerc = { 0.22f, 0.14f, 0.11f, 0.25f, 0.14f, 0.14f };
		float[] colWidths = new float[headers.length];
		for (int i = 0; i < headers.length; i++)
			colWidths[i] = tableWidth * colPerc[i];

		float rowHeight = 18f;
		float headerHeight = 20f;

		float x = margin;
		drawRowBackground(cs, x, y - headerHeight, tableWidth, headerHeight);
		cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
		float textYOffset = 5f;

		float cellX = x;
		for (int i = 0; i < headers.length; i++) {
			drawCellBorders(cs, cellX, y - headerHeight, colWidths[i], headerHeight);
			drawText(cs, headers[i], cellX + 2f, y - headerHeight + textYOffset, PDType1Font.HELVETICA_BOLD, 11);
			cellX += colWidths[i];
		}
		y -= headerHeight;

		cs.setFont(PDType1Font.HELVETICA, 11);

		for (Map.Entry<String, Map<String, String>> e : mapSuplencias.entrySet()) {
			y = drawDataRow(cs, y, margin, tableWidth, rowHeight, colWidths, e);
		}

		return y - 10f;
	}

	private float drawDataRow(PDPageContentStream cs, float y, float margin, float tableWidth, float rowHeight,
			float[] colWidths, Map.Entry<String, Map<String, String>> e) throws IOException {

		String[] key = e.getKey().split("_");
		Map<String, String> value = e.getValue();

		String idSustituto = value.get("idSustituto");
		String observaciones = value.get("observaciones");

		ProfesorDTO profSubs = null;
		if (idSustituto != null && !"0".equals(idSustituto)) {
			profSubs = profesoresService.findProfesorById(Integer.parseInt(idSustituto));
		}

		String sustituta = (profSubs != null && profSubs.getNombre() != null) ? profSubs.getNombre() : "-";
		String curso = "-";
		String hora = "-";
		String asignatura = "-";
		String espacio = "-";

			HorarioDTO h = horariosService.findHorarioById(Integer.parseInt(key[1]));
			if (h != null) {
				curso = safe(h.getCurso());
				hora = safe(h.getHora());
				asignatura = safe(h.getAsignatura());
				espacio = safe(h.getEspacio());
			}


		float x = margin;
		String[] values = { sustituta, curso, hora, asignatura, espacio, observaciones };

		for (int i = 0; i < values.length; i++) {
			drawCellBorders(cs, x, y - rowHeight, colWidths[i], rowHeight);
			drawText(cs, truncate(values[i], 160), x + 2f, y - rowHeight + 4f, PDType1Font.HELVETICA, 11);
			x += colWidths[i];
		}

		return y - rowHeight;
	}

	private static void drawRowBackground(PDPageContentStream cs, float x, float y, float width, float height)
			throws IOException {
	}

	private static void drawCellBorders(PDPageContentStream cs, float x, float y, float width, float height)
			throws IOException {
		cs.setLineWidth(0.5f);
		cs.addRect(x, y, width, height);
		cs.stroke();
	}

	private static void drawText(PDPageContentStream cs, String text, float x, float y, PDFont font, float fontSize)
			throws IOException {
		cs.beginText();
		cs.setFont(font, fontSize);
		cs.newLineAtOffset(x, y);
		cs.showText(text != null ? text : "");
		cs.endText();
	}

	private static String safe(String s) {
		return (s == null) ? "-" : s;
	}

	private static String truncate(String s, int maxLen) {
		if (s == null)
			return "";
		return s.length() <= maxLen ? s : s.substring(0, maxLen - 1) + "…";
	}

}