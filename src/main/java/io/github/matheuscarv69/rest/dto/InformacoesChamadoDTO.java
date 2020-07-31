package io.github.matheuscarv69.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacoesChamadoDTO {

    private Integer id;
    private String requerente;
    private String matricula;
    private String titulo;
    private String descricao;
    private String tipo;
    private Character bloco;
    private Integer sala;
    private String status;
    private String dataInicio;
    private String dataSolucao;
    private String tecnico;
    private String matriculaTecn;
    private boolean ativo;

}
