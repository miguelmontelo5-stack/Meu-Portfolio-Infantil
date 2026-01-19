package br.com.meuportfolio.dao;

import br.com.meuportfolio.model.Aluno;
import br.com.meuportfolio.model.Midia;
import br.com.meuportfolio.model.Midia.TipoMidia;
import br.com.meuportfolio.model.Turma;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação concreta do MidiaDAO para PostgreSQL.
 */
public class MidiaDAOImpl implements MidiaDAO {

    private static final String SQL_SELECT_MIDIAS = 
        "SELECT m.id as midia_id, m.descricao, m.caminho_arquivo, m.data_envio, m.tipo_midia, " +
        "a.id as aluno_id, a.nome as aluno_nome, " +
        "t.id as turma_id, t.nome as turma_nome " +
        "FROM midias m " +
        "LEFT JOIN alunos a ON m.aluno_id = a.id " +
        "LEFT JOIN turmas t ON m.turma_id = t.id ";

    @Override
    public void salvar(Midia midia) throws SQLException {
        String sql = "INSERT INTO midias (descricao, caminho_arquivo, data_envio, tipo_midia, aluno_id, turma_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, midia.getDescricao());
            stmt.setString(2, midia.getCaminhoArquivo());
            stmt.setDate(3, Date.valueOf(midia.getDataEnvio())); // Converte LocalDate para java.sql.Date
            stmt.setString(4, midia.getTipoMidia().name());

            if (midia.getAluno() != null) {
                stmt.setInt(5, midia.getAluno().getId());
                stmt.setNull(6, Types.INTEGER);
            } else if (midia.getTurma() != null) {
                stmt.setNull(5, Types.INTEGER);
                stmt.setInt(6, midia.getTurma().getId());
            }

            stmt.executeUpdate();
        }
    }

    @Override
    public void atualizar(Midia midia) throws SQLException {
        String sql = "UPDATE midias SET descricao = ?, caminho_arquivo = ?, data_envio = ?, tipo_midia = ?, aluno_id = ?, turma_id = ? WHERE id = ?";
         try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, midia.getDescricao());
            stmt.setString(2, midia.getCaminhoArquivo());
            stmt.setDate(3, Date.valueOf(midia.getDataEnvio()));
            stmt.setString(4, midia.getTipoMidia().name());

            if (midia.getAluno() != null) {
                stmt.setInt(5, midia.getAluno().getId());
                stmt.setNull(6, Types.INTEGER);
            } else if (midia.getTurma() != null) {
                stmt.setNull(5, Types.INTEGER);
                stmt.setInt(6, midia.getTurma().getId());
            }
            stmt.setInt(7, midia.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM midias WHERE id = ?";
        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Midia buscarPorId(int id) throws SQLException {
        String sql = SQL_SELECT_MIDIAS + " WHERE m.id = ?";
        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarMidia(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Midia> listarPorAluno(int alunoId) throws SQLException {
        String sql = SQL_SELECT_MIDIAS + " WHERE m.aluno_id = ? ORDER BY m.data_envio DESC";
        return listar(sql, alunoId);
    }

    @Override
    public List<Midia> listarPorTurma(int turmaId) throws SQLException {
         String sql = SQL_SELECT_MIDIAS + " WHERE m.turma_id = ? ORDER BY m.data_envio DESC";
        return listar(sql, turmaId);
    }

    private List<Midia> listar(String sql, int id) throws SQLException {
        List<Midia> midias = new ArrayList<>();
        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    midias.add(montarMidia(rs));
                }
            }
        }
        return midias;
    }

    private Midia montarMidia(ResultSet rs) throws SQLException {
        Midia midia = new Midia();
        midia.setId(rs.getInt("midia_id"));
        midia.setDescricao(rs.getString("descricao"));
        midia.setCaminhoArquivo(rs.getString("caminho_arquivo"));
        midia.setDataEnvio(rs.getDate("data_envio").toLocalDate()); // Converte java.sql.Date para LocalDate
        midia.setTipoMidia(TipoMidia.valueOf(rs.getString("tipo_midia")));

        // Verifica se a mídia pertence a um aluno ou a uma turma
        int alunoId = rs.getInt("aluno_id");
        if (!rs.wasNull()) {
            Aluno aluno = new Aluno();
            aluno.setId(alunoId);
            aluno.setNome(rs.getString("aluno_nome"));
            midia.setAluno(aluno);
        }

        int turmaId = rs.getInt("turma_id");
        if (!rs.wasNull()) {
            Turma turma = new Turma();
            turma.setId(turmaId);
            turma.setNome(rs.getString("turma_nome"));
            midia.setTurma(turma);
        }

        return midia;
    }
}