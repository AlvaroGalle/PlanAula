package com.example.planaula.services;

import com.example.planaula.Dto.GuardiasDTO;
import com.example.planaula.Dto.HoraDTO;
import com.example.planaula.Dto.HorarioDTO;
import com.example.planaula.Dto.UsuarioDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HorariosService {

    private static final Map<String, String> DIA_ESPANOL_A_INGLES = new HashMap<>();

    static {
        DIA_ESPANOL_A_INGLES.put("lunes", "MONDAY");
        DIA_ESPANOL_A_INGLES.put("martes", "TUESDAY");
        DIA_ESPANOL_A_INGLES.put("miercoles", "WEDNESDAY");
        DIA_ESPANOL_A_INGLES.put("jueves", "THURSDAY");
        DIA_ESPANOL_A_INGLES.put("viernes", "FRIDAY");
    }

    @Autowired
    private EntityManager entityManager;

    public List<HorarioDTO> findAllHorariosByDiaAndHoraAndCurso(Integer dia, Integer hora, Integer curso, Integer asignatura, Integer profesor, Integer aula) {
        String sql = "SELECT h.id, d.dia, ho.hora, c.curso, asig.asignatura, p.nombre, a.aula, h.observaciones "
                + "FROM horarios h "
                + "JOIN cursos c ON h.id_curso = c.id "
                + "JOIN profesores p ON h.id_profesor = p.id "
                + "JOIN asignaturas asig ON h.id_asignatura = asig.id "
                + "JOIN aulas a ON h.id_aula = a.id "
                + "JOIN dias d ON h.id_dia = d.id "
                + "JOIN horas ho ON h.id_hora = ho.id "
                + "WHERE (:dia IS NULL OR :dia = 0 OR d.id = :dia) "
                + "AND (:hora IS NULL OR :hora = 0 OR ho.id = :hora) "
                + "AND (:curso IS NULL OR :curso = 0 OR c.id = :curso) "
                + "AND (:asignatura IS NULL OR :asignatura = 0 OR asig.id = :asignatura) "
                + "AND (:profesor IS NULL OR :profesor = 0 OR p.id = :profesor) "
                + "AND (:aula IS NULL OR :aula = 0 OR a.id = :aula)";

        List<Object[]> resultados = entityManager.createNativeQuery(sql)
                .setParameter("dia", dia)
                .setParameter("hora", hora)
                .setParameter("curso", curso)
                .setParameter("asignatura", asignatura)
                .setParameter("profesor", profesor)
                .setParameter("aula", aula)
                .getResultList();

        if (resultados == null || resultados.isEmpty()) {
            return Collections.emptyList();
        }

        return resultados.stream()
                .map(resultado -> new HorarioDTO(
                        ((Number) resultado[0]).intValue(),
                        (String) resultado[1],
                        (String) resultado[2],
                        (String) resultado[3],
                        (String) resultado[4],
                        (String) resultado[5],
                        (String) resultado[6],
                        (String) resultado[7]
                ))
                .collect(Collectors.toList());
    }

    public List<HorarioDTO> filtrarHorariosFuturos(List<HorarioDTO> horarios) {
        DayOfWeek diaActual = LocalDate.now().getDayOfWeek();

        LocalTime ahora = LocalTime.now();

        return horarios.stream()
                .filter(h -> esHorarioFuturo(h.getDia(), h.getHora(), diaActual, ahora))
                .collect(Collectors.toList());
    }

    private boolean esHorarioFuturo(String diaStr, String horaStr, DayOfWeek diaActual, LocalTime ahora) {
        String diaIngles = DIA_ESPANOL_A_INGLES.get(diaStr.toLowerCase());

        if (diaIngles == null) {
            throw new IllegalArgumentException("Día no válido: " + diaStr);
        }

        DayOfWeek diaHorario = DayOfWeek.valueOf(diaIngles);

        LocalTime hora = LocalTime.parse(horaStr);

        int diaActualValor = diaActual.getValue();
        int diaHorarioValor = diaHorario.getValue();

        if (diaHorarioValor > diaActualValor) {
            return true;
        } else if (diaHorarioValor == diaActualValor) {
            return hora.isAfter(ahora);
        } else {
            return false;
        }
    }
    
    public HorarioDTO findHorarioById(int id) {
        String sql = """
            SELECT id, id_dia, id_hora, id_curso, id_asignatura, id_profesor, id_aula, observaciones
            FROM horarios
            WHERE id = :id
        """;

        try {
            Object[] row = (Object[]) entityManager.createNativeQuery(sql)
                    .setParameter("id", id)
                    .getSingleResult();

            return new HorarioDTO(
                    (Integer) row[0],                    
                    String.valueOf(row[1]),              
                    String.valueOf(row[2]),              
                    String.valueOf(row[3]),             
                    String.valueOf(row[4]),             
                    String.valueOf(row[5]),             
                    String.valueOf(row[6]),             
                    row[7] != null ? row[7].toString() : null
            );

        } catch (NoResultException e) {
        	e.printStackTrace();
            return new HorarioDTO();
        }
    }
    
    @Transactional
    public void anadirHorario(HorarioDTO horarioDTO) {
    	String sql = """
    		    INSERT INTO horarios (id_dia, id_hora, id_curso, id_asignatura, id_profesor, id_aula, observaciones)
    		    VALUES (:dia, :hora, :curso, :asignatura, :profesor, :aula, :observaciones)
    		""";

    		entityManager.createNativeQuery(sql)
    		        .setParameter("dia", Integer.parseInt(horarioDTO.getDia()))
    		        .setParameter("hora", Integer.parseInt(horarioDTO.getHora()))
    		        .setParameter("curso", Integer.parseInt(horarioDTO.getCurso()))
    		        .setParameter("asignatura", Integer.parseInt(horarioDTO.getAsignatura()))
    		        .setParameter("profesor", Integer.parseInt(horarioDTO.getProfesor()))
    		        .setParameter("aula", Integer.parseInt(horarioDTO.getEspacio()))
    		        .setParameter("observaciones", horarioDTO.getObservaciones())
    		        .executeUpdate();

        }
    
    @Transactional
    public void editarHorario(HorarioDTO horarioDTO) {
    	String sql = """
    		    UPDATE horarios
    		    SET id_dia = :dia,
    		        id_hora = :hora,
    		        id_curso = :curso,
    		        id_asignatura = :asignatura,
    		        id_profesor = :profesor,
    		        id_aula = :aula,
    		        observaciones = :observaciones
    		    WHERE id = :id
    		""";

    		entityManager.createNativeQuery(sql)
    		    .setParameter("dia", Integer.parseInt(horarioDTO.getDia()))
    		    .setParameter("hora", Integer.parseInt(horarioDTO.getHora()))
    		    .setParameter("curso", Integer.parseInt(horarioDTO.getCurso()))
    		    .setParameter("asignatura", Integer.parseInt(horarioDTO.getAsignatura()))
    		    .setParameter("profesor", Integer.parseInt(horarioDTO.getProfesor()))
    		    .setParameter("aula", Integer.parseInt(horarioDTO.getEspacio()))
    		    .setParameter("observaciones", horarioDTO.getObservaciones())
    		    .setParameter("id", horarioDTO.getId())
    		    .executeUpdate();

    }
    
    @Transactional
    public void eliminarHorario(int id) {
    	String sql = "DELETE FROM horarios WHERE id = :id";

    	entityManager.createNativeQuery(sql)
    	    .setParameter("id", id)
    	    .executeUpdate();
    }
}
