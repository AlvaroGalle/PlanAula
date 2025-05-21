package com.example.planaula.services;

import com.example.planaula.Dto.CursoDTO;
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
public class HorasService {

    @Autowired
    private EntityManager entityManager;

    public List<HoraDTO> findAllHoras() {
        String sql = "SELECT * FROM horas";

        List<Object[]> resultados = entityManager.createNativeQuery(sql).getResultList();

        if (resultados == null || resultados.isEmpty()) {
            return Collections.emptyList();
        }

        return resultados.stream()
                .map(resultado -> new HoraDTO(
                        ((Number) resultado[0]).intValue(),
                        (resultado[1].toString())
                ))
                .collect(Collectors.toList());
    }


}
