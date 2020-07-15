package io.github.matheuscarv69.domain.entity;

import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.util.Set;

//@Data
@AllArgsConstructor
//@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    public Usuario(String nome) {
        this.nome = nome;
    }

    public Usuario(String matricula, String nome, boolean admin, boolean tec) {
        this.matricula = matricula;
        this.nome = nome;
        this.admin = admin;
        this.tec = tec;
    }

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

    @OneToMany(mappedBy = "requerente", fetch = FetchType.LAZY)
    private Set<Chamado> chamadosReq;

    @OneToMany(mappedBy = "tecnico", fetch = FetchType.LAZY)
    private Set<Chamado> chamadosTec;

    public Usuario() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isTec() {
        return tec;
    }

    public void setTec(boolean tec) {
        this.tec = tec;
    }

    public Set<Chamado> getChamadosReq() {
        return chamadosReq;
    }

    public void setChamadosReq(Set<Chamado> chamadosReq) {
        this.chamadosReq = chamadosReq;
    }

    public Set<Chamado> getChamadosTec() {
        return chamadosTec;
    }

    public void setChamadosTec(Set<Chamado> chamadosTec) {
        this.chamadosTec = chamadosTec;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
