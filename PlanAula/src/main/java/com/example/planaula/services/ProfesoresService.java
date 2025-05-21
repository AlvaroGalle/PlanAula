package com.example.planaula.services;

import com.example.planaula.Dto.ProfesorDTO;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProfesoresService {

    @Autowired
    private EntityManager entityManager;

    public List<ProfesorDTO> findAllProfesores() {
        String sql = "SELECT id, nombre FROM profesores"; // Ajuste de columnas

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
        String sql = "SELECT id, nombre FROM profesores WHERE id = :id"; // Ajuste de columnas
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

            String sql = "SELECT id, nombre FROM profesores"; // Ajuste de columnas
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
            String insertSql = "INSERT INTO profesores (nombre, es_tutor) VALUES (:nombre, :esTutor)"; // Ajuste de columnas
            int filasInsertadas = entityManager.createNativeQuery(insertSql)
                    .setParameter("nombre", profesorDTO.getNombre())
                    .setParameter("esTutor", profesorDTO.getApellidos())
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
            String updateSql = "UPDATE profesores SET nombre = :nombre, es_tutor = :esTutor WHERE id = :id"; // Ajuste de columna
            int filasActualizadas = entityManager.createNativeQuery(updateSql)
                    .setParameter("id", profesorDTO.getId())
                    .setParameter("nombre", profesorDTO.getNombre())
                    .setParameter("esTutor", profesorDTO.isTutor())
                    .executeUpdate();
        } catch (Exception e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }


    public List<TutorDTO> findAllTutores() {
        String sql = "SELECT t.id_tutoria, c.nombre AS nombre_curso, p1.nombre AS nombre_tutor_2425, p2.nombre AS nombre_tutor_2324 " +
                "FROM tutoria t " +
                "LEFT JOIN cursos c ON t.id_curso = c.id_curso " +
                "LEFT JOIN profesores p1 ON t.id = p1.id " +
                "LEFT JOIN profesores p2 ON t.id = p2.id"; // Ajuste de nombres de tablas y columnas

        List<Object[]> resultados = entityManager.createNativeQuery(sql).getResultList();

        if (resultados == null || resultados.isEmpty()) {
            return Collections.emptyList();
        }

        return resultados.stream()
                .map(resultado -> new TutorDTO(
                        (resultado[0] != null ? ((Number) resultado[0]).intValue() : 0),
                        (String) resultado[1],
                        (String) resultado[2],
                        (String) resultado[3]))
                .collect(Collectors.toList());
    }

    public List<TutorDTO> findAllTutoresByCursoAndProfesor(int curso, int profesor) {
        String sql = "SELECT t.id_tutoria, c.nombre AS nombre_curso, p1.nombre AS nombre_tutor_2425, p2.nombre AS nombre_tutor_2324 " +
                "FROM tutoria t " +
                "LEFT JOIN cursos c ON t.id_curso = c.id_curso " +
                "LEFT JOIN profesores p1 ON t.id = p1.id " +
                "LEFT JOIN profesores p2 ON t.id = p2.id " +
                "WHERE (:curso = 0 OR c.id_curso = :curso) AND (:profesor = 0 OR p1.id = :profesor OR p2.id = :profesor)"; // Ajuste de nombres de tablas y columnas

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
                        (String) resultado[3]))
                .collect(Collectors.toList());
    }

    public Page<TutorDTO> findPageTutoresByCursoAndProfesor(int curso, int profesor, Pageable pageable) {
        try {
            String countSql = "SELECT COUNT(*) FROM tutoria t " +
                    "LEFT JOIN cursos c ON t.id_curso = c.id_curso " +
                    "LEFT JOIN profesores p1 ON t.id = p1.id " +
                    "LEFT JOIN profesores p2 ON t.id = p2.id " +
                    "WHERE (:curso = 0 OR c.id_curso = :curso) AND (:profesor = 0 OR p1.id = :profesor OR p2.id = :profesor)";
            Query countQuery = entityManager.createNativeQuery(countSql)
                    .setParameter("curso", curso)
                    .setParameter("profesor", profesor);
            long total = ((Number) countQuery.getSingleResult()).longValue();

            String sql = "SELECT t.id_tutoria, c.nombre AS nombre_curso, p1.nombre AS nombre_tutor_2425, p2.nombre AS nombre_tutor_2324 " +
                    "FROM tutoria t " +
                    "LEFT JOIN cursos c ON t.id_curso = c.id_curso " +
                    "LEFT JOIN profesores p1 ON t.id = p1.id " +
                    "LEFT JOIN profesores p2 ON t.id = p2.id " +
                    "WHERE (:curso = 0 OR c.id_curso = :curso) AND (:profesor = 0 OR p1.id = :profesor OR p2.id = :profesor)";

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
                            (String) resultado[3]))
                    .collect(Collectors.toList());

            return new PageImpl<>(tutores, pageable, total);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
    }

    public void modificarTutor(TutorDTO tutorDTO) {
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


}