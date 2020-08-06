package io.github.matheuscarv69.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChamadoDTO {

    @NotNull(message = "Informe o ID do usuário.")
    private Integer requerente;

    @NotEmpty(message = "Informe o Título")
    private String titulo;

    @NotEmpty(message = "Informe a Descrição")
    private String descricao;

    @NotEmpty(message = "Informe o Tipo do Chamado")
    private String tipo;

    @NotNull(message = "Informe o Bloco")
    private Character bloco;

    @NotNull(message = "Informe a sala")
    private Integer sala;

}
