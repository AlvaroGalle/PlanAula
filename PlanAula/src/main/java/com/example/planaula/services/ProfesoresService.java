package com.example.planaula.services;

import com.example.planaula.Dto.AsignaturaDTO;
import com.example.planaula.Dto.ProfesorDTO;
import com.example.planaula.Dto.TutorDTO;
import jakarta.persistence.EntityManager;
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
        String sql = "SELECT * FROM `tutores 2425`";

        List<Object[]> resultados = entityManager.createNativeQuery(sql).getResultList();

        if (resultados == null || resultados.isEmpty()) {
            return Collections.emptyList();
        }

        return resultados.stream()
                .map(resultado -> new TutorDTO(
                        ((Number) resultado[0]).intValue(),
                        ((Number) resultado[1]).intValue(),
                        ((Number) resultado[2]).intValue(),
                        ((Number) resultado[3]).intValue()))
                .collect(Collectors.toList());
    }
}

