package com.example.planaula.services;

import com.example.planaula.Dto.AsignaturaDTO;
import com.example.planaula.Dto.ProfesorDTO;
import com.example.planaula.Dto.TutorDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
                        (String)resultado[1],
                        (String)resultado[2],
                        (String)resultado[3]))
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
                        (String)resultado[1],
                        (String)resultado[2],
                        (String)resultado[3]))
                .collect(Collectors.toList());
    }
}

