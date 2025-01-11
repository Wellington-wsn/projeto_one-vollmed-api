package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.net.Authenticator;

@Configuration
@EnableWebSecurity
// Configuração do web Security do Spring boot, para modo stateless (não guarda informação do usuario logado)
public class SecurityConfigurations {

    @Autowired
    private SecurityFIlter securityFIlter;

    @Bean // retorna objeto gerenciado pelo spring
    // metodo de objeto do spring - configurar autenticações
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF(Cross-Site Request Forgery) - proteçõa do spring contra ataque CSRF
        return http.csrf().disable()
                // gerenciamento da sessão, politica para stateless (libera as requisições)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll() // liberando o endpoint(URL) pra requisição publica
                .antMatchers(HttpMethod.DELETE, "/medicos").hasRole("ADMIN") // permissão para usuario DMIN
                .antMatchers(HttpMethod.DELETE, "/pacientes").hasRole("ADMIN")
                .antMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**", "/webjars/**" ).permitAll()// libera os endpoint do swagger pra acesso publico
                .anyRequest().authenticated() // qual quer outra requisição tem que estar autenticada
                .and().addFilterBefore(securityFIlter, UsernamePasswordAuthenticationFilter.class) // definir a ordem dos filter na execução (filter personalizado, filter do spring)
                .build();
    }

    @Bean
    // Configurar a criaçõa do objeto para injecao de dependencias para o metodo AutheticationMenager
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    // configuração tipo de senha utilizar na aplicação - BCrypt
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
