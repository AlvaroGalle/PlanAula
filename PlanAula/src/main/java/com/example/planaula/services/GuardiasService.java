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
        String sql = "SELECT g.id, \n" +
                "       ch.codigo, \n" +
                "       d.campo1 dia, \n" +
                "       h.campo1 hora, \n" +
                "       p1.campo1 guardia1, \n" +
                "       p2.campo1 guardia2, \n" +
                "       p3.campo1 guardia3, \n" +
                "       p4.campo1 libranza1, \n" +
                "       p5.campo1 libranza2, \n" +
                "       p6.campo1 libranza3, \n" +
                "       p7.campo1 recreo1, \n" +
                "       p8.campo1 recreo2, \n" +
                "       p9.campo1 recreo3\n" +
                "FROM `guardias 2425` g\n" +
                "INNER JOIN `codigo horas` ch ON g.codigo = ch.id\n" +
                "INNER JOIN dias d ON ch.día = d.id\n" +
                "INNER JOIN horas h ON ch.hora = h.id\n" +
                "LEFT JOIN `profes 2425` p1 ON g.`guardia 1` = p1.id\n" +
                "LEFT JOIN `profes 2425` p2 ON g.`guardia 2` = p2.id\n" +
                "LEFT JOIN `profes 2425` p3 ON g.`guardia 3` = p3.id\n" +
                "LEFT JOIN `profes 2425` p4 ON g.`libranza 1` = p4.id\n" +
                "LEFT JOIN `profes 2425` p5 ON g.`libranza 2` = p5.id\n" +
                "LEFT JOIN `profes 2425` p6 ON g.`libranza 3` = p6.id\n" +
                "LEFT JOIN `profes 2425` p7 ON g.recreo1 = p7.id\n" +
                "LEFT JOIN `profes 2425` p8 ON g.recreo2 = p8.id\n" +
                "LEFT JOIN `profes 2425` p9 ON g.recreo3 = p9.id\n" +
                "LEFT JOIN `profes 2425` p10 ON g.recreo4 = p10.id\n" +
                "WHERE 1=1\n" +
                "  AND (:dia = 0 OR d.id = :dia)\n" +
                "  AND (:hora = 0 OR h.id = :hora)\n" +
                "  AND (:profesor = 0 OR \n" +
                "       g.`guardia 1` = :profesor OR g.`guardia 2` = :profesor OR g.`guardia 3` = :profesor OR \n" +
                "       g.`libranza 1` = :profesor OR g.`libranza 2` = :profesor OR g.`libranza 3` = :profesor OR \n" +
                "       g.recreo1 = :profesor OR g.recreo2 = :profesor OR g.recreo3 = :profesor OR g.recreo4 = :profesor)\n";


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
                        (String) resultado[11],
                        (String) resultado[12]
                ))
                .collect(Collectors.toList());
    }
}
