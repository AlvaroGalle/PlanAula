package com.example.planaula.services;

import com.example.planaula.Dto.CentroDTO;
import com.example.planaula.Dto.ProfesorDTO;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CentrosService {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public List<CentroDTO> obtenerCentrosPorUsuario(Integer idUsuario) {
        String sql = """
            SELECT c.id, c.nombre, c.descripcion, c.logo, uc.favorito
            FROM centros c
            INNER JOIN usuarios_centros uc ON uc.id_centro = c.id
            WHERE uc.id_usuario = :idUsuario ORDER BY uc.favorito
        """;

        List<Object[]> resultados = entityManager.createNativeQuery(sql)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
        List<CentroDTO> centros = new ArrayList<>();

        for (Object[] fila : resultados) {
            Integer id = (Integer) fila[0];
            String nombre = (String) fila[1];
            String descripcion = (String) fila[2];
            byte[] logo = (byte[]) fila[3];
            boolean favorito = (boolean) fila[4];
            centros.add(new CentroDTO(id, nombre, descripcion, logo, favorito));
        }

        return centros;
    }
    
    @Transactional
    public void modificarCentrosFav(int idUsuario, int idCentro, boolean fav) {
        try {
            String updateSql = "UPDATE usuarios_centros SET favorito = :favorito WHERE id_centro = :idCentro AND id_usuario = :idUsuario";
            int filasActualizadas = entityManager.createNativeQuery(updateSql)
            		.setParameter("idUsuario", idUsuario)
                    .setParameter("idCentro", idCentro)
                    .setParameter("favorito", fav)
                    .executeUpdate();
        } catch (Exception e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }
    }
}
