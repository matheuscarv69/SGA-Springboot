package io.github.matheuscarv69.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome", length = 100)
    private String nome;

    @Column(name = "matricula", length = 13)
    private String matricula;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column
    private String login;

    @Column
    private String senha;

    @Column
    private boolean admin;

    @Column
    private boolean tecn;

    @JsonIgnore
    @OneToMany(mappedBy = "requerente")
    private List<Chamado> chamadosReq;

    @JsonIgnore
    @OneToMany(mappedBy = "tecnico")
    private List<Chamado> chamadosTecn;

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
