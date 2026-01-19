// Arquivo: TelaGerenciamentoResponsaveis.java (Novo e Completo)
package br.com.meuportfolio.ui;

import br.com.meuportfolio.controller.ResponsavelController;
import br.com.meuportfolio.dao.UsuarioDAOImpl;
import br.com.meuportfolio.model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaGerenciamentoResponsaveis extends JFrame {

    private JTable tabelaResponsaveis;
    private DefaultTableModel modeloTabela;
    private ResponsavelController responsavelController;

    public TelaGerenciamentoResponsaveis() {
        super("Gerenciamento de Responsáveis");
        this.responsavelController = new ResponsavelController();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoAdicionar = new JButton("Adicionar Responsável");
        JButton botaoEditar = new JButton("Editar Selecionado");
        JButton botaoExcluir = new JButton("Excluir Selecionado");
        painelBotoes.add(botaoAdicionar);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoExcluir);
        add(painelBotoes, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Login (CPF)"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaResponsaveis = new JTable(modeloTabela);
        add(new JScrollPane(tabelaResponsaveis), BorderLayout.CENTER);

        JPanel painelVoltar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botaoVoltar = new JButton("Voltar");
        painelVoltar.add(botaoVoltar);
        add(painelVoltar, BorderLayout.SOUTH);
        
        carregarResponsaveis();

        botaoVoltar.addActionListener(e -> dispose());
        botaoAdicionar.addActionListener(e -> abrirDialogoCadastro(null));
        botaoEditar.addActionListener(e -> abrirDialogoEdicao());
        botaoExcluir.addActionListener(e -> excluirResponsavelSelecionado());
    }

    private void carregarResponsaveis() {
        modeloTabela.setRowCount(0);
        try {
            List<Usuario> responsaveis = responsavelController.listarTodosResponsaveis();
            for (Usuario resp : responsaveis) {
                modeloTabela.addRow(new Object[]{resp.getId(), resp.getNome(), resp.getLogin()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar responsáveis: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirDialogoCadastro(Usuario responsavel) {
        TelaCadastroResponsavel dialogo = new TelaCadastroResponsavel(this, responsavel);
        dialogo.setVisible(true);
        if (dialogo.isSalvo()) {
            carregarResponsaveis();
        }
    }

    private void abrirDialogoEdicao() {
        int linha = tabelaResponsaveis.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um responsável para editar.");
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        try {
            Usuario resp = new UsuarioDAOImpl().buscarPorId(id);
            abrirDialogoCadastro(resp);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar dados do responsável: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirResponsavelSelecionado() {
        int linha = tabelaResponsaveis.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um responsável para excluir.");
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        String nome = (String) modeloTabela.getValueAt(linha, 1);
        
        Object[] options = {"Sim, excluir", "Não"};
        int confirmacao = JOptionPane.showOptionDialog(this, "Tem certeza que deseja excluir " + nome + "?", "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
        
        if (confirmacao == 0) {
            try {
                new UsuarioDAOImpl().excluir(id); // Pode chamar direto o DAO ou usar o controller
                JOptionPane.showMessageDialog(this, "Responsável excluído com sucesso.");
                carregarResponsaveis();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir responsável: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}