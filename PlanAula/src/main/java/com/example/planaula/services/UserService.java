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
        INSERT INTO usuarios (username, password, role)
        VALUES (:username, :password, :role)
    """;

            entityManager.createNativeQuery(sql)
                    .setParameter("username", usuarioDTO.getUsername())
                    .setParameter("password", passwordEncriptada)
                    .setParameter("role", "admin")
                    .executeUpdate();
        }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String sql = """
            SELECT id, username, password, role, id_profesor, id_alumno
            FROM usuarios
            WHERE username = :username
        """;

        try {
            Object[] result = (Object[]) entityManager.createNativeQuery(sql)
                    .setParameter("username", username)
                    .getSingleResult();

            Integer id = (Integer) result[0];
            String nombre = (String) result[1];
            String pass = (String) result[2];
            String rol = (String) result[3];

            return User.builder()
                    .username(nombre)
                    .password(pass)
                    .roles(rol)
                    .build();

        } catch (NoResultException e) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}