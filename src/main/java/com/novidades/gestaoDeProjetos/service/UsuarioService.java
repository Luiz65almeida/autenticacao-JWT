package com.novidades.gestaoDeProjetos.service;


import java.util.List;
import java.util.Optional;


import com.novidades.gestaoDeProjetos.model.Usuario;
import com.novidades.gestaoDeProjetos.repository.UsuarioRepository;


public class UsuarioService {

    UsuarioRepository usuarioRepository;

    public List <Usuario> obterTodos(){
        return usuarioRepository.findAll();
    }

    public Optional <Usuario> obterPorId (Long id){
        return usuarioRepository.findById(id);
    }

    public Optional <Usuario> obterPorEmail (String email){
        return usuarioRepository.findByEmail(email);
    }

    public Usuario adicionar (Usuario usuario){
        usuario.setId(null);

        if (obterPorEmail(usuario.getEmail()).isPresent()){

            throw new inputmismatchException("Já existe um usuario cadastrado com o e-mail " + usuario.getEmail());

        }
    }
    
}
