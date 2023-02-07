package com.novidades.gestaoDeProjetos.security;

import java.io.IOException;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JWTservice jwTservice;

    @Autowired
    private CustomUSerDetailsService customUSerDetailsService;

    // Método principal onde toda a requisição bate antes de chegar ao endpoint
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Pego o token dentro da requisição
        String token = obterToken(request);

        // Pego o id do Usuário que está detro do token
        Optional<Long> id = jwTservice.obterIdDoUsuario(token);

        // Se não achou o id, é porque o usuario não mandou o token correto
        if (!id.isPresent()) {
            throw new InputMismatchException("Token Inválido");
        }
        // Pego o Usuário dono do token pelo seu id
        UserDetails usuario = customUSerDetailsService.obterUsuarioPorId(id.get());

        // Nesse ponto verificamos se o usuário está autenticado ou não
        // aqui também poderiamos validar permissões
        UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken(usuario, null,
                Collections.emptyList());

        // Mando a autenticação para a propria requisição
        autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Repasso a autenticação para o contexto security
        // A partir de agora o spring tomar conta de tudo
        SecurityContextHolder.getContext().setAuthentication(autenticacao);

    }

    private String obterToken(HttpServletRequest request) {

        String token = request.getHeader("Authorization");

        // Verifica se veio sem espaços em branco no token
        if (!StringUtils.hasText(token)) {
            return null;
        }

        return token.substring(7);
    }

}
