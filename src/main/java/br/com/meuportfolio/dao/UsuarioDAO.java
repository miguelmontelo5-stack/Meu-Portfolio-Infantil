package br.com.meuportfolio.dao;

import br.com.meuportfolio.model.Usuario;
import java.util.List;

public interface UsuarioDAO {
    void salvar(Usuario usuario) throws Exception;
    void atualizar(Usuario usuario) throws Exception;
    void excluir(int id) throws Exception;
    Usuario buscarPorId(int id) throws Exception;
    Usuario buscarPorLogin(String login) throws Exception;
    List<Usuario> listarTodos() throws Exception;
    void vincularAluno(int responsavelId, int alunoId) throws Exception;
    void limparVinculos(int responsavelId) throws Exception;
}