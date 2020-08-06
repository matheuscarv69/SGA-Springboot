package io.github.matheuscarv69.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

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
    @NotEmpty(message = "Campo Nome é obrigatório")
    private String nome;

    @Column(name = "matricula", length = 13)
    @Size(min = 12, max = 12, message = "A Matrícula deve ter 12 dígitos")
    private String matricula;

    @Column(name = "email", length = 50)
    @NotEmpty(message = "Campo Email é obrigatório")
    @Email(message = "Informe um Email válido")
    private String email;

    @Column(name = "phone", length = 15)
    @NotEmpty(message = "Campo telefone é obrigatório")
    private String phone;

    @Column
    @NotEmpty(message = "Campo login é obrigatório")
    private String login;

    @Column
    @NotEmpty(message = "Campo senha é obrigatório")
    private String senha;

    @Column
    private boolean ativo = true;

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
