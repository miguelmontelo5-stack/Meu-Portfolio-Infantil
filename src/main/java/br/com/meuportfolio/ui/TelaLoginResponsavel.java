package br.com.meuportfolio.ui;

import br.com.meuportfolio.controller.LoginController;
import br.com.meuportfolio.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tela de login para o usuário Responsável, agora como um JDialog.
 */
public class TelaLoginResponsavel extends JDialog {

    private JTextField campoCpf;
    private JPasswordField campoSenha;

    public TelaLoginResponsavel(Frame owner) {
        super(owner, "Login - Responsável", true);
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelTitulo = new JLabel("Acesso do Responsável", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(labelTitulo, gbc);

        JLabel labelCpf = new JLabel("CPF:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(labelCpf, gbc);

        campoCpf = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(campoCpf, gbc);

        JLabel labelSenha = new JLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(labelSenha, gbc);

        campoSenha = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(campoSenha, gbc);

        JButton botaoEntrar = new JButton("Entrar");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(botaoEntrar, gbc);

        // --- AÇÃO DO BOTÃO ATUALIZADA ---
        botaoEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpf = campoCpf.getText();
                String senha = new String(campoSenha.getPassword());

                LoginController loginController = new LoginController();
                try {
                    Usuario usuarioAutenticado = loginController.autenticar(cpf, senha, Usuario.TipoUsuario.RESPONSAVEL);

                    if (usuarioAutenticado != null) {
                        // Esconde a tela de login
                        setVisible(false);
                        // Abre a tela principal do responsável, passando quem logou
                        new TelaPrincipalResponsavel( (Frame) getOwner(), usuarioAutenticado).setVisible(true);
                        // Fecha a janela de seleção de usuário que ficou para trás
                        getOwner().dispose();
                        // Fecha esta janela de login de vez
                        dispose();

                    } else {
                        JOptionPane.showMessageDialog(TelaLoginResponsavel.this, "CPF ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(TelaLoginResponsavel.this, "Ocorreu um erro ao tentar conectar ao banco de dados.", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}