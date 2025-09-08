package com.example.planaula.services;

import com.example.planaula.Dto.ProfesorDTO;
import com.example.planaula.Dto.TurnoDTO;
import com.example.planaula.Dto.TutorDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProfesoresService {

    @Autowired
    private EntityManager entityManager;

    public List<ProfesorDTO> findAllProfesores() {
        String sql = "SELECT id, nombre FROM profesores order by nombre";

        List<Object[]> resultados = entityManager.createNativeQuery(sql).getResultList();

        if (resultados == null || resultados.isEmpty()) {
            return Collections.emptyList();
        }

        return resultados.stream()
                .map(resultado -> new ProfesorDTO(
                        ((Number) resultado[0]).intValue(),
                        (String) resultado[1]
                ))
                .collect(Collectors.toList());
    }

    public ProfesorDTO findProfesorById(Integer id) {
        String sql = "SELECT id, nombre FROM profesores WHERE id = :id";
        Object[] resultado = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("id", id)
                .getSingleResult();
        return new ProfesorDTO(
                ((Number) resultado[0]).intValue(),
                (String) resultado[1]);
    }

    public Page<ProfesorDTO> findPageProfesores(Pageable pageable) {
        try {
            String countSql = "SELECT COUNT(*) FROM profesores";
            Query countQuery = entityManager.createNativeQuery(countSql);
            long total = ((Number) countQuery.getSingleResult()).longValue();

            String sql = "SELECT id, nombre FROM profesores order by nombre";
            Query query = entityManager.createNativeQuery(sql)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());

            List<Object[]> resultados = query.getResultList();

            List<ProfesorDTO> profesores = resultados.stream()
                    .map(resultado -> new ProfesorDTO(
                            ((Number) resultado[0]).intValue(),
                            (String) resultado[1]
                    ))
                    .collect(Collectors.toList());

            return new PageImpl<>(profesores, pageable, total);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
    }

    @Transactional
    public void anadirProfesor(ProfesorDTO profesorDTO) {
        try {
            String insertSql = "INSERT INTO profesores (nombre, apellidos) VALUES (:nombre, :apellidos)";
            int filasInsertadas = entityManager.createNativeQuery(insertSql)
                    .setParameter("nombre", profesorDTO.getNombre())
                    .setParameter("apellidos", profesorDTO.getApellidos())
                    .executeUpdate();

            if (filasInsertadas == 0) {
                throw new RuntimeException("No se insertó ningún registro en la base de datos.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al insertar profesor: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void eliminarProfesorById(int id) {
        try {
            String deleteSql = "DELETE FROM profesores WHERE id = :id"; // Ajuste de columna
            int filasEliminadas = entityManager.createNativeQuery(deleteSql)
                    .setParameter("id", id)
                    .executeUpdate();

            if (filasEliminadas == 0) {
                throw new RuntimeException("No se encontró un profesor con ID: " + id);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar profesor: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void modificarProfesor(ProfesorDTO profesorDTO) {
        try {
            String updateSql = "UPDATE profesores SET nombre = :nombre, apellidos = :apellidos WHERE id = :id";
            int filasActualizadas = entityManager.createNativeQuery(updateSql)
                    .setParameter("id", profesorDTO.getId())
                    .setParameter("nombre", profesorDTO.getNombre())
                    .setParameter("apellidos", profesorDTO.getApellidos())
                    .executeUpdate();
        } catch (Exception e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }


    public List<TutorDTO> findAllTutores() {
        String sql = "SELECT t.id, p.nombre, p.apellidos, c.curso t.anio\n" +
                "FROM tutores t\n" +
                "JOIN profesores p ON t.id_profesor = p.id";

        List<Object[]> resultados = entityManager.createNativeQuery(sql).getResultList();

        if (resultados == null || resultados.isEmpty()) {
            return Collections.emptyList();
        }

        return resultados.stream()
                .map(resultado -> new TutorDTO(
                        (resultado[0] != null ? ((Number) resultado[0]).intValue() : 0),
                        (String) resultado[1],
                        (String) resultado[2],
                        (String) resultado[3],
                        (String) resultado[4]
                ))
                .collect(Collectors.toList());
    }

    public List<TutorDTO> findAllTutoresByCursoAndProfesor(int curso, int profesor) {
        String sql = "SELECT t.id, p.nombre, p.apellidos, c.curso, t.anio\n" +
                "FROM tutores t\n" +
                "JOIN profesores p ON t.id_profesor = p.id\n" +
                "JOIN cursos c ON t.id_curso = c.id\n" +
                "WHERE (:curso IS NULL OR :curso = 0 OR c.id = :curso)\n" +
                "AND (:profesor IS NULL OR :profesor = 0 OR p.id = :profesor)";

        List<Object[]> resultados = entityManager.createNativeQuery(sql)
                .setParameter("curso", curso)
                .setParameter("profesor", profesor)
                .getResultList();

        if (resultados == null || resultados.isEmpty()) {
            return Collections.emptyList();
        }

        return resultados.stream()
                .map(resultado -> new TutorDTO(
                        (resultado[0] != null ? ((Number) resultado[0]).intValue() : 0),
                        (String) resultado[1],
                        (String) resultado[2],
                        (String) resultado[3],
                        (String) resultado[4]
                ))
                .collect(Collectors.toList());
    }

    public Page<TutorDTO> findPageTutoresByCursoAndProfesor(int curso, int profesor, Pageable pageable) {
        try {
            String countSql = "SELECT count(*)\n" +
                    "FROM tutores t\n" +
                    "JOIN profesores p ON t.id_profesor = p.id\n" +
                    "JOIN cursos c ON t.id_curso = c.id\n" +
                    "WHERE (:curso IS NULL OR :curso = 0 OR c.id = :curso)\n" +
                    "AND (:profesor IS NULL OR :profesor = 0 OR p.id = :profesor)";

            Query countQuery = entityManager.createNativeQuery(countSql)
                    .setParameter("curso", curso)
                    .setParameter("profesor", profesor);
            long total = ((Number) countQuery.getSingleResult()).longValue();

            String sql = "SELECT t.id, p.nombre, p.apellidos, c.curso, t.anio\n" +
                    "FROM tutores t\n" +
                    "JOIN profesores p ON t.id_profesor = p.id\n" +
                    "JOIN cursos c ON t.id_curso = c.id\n" +
                    "WHERE (:curso IS NULL OR :curso = 0 OR c.id = :curso)\n" +
                    "AND (:profesor IS NULL OR :profesor = 0 OR p.id = :profesor)";

            Query query = entityManager.createNativeQuery(sql)
                    .setParameter("curso", curso)
                    .setParameter("profesor", profesor)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());

            List<Object[]> resultados = query.getResultList();

            List<TutorDTO> tutores = resultados.stream()
                    .map(resultado -> new TutorDTO(
                            (resultado[0] != null ? ((Number) resultado[0]).intValue() : 0),
                            (String) resultado[1],
                            (String) resultado[2],
                            (String) resultado[3],
                            (String) resultado[4]
                    ))
                    .collect(Collectors.toList());

            return new PageImpl<>(tutores, pageable, total);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
    }

    /*public void modificarTutor(TutorDTO tutorDTO) {
        List<String> campos = new ArrayList<>();
        if (tutorDTO.getCurso() != null) campos.add("id_curso = :curso");
        if (tutorDTO.getTutor2425() != null) campos.add("id = :tutor2425");
        if (tutorDTO.getTutor2324() != null) campos.add("id = :tutor2324");

        try {
            if (campos.isEmpty()) {
                throw new IllegalArgumentException("Debe proporcionar al menos un campo para actualizar");
            }
            String updateSql = "UPDATE tutoria SET " + String.join(", ", campos) + " WHERE id_tutoria = :id"; // Ajuste de columna

            Query query = entityManager.createNativeQuery(updateSql)
                    .setParameter("id", tutorDTO.getId());

            if (tutorDTO.getCurso() != null) {
                query.setParameter("curso", tutorDTO.getCurso());
            }
            if (tutorDTO.getTutor2425() != null) {
                query.setParameter("tutor2425", tutorDTO.getTutor2425());
            }
            if (tutorDTO.getTutor2324() != null) {
                query.setParameter("tutor2324", tutorDTO.getTutor2324());
            }
            query.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<TurnoDTO> findTaskById(Integer id_profesor){
        String sql = "SELECT 'G' AS tipo, g.id AS id_guardia, d.dia, ho.hora FROM guardias g JOIN dias d ON g.id_dia = d.id JOIN horas ho ON g.id_hora = ho.id WHERE g.id_profesor = :id_profesor UNION ALL SELECT 'L' AS tipo, l.id AS id_libranza, d.dia, ho.hora FROM libranzas l JOIN dias d ON l.id_dia = d.id JOIN horas ho ON l.id_hora = ho.id WHERE l.id_profesor = :id_profesor UNION ALL SELECT 'R' AS tipo, r.id AS id_recreo, d.dia, ho.hora FROM recreos r JOIN dias d ON r.id_dia = d.id JOIN horas ho ON r.id_hora = ho.id WHERE r.id_profesor = :id_profesor;";

        List<Object[]> resultados = entityManager.createNativeQuery(sql)
                .setParameter("id_profesor", id_profesor)
                .getResultList();

        if (resultados == null || resultados.isEmpty()) {
            return Collections.emptyList();
        }

        return resultados.stream()
                .map(resultado -> new TurnoDTO(
                        ((Number) resultado[1]).intValue(),
                        (String) resultado[0] + resultado[1],
                        (String) resultado[2],
                        (String) resultado[3],
                        null,
                        null
                ))
                .collect(Collectors.toList());
    }*/

}