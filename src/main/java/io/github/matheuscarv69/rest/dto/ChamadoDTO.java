package io.github.matheuscarv69.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChamadoDTO {

    @NotNull(message = "{campo.id-usuario.obrigatorio}")
    private Integer requerente;

    @NotEmpty(message = "{campo.titulo.obrigatorio}")
    private String titulo;

    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    private String descricao;

    @NotEmpty(message = "{campo.tipo-chamado.obrigatorio}")
    private String tipo;

    @NotNull(message = "{campo.bloco-chamado.obrigatorio}")
    private Character bloco;

    @NotNull(message = "{campo.sala-chamado.obrigatorio}")
    private Integer sala;

}
