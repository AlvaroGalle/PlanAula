package com.example.planaula.services;

import com.example.planaula.Dto.EspacioDTO;
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

    public List<EspacioDTO> findAllEspaciosByDiaAndHoraAndAulaAndAsignatura(Integer dia, Integer hora, Integer aula, Integer asignatura) {
        String sql = "SELECT e.id, d.campo1, h.campo1, \n" +
                "       a1.campo1 AS Aula_1A, a2.campo1 AS Aula_1B, \n" +
                "       a3.campo1 AS Aula_2A, a4.campo1 AS Aula_2B, \n" +
                "       a5.campo1 AS Aula_3A, a6.campo1 AS Aula_3B, \n" +
                "       a7.campo1 AS Aula_4A, a8.campo1 AS Aula_4B, \n" +
                "       a9.campo1 AS Aula_1CC, a10.campo1 AS Aula_1SS, \n" +
                "       a11.campo1 AS Lab_CC, a12.campo1 AS Lab_Id, \n" +
                "       a13.campo1 AS Desdoble_A, a14.campo1 AS Desdoble_B, \n" +
                "       a15.campo1 AS Desdoble_C, a16.campo1 AS A_ENLACE, \n" +
                "       a17.campo1 AS Bajo_Patio, a18.campo1 AS Bajo_Espejos \n" +
                "FROM `espacios 2425` e \n" +
                "INNER JOIN dias d ON e.Día = d.id \n" +
                "INNER JOIN horas h ON e.Hora = h.id \n" +
                "LEFT JOIN asignaturas a1 ON e.`Aula 1A` = a1.id \n" +
                "LEFT JOIN asignaturas a2 ON e.`Aula 1B` = a2.id \n" +
                "LEFT JOIN asignaturas a3 ON e.`Aula 2A` = a3.id \n" +
                "LEFT JOIN asignaturas a4 ON e.`Aula 2B` = a4.id \n" +
                "LEFT JOIN asignaturas a5 ON e.`Aula 3A` = a5.id \n" +
                "LEFT JOIN asignaturas a6 ON e.`Aula 3B` = a6.id \n" +
                "LEFT JOIN asignaturas a7 ON e.`Aula 4A` = a7.id \n" +
                "LEFT JOIN asignaturas a8 ON e.`Aula 4B` = a8.id \n" +
                "LEFT JOIN asignaturas a9 ON e.`Aula 1CC` = a9.id \n" +
                "LEFT JOIN asignaturas a10 ON e.`Aula 1SS` = a10.id \n" +
                "LEFT JOIN asignaturas a11 ON e.`Lab CC` = a11.id \n" +
                "LEFT JOIN asignaturas a12 ON e.`Lab Id` = a12.id \n" +
                "LEFT JOIN asignaturas a13 ON e.`Desdoble A` = a13.id \n" +
                "LEFT JOIN asignaturas a14 ON e.`Desdoble B` = a14.id \n" +
                "LEFT JOIN asignaturas a15 ON e.`Desdoble C` = a15.id \n" +
                "LEFT JOIN asignaturas a16 ON e.`A ENLACE` = a16.id \n" +
                "LEFT JOIN asignaturas a17 ON e.`Bajo Patio` = a17.id \n" +
                "LEFT JOIN asignaturas a18 ON e.`Bajo Espejos` = a18.id \n" +
                "WHERE 1=1 \n" +
                "AND (:dia = 0 OR d.id = :dia) \n" +
                "AND (:hora = 0 OR h.id = :hora) \n" +
                "AND (:aula = 0 OR \n" +
                "     e.`Aula 1A` = :aula OR e.`Aula 1B` = :aula OR \n" +
                "     e.`Aula 2A` = :aula OR e.`Aula 2B` = :aula OR \n" +
                "     e.`Aula 3A` = :aula OR e.`Aula 3B` = :aula OR \n" +
                "     e.`Aula 4A` = :aula OR e.`Aula 4B` = :aula OR \n" +
                "     e.`Aula 1CC` = :aula OR e.`Aula 1SS` = :aula OR \n" +
                "     e.`Lab CC` = :aula OR e.`Lab Id` = :aula OR \n" +
                "     e.`Desdoble A` = :aula OR e.`Desdoble B` = :aula OR \n" +
                "     e.`Desdoble C` = :aula OR e.`A ENLACE` = :aula OR \n" +
                "     e.`Bajo Patio` = :aula OR e.`Bajo Espejos` = :aula) \n" +
                "AND (:asignatura = 0 OR \n" +
                "     a1.id = :asignatura OR a2.id = :asignatura OR \n" +
                "     a3.id = :asignatura OR a4.id = :asignatura OR \n" +
                "     a5.id = :asignatura OR a6.id = :asignatura OR \n" +
                "     a7.id = :asignatura OR a8.id = :asignatura OR \n" +
                "     a9.id = :asignatura OR a10.id = :asignatura OR \n" +
                "     a11.id = :asignatura OR a12.id = :asignatura OR \n" +
                "     a13.id = :asignatura OR a14.id = :asignatura OR \n" +
                "     a15.id = :asignatura OR a16.id = :asignatura OR \n" +
                "     a17.id = :asignatura OR a18.id = :asignatura)";

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
                        (String) resultado[7],
                        (String) resultado[8],
                        (String) resultado[9],
                        (String) resultado[10],
                        (String) resultado[11],
                        (String) resultado[12],
                        (String) resultado[13],
                        (String) resultado[14],
                        (String) resultado[15],
                        (String) resultado[16],
                        (String) resultado[17],
                        (String) resultado[18],
                        (String) resultado[19],
                        (String) resultado[20]
                )).collect(Collectors.toList());
    }

}
