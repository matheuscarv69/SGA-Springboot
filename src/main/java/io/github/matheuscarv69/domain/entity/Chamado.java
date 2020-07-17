package io.github.matheuscarv69.domain.entity;

import io.github.matheuscarv69.domain.enums.StatusChamado;
import io.github.matheuscarv69.domain.enums.TipoChamado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

//@Data
@AllArgsConstructor
//@NoArgsConstructor
@Entity
@Table(name = "chamado")
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "titulo", length = 40)
    private String titulo;

    @Column
    private String descricao;

    @Column
    private TipoChamado tipo;

    @Column
    private LocalDate dataInicio;

    @Column
    private LocalDate dataFinal;

    @Column
    private Character bloco;

    @Column
    private Integer sala;

    @Column
    private StatusChamado status;

    @ManyToOne
    @JoinColumn(name = "requerente_id")
    private Usuario requerente;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Usuario tecnico;

    public Chamado() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoChamado getTipo() {
        return tipo;
    }

    public void setTipo(TipoChamado tipo) {
        this.tipo = tipo;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Character getBloco() {
        return bloco;
    }

    public void setBloco(Character bloco) {
        this.bloco = bloco;
    }

    public Integer getSala() {
        return sala;
    }

    public void setSala(Integer sala) {
        this.sala = sala;
    }

    public StatusChamado getStatus() {
        return status;
    }

    public void setStatus(StatusChamado status) {
        this.status = status;
    }

    public Usuario getRequerente() {
        return requerente;
    }

    public void setRequerente(Usuario requerente) {
        this.requerente = requerente;
    }

    public Usuario getTecnico() {
        return tecnico;
    }

    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    }

    @Override
    public String toString() {
        return "Chamado{" +
                "id=" + id +
                ", titulo=" + titulo +
                ", requerente=" + requerente.getId() +
                ", tecnico=" + tecnico.getId() +
                '}';
    }
}
