package med.voll.api.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import med.voll.api.domain.ValidacaoException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    //metodo que vai tratar exceptions

    @ExceptionHandler(EntityNotFoundException.class) // qual quer exception(erro) desse tipo sera tratado pelo metodo
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();

    }

    // erro de busca validação
    @ExceptionHandler(ValidacaoException.class)
    // parametro recebe o objeto do erro na ex
    public ResponseEntity tratarErroRegraDeNegocio(ValidacaoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    // parametro recebe o objeto do erro na ex
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();

        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());

    }

    // DTO para representar o objeto do erro e apresentar informações personalizada

    private record DadosErroValidacao(String campo, String mensagem) {

        // construtor do DTO // recebe objeto do tipo FieldError
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage()); // Nome do campo e mensagem do erro
        }
    }
}
