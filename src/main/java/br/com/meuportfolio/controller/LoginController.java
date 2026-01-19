package br.com.meuportfolio.controller;

import br.com.meuportfolio.dao.UsuarioDAO;
import br.com.meuportfolio.dao.UsuarioDAOImpl;
import br.com.meuportfolio.model.Usuario;
import br.com.meuportfolio.model.Usuario.TipoUsuario;

/**
 * Controller responsável pela lógica de negócio de autenticação.
 */
public class LoginController {

    private UsuarioDAO usuarioDAO;

    // No construtor, instanciamos o DAO que será usado pelo controller.
    public LoginController() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    /**
     * Autentica um usuário com base no login, senha e tipo esperado.
     *
     * @param login O login digitado pelo usuário.
     * @param senha A senha digitada pelo usuário.
     * @param tipoEsperado O tipo de usuário que se espera autenticar (ADMINISTRADOR ou RESPONSAVEL).
     * @return O objeto Usuario se a autenticação for bem-sucedida, caso contrário, null.
     * @throws Exception Se ocorrer um erro durante a consulta ao banco de dados.
     */
    public Usuario autenticar(String login, String senha, TipoUsuario tipoEsperado) throws Exception {
        
        // 1. Usa o DAO para buscar o usuário pelo login
        Usuario usuarioDoBanco = usuarioDAO.buscarPorLogin(login);

        // 2. Verifica se o usuário existe, se a senha confere e se o tipo está correto
        if (usuarioDoBanco != null && usuarioDoBanco.getSenha().equals(senha) && usuarioDoBanco.getTipoUsuario() == tipoEsperado) {
            // Lembre-se: a verificação de senha aqui é simples. Num sistema real, compararíamos hashes.
            return usuarioDoBanco; // Sucesso! Retorna o objeto do usuário.
        }

        // Se qualquer uma das verificações falhar, retorna nulo.
        return null;
    }
}