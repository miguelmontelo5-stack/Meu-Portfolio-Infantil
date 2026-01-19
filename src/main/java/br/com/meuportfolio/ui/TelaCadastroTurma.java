// Arquivo: TelaCadastroTurma.java (Versão Correta para Edição)
package br.com.meuportfolio.ui;

import br.com.meuportfolio.controller.TurmaController;
import br.com.meuportfolio.model.Turma;
import javax.swing.*;
import java.awt.*;

public class TelaCadastroTurma extends JDialog {

    private JTextField campoNome;
    private JTextField campoAno;
    private TurmaController turmaController;
    private boolean salvo = false;
    private Turma turmaParaEditar;

    public TelaCadastroTurma(Frame owner, Turma turmaParaEditar) {
        super(owner, "Cadastro de Turma", true);
        this.turmaController = new TurmaController();
        this.turmaParaEditar = turmaParaEditar;

        if (turmaParaEditar != null) {
            setTitle("Editar Turma");
        }

        setSize(400, 200);
        setLocationRelativeTo(owner);
        setLayout(new GridLayout(3, 2, 10, 10));
        
        add(new JLabel("Nome da Turma:"));
        campoNome = new JTextField();
        add(campoNome);

        add(new JLabel("Ano Letivo:"));
        campoAno = new JTextField();
        add(campoAno);

        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoCancelar = new JButton("Cancelar");
        add(botaoSalvar);
        add(botaoCancelar);

        if (turmaParaEditar != null) {
            campoNome.setText(turmaParaEditar.getNome());
            campoAno.setText(String.valueOf(turmaParaEditar.getAnoLetivo()));
        }

        botaoCancelar.addActionListener(e -> dispose());
        botaoSalvar.addActionListener(e -> salvar());
    }
    
    private void salvar() {
        try {
            boolean isNew = (turmaParaEditar == null);
            if (isNew) {
                turmaParaEditar = new Turma();
            }

            turmaParaEditar.setNome(campoNome.getText());
            turmaParaEditar.setAnoLetivo(Integer.parseInt(campoAno.getText()));

            if (isNew) {
                turmaController.salvar(turmaParaEditar);
            } else {
                turmaController.atualizar(turmaParaEditar);
            }
            
            JOptionPane.showMessageDialog(this, "Turma salva com sucesso!");
            this.salvo = true;
            dispose();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano letivo inválido. Por favor, insira um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar turma: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSalvo() {
        return salvo;
    }
}