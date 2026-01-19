// Arquivo: AlunoDAOImpl.java (Versão Final e Completa)
package br.com.meuportfolio.dao;

import br.com.meuportfolio.model.Aluno;
import br.com.meuportfolio.model.Turma;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAOImpl implements AlunoDAO {

    private static final String SQL_SELECT_ALUNOS = "SELECT a.id as aluno_id, a.nome as aluno_nome, a.matricula, a.caminho_foto, " +
                                                    "t.id as turma_id, t.nome as turma_nome, t.ano_letivo " +
                                                    "FROM alunos a " +
                                                    "LEFT JOIN turmas t ON a.turma_id = t.id ";

    @Override
    public void salvar(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO alunos (nome, matricula, caminho_foto, turma_id) VALUES (?, ?, ?, ?)";
        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getMatricula());
            stmt.setString(3, aluno.getCaminhoFoto());
            
            if (aluno.getTurma() != null) {
                stmt.setInt(4, aluno.getTurma().getId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void atualizar(Aluno aluno) throws SQLException {
        String sql = "UPDATE alunos SET nome = ?, matricula = ?, caminho_foto = ?, turma_id = ? WHERE id = ?";
        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getMatricula());
            stmt.setString(3, aluno.getCaminhoFoto());
            
            if (aluno.getTurma() != null) {
                stmt.setInt(4, aluno.getTurma().getId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            
            stmt.setInt(5, aluno.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void excluir(int id) throws SQLException {
        String sqlDeleteMidias = "DELETE FROM midias WHERE aluno_id = ?";
        String sqlDeleteVinculos = "DELETE FROM responsaveis_alunos WHERE aluno_id = ?";
        String sqlDeleteAluno = "DELETE FROM alunos WHERE id = ?";
        
        Connection conexao = null;
        try {
            conexao = DAOFactory.criarConexao();
            conexao.setAutoCommit(false);

            try (PreparedStatement stmt = conexao.prepareStatement(sqlDeleteMidias)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conexao.prepareStatement(sqlDeleteVinculos)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conexao.prepareStatement(sqlDeleteAluno)) {
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
                conexao.close();
            }
        }
    }

    @Override
    public Aluno buscarPorId(int id) throws SQLException {
        String sql = SQL_SELECT_ALUNOS + " WHERE a.id = ?";
        Aluno aluno = null;

        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    aluno = montarAluno(rs);
                }
            }
        }
        return aluno;
    }

    @Override
    public List<Aluno> listarTodos() throws SQLException {
        String sql = SQL_SELECT_ALUNOS + " ORDER BY a.nome";
        List<Aluno> alunos = new ArrayList<>();

        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                alunos.add(montarAluno(rs));
            }
        }
        return alunos;
    }
    
    // MÉTODO QUE ESTAVA FALTANDO
    @Override
    public List<Aluno> listarPorResponsavel(int responsavelId) throws SQLException {
        String sql = SQL_SELECT_ALUNOS + " JOIN responsaveis_alunos ra ON a.id = ra.aluno_id WHERE ra.responsavel_id = ?";
        List<Aluno> alunos = new ArrayList<>();

        try (Connection conexao = DAOFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, responsavelId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    alunos.add(montarAluno(rs));
                }
            }
        }
        return alunos;
    }
    
    private Aluno montarAluno(ResultSet rs) throws SQLException {
        Aluno aluno = new Aluno();
        aluno.setId(rs.getInt("aluno_id"));
        aluno.setNome(rs.getString("aluno_nome"));
        aluno.setMatricula(rs.getString("matricula"));
        aluno.setCaminhoFoto(rs.getString("caminho_foto"));

        int turmaId = rs.getInt("turma_id");
        if (!rs.wasNull()) {
            Turma turma = new Turma();
            turma.setId(turmaId);
            turma.setNome(rs.getString("turma_nome"));
            turma.setAnoLetivo(rs.getInt("ano_letivo"));
            aluno.setTurma(turma);
        }
        
        return aluno;
    }
}