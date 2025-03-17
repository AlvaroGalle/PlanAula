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
        String sql = "SELECT * FROM `profes 2425`";

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
        String sql = "SELECT * FROM `profes 2425` where `id` = :id";
        Object[] resultado = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("id", id)
                .getSingleResult();
        return new ProfesorDTO(
                ((Number) resultado[0]).intValue(),
                (String) resultado[1]);
    }

    public Page<ProfesorDTO> findPageProfesores(Pageable pageable) {
        try {
            String countSql = "SELECT COUNT(*) FROM `profes 2425`";
            Query countQuery = entityManager.createNativeQuery(countSql);
            long total = ((Number) countQuery.getSingleResult()).longValue();

            String sql = "SELECT * FROM `profes 2425`";
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
            String maxIdSql = "SELECT COALESCE(MAX(id), 0) FROM `profes 2425`";
            int newId = ((Number) entityManager.createNativeQuery(maxIdSql).getSingleResult()).intValue() + 1;
            profesorDTO.setId(newId);

            String insertSql = "INSERT INTO `profes 2425` (id, campo1) VALUES (:id, :nombre)";
            int filasInsertadas = entityManager.createNativeQuery(insertSql)
                    .setParameter("id", newId)
                    .setParameter("nombre", profesorDTO.getNombre())
                    .executeUpdate();

            if (filasInsertadas == 0) {
                throw new RuntimeException("No se insertó ningún registro en la base de datos.");
            }

        } catch (Exception e) {
            // Usa logging en lugar de imprimir en consola
            throw new RuntimeException("Error al insertar profesor: " + e.getMessage(), e);
        }
    }


    @Transactional
    public void eliminarProfesorById(int id) {
        try {
            String deleteSql = "DELETE FROM `profes 2425` WHERE id = :id";
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
                String updateSql = "UPDATE `profes 2425` SET campo1 = :nombre WHERE id = :id";
                int filasActualizadas = entityManager.createNativeQuery(updateSql)
                        .setParameter("id", profesorDTO.getId())
                        .setParameter("nombre", profesorDTO.getNombre())
                        .executeUpdate();
            } catch (Exception e) {
                System.out.println("Error en la consulta: " + e.getMessage());
            }

        }

    public List<TutorDTO> findAllTutores() {
        String sql = "SELECT t.id, c.campo1 AS nombre_curso, p1.campo1 AS nombre_tutor_2425, p2.campo1 AS nombre_tutor_2324 " +
                "FROM `tutores 2425` t LEFT JOIN cursos c ON t.curso = c.id " +
                "LEFT JOIN `profes 2425` p1 ON t.`tutor 2425` = p1.id " +
                "LEFT JOIN `profes 2425` p2 ON t.`tutor 2324` = p2.id";

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
        String sql = "SELECT t.id, c.campo1 AS nombre_curso, p1.campo1 AS nombre_tutor_2425, p2.campo1 AS nombre_tutor_2324 FROM `tutores 2425` t " +
                "LEFT JOIN cursos c ON t.curso = c.id " +
                "LEFT JOIN `profes 2425` p1 ON t.`tutor 2425` = p1.id " +
                "LEFT JOIN `profes 2425` p2 ON t.`tutor 2324` = p2.id " +
                "WHERE (:curso = 0 OR c.id = :curso) AND (:profesor = 0 OR p1.id = :profesor OR p2.id = :profesor)";

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
            String countSql = "SELECT COUNT(*) FROM `tutores 2425` t " +
                    "LEFT JOIN cursos c ON t.curso = c.id " +
                    "LEFT JOIN `profes 2425` p1 ON t.`tutor 2425` = p1.id " +
                    "LEFT JOIN `profes 2425` p2 ON t.`tutor 2324` = p2.id " +
                    "WHERE (:curso = 0 OR c.id = :curso) AND (:profesor = 0 OR p1.id = :profesor OR p2.id = :profesor)";
            Query countQuery = entityManager.createNativeQuery(countSql)
                    .setParameter("curso", curso)
                    .setParameter("profesor", profesor);
            long total = ((Number) countQuery.getSingleResult()).longValue();

            String sql = "SELECT t.id, c.campo1 AS nombre_curso, p1.campo1 AS nombre_tutor_2425, p2.campo1 AS nombre_tutor_2324 FROM `tutores 2425` t " +
                    "LEFT JOIN cursos c ON t.curso = c.id " +
                    "LEFT JOIN `profes 2425` p1 ON t.`tutor 2425` = p1.id " +
                    "LEFT JOIN `profes 2425` p2 ON t.`tutor 2324` = p2.id " +
                    "WHERE (:curso = 0 OR c.id = :curso) AND (:profesor = 0 OR p1.id = :profesor OR p2.id = :profesor)";

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
        if (tutorDTO.getCurso() != null) campos.add("`curso` = :curso");
        if (tutorDTO.getTutor2425() != null) campos.add("`tutor 2425` = :tutor2425");
        if (tutorDTO.getTutor2324() != null) campos.add("`tutor 2324` = :tutor2324");

        try {


            if (campos.isEmpty()) {
                throw new IllegalArgumentException("Debe proporcionar al menos un campo para actualizar");
            }
            String updateSql = "UPDATE `tutores 2425` SET " + String.join(", ", campos) + " WHERE id = :id";

            Query query = entityManager.createNativeQuery(updateSql)
                    .setParameter("id", tutorDTO.getId());

            entityManager.close();
            if (tutorDTO.getCurso() != null) {
                query.setParameter("curso", tutorDTO.getCurso());
            }
            if (tutorDTO.getTutor2425() != null) {
                query.setParameter("tutor2425", Objects.equals(tutorDTO.getTutor2425(), "") ? null : tutorDTO.getTutor2425());
            }
            if (tutorDTO.getTutor2324() != null) {
                query.setParameter("tutor2324", Objects.equals(tutorDTO.getTutor2324(), "") ? null : tutorDTO.getTutor2324());
            }
            query.executeUpdate();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}