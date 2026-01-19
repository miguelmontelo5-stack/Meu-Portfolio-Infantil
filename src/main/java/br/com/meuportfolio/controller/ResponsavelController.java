// Arquivo: ResponsavelController.java (Novo e Completo)
package br.com.meuportfolio.controller;

import br.com.meuportfolio.dao.AlunoDAO;
import br.com.meuportfolio.dao.AlunoDAOImpl;
import br.com.meuportfolio.dao.UsuarioDAO;
import br.com.meuportfolio.dao.UsuarioDAOImpl;
import br.com.meuportfolio.model.Aluno;
import br.com.meuportfolio.model.Usuario;
import java.util.List;
import java.util.stream.Collectors;

public class ResponsavelController {

    private UsuarioDAO usuarioDAO;
    private AlunoDAO alunoDAO;

    public ResponsavelController() {
        this.usuarioDAO = new UsuarioDAOImpl();
        this.alunoDAO = new AlunoDAOImpl();
    }

    public List<Usuario> listarTodosResponsaveis() throws Exception {
        return usuarioDAO.listarTodos().stream()
                .filter(u -> u.getTipoUsuario() == Usuario.TipoUsuario.RESPONSAVEL)
                .collect(Collectors.toList());
    }
    
    public List<Aluno> getAlunosDoResponsavel(int responsavelId) throws Exception {
        return alunoDAO.listarPorResponsavel(responsavelId);
    }

    public void salvar(Usuario responsavel, List<Aluno> alunosVinculados) throws Exception {
        if (responsavel.getNome() == null || responsavel.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do responsável não pode ser vazio.");
        }
        if (responsavel.getLogin() == null || responsavel.getLogin().trim().isEmpty()) {
            throw new IllegalArgumentException("O login (CPF) não pode ser vazio.");
        }
        
        responsavel.setTipoUsuario(Usuario.TipoUsuario.RESPONSAVEL);
        
        // Salva ou atualiza o usuário
        if (responsavel.getId() > 0) {
            usuarioDAO.atualizar(responsavel);
        } else {
            usuarioDAO.salvar(responsavel);
        }
        
        Usuario usuarioSalvo = (responsavel.getId() > 0) ? responsavel : usuarioDAO.buscarPorLogin(responsavel.getLogin());
        
        // Atualiza os vínculos com os alunos
        usuarioDAO.limparVinculos(usuarioSalvo.getId());
        if (alunosVinculados != null) {
            for (Aluno aluno : alunosVinculados) {
                usuarioDAO.vincularAluno(usuarioSalvo.getId(), aluno.getId());
            }
        }
    }
}