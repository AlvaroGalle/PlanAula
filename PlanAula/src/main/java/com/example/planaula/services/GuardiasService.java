package com.example.planaula.services;

import com.example.planaula.Dto.TurnoDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public Page<TurnoDTO> findAllTurnosByDiaHoraProfesor(
            Integer dia, Integer hora, Integer profesor, String tipo, Pageable pageable) {

        if (tipo == null || tipo.equals("0") || tipo.isBlank()) {
            tipo = "T";
        }

        String sql = """
        WITH u AS (
            SELECT 'Guardia' AS tipo, g.id, g.id_profesor, p.nombre, p.apellidos, d.dia, h.hora
            FROM guardias g
            JOIN profesores p ON g.id_profesor = p.id
            JOIN dias d ON g.id_dia = d.id
            JOIN horas h ON g.id_hora = h.id

            UNION ALL

            SELECT 'Recreo' AS tipo, r.id, r.id_profesor, p.nombre, p.apellidos, d.dia, h.hora
            FROM recreos r
            JOIN profesores p ON r.id_profesor = p.id
            JOIN dias d ON r.id_dia = d.id
            JOIN horas h ON r.id_hora = h.id

            UNION ALL

            SELECT 'Libranza' AS tipo, l.id, l.id_profesor, p.nombre, p.apellidos, d.dia, h.hora
            FROM libranzas l
            JOIN profesores p ON l.id_profesor = p.id
            JOIN dias d ON l.id_dia = d.id
            JOIN horas h ON l.id_hora = h.id
        )
        SELECT tipo, id, id_profesor, nombre, apellidos, dia, hora
        FROM u
        WHERE (:tipo = 'T' OR u.tipo = :tipo)
          AND (:profesor = 0 OR u.id_profesor = :profesor)
          AND (:dia = 0 OR u.dia = (SELECT dia FROM dias WHERE id = :dia))
          AND (:hora = 0 OR u.hora = (SELECT hora FROM horas WHERE id = :hora))
        ORDER BY u.dia, u.hora, u.tipo
        """;

        @SuppressWarnings("unchecked")
        List<Object[]> filas = entityManager.createNativeQuery(sql)
                .setParameter("tipo", tipo)
                .setParameter("profesor", profesor)
                .setParameter("dia", dia)
                .setParameter("hora", hora)
                .getResultList();

        if (filas == null || filas.isEmpty()){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        List<TurnoDTO> turnos =  filas.stream()
                .map(r -> new TurnoDTO(
                        ((String) r[0]),
                        ((Number) r[1]).intValue(),
                        (String) r[5],
                        (String) r[6],
                        (String) r[3],
                        (String) r[4]
                ))
                .collect(Collectors.toList());
        return new PageImpl<>(turnos, pageable, turnos.size());
    }


    /*public List<TurnoDTO> findAllRecreosByDiaAndHoraAndProfesor(Integer dia, Integer hora, Integer profesor) {
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
                .map(resultado -> new TurnoDTO(
                        ((Number) resultado[0]).intValue(),
                        (String) "L" + resultado[0],
                        (String) resultado[1],
                        (String) resultado[2],
                        (String) resultado[3],
                        (String) resultado[4]
                ))
                .collect(Collectors.toList());
    }

    public List<TurnoDTO> findAllLibranzasByDiaAndHoraAndProfesor(Integer dia, Integer hora, Integer profesor) {
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
                .map(resultado -> new TurnoDTO(
                        ((Number) resultado[0]).intValue(),
                        (String) "L" + resultado[0],
                        (String) resultado[1],
                        (String) resultado[2],
                        (String) resultado[3],
                        (String) resultado[4]
                ))
                .collect(Collectors.toList());
    }

    public List<TurnoDTO> findAllGuardiasByDiaAndHoraAndProfesor(Integer dia, Integer hora, Integer profesor) {
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
                .map(resultado -> new TurnoDTO(
                        ((Number) resultado[0]).intValue(),
                        (String) "G" + resultado[0],
                        (String) resultado[1],
                        (String) resultado[2],
                        (String) resultado[3],
                        (String) resultado[4]
                ))
                .collect(Collectors.toList());
    }

    public List<TurnoDTO> findAllByDiaAndHoraAndProfesor(Integer dia, Integer hora, Integer profesor) {
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
                .map(resultado -> new TurnoDTO(
                        ((Number) resultado[0]).intValue(),
                        (String) "X" + resultado[0],
                        (String) resultado[1],
                        (String) resultado[2],
                        (String) resultado[3],
                        (String) resultado[4]
                ))
                .collect(Collectors.toList());
    }*/

    public TurnoDTO findTurnoByTipoAndId(String tipo, Integer id) {
        String sql = """
        SELECT 'Guardia' AS tipo, g.id, g.id_profesor, p.nombre, p.apellidos,
               d.id AS id_dia, d.dia, h.id AS id_hora, h.hora
        FROM guardias g
        JOIN profesores p ON g.id_profesor = p.id
        JOIN dias d ON g.id_dia = d.id
        JOIN horas h ON g.id_hora = h.id
        WHERE :tipo = 'Guardia' AND g.id = :id

        UNION ALL

        SELECT 'Recreo', r.id, r.id_profesor, p.nombre, p.apellidos,
               d.id, d.dia, h.id, h.hora
        FROM recreos r
        JOIN profesores p ON r.id_profesor = p.id
        JOIN dias d ON r.id_dia = d.id
        JOIN horas h ON r.id_hora = h.id
        WHERE :tipo = 'Recreo' AND r.id = :id

        UNION ALL

        SELECT 'Libranza', l.id, l.id_profesor, p.nombre, p.apellidos,
               d.id, d.dia, h.id, h.hora
        FROM libranzas l
        JOIN profesores p ON l.id_profesor = p.id
        JOIN dias d ON l.id_dia = d.id
        JOIN horas h ON l.id_hora = h.id
        WHERE :tipo = 'Libranza' AND l.id = :id
        """;

        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery(sql)
                .setParameter("tipo", tipo)
                .setParameter("id", id)
                .getResultList();

        if (rows.isEmpty()) return new TurnoDTO();

        Object[] r = rows.get(0);
        return new TurnoDTO(
                (String) r[0],
                ((Number) r[1]).intValue(),
                (String) r[6],
                (String) r[8],
                r[2].toString(),
                (String) r[4]
        );
    }


    @Transactional
    public void anadirTurno(TurnoDTO turno) {
        String tabla;
        switch (turno.getTipo()) {
            case "G" -> tabla = "guardias";
            case "L" -> tabla = "libranzas";
            case "R" -> tabla = "recreos";
            default -> throw new IllegalArgumentException("Tipo de turno inválido: " + turno.getTipo());
        }

        String sql = """
        INSERT INTO %s (id_profesor, id_dia, id_hora)
        VALUES (:profesor, :dia, :hora)
        """.formatted(tabla);

        entityManager.createNativeQuery(sql)
                .setParameter("profesor", Integer.parseInt(turno.getNombre()))
                .setParameter("dia", Integer.parseInt(turno.getDia()))
                .setParameter("hora", Integer.parseInt(turno.getHora()))
                .executeUpdate();
    }

    @Transactional
    public void accionGuardias(String accion, String tipo, Integer profesor, Integer id) {
        if (accion == null || tipo == null || id == null) {
            throw new IllegalArgumentException("accion, tipo e id son obligatorios");
        }
        String sql;

        if (accion.equals("E")) {
            sql = "DELETE FROM " + tipo.toLowerCase() + "s" + " WHERE id = :id";
            entityManager.createNativeQuery(sql)
                    .setParameter("id", id)
                    .executeUpdate();

        } else if (accion.equals("M")) {
            if (profesor == null) {
                throw new IllegalArgumentException("Para UPDATE se requiere profesor");
            }
            sql = "UPDATE " + tipo.toLowerCase() + "s" + " SET id_profesor = :profesor WHERE id = :id";
            entityManager.createNativeQuery(sql)
                    .setParameter("profesor", profesor)
                    .setParameter("id", id)
                    .executeUpdate();

        } else {
            throw new IllegalArgumentException("Acción no soportada: " + accion);
        }
    }
}
