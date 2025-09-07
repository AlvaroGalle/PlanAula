package com.example.planaula.services;

import com.example.planaula.Dto.DiaDTO;
import com.example.planaula.Dto.HoraDTO;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DiasService {

    @Autowired
    private EntityManager entityManager;

    public List<DiaDTO> findAllDias() {
        String sql = "SELECT * FROM dias order by id asc";

        List<Object[]> resultados = entityManager.createNativeQuery(sql).getResultList();

        if (resultados == null || resultados.isEmpty()) {
            return Collections.emptyList();
        }

        return resultados.stream()
                .map(resultado -> new DiaDTO(
                        ((Number) resultado[0]).intValue(),
                        (String) resultado[1]
                ))
                .collect(Collectors.toList());
    }
}
