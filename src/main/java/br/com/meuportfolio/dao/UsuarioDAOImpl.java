package br.com.meuportfolio.dao;

import br.com.meuportfolio.model.Usuario;
import br.com.meuportfolio.model.Usuario.TipoUsuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação concreta do UsuarioDAO para PostgreSQL.
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public void salvar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, login, senha, tipo_usuario) VALUES (?, ?, ?, ?)";
        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTipoUsuario().name());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, login = ?, senha = ?, tipo_usuario = ? WHERE id = ?";
        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTipoUsuario().name());
            stmt.setInt(5, usuario.getId());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws SQLException {
        String sqlDeleteVinculos = "DELETE FROM responsaveis_alunos WHERE responsavel_id = ?";
        String sqlDeleteUsuario = "DELETE FROM usuarios WHERE id = ?";
        
        Connection conexao = null;
        try {
            conexao = DAOFactory.criarConexao();
            conexao.setAutoCommit(false);

            try (PreparedStatement stmt = conexao.prepareStatement(sqlDeleteVinculos)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conexao.prepareStatement(sqlDeleteUsuario)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            conexao.commit();

        } catch (SQLException e) {
            if (conexao != null) conexao.rollback();
            throw e;
        } finally {
            if (conexao != null) conexao.close();
        }
    }

    @Override
    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        return buscar(sql, id);
    }

    @Override
    public Usuario buscarPorLogin(String login) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE login = ?";
        return buscar(sql, login);
    }

    @Override
    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(montarUsuario(rs));
            }
        }
        return usuarios;
    }

    @Override
    public void vincularAluno(int responsavelId, int alunoId) throws SQLException {
        String sql = "INSERT INTO responsaveis_alunos (responsavel_id, aluno_id) VALUES (?, ?)";
        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, responsavelId);
            stmt.setInt(2, alunoId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void limparVinculos(int responsavelId) throws SQLException {
        String sql = "DELETE FROM responsaveis_alunos WHERE responsavel_id = ?";
        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, responsavelId);
            stmt.executeUpdate();
        }
    }

    private Usuario buscar(String sql, Object parametro) throws SQLException {
        Usuario usuario = null;
        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setObject(1, parametro);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = montarUsuario(rs);
                }
            }
        }
        return usuario;
    }

    private Usuario montarUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setLogin(rs.getString("login"));
        usuario.setSenha(rs.getString("senha"));
        
        usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getString("tipo_usuario")));
        
        return usuario;
    }
}