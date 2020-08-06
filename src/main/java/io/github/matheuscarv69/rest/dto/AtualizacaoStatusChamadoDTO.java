package io.github.matheuscarv69.rest.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class AtualizacaoStatusChamadoDTO {

    @NotEmpty(message = "Informe o Status")
    private String novoStatus;

}
