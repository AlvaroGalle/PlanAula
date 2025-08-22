package com.example.planaula.services;

import com.example.planaula.Dto.HorarioDTO;

import jakarta.persistence.EntityManager;
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

    @Autowired
    private EntityManager entityManager;

    public List<HorarioDTO> findAllHorariosByDiaAndHoraAndCurso(Integer dia, Integer hora, Integer curso, Integer asignatura, Integer profesor, Integer aula) {
    	String sql = 
    		    "SELECT h.id, d.dia, ho.hora, c.curso, asig.asignatura, p.nombre AS profesor, a.aula, h.observaciones, 'clase' AS tipo, 'horarios' AS origen FROM horarios h LEFT JOIN cursos c ON h.id_curso = c.id LEFT JOIN profesores p ON h.id_profesor = p.id LEFT JOIN asignaturas asig ON h.id_asignatura = asig.id LEFT JOIN aulas a ON h.id_aula = a.id LEFT JOIN dias d ON h.id_dia = d.id LEFT JOIN horas ho ON h.id_hora = ho.id WHERE (:dia IS NULL OR :dia = 0 OR h.id_dia = :dia) AND (:hora IS NULL OR :hora = 0 OR h.id_hora = :hora) AND (:curso IS NULL OR :curso = 0 OR h.id_curso = :curso) AND (:asignatura IS NULL OR :asignatura = 0 OR h.id_asignatura = :asignatura) AND (:profesor IS NULL OR :profesor = 0 OR h.id_profesor = :profesor) AND (:aula IS NULL OR :aula = 0 OR h.id_aula = :aula) UNION ALL SELECT g.id, d.dia, ho.hora, NULL AS curso, NULL AS asignatura, p.nombre AS profesor, NULL AS aula, NULL AS observaciones, 'guardia' AS tipo, 'guardias' AS origen FROM guardias g LEFT JOIN profesores p ON g.id_profesor = p.id LEFT JOIN dias d ON g.id_dia = d.id LEFT JOIN horas ho ON g.id_hora = ho.id WHERE (:dia IS NULL OR :dia = 0 OR g.id_dia = :dia) AND (:hora IS NULL OR :hora = 0 OR g.id_hora = :hora) AND (:profesor IS NULL OR :profesor = 0 OR g.id_profesor = :profesor) AND (:curso IS NULL OR :curso = 0) AND (:asignatura IS NULL OR :asignatura = 0) AND (:aula IS NULL OR :aula = 0) UNION ALL SELECT r.id, d.dia, ho.hora, NULL AS curso, NULL AS asignatura, p.nombre AS profesor, NULL AS aula, NULL AS observaciones, 'recreo' AS tipo, 'recreo' AS origen FROM recreos r LEFT JOIN profesores p ON r.id_profesor = p.id LEFT JOIN dias d ON r.id_dia = d.id LEFT JOIN horas ho ON r.id_hora = ho.id WHERE (:dia IS NULL OR :dia = 0 OR r.id_dia = :dia) AND (:hora IS NULL OR :hora = 0 OR r.id_hora = :hora) AND (:profesor IS NULL OR :profesor = 0 OR r.id_profesor = :profesor) AND (:curso IS NULL OR :curso = 0) AND (:asignatura IS NULL OR :asignatura = 0) AND (:aula IS NULL OR :aula = 0) UNION ALL SELECT l.id, d.dia, ho.hora, NULL AS curso, NULL AS asignatura, p.nombre AS profesor, NULL AS aula, NULL AS observaciones, 'libranza' AS tipo, 'libranza' AS origen FROM libranzas l LEFT JOIN profesores p ON l.id_profesor = p.id LEFT JOIN dias d ON l.id_dia = d.id LEFT JOIN horas ho ON l.id_hora = ho.id WHERE (:dia IS NULL OR :dia = 0 OR l.id_dia = :dia) AND (:hora IS NULL OR :hora = 0 OR l.id_hora = :hora) AND (:profesor IS NULL OR :profesor = 0 OR l.id_profesor = :profesor) AND (:curso IS NULL OR :curso = 0) AND (:asignatura IS NULL OR :asignatura = 0) AND (:aula IS NULL OR :aula = 0) ORDER BY dia, hora;";


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
                        (String) resultado[7],
                        (String) resultado[8]
                ))
                .collect(Collectors.toList());
    }


   /* public HorarioDTO findHorarioById(int id) {
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
    }*/
    
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
