package io.github.matheuscarv69.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FiltroChamadoDTO {

    private String nomeRequerente;
    private String matriculaRequerente;
    private String nomeTecnico;
    private String matriculaTecn;
    private String dataInicial;
    private String dataSolucao;
    private String status;
    private String tipo;

}
