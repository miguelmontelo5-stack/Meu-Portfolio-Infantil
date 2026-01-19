package br.com.meuportfolio.model;

/**
 * Representa um usuário genérico do sistema, que pode ser
 * um Administrador ou um Responsável.
 */
public class Usuario {

    // Enum para definir os tipos de usuário permitidos no sistema.
    public enum TipoUsuario {
        ADMINISTRADOR,
        RESPONSAVEL
    }

    private int id;
    private String nome;
    private String login; // Pode ser um username para admin ou CPF para responsável
    private String senha; // Em um sistema real, isso seria um hash da senha
    private TipoUsuario tipoUsuario;

    // Construtor padrão
    public Usuario() {
    }

    // Construtor completo
    public Usuario(int id, String nome, String login, String senha, TipoUsuario tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", login='" + login + '\'' +
                ", tipoUsuario=" + tipoUsuario +
                '}';
    }
}