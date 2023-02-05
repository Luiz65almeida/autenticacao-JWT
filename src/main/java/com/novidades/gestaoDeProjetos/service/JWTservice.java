package com.novidades.gestaoDeProjetos.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.novidades.gestaoDeProjetos.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTservice {

    // Chave secreta utilixada pelo JWT para codificar e decodificar o token
    private static final String chavePrivadaJWT = "secretKey";

    public String gerarToken(Authentication authentication) {

        // 1 dia em milisegundos
        // Aqui pode variar de acordo com a regra de negocio
        int tempoExpiracao = 86400000;

        // Criando uma data de expiração para o token com base no tempode expiração
        // Pega a data Atual e soma mais 1 dia em milisegundos
        Date dataExpiracao = new Date(new Date().getTime() + tempoExpiracao);

        // Pega o usuário atual da autenticação
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // pega todos os dados e retorna um token bonito do JWT
        return Jwts.builder()
                .setSubject(usuario.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS512, chavePrivadaJWT)
                .compact();
    }

    public Optional<Long> obterIdDoUsuario(String token) {
       
        try {
            //Retorna as permissões do token
            Claims claims = parse(token).getBody();
            //Retorna o id de dentro do token se entrar, caso contrario retorna null 
            return Optional.ofNullable(Long.parseLong(claims.getSubject()));

        } catch (Exception e) {
            // Se não encontar nada, devolve um Optional null
            return Optional.empty();
        }
    }
    // Método que sabe descobrir de dentro do token com base na chave orivada qual as permissões de usuário 
    private Jws<Claims> parse(String token) {
        return Jwts.parser().setSigningKey(chavePrivadaJWT).parseClaimsJws(token);
    }

}
