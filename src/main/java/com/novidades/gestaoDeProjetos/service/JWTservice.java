package com.novidades.gestaoDeProjetos.service;

import java.util.Date;

import org.springframework.security.core.Authentication;

import com.novidades.gestaoDeProjetos.model.Usuario;



public class JWTservice {

    private static final String chavePrivadaJWT = "secretKey";

public String gerarToken (Authentication authentication ){
    
    //1 dia em milisegundos
    //Aqui pode variar de acordo com a regra de negocio 
    int tempoExpiracao = 86400000;


    //Criando uma data de expiração para o token com base no tempode expiração
    //Pega a data Atual e soma mais 1 dia em milisegundos
    Date dataExpiracao = new Date (new Date().getTime() + tempoExpiracao); 

    Usuario usuario = (Usuario) authentication.getPrincipal();
    
    return Jwts
}

}
