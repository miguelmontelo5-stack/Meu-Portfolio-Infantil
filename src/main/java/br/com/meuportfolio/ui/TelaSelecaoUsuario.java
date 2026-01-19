// Arquivo: TelaSelecaoUsuario.java (Versão Final Corrigida)
package br.com.meuportfolio.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaSelecaoUsuario extends JFrame {

    public TelaSelecaoUsuario() {
        super("Meu Portifolio - Bem-vindo(a)!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel labelTitulo = new JLabel("Meu Portifolio", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(labelTitulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton botaoAdmin = new JButton("Sou Administrador");
        JButton botaoResponsavel = new JButton("Sou Responsável");

        painelBotoes.add(botaoAdmin);
        painelBotoes.add(botaoResponsavel);

        add(painelBotoes, BorderLayout.CENTER);

        // Ação para o botão de Administrador
        botaoAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abre a tela de login do Admin (agora como JDialog)
                new TelaLoginAdmin(TelaSelecaoUsuario.this).setVisible(true);
                // Esconde a tela de seleção enquanto o login está ativo
                setVisible(false);
            }
        });

        // --- MUDANÇA ESTÁ AQUI ---
        // Ação para o botão de Responsável
        botaoResponsavel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abre a tela de login do Responsável, passando esta janela (this) como "mãe"
                new TelaLoginResponsavel(TelaSelecaoUsuario.this).setVisible(true);
                // Esconde a tela de seleção
                setVisible(false);
            }
        });
    }
}