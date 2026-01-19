package br.com.meuportfolio.model;

/**
 * Esta classe representa a entidade Turma.
 * É um DTO (Data Transfer Object) para carregar os dados da turma.
 */
public class Turma {

    private int id;
    private String nome;
    private int anoLetivo;

    // Construtor padrão (vazio)
    public Turma() {
    }

    // Construtor com todos os campos
    public Turma(int id, String nome, int anoLetivo) {
        this.id = id;
        this.nome = nome;
        this.anoLetivo = anoLetivo;
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

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    // Método toString() para facilitar a depuração e logs
    @Override
    public String toString() {
        return "Turma{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", anoLetivo=" + anoLetivo +
                '}';
    }
}