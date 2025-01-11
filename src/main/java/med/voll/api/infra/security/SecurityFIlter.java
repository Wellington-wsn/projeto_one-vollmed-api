package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Security;

// Classse de configiração de componente (generico)
@Component
// erdando classe do spring, que implementa a interface filter
public class SecurityFIlter extends OncePerRequestFilter {

    @Autowired
    // injetar classe
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // receber cabeçalho com o token da requisição
        var tokenJWT = recuperarToken(request);

        if(tokenJWT != null) {
            //validar token, chamando metodo da classe injetada TokenService e passando o token pelo paramêtro
            var subject = tokenService.getSubject(tokenJWT); // recebe na variavel o subject(login) da requisição

            // autenticação forçada para o spring(confirmando que a pessoa esta logada)
            var usuario = repository.findAllByLogin(subject); // recebe o usuario do token e busca no DB

            // autenticaçõa do usuario
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()); // classe DTO, representa o objeto para autenticacão
            SecurityContextHolder.getContext().setAuthentication(authentication); // classe de autenticação usuario, valida usuario logado
        }

        // Chamando o proximo filtro de aplicação, sem essa linha a execução da aplicação não continua
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        // pegar o cabeçalho da requisição
        var authorizationHeader = request.getHeader("Authorization"); // se não existir cabeçalho retorna null
        if (authorizationHeader != null){
            return authorizationHeader.replace("Bearer ", ""); // não mostrar(substituir) o prefixo "Bearer"
        }

        return null;
    }
}
