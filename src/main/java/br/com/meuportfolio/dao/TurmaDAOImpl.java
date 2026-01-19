package br.com.meuportfolio.dao;

import br.com.meuportfolio.model.Turma;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TurmaDAOImpl implements TurmaDAO {

    @Override
    public void salvar(Turma turma) throws SQLException {
        String sql = "INSERT INTO turmas (nome, ano_letivo) VALUES (?, ?)";
        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, turma.getNome());
            stmt.setInt(2, turma.getAnoLetivo());
            stmt.executeUpdate();
        }
    }

    @Override
    public void atualizar(Turma turma) throws SQLException {
        String sql = "UPDATE turmas SET nome = ?, ano_letivo = ? WHERE id = ?";
        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, turma.getNome());
            stmt.setInt(2, turma.getAnoLetivo());
            stmt.setInt(3, turma.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws SQLException {
        String sqlSelectMidias = "SELECT caminho_arquivo FROM midias WHERE turma_id = ?";
        String sqlDeleteMidias = "DELETE FROM midias WHERE turma_id = ?";
        String sqlUpdateAlunos = "UPDATE alunos SET turma_id = NULL WHERE turma_id = ?";
        String sqlDeleteTurma = "DELETE FROM turmas WHERE id = ?";
        
        Connection conexao = null;
        try {
            conexao = DAOFactory.criarConexao();
            conexao.setAutoCommit(false);

            List<String> caminhosParaDeletar = new ArrayList<>();
            try (PreparedStatement stmt = conexao.prepareStatement(sqlSelectMidias)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        caminhosParaDeletar.add(rs.getString("caminho_arquivo"));
                    }
                }
            }

            for (String caminho : caminhosParaDeletar) {
                try {
                    Files.deleteIfExists(Paths.get(caminho));
                } catch (IOException e) {
                    System.err.println("Falha ao deletar arquivo físico: " + caminho);
                }
            }
            
            try (PreparedStatement stmt = conexao.prepareStatement(sqlDeleteMidias)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conexao.prepareStatement(sqlUpdateAlunos)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conexao.prepareStatement(sqlDeleteTurma)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
            
            conexao.commit();

        } catch (SQLException e) {
            if (conexao != null) {
                conexao.rollback();
            }
            throw e;
        } finally {
            if (conexao != null) {
                try {
                    conexao.setAutoCommit(true);
                    conexao.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Turma buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, ano_letivo FROM turmas WHERE id = ?";
        Turma turma = null;

        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    turma = new Turma();
                    turma.setId(rs.getInt("id"));
                    turma.setNome(rs.getString("nome"));
                    turma.setAnoLetivo(rs.getInt("ano_letivo"));
                }
            }
        }
        return turma;
    }

    @Override
    public List<Turma> listarTodas() throws SQLException {
        String sql = "SELECT id, nome, ano_letivo FROM turmas ORDER BY nome";
        List<Turma> turmas = new ArrayList<>();

        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Turma turma = new Turma();
                turma.setId(rs.getInt("id"));
                turma.setNome(rs.getString("nome"));
                turma.setAnoLetivo(rs.getInt("ano_letivo"));
                turmas.add(turma);
            }
        }
        return turmas;
    }
}