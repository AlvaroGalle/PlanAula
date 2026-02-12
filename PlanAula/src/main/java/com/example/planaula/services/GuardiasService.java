package com.example.planaula.services;

import com.example.planaula.dto.TurnoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
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

    public Page<TurnoDTO> findPageTurnosByDiaHoraProfesor(
            Integer dia, Integer hora, Integer profesor, String tipo, Pageable pageable, int idCentro) {

        if (tipo == null || tipo.equals("0") || tipo.isBlank()) {
            tipo = "T";
        }
        if (tipo.equals("G")) tipo = "Guardia";
        if (tipo.equals("R")) tipo = "Recreo";
        if (tipo.equals("L")) tipo = "Libranza";

        String sql = """
        	    WITH u AS (
        	        SELECT 'Guardia' AS tipo, g.id, g.id_profesor, p.nombre, p.apellidos, d.dia, h.hora
        	        FROM guardias g
        	        JOIN profesores p ON g.id_profesor = p.id
        	        JOIN dias d ON g.id_dia = d.id
        	        JOIN horas h ON g.id_hora = h.id
        	        WHERE g.id_centro = :idCentro
        	          AND p.id_centro = :idCentro
        	          AND d.id_centro = :idCentro
        	          AND h.id_centro = :idCentro

        	        UNION ALL

        	        SELECT 'Recreo' AS tipo, r.id, r.id_profesor, p.nombre, p.apellidos, d.dia, h.hora
        	        FROM recreos r
        	        JOIN profesores p ON r.id_profesor = p.id
        	        JOIN dias d ON r.id_dia = d.id
        	        JOIN horas h ON r.id_hora = h.id
        	        WHERE r.id_centro = :idCentro
        	          AND p.id_centro = :idCentro
        	          AND d.id_centro = :idCentro
        	          AND h.id_centro = :idCentro

        	        UNION ALL

        	        SELECT 'Libranza' AS tipo, l.id, l.id_profesor, p.nombre, p.apellidos, d.dia, h.hora
        	        FROM libranzas l
        	        JOIN profesores p ON l.id_profesor = p.id
        	        JOIN dias d ON l.id_dia = d.id
        	        JOIN horas h ON l.id_hora = h.id
        	        WHERE l.id_centro = :idCentro
        	          AND p.id_centro = :idCentro
        	          AND d.id_centro = :idCentro
        	          AND h.id_centro = :idCentro
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
                .setParameter("idCentro", idCentro)
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
        return new PageImpl<>(turnos, pageable, countTurnos(dia, hora, profesor, tipo, idCentro));
    }

    public List<TurnoDTO> findAllTurnosByDiaHoraProfesor(
            Integer dia, Integer hora, Integer profesor, String tipo, int idCentro) {

        if (tipo == null || tipo.equals("0") || tipo.isBlank()) {
            tipo = "T";
        }
        if (tipo.equals("G")) tipo = "Guardia";
        if (tipo.equals("R")) tipo = "Recreo";
        if (tipo.equals("L")) tipo = "Libranza";

        String sql = """
        	    WITH u AS (
        	        SELECT 'Guardia' AS tipo, g.id, g.id_profesor, p.nombre, p.apellidos, d.dia, h.hora
        	        FROM guardias g
        	        JOIN profesores p ON g.id_profesor = p.id
        	        JOIN dias d ON g.id_dia = d.id
        	        JOIN horas h ON g.id_hora = h.id
        	        WHERE g.id_centro = :idCentro
        	          AND p.id_centro = :idCentro
        	          AND d.id_centro = :idCentro
        	          AND h.id_centro = :idCentro

        	        UNION ALL

        	        SELECT 'Recreo' AS tipo, r.id, r.id_profesor, p.nombre, p.apellidos, d.dia, h.hora
        	        FROM recreos r
        	        JOIN profesores p ON r.id_profesor = p.id
        	        JOIN dias d ON r.id_dia = d.id
        	        JOIN horas h ON r.id_hora = h.id
        	        WHERE r.id_centro = :idCentro
        	          AND p.id_centro = :idCentro
        	          AND d.id_centro = :idCentro
        	          AND h.id_centro = :idCentro

        	        UNION ALL

        	        SELECT 'Libranza' AS tipo, l.id, l.id_profesor, p.nombre, p.apellidos, d.dia, h.hora
        	        FROM libranzas l
        	        JOIN profesores p ON l.id_profesor = p.id
        	        JOIN dias d ON l.id_dia = d.id
        	        JOIN horas h ON l.id_hora = h.id
        	        WHERE l.id_centro = :idCentro
        	          AND p.id_centro = :idCentro
        	          AND d.id_centro = :idCentro
        	          AND h.id_centro = :idCentro
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
                .setParameter("idCentro", idCentro)
                .getResultList();


        return filas.stream()
                .map(r -> new TurnoDTO(
                        ((String) r[0]),
                        ((Number) r[1]).intValue(),
                        (String) r[5],
                        (String) r[6],
                        (String) r[3],
                        (String) r[4]
                ))
                .collect(Collectors.toList());
    }

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

    public Long countTurnos(Integer dia, Integer hora, Integer profesor, String tipo, int idCentro) {
    	String countSql = """
    		    WITH u AS (
    		        SELECT 'Guardia' AS tipo, g.id, g.id_profesor, p.nombre, p.apellidos, d.dia, h.hora
    		        FROM guardias g
    		        JOIN profesores p ON g.id_profesor = p.id
    		        JOIN dias d ON g.id_dia = d.id
    		        JOIN horas h ON g.id_hora = h.id
    		        WHERE g.id_centro = :idCentro
    		          AND p.id_centro = :idCentro
    		          AND d.id_centro = :idCentro
    		          AND h.id_centro = :idCentro

    		        UNION ALL

    		        SELECT 'Recreo' AS tipo, r.id, r.id_profesor, p.nombre, p.apellidos, d.dia, h.hora
    		        FROM recreos r
    		        JOIN profesores p ON r.id_profesor = p.id
    		        JOIN dias d ON r.id_dia = d.id
    		        JOIN horas h ON r.id_hora = h.id
    		        WHERE r.id_centro = :idCentro
    		          AND p.id_centro = :idCentro
    		          AND d.id_centro = :idCentro
    		          AND h.id_centro = :idCentro

    		        UNION ALL

    		        SELECT 'Libranza' AS tipo, l.id, l.id_profesor, p.nombre, p.apellidos, d.dia, h.hora
    		        FROM libranzas l
    		        JOIN profesores p ON l.id_profesor = p.id
    		        JOIN dias d ON l.id_dia = d.id
    		        JOIN horas h ON l.id_hora = h.id
    		        WHERE l.id_centro = :idCentro
    		          AND p.id_centro = :idCentro
    		          AND d.id_centro = :idCentro
    		          AND h.id_centro = :idCentro
    		    )
    		    SELECT COUNT(*)
    		    FROM u
    		    WHERE (:tipo = 'T' OR u.tipo = :tipo)
    		      AND (:profesor = 0 OR u.id_profesor = :profesor)
    		      AND (:dia = 0 OR u.dia = (SELECT dia FROM dias WHERE id = :dia))
    		      AND (:hora = 0 OR u.hora = (SELECT hora FROM horas WHERE id = :hora))
    		""";

        @SuppressWarnings("unchecked")
        Query countQuery = entityManager.createNativeQuery(countSql)
                .setParameter("tipo", tipo)
                .setParameter("profesor", profesor)
                .setParameter("dia", dia)
                .setParameter("hora", hora)
                .setParameter("idCentro", idCentro);
        return ((Number) countQuery.getSingleResult()).longValue();
    }

    @Transactional
    public void anadirTurno(TurnoDTO turno, int idCentro) {
        String tabla;
        switch (turno.getTipo()) {
            case "G" -> tabla = "guardias";
            case "L" -> tabla = "libranzas";
            case "R" -> tabla = "recreos";
            default -> throw new IllegalArgumentException("Tipo de turno inválido: " + turno.getTipo());
        }

        String sql = """
        INSERT INTO %s (id_profesor, id_dia, id_hora, id_centro)
        VALUES (:profesor, :dia, :hora, :idCentro)
        """.formatted(tabla);

        entityManager.createNativeQuery(sql)
                .setParameter("profesor", Integer.parseInt(turno.getNombre()))
                .setParameter("dia", Integer.parseInt(turno.getDia()))
                .setParameter("hora", Integer.parseInt(turno.getHora()))
                .setParameter("idCentro", idCentro)
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
