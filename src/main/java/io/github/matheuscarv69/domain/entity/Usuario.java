package io.github.matheuscarv69.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome", length = 100)
    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String nome;

    @Column(name = "matricula", length = 13)
    @Size(min = 12, max = 12, message = "{campo.matricula.obrigatorio}")
    private String matricula;

    @Column(name = "email", length = 50)
    @NotEmpty(message = "{campo.email.obrigatorio}")
    @Email(message = "{campo.email.invalido}")
    private String email;

    @Column(name = "phone", length = 15)
    @NotEmpty(message = "{campo.telefone.obrigatorio}")
    private String phone;

    @Column
    @NotEmpty(message = "{campo.login.obrigatorio}")
    private String login;

    @Column
    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String senha;

//    @JsonIgnore
//    @Column
//    private Byte[] imagem;

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
