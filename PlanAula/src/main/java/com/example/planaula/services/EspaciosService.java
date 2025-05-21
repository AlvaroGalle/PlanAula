package com.example.planaula.services;

import com.example.planaula.Dto.EspacioDTO;
import com.example.planaula.Dto.GuardiasDTO;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EspaciosService {

    @Autowired
    private EntityManager entityManager;

    public List<EspacioDTO> findAllEspaciosByDiaAndHoraAndAulaAndAsignatura(Integer dia, Integer hora, Integer asignatura, Integer aula) {
        String sql = "SELECT h.id, d.dia, ho.hora, c.curso, asig.asignatura, p.nombre, a.aula, h.observaciones\n" +
                "FROM horarios h\n" +
                "JOIN cursos c ON h.id_curso = c.id\n" +
                "JOIN profesores p ON h.id_profesor = p.id\n" +
                "JOIN asignaturas asig ON h.id_asignatura = asig.id\n" +
                "JOIN aulas a ON h.id_aula = a.id\n" +
                "JOIN dias d ON h.id_dia = d.id\n" +
                "JOIN horas ho ON h.id_hora = ho.id\n" +
                "WHERE (:dia IS NULL OR :dia = 0 OR d.id = :dia)\n" +
                "AND (:hora IS NULL OR :hora = 0 OR ho.id = :hora)\n" +
                "AND (:aula IS NULL OR :aula = 0 OR a.id = :aula)\n" +
                "AND (:asignatura IS NULL OR :asignatura = 0 OR asig.id = :asignatura)\n";

        List<Object[]> resultados = entityManager.createNativeQuery(sql)
                .setParameter("dia", dia)
                .setParameter("hora", hora)
                .setParameter("aula", aula)
                .setParameter("asignatura", asignatura)
                .getResultList();

        if (resultados == null || resultados.isEmpty()) {
            return Collections.emptyList();
        }

        return resultados.stream()
                .map(resultado -> new EspacioDTO(
                        ((Number) resultado[0]).intValue(),
                        (String) resultado[1],
                        (String) resultado[2],
                        (String) resultado[3],
                        (String) resultado[4],
                        (String) resultado[5],
                        (String) resultado[6],
                        (String) resultado[7]
                )).collect(Collectors.toList());
    }


    public EspacioDTO findEspacioById(Integer id) {
        String sql = "SELECT e.id_espacio, e.nombre_espacio, e.dia, e.hora_inicio, e.hora_fin, " +
                "a1.nombre AS Aula_1A, a2.nombre AS Aula_1B, a3.nombre AS Aula_2A, a4.nombre AS Aula_2B, " +
                "a5.nombre AS Aula_3A, a6.nombre AS Aula_3B, a7.nombre AS Aula_4A, a8.nombre AS Aula_4B, " +
                "a9.nombre AS Aula_1CC, a10.nombre AS Aula_1SS, a11.nombre AS Lab_CC, a12.nombre AS Lab_Id, " +
                "a13.nombre AS Desdoble_A, a14.nombre AS Desdoble_B, a15.nombre AS Desdoble_C, a16.nombre AS A_ENLACE, " +
                "a17.nombre AS Bajo_Patio, a18.nombre AS Bajo_Espejos " +
                "FROM espacios e " +
                "LEFT JOIN asignaturas a1 ON e.nombre_espacio = 'Aula 1A' " +
                "LEFT JOIN asignaturas a2 ON e.nombre_espacio = 'Aula 1B' " +
                "LEFT JOIN asignaturas a3 ON e.nombre_espacio = 'Aula 2A' " +
                "LEFT JOIN asignaturas a4 ON e.nombre_espacio = 'Aula 2B' " +
                "LEFT JOIN asignaturas a5 ON e.nombre_espacio = 'Aula 3A' " +
                "LEFT JOIN asignaturas a6 ON e.nombre_espacio = 'Aula 3B' " +
                "LEFT JOIN asignaturas a7 ON e.nombre_espacio = 'Aula 4A' " +
                "LEFT JOIN asignaturas a8 ON e.nombre_espacio = 'Aula 4B' " +
                "LEFT JOIN asignaturas a9 ON e.nombre_espacio = 'Aula 1CC' " +
                "LEFT JOIN asignaturas a10 ON e.nombre_espacio = 'Aula 1SS' " +
                "LEFT JOIN asignaturas a11 ON e.nombre_espacio = 'Lab CC' " +
                "LEFT JOIN asignaturas a12 ON e.nombre_espacio = 'Lab Id' " +
                "LEFT JOIN asignaturas a13 ON e.nombre_espacio = 'Desdoble A' " +
                "LEFT JOIN asignaturas a14 ON e.nombre_espacio = 'Desdoble B' " +
                "LEFT JOIN asignaturas a15 ON e.nombre_espacio = 'Desdoble C' " +
                "LEFT JOIN asignaturas a16 ON e.nombre_espacio = 'A ENLACE' " +
                "LEFT JOIN asignaturas a17 ON e.nombre_espacio = 'Bajo Patio' " +
                "LEFT JOIN asignaturas a18 ON e.nombre_espacio = 'Bajo Espejos' " +
                "WHERE e.id_espacio = :id";

        Object[] resultado = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("id", id)
                .getSingleResult();

        return new EspacioDTO(((Number) resultado[0]).intValue(),
                (String) resultado[1],
                (String) resultado[2],
                (String) resultado[3],
                (String) resultado[4],
                (String) resultado[5],
                (String) resultado[6],
                (String) resultado[7]);
    }

}
