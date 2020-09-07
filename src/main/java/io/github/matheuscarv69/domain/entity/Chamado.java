package io.github.matheuscarv69.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.matheuscarv69.domain.enums.StatusChamado;
import io.github.matheuscarv69.domain.enums.TipoChamado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chamado")
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "titulo", length = 30)
    private String titulo;

    @Column
    private String descricao;

    @Column
    private String solucao;

    @Enumerated(EnumType.STRING)
    private TipoChamado tipoChamado;

    @Column
    private LocalDate dataInicio;

    @Column
    private LocalDate dataFinal;

    @Column
    private Character bloco;

    @Column
    private Integer sala;

    @Enumerated(EnumType.STRING)
    private StatusChamado statusChamado;

    @JsonIgnore
    @Column
    private byte[] imagem;

    @Column
    private boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "requerente_id")
    private Usuario requerente;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Usuario tecnico;

}
