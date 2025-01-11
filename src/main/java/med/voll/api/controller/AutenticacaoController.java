package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.usuario.DadosAtualizacao;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.infra.security.DadosTokenJWT;
import med.voll.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login") // endpoint
public class AutenticacaoController {

    // intansiando o metodo autetication menager
    @Autowired // instanciar o objeto, para o string injetar os parametro
    private AuthenticationManager menager;

    // instanciando a classe que gera o token
    @Autowired
    private TokenService tokenService;

    // Ações (métodos) HTTP
    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAtualizacao dados) {
        // Gerando o token (pelo DTO do spring)
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        // chamando o metodo de autenticacao
        var authentication = menager.authenticate(authenticationToken);

        // retornar o token gerado padrão JWT, utilizando a biblioteca auth0, retornando como objeto token
        //return ResponseEntity.ok(tokenService.gerarToken((Usuario) authentication.getPrincipal()));

        //retornando por um DTO, encapsulando o token
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }

}
