package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

public interface ValidadorAgendamentoDeConsulta {
    // toas as validações são ingetadas por essa interface, utilizando a sssinatura (metodo - validar)
    void validar(DadosAgendamentoConsulta dados);
}
