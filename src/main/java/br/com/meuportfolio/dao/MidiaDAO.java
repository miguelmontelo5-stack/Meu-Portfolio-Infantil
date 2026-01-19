package br.com.meuportfolio.dao;

import br.com.meuportfolio.model.Midia;
import java.util.List;

/**
 * Interface que define o contrato para as operações de persistência da entidade Midia.
 */
public interface MidiaDAO {

    void salvar(Midia midia) throws Exception;

    void atualizar(Midia midia) throws Exception;

    void excluir(int id) throws Exception;

    Midia buscarPorId(int id) throws Exception;

    /**
     * Lista todas as mídias de um aluno específico.
     * @param alunoId O ID do aluno.
     * @return Uma lista de mídias.
     */
    List<Midia> listarPorAluno(int alunoId) throws Exception;

    /**
     * Lista todas as mídias de uma turma específica.
     * @param turmaId O ID da turma.
     * @return Uma lista de mídias.
     */
    List<Midia> listarPorTurma(int turmaId) throws Exception;
}