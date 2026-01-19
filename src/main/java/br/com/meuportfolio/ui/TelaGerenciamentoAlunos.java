package br.com.meuportfolio.ui;

import br.com.meuportfolio.controller.AlunoController;
import br.com.meuportfolio.dao.AlunoDAOImpl;
import br.com.meuportfolio.model.Aluno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaGerenciamentoAlunos extends JFrame {

    private JTable tabelaAlunos;
    private DefaultTableModel modeloTabela;
    private AlunoController alunoController;

    public TelaGerenciamentoAlunos() {
        super("Gerenciamento de Alunos");
        this.alunoController = new AlunoController();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoAdicionar = new JButton("Adicionar Novo Aluno");
        JButton botaoEditar = new JButton("Editar Selecionado");
        JButton botaoExcluir = new JButton("Excluir Selecionado");
        JButton botaoPortfolio = new JButton("Gerenciar Portfólio");
        
        painelBotoes.add(botaoAdicionar);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoPortfolio);
        add(painelBotoes, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome do Aluno", "Matrícula", "Turma"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaAlunos = new JTable(modeloTabela);
        add(new JScrollPane(tabelaAlunos), BorderLayout.CENTER);

        JPanel painelVoltar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botaoVoltar = new JButton("Voltar");
        painelVoltar.add(botaoVoltar);
        add(painelVoltar, BorderLayout.SOUTH);
        
        carregarAlunos();

        // --- Ações dos Botões ---
        botaoVoltar.addActionListener(e -> dispose());
        botaoAdicionar.addActionListener(e -> abrirDialogoCadastro(null));
        botaoEditar.addActionListener(e -> abrirDialogoEdicao());
        botaoExcluir.addActionListener(e -> excluirAlunoSelecionado());
        
        // --- MUDANÇA FINAL AQUI ---
        botaoPortfolio.addActionListener(e -> abrirTelaPortfolio());
    }

    private void carregarAlunos() {
        modeloTabela.setRowCount(0);
        try {
            List<Aluno> alunos = alunoController.listarTodos();
            for (Aluno aluno : alunos) {
                String nomeTurma = (aluno.getTurma() != null) ? aluno.getTurma().getNome() : "Sem Turma";
                modeloTabela.addRow(new Object[]{
                    aluno.getId(),
                    aluno.getNome(),
                    aluno.getMatricula(),
                    nomeTurma
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar alunos do banco de dados.", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirDialogoCadastro(Aluno aluno) {
        TelaCadastroAluno dialogo = new TelaCadastroAluno(this, aluno);
        dialogo.setVisible(true);

        if (dialogo.isSalvo()) {
            carregarAlunos();
        }
    }

    private void abrirDialogoEdicao() {
        int linhaSelecionada = tabelaAlunos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um aluno para editar.", "Nenhum Aluno Selecionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idAluno = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        try {
            Aluno alunoSelecionado = new AlunoDAOImpl().buscarPorId(idAluno);
            if (alunoSelecionado != null) {
                abrirDialogoCadastro(alunoSelecionado);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao buscar dados do aluno para edição.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirAlunoSelecionado() {
    int linhaSelecionada = tabelaAlunos.getSelectedRow();
    if (linhaSelecionada == -1) {
        JOptionPane.showMessageDialog(this, "Por favor, selecione um aluno para excluir.", "Nenhum Aluno Selecionado", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int idAluno = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
    String nomeAluno = (String) modeloTabela.getValueAt(linhaSelecionada, 1);

    Object[] options = {"Sim, excluir", "Não"};
    int confirmacao = JOptionPane.showOptionDialog(this,
            "Tem certeza que deseja excluir o aluno '" + nomeAluno + "'?\nTODOS os seus dados, incluindo portfólio e vínculos, serão apagados!",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[1]);

    if (confirmacao == 0) { // O primeiro botão ("Sim, excluir") retorna 0
        try {
            alunoController.excluir(idAluno);
            JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!");
            carregarAlunos(); // Atualiza a tabela
        } catch (Exception ex) { // 'ex' SÓ EXISTE DENTRO DESTE BLOCO
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao excluir aluno: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

       private void abrirTelaPortfolio() {
        int linhaSelecionada = tabelaAlunos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um aluno para gerenciar o portfólio.", "Nenhum Aluno Selecionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idAluno = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
        try {
            Aluno alunoSelecionado = new AlunoDAOImpl().buscarPorId(idAluno);
            if (alunoSelecionado != null) {
                // Abre a tela de portfólio, passando o aluno selecionado
                new TelaGerenciamentoPortfolio(this, alunoSelecionado).setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao buscar dados do aluno.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}