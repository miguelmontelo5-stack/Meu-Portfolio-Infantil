package br.com.meuportfolio.ui;

import br.com.meuportfolio.ui.TelaGerenciamentoTurmas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tela principal (dashboard) para o usuário Administrador.
 */
public class TelaPrincipalAdmin extends JFrame {

    public TelaPrincipalAdmin() {
        // --- Configurações da Janela ---
        super("Painel do Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Título ---
        JLabel labelTitulo = new JLabel("Central de Controle", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(labelTitulo, BorderLayout.NORTH);

        // --- Painel de Botões de Gerenciamento ---
        JPanel painelBotoes = new JPanel();
        // GridLayout com 3 linhas, 1 coluna, e espaçamento vertical e horizontal
        painelBotoes.setLayout(new GridLayout(3, 1, 15, 15));

        JButton botaoTurmas = new JButton("Gerenciar Turmas");
        JButton botaoAlunos = new JButton("Gerenciar Alunos");
        JButton botaoResponsaveis = new JButton("Gerenciar Responsáveis");

        // Adiciona um preenchimento (padding) ao redor do painel de botões
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        painelBotoes.add(botaoTurmas);
        painelBotoes.add(botaoAlunos);
        painelBotoes.add(botaoResponsaveis);

        add(painelBotoes, BorderLayout.CENTER);

        // --- Botão de Voltar/Sair ---
        JPanel painelVoltar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botaoVoltar = new JButton("Sair");
        painelVoltar.add(botaoVoltar);
        add(painelVoltar, BorderLayout.SOUTH);


        // --- Ações dos Botões ---

        botaoTurmas.addActionListener(e -> new TelaGerenciamentoTurmas().setVisible(true));
        botaoAlunos.addActionListener(e -> new TelaGerenciamentoAlunos().setVisible(true));
        botaoResponsaveis.addActionListener(e -> new TelaGerenciamentoResponsaveis().setVisible(true));

        botaoVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Volta para a tela de seleção de usuário
                new TelaSelecaoUsuario().setVisible(true);
                // Fecha a tela atual
                dispose();
            }
        });
    }
}