package br.com.meuportfolio.ui;

import br.com.meuportfolio.controller.AlunoController;
import br.com.meuportfolio.controller.TurmaController;
import br.com.meuportfolio.model.Aluno;
import br.com.meuportfolio.model.Turma;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class TelaCadastroAluno extends JDialog {

    private JTextField campoNome;
    private JTextField campoMatricula;
    private JComboBox<Turma> comboTurmas; // JComboBox para listar as turmas

    private AlunoController alunoController;
    private TurmaController turmaController;
    
    private boolean salvo = false;
    private Aluno alunoParaEditar;

    // Construtor principal que recebe a turma para editar (pode ser nula)
    public TelaCadastroAluno(Frame owner, Aluno alunoParaEditar) {
        super(owner, "Cadastro de Aluno", true);
        this.alunoParaEditar = alunoParaEditar;
        this.alunoController = new AlunoController();
        this.turmaController = new TurmaController();

        setSize(450, 250);
        setLocationRelativeTo(owner);
        setLayout(new GridBagLayout());
        
        initComponents();
        carregarTurmasNoComboBox();

        if (alunoParaEditar != null) {
            setTitle("Editar Aluno");
            preencherFormulario();
        }
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Campos do Formulário ---
        gbc.gridy = 0; gbc.gridx = 0; add(new JLabel("Nome:"), gbc);
        gbc.gridy = 0; gbc.gridx = 1; campoNome = new JTextField(20); add(campoNome, gbc);

        gbc.gridy = 1; gbc.gridx = 0; add(new JLabel("Matrícula:"), gbc);
        gbc.gridy = 1; gbc.gridx = 1; campoMatricula = new JTextField(20); add(campoMatricula, gbc);

        gbc.gridy = 2; gbc.gridx = 0; add(new JLabel("Turma:"), gbc);
        gbc.gridy = 2; gbc.gridx = 1; comboTurmas = new JComboBox<>(); add(comboTurmas, gbc);
        
        // --- Painel de Botões ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoCancelar = new JButton("Cancelar");
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoCancelar);
        
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2; add(painelBotoes, gbc);

        // --- Ações ---
        botaoCancelar.addActionListener(e -> dispose());
        botaoSalvar.addActionListener(e -> salvar());
    }

    private void carregarTurmasNoComboBox() {
        try {
            List<Turma> turmas = turmaController.listarTodas();
            // Usamos um Vector, pois é o que o JComboBox aceita nativamente
            comboTurmas.setModel(new DefaultComboBoxModel<>(new Vector<>(turmas)));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar turmas.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Sobrescrevemos o "toString" do JComboBox para mostrar apenas o nome da turma
    // Esta é uma técnica comum para exibir objetos complexos em listas
    @Override
    public void addNotify() {
        super.addNotify();
        comboTurmas.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Turma) {
                    setText(((Turma) value).getNome());
                }
                return this;
            }
        });
    }

    private void preencherFormulario() {
        campoNome.setText(alunoParaEditar.getNome());
        campoMatricula.setText(alunoParaEditar.getMatricula());
        // Seleciona a turma correta no ComboBox
        comboTurmas.setSelectedItem(alunoParaEditar.getTurma());
    }

    private void salvar() {
        try {
            boolean isNew = (alunoParaEditar == null);
            if (isNew) {
                alunoParaEditar = new Aluno();
            }

            alunoParaEditar.setNome(campoNome.getText());
            alunoParaEditar.setMatricula(campoMatricula.getText());
            alunoParaEditar.setTurma((Turma) comboTurmas.getSelectedItem()); // Pega o objeto Turma selecionado

            if (isNew) {
                alunoController.salvar(alunoParaEditar);
            } else {
                alunoController.atualizar(alunoParaEditar);
            }

            JOptionPane.showMessageDialog(this, "Aluno salvo com sucesso!");
            this.salvo = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar aluno: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSalvo() {
        return salvo;
    }
}