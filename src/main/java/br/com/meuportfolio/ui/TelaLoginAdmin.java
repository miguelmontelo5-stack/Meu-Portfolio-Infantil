// Arquivo: TelaLoginAdmin.java (Versão Final Corrigida para JDialog)
package br.com.meuportfolio.ui;

import br.com.meuportfolio.controller.LoginController;
import br.com.meuportfolio.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaLoginAdmin extends JDialog { // MUDOU DE JFrame PARA JDialog

    private JTextField campoUsuario;
    private JPasswordField campoSenha;

    public TelaLoginAdmin(Frame owner) { // MUDOU para receber a janela "mãe"
        super(owner, "Login - Administrador", true);
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new GridBagLayout());

        // ... O resto do código do layout continua exatamente o mesmo ...
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel labelTitulo = new JLabel("Acesso do Administrador", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; add(labelTitulo, gbc);
        JLabel labelUsuario = new JLabel("Usuário:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; add(labelUsuario, gbc);
        campoUsuario = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1; add(campoUsuario, gbc);
        JLabel labelSenha = new JLabel("Senha:");
        gbc.gridx = 0; gbc.gridy = 2; add(labelSenha, gbc);
        campoSenha = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 2; add(campoSenha, gbc);
        JButton botaoEntrar = new JButton("Entrar");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE; add(botaoEntrar, gbc);


        // --- AÇÃO DO BOTÃO ATUALIZADA ---
        botaoEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = campoUsuario.getText();
                String senha = new String(campoSenha.getPassword());

                LoginController loginController = new LoginController();
                try {
                    Usuario usuarioAutenticado = loginController.autenticar(usuario, senha, Usuario.TipoUsuario.ADMINISTRADOR);

                    if (usuarioAutenticado != null) {
                        setVisible(false); // Esconde a tela de login
                        new TelaPrincipalAdmin().setVisible(true); // Abre a tela principal do admin
                        getOwner().dispose(); // Fecha a tela de seleção que ficou para trás
                        dispose(); // Fecha esta tela de login de vez
                    } else {
                        JOptionPane.showMessageDialog(TelaLoginAdmin.this, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(TelaLoginAdmin.this, "Ocorreu um erro ao tentar conectar ao banco de dados.", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}