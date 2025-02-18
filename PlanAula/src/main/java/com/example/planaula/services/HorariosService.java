package com.example.planaula.services;

import com.example.planaula.Dto.GuardiasDTO;
import com.example.planaula.Dto.HoraDTO;
import com.example.planaula.Dto.HorarioDTO;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HorariosService {

    @Autowired
    private EntityManager entityManager;

    public List<HorarioDTO> findAllHorariosByDiaAndHoraAndCurso(Integer dia, Integer hora, Integer curso, Integer asignatura, Integer profesor, Integer aula) {
        String sql = "SELECT h.id, d.campo1 AS dia, t.campo1 AS hora, c.campo1 AS curso,\n" +
                "       a1.campo1 AS asignatura1, p1.campo1 AS profesor1, au1.campo1 AS espacio1,\n" +
                "       a2.campo1 AS asignatura2, p2.campo1 AS profesor2, au2.campo1 AS espacio2,\n" +
                "       a3.campo1 AS asignatura3, p3.campo1 AS profesor3, au3.campo1 AS espacio3,\n" +
                "       h.observaciones\n" +
                "FROM `horarios 2425` h\n" +
                "LEFT JOIN dias d ON h.`día de la semana` = d.id\n" +
                "LEFT JOIN horas t ON h.hora = t.id\n" +
                "LEFT JOIN cursos c ON h.curso = c.id\n" +
                "LEFT JOIN `profes 2425` p1 ON h.`profesor 1` = p1.id\n" +
                "LEFT JOIN `profes 2425` p2 ON h.`profesor 2` = p2.id\n" +
                "LEFT JOIN `profes 2425` p3 ON h.`profesor 3` = p3.id\n" +
                "LEFT JOIN asignaturas a1 ON h.`asignatura 1` = a1.id\n" +
                "LEFT JOIN asignaturas a2 ON h.`asignatura 2` = a2.id\n" +
                "LEFT JOIN asignaturas a3 ON h.`asignatura 3` = a3.id\n" +
                "LEFT JOIN aulas au1 ON h.`espacio 1` = au1.id\n" +
                "LEFT JOIN aulas au2 ON h.`espacio 2` = au2.id\n" +
                "LEFT JOIN aulas au3 ON h.`espacio 3` = au3.id\n" +
                "WHERE (:dia = 0 OR h.`día de la semana` = :dia)\n" +
                "  AND (:hora = 0 OR h.hora = :hora)\n" +
                "  AND (:curso = 0 OR h.curso = :curso)\n" +
                "  AND (:asignatura = 0 OR h.`asignatura 1` = :asignatura OR h.`asignatura 2` = :asignatura OR h.`asignatura 3` = :asignatura)\n" +
                "  AND (:profesor = 0 OR h.`profesor 1` = :profesor OR h.`profesor 2` = :profesor OR h.`profesor 3` = :profesor)\n" +
                "  AND (:aula = 0 OR h.`espacio 1` = :aula OR h.`espacio 2` = :aula OR h.`espacio 3` = :aula)\n";

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
                        (String) resultado[8],
                        (String) resultado[9],
                        (String) resultado[10],
                        (String) resultado[11],
                        (String) resultado[12],
                        (String) resultado[13]
                ))
                .collect(Collectors.toList());
    }
}
