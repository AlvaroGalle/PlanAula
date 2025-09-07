package com.example.planaula.services;

import com.example.planaula.Dto.AsignaturaDTO;
import com.example.planaula.Dto.ProfesorDTO;
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
public class AsignaturasService {

    @Autowired
    private EntityManager entityManager;

    public List<AsignaturaDTO> findAllAsignaturas() {
        String sqlfindAllAsignaturas = "SELECT * FROM asignaturas order by asignatura asc";

        List<Object[]> resultados = entityManager.createNativeQuery(sqlfindAllAsignaturas).getResultList();

        if (resultados == null || resultados.isEmpty()) {
            return Collections.emptyList();
        }

        return resultados.stream()
                .map(resultado -> new AsignaturaDTO(
                        ((Number) resultado[0]).intValue(),
                        (String) resultado[1]
                ))
                .collect(Collectors.toList());
    }

    public AsignaturaDTO findAsignaturaById(Integer id) {
        String sql = "SELECT id, asignatura FROM asignaturas WHERE id = :id order by asignatura";
        Object[] resultado = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter("id", id)
                .getSingleResult();
        return new AsignaturaDTO(
                ((Number) resultado[0]).intValue(),
                (String) resultado[1]);
    }

    public Page<AsignaturaDTO> findPageAsignaturas(Pageable pageable) {
        try {
            String countSql = "SELECT COUNT(*) FROM asignaturas";
            Query countQuery = entityManager.createNativeQuery(countSql);
            long total = ((Number) countQuery.getSingleResult()).longValue();

            String sql = "SELECT * FROM asignaturas";
            Query query = entityManager.createNativeQuery(sql)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());

            List<Object[]> resultados = query.getResultList();

            List<AsignaturaDTO> asignaturas = resultados.stream()
                    .map(resultado -> new AsignaturaDTO(
                            ((Number) resultado[0]).intValue(),
                            (String) resultado[1]
                    ))
                    .collect(Collectors.toList());

            return new PageImpl<>(asignaturas, pageable, total);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
    }

    @Transactional
    public void addAsignatura(String nombre) {
    	String sql = """
    		    INSERT INTO asignaturas (asignatura)
    		    VALUES (:nombre)
    		""";

    		entityManager.createNativeQuery(sql)
    		        .setParameter("nombre", nombre)
    		        .executeUpdate();

     }

    @Transactional
    public void modifyAsignatura(AsignaturaDTO asignaturaDTO) {
        try {
            String updateSql = "UPDATE asignaturas SET asignatura = :nombre WHERE id = :id";
            int filasActualizadas = entityManager.createNativeQuery(updateSql)
                    .setParameter("id", asignaturaDTO.getId())
                    .setParameter("nombre", asignaturaDTO.getNombre())
                    .executeUpdate();
        } catch (Exception e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteAsignatura(int id) {
    	String sql = "DELETE FROM asignaturas WHERE id = :id";
    	
    		entityManager.createNativeQuery(sql)
    		        .setParameter("id", id)
    		        .executeUpdate();

     }
}
