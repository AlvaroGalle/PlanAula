package com.example.planaula.services;

import com.example.planaula.Dto.EspacioDTO;
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
public class EspaciosService {

    @Autowired
    private EntityManager entityManager;

    public List<EspacioDTO> findAllEspaciosByDiaAndHoraAndAulaAndAsignatura(Integer dia, Integer hora, Integer asignatura, Integer aula) {
        String sql = "SELECT h.id, d.dia, ho.hora, c.curso, asig.asignatura, p.nombre, a.aula, h.observaciones\n" +
                "FROM horarios h\n" +
                "JOIN cursos c ON h.id_curso = c.id\n" +
                "JOIN profesores p ON h.id_profesor = p.id\n" +
                "JOIN asignaturas asig ON h.id_asignatura = asig.id\n" +
                "JOIN aulas a ON h.id_aula = a.id\n" +
                "JOIN dias d ON h.id_dia = d.id\n" +
                "JOIN horas ho ON h.id_hora = ho.id\n" +
                "WHERE (:dia IS NULL OR :dia = 0 OR d.id = :dia)\n" +
                "AND (:hora IS NULL OR :hora = 0 OR ho.id = :hora)\n" +
                "AND (:aula IS NULL OR :aula = 0 OR a.id = :aula)\n" +
                "AND (:asignatura IS NULL OR :asignatura = 0 OR asig.id = :asignatura)\n";

        List<Object[]> resultados = entityManager.createNativeQuery(sql)
                .setParameter("dia", dia)
                .setParameter("hora", hora)
                .setParameter("aula", aula)
                .setParameter("asignatura", asignatura)
                .getResultList();

        if (resultados == null || resultados.isEmpty()) {
            return Collections.emptyList();
        }

        return resultados.stream()
                .map(resultado -> new EspacioDTO(
                        ((Number) resultado[0]).intValue(),
                        (String) resultado[1],
                        (String) resultado[2],
                        (String) resultado[3],
                        (String) resultado[4],
                        (String) resultado[5],
                        (String) resultado[6],
                        (String) resultado[7]
                )).collect(Collectors.toList());
    }


    public EspacioDTO findEspacioById(Integer id) {
        String sql = "SELECT h.id, d.dia, ho.hora, c.curso, asig.asignatura, p.nombre, a.aula, h.observaciones\n" +
                "FROM horarios h\n" +
                "JOIN cursos c ON h.id_curso = c.id\n" +
                "JOIN profesores p ON h.id_profesor = p.id\n" +
                "JOIN asignaturas asig ON h.id_asignatura = asig.id\n" +
                "JOIN aulas a ON h.id_aula = a.id\n" +
                "JOIN dias d ON h.id_dia = d.id\n" +
                "JOIN horas ho ON h.id_hora = ho.id\n" +
                "WHERE h.id = :id";

        Object[] resultado = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("id", id)
                .getSingleResult();

        return new EspacioDTO(((Number) resultado[0]).intValue(),
                (String) resultado[1],
                (String) resultado[2],
                (String) resultado[3],
                (String) resultado[4],
                (String) resultado[5],
                (String) resultado[6],
                (String) resultado[7]);
    }

}
