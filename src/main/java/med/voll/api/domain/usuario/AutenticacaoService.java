package med.voll.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
// interface de serviço de autenticação do Spring boot
public class AutenticacaoService implements UserDetailsService {

    // intanciar o objeto para injeção de dependencia
    @Autowired
    private UsuarioRepository repository;

    // metodo da interface, apos o loging metodo de avaliar o usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findAllByLogin(username) ;
    }
}
