package io.github.matheuscarv69.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(name = "matricula", length = 12)
    private String matricula;

    @Column(name = "nome", length = 100)
    private String nome;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 11)
    private String phone;

    @Column
    private String login;

    @Column
    private String senha;

    @Column
    private boolean admin;

    @Column
    private boolean tec;


}
