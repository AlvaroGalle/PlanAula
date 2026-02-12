package com.example.planaula.services;

import com.example.planaula.dto.UsuarioDTO;
import com.example.planaula.security.CustomUserDetails;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private EntityManager entityManager;

    private final PasswordEncoder passwordEncoder;
    
    private final CentrosService centrosService;

    public UserService(PasswordEncoder passwordEncoder, CentrosService centrosService) {
        this.passwordEncoder = passwordEncoder;
        this.centrosService = centrosService;
    }

    @Transactional
    public void createUser(UsuarioDTO usuarioDTO) {
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
        String sqlUsuario = """
            SELECT id, username, password, role
            FROM usuarios
            WHERE username = :username
        """;

        try {
            Object[] result = (Object[]) entityManager.createNativeQuery(sqlUsuario)
                    .setParameter("username", username)
                    .getSingleResult();

            Integer id = (Integer) result[0];
            String nombre = (String) result[1];
            String pass = (String) result[2];
            String rol = (String) result[3];

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()));

            return new CustomUserDetails(id, nombre, pass, authorities);

        } catch (NoResultException e) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}
