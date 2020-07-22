package io.github.matheuscarv69.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChamadoDTO {

    private Integer requerente;
    private String titulo;
    private String descricao;
    private String tipo;
    private Character bloco;
    private Integer sala;

}
