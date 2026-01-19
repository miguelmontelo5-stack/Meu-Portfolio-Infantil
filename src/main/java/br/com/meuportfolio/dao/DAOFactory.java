package br.com.meuportfolio.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por criar e gerenciar a conexão com o banco de dados.
 */
public class DAOFactory {

    private static final String HOST = "localhost"; // 
    private static final String PORT = "5432"; // 
    private static final String DB_NAME = "postgres"; // O nome do banco no script
    private static final String USER = "postgres"; // 
    private static final String PASS = "@Miguel12"; 
    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;

    /**
     * Cria e retorna uma nova conexão com o banco de dados.
     * @return um objeto Connection
     * @throws SQLException se a conexão falhar
     */
    public static Connection criarConexao() throws SQLException {
        try {
            // Carrega o driver do PostgreSQL
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            // Este erro não deve acontecer se a dependência do Maven estiver correta
            throw new SQLException("Driver do PostgreSQL não encontrado.", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}