package br.com.meuportfolio.controller;

import br.com.meuportfolio.dao.AlunoDAO;
import br.com.meuportfolio.dao.AlunoDAOImpl;
import br.com.meuportfolio.model.Aluno;
import java.util.List;

/**
 * Controller para as operações de negócio relacionadas a Alunos.
 */
public class AlunoController {

    private AlunoDAO alunoDAO;

    public AlunoController() {
        this.alunoDAO = new AlunoDAOImpl();
    }

    /**
     * Busca e retorna todos os alunos do banco de dados.
     * @return Uma lista de objetos Aluno.
     * @throws Exception Se ocorrer um erro na comunicação com o banco.
     */
    public List<Aluno> listarTodos() throws Exception {
        return alunoDAO.listarTodos();
    }

    /**
     * Salva um novo aluno, aplicando as regras de negócio.
     * @param aluno O objeto Aluno a ser salvo.
     * @throws Exception Se a validação falhar ou ocorrer um erro no banco.
     */
    public void salvar(Aluno aluno) throws Exception {
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do aluno não pode ser vazio.");
        }
        // Outras validações podem ser adicionadas aqui (ex: matrícula, etc.)
        
        alunoDAO.salvar(aluno);
    }

    /**
     * Atualiza um aluno existente.
     * @param aluno O objeto Aluno com os dados atualizados.
     * @throws Exception Se a validação falhar ou ocorrer um erro no banco.
     */
    public void atualizar(Aluno aluno) throws Exception {
        if (aluno == null || aluno.getId() <= 0) {
            throw new IllegalArgumentException("Aluno inválido para atualização.");
        }
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do aluno não pode ser vazio.");
        }
        
        alunoDAO.atualizar(aluno);
    }

    /**
     * Exclui um aluno pelo seu ID.
     * @param id O ID do aluno a ser excluído.
     * @throws Exception Se o ID for inválido ou ocorrer um erro no banco.
     */
    public void excluir(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido para exclusão.");
        }
        alunoDAO.excluir(id);
    }
}