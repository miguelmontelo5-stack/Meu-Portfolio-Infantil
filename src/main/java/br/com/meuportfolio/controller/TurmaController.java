// Arquivo: TurmaController.java (Versão FINAL e COMPLETA)
package br.com.meuportfolio.controller;

import br.com.meuportfolio.dao.TurmaDAO;
import br.com.meuportfolio.dao.TurmaDAOImpl;
import br.com.meuportfolio.model.Turma;
import java.util.List;

public class TurmaController {

    private TurmaDAO turmaDAO;

    public TurmaController() {
        this.turmaDAO = new TurmaDAOImpl();
    }

    public List<Turma> listarTodas() throws Exception {
        return turmaDAO.listarTodas();
    }
    
    public void salvar(Turma turma) throws Exception {
        if (turma.getNome() == null || turma.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da turma não pode ser vazio.");
        }
        if (turma.getAnoLetivo() <= 0) {
            throw new IllegalArgumentException("O ano letivo deve ser um valor válido.");
        }
        turmaDAO.salvar(turma);
    }
    
    public void excluir(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido para exclusão.");
        }
        turmaDAO.excluir(id);
    }

    public void atualizar(Turma turma) throws Exception {
         if (turma == null || turma.getId() <= 0) {
            throw new IllegalArgumentException("Turma inválida para atualização.");
        }
        if (turma.getNome() == null || turma.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da turma não pode ser vazio.");
        }
        if (turma.getAnoLetivo() <= 0) {
            throw new IllegalArgumentException("O ano letivo deve ser um valor válido.");
        }
        turmaDAO.atualizar(turma);
    }
}