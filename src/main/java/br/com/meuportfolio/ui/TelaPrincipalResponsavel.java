// Arquivo: TelaPrincipalResponsavel.java (Versão Final e Dinâmica)
package br.com.meuportfolio.ui;

import br.com.meuportfolio.controller.ResponsavelController;
import br.com.meuportfolio.model.Aluno;
import br.com.meuportfolio.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaPrincipalResponsavel extends JDialog { // Mudei para JDialog para melhor comportamento

    private Usuario responsavel;
    private ResponsavelController responsavelController;

    public TelaPrincipalResponsavel(Frame owner, Usuario responsavel) {
        super(owner, "Painel do Responsável", true);
        this.responsavel = responsavel;
        this.responsavelController = new ResponsavelController();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JLabel labelTitulo = new JLabel("Bem-vindo(a), " + responsavel.getNome(), SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(labelTitulo, BorderLayout.NORTH);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));

        try {
            // Busca os filhos REAIS vinculados a este responsável
            List<Aluno> filhos = responsavelController.getAlunosDoResponsavel(responsavel.getId());

            if (filhos.isEmpty()) {
                painelPrincipal.add(new JLabel("Nenhum aluno vinculado a esta conta."));
            } else {
                for (Aluno filho : filhos) {
                    painelPrincipal.add(criarPainelDoFilho(filho));
                    painelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            painelPrincipal.add(new JLabel("Erro ao carregar dados dos alunos."));
        }

        JScrollPane scrollPane = new JScrollPane(painelPrincipal);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        JPanel painelSair = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botaoSair = new JButton("Sair");
        painelSair.add(botaoSair);
        add(painelSair, BorderLayout.SOUTH);

        botaoSair.addActionListener(e -> {
            // Ação de sair: fecha esta janela e mostra a tela de seleção inicial
            getOwner().setVisible(true); // Mostra a janela que estava por trás (TelaSelecaoUsuario)
            dispose();
        });
    }

    private JPanel criarPainelDoFilho(Aluno aluno) {
        JPanel painel = new JPanel(new BorderLayout(10, 5));
        painel.setBorder(BorderFactory.createTitledBorder(aluno.getNome()));

        JLabel info = new JLabel("Selecione qual portfólio você deseja visualizar:");
        painel.add(info, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton botaoPortfolioIndividual = new JButton("Ver Portfólio Individual");
        JButton botaoPortfolioTurma = new JButton("Ver Portfólio da Turma");

        painelBotoes.add(botaoPortfolioIndividual);
        painelBotoes.add(botaoPortfolioTurma);
        painel.add(painelBotoes, BorderLayout.CENTER);
        
        // Ações para abrir a tela de visualização
        botaoPortfolioIndividual.addActionListener(e -> {
            new TelaVisualizacaoPortfolio(this, aluno).setVisible(true);
        });
        
        botaoPortfolioTurma.addActionListener(e -> {
            if (aluno.getTurma() != null) {
                new TelaVisualizacaoPortfolio(this, aluno.getTurma()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Este aluno não está matriculado em uma turma.");
            }
        });

        return painel;
    }
}