package com.example.planaula.services;

import com.example.planaula.Dto.AsignaturaDTO;
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
public class GuardiasService {

    @Autowired
    private EntityManager entityManager;

    public List<GuardiasDTO> findAllGuardiasByDiaAndHoraAndProfesor(Integer dia, Integer hora, Integer profesor) {
        String sql = "SELECT g.id, d.campo1, h.campo1, \n" +
        "       a1.campo1 AS Aula_1A, a2.campo1 AS Aula_1B, \n" +
                "       a3.campo1 AS Aula_2A, a4.campo1 AS Aula_2B, \n" +
                "       a5.campo1 AS Aula_3A, a6.campo1 AS Aula_3B, \n" +
                "       a7.campo1 AS Aula_4A, a8.campo1 AS Aula_4B, \n" +
                "       a9.campo1 AS Aula_1CC \n" +
                "FROM `guardias 2425` g \n" +
                "INNER JOIN dias d ON g.Día = d.id \n" +
                "INNER JOIN horas h ON g.Hora = h.id \n" +
                "LEFT JOIN `profes 2425` a1 ON g.`guardia 1` = a1.id \n" +
                "LEFT JOIN `profes 2425` a2 ON g.`guardia 2` = a2.id \n" +
                "LEFT JOIN `profes 2425` a3 ON g.`guardia 3` = a3.id \n" +
                "LEFT JOIN `profes 2425` a4 ON g.`libranza 1` = a4.id \n" +
                "LEFT JOIN `profes 2425` a5 ON g.`libranza 2` = a5.id \n" +
                "LEFT JOIN `profes 2425` a6 ON g.`libranza 3` = a6.id \n" +
                "LEFT JOIN `profes 2425` a7 ON g.recreo1 = a7.id \n" +
                "LEFT JOIN `profes 2425` a8 ON g.recreo2 = a8.id \n" +
                "LEFT JOIN `profes 2425` a9 ON g.recreo3 = a9.id \n" +
                "WHERE 1=1 \n" +
                "AND (:dia = 0 OR d.id = :dia) \n" +
                "AND (:hora = 0 OR h.id = :hora) \n" +
                "AND (:profesor = 0 OR \n" +
                "     a1.id = :profesor OR a2.id = :profesor OR \n" +
                "     a3.id = :profesor OR a4.id = :profesor OR \n" +
                "     a5.id = :profesor OR a6.id = :profesor OR \n" +
                "     a7.id = :profesor OR a8.id = :profesor OR \n" +
                "     a9.id = :profesor)";

        List<Object[]> resultados = entityManager.createNativeQuery(sql)
                .setParameter("dia", dia)
                .setParameter("hora", hora)
                .setParameter("profesor", profesor)
                .getResultList();

        if (resultados == null || resultados.isEmpty()) {
            return Collections.emptyList();
        }

        return resultados.stream()
                .map(resultado -> new GuardiasDTO(
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
                        (String) resultado[11]
                ))
                .collect(Collectors.toList());
    }
}
