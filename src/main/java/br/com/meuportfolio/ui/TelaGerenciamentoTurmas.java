package br.com.meuportfolio.ui;

import br.com.meuportfolio.controller.TurmaController;
import br.com.meuportfolio.dao.TurmaDAOImpl;
import br.com.meuportfolio.model.Turma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaGerenciamentoTurmas extends JFrame {

    private JTable tabelaTurmas;
    private DefaultTableModel modeloTabela;
    private TurmaController turmaController;

    public TelaGerenciamentoTurmas() {
        super("Gerenciamento de Turmas");
        this.turmaController = new TurmaController();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoAdicionar = new JButton("Adicionar Nova Turma");
        JButton botaoEditar = new JButton("Editar Selecionada");
        JButton botaoExcluir = new JButton("Excluir Selecionada");
        JButton botaoPortfolio = new JButton("Gerenciar Portfólio da Turma");
        
        painelBotoes.add(botaoAdicionar);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoPortfolio);
        add(painelBotoes, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome da Turma", "Ano Letivo"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaTurmas = new JTable(modeloTabela);
        add(new JScrollPane(tabelaTurmas), BorderLayout.CENTER);

        JPanel painelVoltar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botaoVoltar = new JButton("Voltar");
        painelVoltar.add(botaoVoltar);
        add(painelVoltar, BorderLayout.SOUTH);
        
        carregarTurmas();

        botaoVoltar.addActionListener(e -> dispose()); 
        botaoAdicionar.addActionListener(e -> abrirDialogoCadastro(null));
        botaoEditar.addActionListener(e -> abrirDialogoEdicao());
        botaoExcluir.addActionListener(e -> excluirTurmaSelecionada());
        botaoPortfolio.addActionListener(e -> abrirTelaPortfolio());
    }

    private void carregarTurmas() {
        modeloTabela.setRowCount(0);
        try {
            List<Turma> turmas = turmaController.listarTodas();
            for (Turma turma : turmas) {
                modeloTabela.addRow(new Object[]{
                    turma.getId(),
                    turma.getNome(),
                    turma.getAnoLetivo()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar turmas do banco de dados.", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirDialogoCadastro(Turma turma) {
        TelaCadastroTurma dialogo = new TelaCadastroTurma(this, turma);
        dialogo.setVisible(true);
        if (dialogo.isSalvo()) {
            carregarTurmas();
        }
    }
    
    private void abrirDialogoEdicao() {
        int linhaSelecionada = tabelaTurmas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma turma para editar.", "Nenhuma Turma Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idTurma = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        try {
            Turma turmaSelecionada = new TurmaDAOImpl().buscarPorId(idTurma); 
            if (turmaSelecionada != null) {
                abrirDialogoCadastro(turmaSelecionada);
            }
        } catch (Exception e) {
            e.printStackTrace();
             JOptionPane.showMessageDialog(this, "Erro ao buscar dados da turma para edição.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirTurmaSelecionada() {
        int linhaSelecionada = tabelaTurmas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma turma na tabela para excluir.", "Nenhuma Turma Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idTurma = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        String nomeTurma = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
        
        Object[] options = {"Sim", "Não"};
        int confirmacao = JOptionPane.showOptionDialog(this,
                "Tem certeza que deseja excluir a turma '" + nomeTurma + "'?\nEsta ação não pode ser desfeita.",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);

        if (confirmacao == 0) {
            try {
                turmaController.excluir(idTurma);
                JOptionPane.showMessageDialog(this, "Turma excluída com sucesso!");
                carregarTurmas();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao excluir turma: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void abrirTelaPortfolio() {
        int linhaSelecionada = tabelaTurmas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma turma para gerenciar o portfólio.", "Nenhuma Turma Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idTurma = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        try {
            Turma turmaSelecionada = new TurmaDAOImpl().buscarPorId(idTurma);
            if (turmaSelecionada != null) {
                new TelaGerenciamentoPortfolioTurma(this, turmaSelecionada).setVisible(true);
            }
        } catch (Exception e) { // A variável 'ex' é declarada AQUI
            e.printStackTrace();
            // E usada AQUI, dentro do mesmo bloco.
            JOptionPane.showMessageDialog(this, "Erro ao buscar dados da turma.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}