package com.example.planaula.services;

import com.example.planaula.Dto.AsignaturaDTO;
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
public class AsignaturasService {

    @Autowired
    private EntityManager entityManager;

    public List<AsignaturaDTO> findAllAsignaturas() {
        String sqlfindAllAsignaturas = "SELECT * FROM asignaturas";

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
}
