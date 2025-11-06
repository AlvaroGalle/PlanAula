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

    public List<ProfesorDTO> findAllProfesores(int idCentro) {
        String sql = "SELECT id, nombre FROM profesores WHERE id_centro = :idCentro order by nombre";

        List<Object[]> resultados = entityManager.createNativeQuery(sql)
        							.setParameter("idCentro", idCentro)
        							.getResultList();

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

    public Page<ProfesorDTO> findPageProfesores(Pageable pageable, int idCentro) {
        try {
            String countSql = "SELECT COUNT(*) FROM profesores WHERE id_centro = :idCentro";
            Query countQuery = entityManager.createNativeQuery(countSql)
            					.setParameter("idCentro", idCentro);
            long total = ((Number) countQuery.getSingleResult()).longValue();

            String sql = "SELECT id, nombre FROM profesores WHERE id_centro = :idCentro order by nombre";
            Query query = entityManager.createNativeQuery(sql)
            		.setParameter("idCentro", idCentro)
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
    public void anadirProfesor(ProfesorDTO profesorDTO, int idCentro) {
        try {
            String insertSql = "INSERT INTO profesores (nombre, apellidos, id_centro) VALUES (:nombre, :apellidos, :idCentro)";
            int filasInsertadas = entityManager.createNativeQuery(insertSql)
                    .setParameter("nombre", profesorDTO.getNombre())
                    .setParameter("apellidos", profesorDTO.getApellidos())
                    .setParameter("idCentro", idCentro)
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
            String deleteSql = "DELETE FROM profesores WHERE id = :id";
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


    public List<TutorDTO> findAllTutores(int idCentro) {
    	String sql = """
    			SELECT t.id, p.nombre, p.apellidos, c.curso, t.anio
    			FROM tutores t
    			JOIN profesores p ON t.id_profesor = p.id
    			JOIN cursos c ON t.id_curso = c.id
    			WHERE (:idCentro IS NULL OR :idCentro = 0 OR t.id_centro = :idCentro);
    			""";

        List<Object[]> resultados = entityManager.createNativeQuery(sql)
        							.setParameter("idCentro", idCentro)
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

    public List<TutorDTO> findAllTutoresByCursoAndProfesor(int curso, int profesor, int idCentro) {
    	String sql = """
    			SELECT t.id, p.nombre, p.apellidos, c.curso, t.anio
    			FROM tutores t
    			JOIN profesores p ON t.id_profesor = p.id
    			JOIN cursos c ON t.id_curso = c.id
    			WHERE (:curso IS NULL OR :curso = 0 OR c.id = :curso)
    			  AND (:profesor IS NULL OR :profesor = 0 OR p.id = :profesor)
    			  AND (:idCentro IS NULL OR :idCentro = 0 OR t.id_centro = :idCentro);
    			""";


        List<Object[]> resultados = entityManager.createNativeQuery(sql)
                .setParameter("curso", curso)
                .setParameter("profesor", profesor)
                .setParameter("idCentro", idCentro)
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

    public Page<TutorDTO> findPageTutoresByCursoAndProfesor(int curso, int profesor, Pageable pageable, int idCentro) {
        try {
        	String countSql = """
        			SELECT COUNT(*)
        			FROM tutores t
        			JOIN profesores p ON t.id_profesor = p.id
        			JOIN cursos c ON t.id_curso = c.id
        			WHERE (:curso IS NULL OR :curso = 0 OR c.id = :curso)
        			  AND (:profesor IS NULL OR :profesor = 0 OR p.id = :profesor)
        			  AND (:idCentro IS NULL OR :idCentro = 0 OR t.id_centro = :idCentro);
        			""";

            Query countQuery = entityManager.createNativeQuery(countSql)
                    .setParameter("curso", curso)
                    .setParameter("profesor", profesor)
                    .setParameter("idCentro", idCentro);
            long total = ((Number) countQuery.getSingleResult()).longValue();

            String sql = """
            		SELECT t.id, p.nombre, p.apellidos, c.curso, t.anio
            		FROM tutores t
            		JOIN profesores p ON t.id_profesor = p.id
            		JOIN cursos c ON t.id_curso = c.id
            		WHERE (:curso IS NULL OR :curso = 0 OR c.id = :curso)
            		  AND (:profesor IS NULL OR :profesor = 0 OR p.id = :profesor)
            		  AND (:idCentro IS NULL OR :idCentro = 0 OR t.id_centro = :idCentro);
            		""";

            Query query = entityManager.createNativeQuery(sql)
                    .setParameter("curso", curso)
                    .setParameter("profesor", profesor)
                    .setParameter("idCentro", idCentro)
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
}