package br.com.meuportfolio.model;

import java.time.LocalDate;

/**
 * Representa um item de mídia (foto, vídeo, etc.) no portfólio.
 * A mídia pode pertencer a um Aluno (portfólio individual) OU
 * a uma Turma (portfólio geral da turma).
 */
public class Midia {

    public enum TipoMidia {
        FOTO,
        VIDEO,
        AUDIO
    }

    private int id;
    private String descricao;
    private String caminhoArquivo; // Caminho para o arquivo no sistema
    private LocalDate dataEnvio;
    private TipoMidia tipoMidia;

    // Apenas um dos dois abaixo será preenchido.
    private Aluno aluno;  // Se pertencer a um aluno
    private Turma turma;  // Se pertencer a uma turma

    // Construtor padrão
    public Midia() {
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public LocalDate getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDate dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public TipoMidia getTipoMidia() {
        return tipoMidia;
    }

    public void setTipoMidia(TipoMidia tipoMidia) {
        this.tipoMidia = tipoMidia;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }
    
    @Override
    public String toString() {
        String proprietario;
        if (aluno != null) {
            proprietario = "aluno=" + aluno.getNome();
        } else if (turma != null) {
            proprietario = "turma=" + turma.getNome();
        } else {
            proprietario = "sem proprietário";
        }

        return "Midia{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", " + proprietario +
                '}';
    }
}