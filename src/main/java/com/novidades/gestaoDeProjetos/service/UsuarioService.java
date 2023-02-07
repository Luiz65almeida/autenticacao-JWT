package com.novidades.gestaoDeProjetos.service;

import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.novidades.gestaoDeProjetos.model.Usuario;
import com.novidades.gestaoDeProjetos.repository.UsuarioRepository;
import com.novidades.gestaoDeProjetos.security.JWTservice;
import com.novidades.gestaoDeProjetos.view.model.usuario.LoginResponse;

public class UsuarioService {

    private static final String hederPefix = "Bearer ";

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JWTservice jwTservice;

    @Autowired
    private AuthenticationManager authenticationManager;

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

    public LoginResponse logar(String email, String senha) {

        // Aqui ocorre a autenticação.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, senha, Collections.emptyList()));

            // Aqui a nova autenticação é cuidada pelo Spring Security .
        SecurityContextHolder.getContext().setAuthentication(authentication);

         // Geroa o  token do usuario.
         // Bearer acf12ghb3jhujh.asdfresdtuopi36jklo541.ascfhjvvcv
        String token = hederPefix + jwTservice.gerarToken(authentication);

        Usuario usuario = usuarioRepository.findByEmail(email).get();

        return new LoginResponse(token, usuario);
    }

}
