package br.com.meuportfolio.model;

/**
 * Esta classe representa a entidade Aluno.
 */
public class Aluno {

    private int id;
    private String nome;
    private String matricula;
    private String caminhoFoto; // Caminho para o arquivo da foto do aluno
    private Turma turma;         // Associação com a classe Turma

    // Construtor padrão
    public Aluno() {
    }

    // Construtor completo
    public Aluno(int id, String nome, String matricula, String caminhoFoto, Turma turma) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.caminhoFoto = caminhoFoto;
        this.turma = turma;
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

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    @Override
    public String toString() {
        // O ?. é um operador ternário que evita erro se a turma for nula
        String nomeTurma = (turma != null) ? turma.getNome() : "Sem Turma";
        return "Aluno{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", matricula='" + matricula + '\'' +
                ", turma=" + nomeTurma +
                '}';
    }
}