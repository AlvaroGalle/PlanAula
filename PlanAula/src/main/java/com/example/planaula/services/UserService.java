package com.example.planaula.services;

import com.example.planaula.Dto.UsuarioDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private EntityManager entityManager;

    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(UsuarioDTO usuarioDTO){
        String passwordEncriptada = passwordEncoder.encode(usuarioDTO.getPassword());

            String sql = """
        INSERT INTO usuarios (username, password, rol, enabled, profesor_id)
        VALUES (:username, :password, :rol, :enabled, :profesorId)
    """;

            entityManager.createNativeQuery(sql)
                    .setParameter("username", usuarioDTO.getUsername())
                    .setParameter("password", passwordEncriptada)
                    .setParameter("rol", "ALUMNO")
                    .setParameter("enabled", true)
                    .setParameter("profesorId", null)
                    .executeUpdate();
        }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String sql = """
            SELECT username, password, rol, enabled
            FROM usuarios
            WHERE username = :username
        """;

        try {
            Object[] result = (Object[]) entityManager.createNativeQuery(sql)
                    .setParameter("username", username)
                    .getSingleResult();

            String nombre = (String) result[0];
            String pass = (String) result[1];
            String rol = (String) result[2];
            boolean enabled = (Boolean) result[3];

            return User.builder()
                    .username(nombre)
                    .password(pass)
                    .roles(rol) // Spring espera "ROLE_PROFESOR", etc.
                    .disabled(!enabled)
                    .build();

        } catch (NoResultException e) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}