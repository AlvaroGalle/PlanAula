package com.example.planaula.services;

import com.example.planaula.Dto.AulaDTO;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AulasService {

    @Autowired
    private EntityManager entityManager;

    public List<AulaDTO> findAllAulas() {
        String sql = "SELECT * FROM aulas";

        List<Object[]> resultados = entityManager.createNativeQuery(sql).getResultList();

        if (resultados == null || resultados.isEmpty()) {
            return Collections.emptyList();
        }

        return resultados.stream()
                .map(resultado -> new AulaDTO(
                        ((Number) resultado[0]).intValue(),
                        (String) resultado[1]
                ))
                .collect(Collectors.toList());
    }

    public AulaDTO findAulaByid(int id) {
        String sql = "SELECT * FROM aulas WHERE id = :id";

        List<Object[]> resultados = entityManager.createNativeQuery(sql)
                .setParameter("id", id)
                .getResultList();

        if (resultados.isEmpty()) {
            return null;
        }

        Object[] resultado = resultados.get(0);
        return new AulaDTO(
                ((Number) resultado[0]).intValue(),
                (String) resultado[1]
        );
    }
}
