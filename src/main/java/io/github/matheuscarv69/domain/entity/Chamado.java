package io.github.matheuscarv69.domain.entity;

import io.github.matheuscarv69.domain.enums.StatusChamado;
import io.github.matheuscarv69.domain.enums.TipoChamado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chamado")
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "titulo", length = 30)
    private String titulo;

    @Column
    private String descricao;

    @Column
    private TipoChamado tipo;

    @Column
    private Date dataInico;

    @Column
    private Date dataFinal;

    @Column
    private Character bloco;

    @Column
    private Integer sala;

    @Column
    private StatusChamado status;

    @OneToOne
    @JoinColumn(name = "requerente_id")
    private Usuario requerente;

    @OneToOne
    @JoinColumn(name = "tecnico_id")
    private Usuario tecnico;
}
