package com.novidades.gestaoDeProjetos.service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.novidades.gestaoDeProjetos.model.Usuario;
import com.novidades.gestaoDeProjetos.repository.UsuarioRepository;

public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<Usuario> obterTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obterPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obterPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario adicionar(Usuario usuario) {
        usuario.setId(null);

        if (obterPorEmail(usuario.getEmail()).isPresent()) {

            throw new InputMismatchException("Já existe um usuario cadastrado com o e-mail: " + usuario.getEmail());

        }

        // Aqui estou codificando a senha para não ficar publica, gerando um hash
        String senha = passwordEncoder.encode(usuario.getSenha());

        usuario.setSenha(senha);

        return usuarioRepository.save(usuario);

    }

}
