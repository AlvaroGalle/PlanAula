package com.example.planaula.services;

import com.example.planaula.dto.CentroDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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
            SELECT c.id, c.nombre, c.descripcion, uc.favorito
            FROM centros c
            INNER JOIN usuarios_centros uc ON uc.id_centro = c.id
            WHERE uc.id_usuario = :idUsuario ORDER BY uc.favorito desc
        """;

        List<Object[]> resultados = entityManager.createNativeQuery(sql)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
        List<CentroDTO> centros = new ArrayList<>();

        for (Object[] fila : resultados) {
        	Integer id = ((Number) fila[0]).intValue();
            String nombre = (String) fila[1];
            String descripcion = (String) fila[2];
            boolean favorito = (boolean) fila[3];
            centros.add(new CentroDTO(id, nombre, descripcion, favorito));
        }

        return centros;
    }

    @Transactional
    public CentroDTO obtenerCentroPorId(Integer idCentro) {
        String sql = """
            SELECT c.id, c.nombre, c.descripcion, u.favorito
            FROM centros c and usuarios_centros u 
            WHERE c.id = :idCentro and u.id_centro = :idCentro
        """;

        try {
            Object[] fila = (Object[]) entityManager.createNativeQuery(sql)
                    .setParameter("idCentro", idCentro)
                    .getSingleResult();

            Integer id = ((Number) fila[0]).intValue();
            String nombre = (String) fila[1];
            String descripcion = (String) fila[2];

            return new CentroDTO(id, nombre, descripcion, false);
        } catch (NoResultException e) {
            return null;
        }
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

    @Transactional
    public Integer crearCentro(int idUsuario, CentroDTO centroDTO, byte[] logoBytes) {
        try {
            String insertCentroSql = "INSERT INTO centros (nombre, descripcion, logo) " +
                                    "VALUES (:nombre, :descripcion, :logo) " +
                                    "RETURNING id";

            Integer idCentroGenerado = ((Number) entityManager.createNativeQuery(insertCentroSql)
                    .setParameter("nombre", centroDTO.getNombre())
                    .setParameter("descripcion", centroDTO.getDescripcion())
                    .setParameter("logo", logoBytes)
                    .getSingleResult()).intValue();

            String insertRelacionSql = "INSERT INTO usuarios_centros (id_usuario, id_centro, favorito) " +
                                      "VALUES (:idUsuario, :idCentro, :favorito)";

            entityManager.createNativeQuery(insertRelacionSql)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("idCentro", idCentroGenerado)
                    .setParameter("favorito", centroDTO.getFavorito())
                    .executeUpdate();

            return idCentroGenerado;

        } catch (Exception e) {
            System.out.println("Error al crear centro: " + e.getMessage());
            throw new RuntimeException("Error al crear centro", e);
        }
    }

    @Transactional
    public void editarCentro(CentroDTO centroDTO, byte[] logoBytes) {
        try {
            StringBuilder sql = new StringBuilder("""
                UPDATE centros
                SET nombre = :nombre,
                    descripcion = :descripcion
            """);
            if (logoBytes != null && logoBytes.length > 0) {
                sql.append(", logo = :logo");
            }

            sql.append(" WHERE id = :idCentro");

            var query = entityManager.createNativeQuery(sql.toString())
                    .setParameter("nombre", centroDTO.getNombre())
                    .setParameter("descripcion", centroDTO.getDescripcion())
                    .setParameter("idCentro", centroDTO.getId());

            if (logoBytes != null && logoBytes.length > 0) {
                query.setParameter("logo", logoBytes);
            }

            query.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al actualizar centro: " + e.getMessage());
            throw new RuntimeException("Error al actualizar centro", e);
        }
    }

    @Transactional
    public void deleteCentro(Integer idCentro) {
        try {
            entityManager.createNativeQuery("""
                DELETE FROM usuarios_centros
                WHERE id_centro = :idCentro
            """)
            .setParameter("idCentro", idCentro)
            .executeUpdate();

            entityManager.createNativeQuery("""
                DELETE FROM centros
                WHERE id = :idCentro
            """)
            .setParameter("idCentro", idCentro)
            .executeUpdate();

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar centro: " + e.getMessage());
            throw new RuntimeException("Error al eliminar centro", e);
        }
    }


    public byte[] obtenerLogoPorId(Integer idCentro) {
        String sql = "SELECT logo FROM centros WHERE id = :idCentro";
        List<byte[]> resultado = entityManager.createNativeQuery(sql)
                .setParameter("idCentro", idCentro)
                .getResultList();

        return resultado.isEmpty() ? null : resultado.get(0);
    }

}
