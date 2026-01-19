package br.com.meuportfolio.dao;

import br.com.meuportfolio.model.Aluno;
import java.util.List;

public interface AlunoDAO {
    void salvar(Aluno aluno) throws Exception;
    void atualizar(Aluno aluno) throws Exception;
    void excluir(int id) throws Exception;
    Aluno buscarPorId(int id) throws Exception;
    List<Aluno> listarTodos() throws Exception;
    List<Aluno> listarPorResponsavel(int responsavelId) throws Exception; // LINHA ADICIONADA AQUI
}