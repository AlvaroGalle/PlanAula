package com.example.planaula.services;

import com.example.planaula.Dto.GuardiasDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GuardiasService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<GuardiasDTO> findAllRecreosByDiaAndHoraAndProfesor(Integer dia, Integer hora, Integer profesor) {
        String sql = "SELECT r.id, p.nombre, p.apellidos, d.dia, h.hora\n" +
                "FROM recreos r\n" +
                "JOIN profesores p ON r.id_profesor = p.id\n" +
                "JOIN dias d ON r.id_dia = d.id\n" +
                "JOIN horas h ON r.id_hora = h.id\n" +
                "WHERE \n" +
                "    (p.id = :profesor OR :profesor = 0)\n" +
                "    AND (d.id = :dia OR :dia = 0)\n" +
                "    AND (h.id = :hora OR :hora = 0)\n" +
                "ORDER BY p.nombre, d.dia, h.hora;\n";

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
                        (String) resultado[4]
                ))
                .collect(Collectors.toList());
    }

    public List<GuardiasDTO> findAllLibranzasByDiaAndHoraAndProfesor(Integer dia, Integer hora, Integer profesor) {
        String sql = "SELECT r.id, p.nombre, p.apellidos, d.dia, h.hora\n" +
                "FROM libranzas r\n" +
                "JOIN profesores p ON r.id_profesor = p.id\n" +
                "JOIN dias d ON r.id_dia = d.id\n" +
                "JOIN horas h ON r.id_hora = h.id\n" +
                "WHERE \n" +
                "    (p.id = :profesor OR :profesor = 0)\n" +
                "    AND (d.id = :dia OR :dia = 0)\n" +
                "    AND (h.id = :hora OR :hora = 0)\n" +
                "ORDER BY p.nombre, d.dia, h.hora;\n";

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
                        (String) resultado[4]
                ))
                .collect(Collectors.toList());
    }

    public List<GuardiasDTO> findAllGuardiasByDiaAndHoraAndProfesor(Integer dia, Integer hora, Integer profesor) {
        String sql = "SELECT r.id, p.nombre, p.apellidos, d.dia, h.hora\n" +
                "FROM guardias r\n" +
                "JOIN profesores p ON r.id_profesor = p.id\n" +
                "JOIN dias d ON r.id_dia = d.id\n" +
                "JOIN horas h ON r.id_hora = h.id\n" +
                "WHERE \n" +
                "    (p.id = :profesor OR :profesor = 0)\n" +
                "    AND (d.id = :dia OR :dia = 0)\n" +
                "    AND (h.id = :hora OR :hora = 0)\n" +
                "ORDER BY p.nombre, d.dia, h.hora;\n";

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
                        (String) resultado[4]
                ))
                .collect(Collectors.toList());
    }

    public List<GuardiasDTO> findAllByDiaAndHoraAndProfesor(Integer dia, Integer hora, Integer profesor) {
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
                        (String) resultado[4]
                ))
                .collect(Collectors.toList());
    }

    public GuardiasDTO findGuardiaById(Integer id) {
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
                "AND (g.id = :id)";

        Object[] resultado = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("id", id)
                .getSingleResult();

        entityManager.close();
        return new GuardiasDTO(
                        ((Number) resultado[0]).intValue(),
                        (String) resultado[1],
                        (String) resultado[2],
                        (String) resultado[3],
                        (String) resultado[4]
                );
    }

	public void accionGuardias(String accion, String turno, Integer profesor, Integer id) {
	       try {
               NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

               String sql = "UPDATE `guardias 2425` SET [" + turno + "] = :profesor WHERE id = :id";
               MapSqlParameterSource parametros = new MapSqlParameterSource();
               parametros.addValue("profesor", profesor);
               parametros.addValue("id", id);
               int filas = namedJdbcTemplate.update(sql, parametros);
               System.out.println("Filas afectadas: " + filas);
           } catch (Exception e) {
	           System.out.println("Error en la consulta: " + e.getMessage());
	       }
	   }
}
