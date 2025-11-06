package com.example.planaula.services;

import com.example.planaula.Dto.EspacioDTO;

import com.example.planaula.Dto.HorarioDTO;
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
public class EspaciosService {

    @Autowired
    private EntityManager entityManager;

    public Page<EspacioDTO> findAllEspaciosByDiaAndHoraAndAulaAndAsignatura(Integer dia, Integer hora, Integer asignatura, Integer aula, Integer profesor, Integer curso, Pageable pageable, int idCentro) {
        try {
        	
        	  String countSql = "SELECT COUNT(*) FROM horarios h\n"
        	  		+ "JOIN cursos c ON h.id_curso = c.id\n"
        	  		+ "JOIN profesores p ON h.id_profesor = p.id\n"
        	  		+ "JOIN asignaturas asig ON h.id_asignatura = asig.id\n"
        	  		+ "JOIN aulas a ON h.id_aula = a.id\n"
        	  		+ "JOIN dias d ON h.id_dia = d.id\n"
        	  		+ "JOIN horas ho ON h.id_hora = ho.id\n"
        	  		+ "WHERE (:dia IS NULL OR :dia = 0 OR d.id = :dia)\n"
        	  		+ "AND (:hora IS NULL OR :hora = 0 OR ho.id = :hora)\n"
        	  		+ "AND (:aula IS NULL OR :aula = 0 OR a.id = :aula)\n"
        	  		+ "AND (:profesor IS NULL OR :profesor = 0 OR p.id = :profesor)\n"
        	  		+ "AND (:curso IS NULL OR :curso = 0 OR c.id = :curso)\n"
        	  		+ "AND (:asignatura IS NULL OR :asignatura = 0 OR asig.id = :asignatura)"
        	  		+ "AND (\r\n"
        	  		+ "           h.id_centro = :idCentro\r\n"
        	  		+ "       AND c.id_centro = :idCentro\r\n"
        	  		+ "       AND p.id_centro = :idCentro\r\n"
        	  		+ "       AND asig.id_centro = :idCentro\r\n"
        	  		+ "       AND a.id_centro = :idCentro\r\n"
        	  		+ "       AND d.id_centro = :idCentro\r\n"
        	  		+ "       AND ho.id_centro = :idCentro\r\n"
        	  		+ "      )";
              Query countQuery = entityManager.createNativeQuery(countSql)
                      .setParameter("dia", dia)
                      .setParameter("hora", hora)
                      .setParameter("aula", aula)
                      .setParameter("asignatura", asignatura)
                      .setParameter("profesor", profesor)
                      .setParameter("curso", curso)
              		  .setParameter("idCentro", idCentro);
              long total = ((Number) countQuery.getSingleResult()).longValue();
              
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
                    "AND (:profesor IS NULL OR :profesor = 0 OR p.id = :profesor)\n" +
                    "AND (:curso IS NULL OR :curso = 0 OR c.id = :curso)\n" +
                    "AND (:asignatura IS NULL OR :asignatura = 0 OR asig.id = :asignatura) "
                    + "AND (\r\n"
                    + "           h.id_centro = :idCentro\r\n"
                    + "       AND c.id_centro = :idCentro\r\n"
                    + "       AND p.id_centro = :idCentro\r\n"
                    + "       AND asig.id_centro = :idCentro\r\n"
                    + "       AND a.id_centro = :idCentro\r\n"
                    + "       AND d.id_centro = :idCentro\r\n"
                    + "       AND ho.id_centro = :idCentro\r\n"
                    + "      ) "
                    + "order by d.id, ho.hora, c.curso\n";

            Query query = entityManager.createNativeQuery(sql)
                    .setParameter("dia", dia)
                    .setParameter("hora", hora)
                    .setParameter("aula", aula)
                    .setParameter("asignatura", asignatura)
                    .setParameter("profesor", profesor)
                    .setParameter("curso", curso)
                    .setParameter("idCentro", idCentro)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());

            List<Object[]> resultados = query.getResultList();

            List<EspacioDTO> espacios = resultados.stream()
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

            return new PageImpl<>(espacios, pageable, total);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
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

    @Transactional
    public void anadirHorario(HorarioDTO horarioDTO) {

    }

}
