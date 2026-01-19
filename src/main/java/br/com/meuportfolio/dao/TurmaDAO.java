package br.com.meuportfolio.dao;

import br.com.meuportfolio.model.Turma;
import java.util.List;

/**
 * Interface que define o contrato para as operações de persistência da entidade Turma.
 */
public interface TurmaDAO {

    /**
     * Salva uma nova turma no banco de dados.
     * @param turma O objeto Turma a ser salvo.
     */
    void salvar(Turma turma) throws Exception;

    /**
     * Atualiza os dados de uma turma existente.
     * @param turma O objeto Turma com os dados atualizados.
     */
    void atualizar(Turma turma) throws Exception;

    /**
     * Exclui uma turma do banco de dados pelo seu ID.
     * @param id O ID da turma a ser excluída.
     */
    void excluir(int id) throws Exception;

    /**
     * Busca uma turma pelo seu ID.
     * @param id O ID da turma.
     * @return O objeto Turma encontrado, ou null se não existir.
     */
    Turma buscarPorId(int id) throws Exception;

    /**
     * Retorna uma lista com todas as turmas cadastradas.
     * @return Uma lista de objetos Turma.
     */
    List<Turma> listarTodas() throws Exception;
}