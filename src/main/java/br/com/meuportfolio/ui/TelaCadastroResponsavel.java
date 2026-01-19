// Arquivo: TelaCadastroResponsavel.java (Novo e Completo)
package br.com.meuportfolio.ui;

import br.com.meuportfolio.controller.AlunoController;
import br.com.meuportfolio.controller.ResponsavelController;
import br.com.meuportfolio.model.Aluno;
import br.com.meuportfolio.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TelaCadastroResponsavel extends JDialog {

    private JTextField campoNome, campoLogin;
    private JPasswordField campoSenha;
    private JList<Aluno> listaAlunos;
    private DefaultListModel<Aluno> modeloLista;
    
    private ResponsavelController responsavelController;
    private AlunoController alunoController;
    private Usuario responsavelParaEditar;
    private boolean salvo = false;

    public TelaCadastroResponsavel(Frame owner, Usuario responsavel) {
        super(owner, "Cadastro de Responsável", true);
        this.responsavelParaEditar = responsavel;
        this.responsavelController = new ResponsavelController();
        this.alunoController = new AlunoController();

        setSize(500, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Painel do Formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; gbc.gridx = 0; formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridy = 0; gbc.gridx = 1; campoNome = new JTextField(20); formPanel.add(campoNome, gbc);

        gbc.gridy = 1; gbc.gridx = 0; formPanel.add(new JLabel("Login (CPF):"), gbc);
        gbc.gridy = 1; gbc.gridx = 1; campoLogin = new JTextField(20); formPanel.add(campoLogin, gbc);

        gbc.gridy = 2; gbc.gridx = 0; formPanel.add(new JLabel("Senha:"), gbc);
        gbc.gridy = 2; gbc.gridx = 1; campoSenha = new JPasswordField(20); formPanel.add(campoSenha, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Painel da Lista de Alunos
        modeloLista = new DefaultListModel<>();
        listaAlunos = new JList<>(modeloLista);
        listaAlunos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listaAlunos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Vincular Alunos"));
        add(scrollPane, BorderLayout.CENTER);

        // Painel de Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoCancelar = new JButton("Cancelar");
        buttonPanel.add(botaoSalvar);
        buttonPanel.add(botaoCancelar);
        add(buttonPanel, BorderLayout.SOUTH);
        
        carregarAlunosNaLista();
        
        if (responsavelParaEditar != null) {
            setTitle("Editar Responsável");
            preencherFormulario();
        }

        botaoCancelar.addActionListener(e -> dispose());
        botaoSalvar.addActionListener(e -> salvar());
    }

    private void carregarAlunosNaLista() {
        try {
            List<Aluno> todosAlunos = alunoController.listarTodos();
            for (Aluno aluno : todosAlunos) {
                modeloLista.addElement(aluno);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar lista de alunos.");
        }
    }

    private void preencherFormulario() {
        campoNome.setText(responsavelParaEditar.getNome());
        campoLogin.setText(responsavelParaEditar.getLogin());
        // A senha não é preenchida por segurança, só pode ser redefinida.
        campoSenha.setToolTipText("Deixe em branco para manter a senha atual");

        try {
            List<Aluno> alunosVinculados = responsavelController.getAlunosDoResponsavel(responsavelParaEditar.getId());
            List<Integer> idsVinculados = alunosVinculados.stream().map(Aluno::getId).collect(Collectors.toList());
            
            List<Integer> indicesParaSelecionar = new ArrayList<>();
            for (int i = 0; i < modeloLista.getSize(); i++) {
                if (idsVinculados.contains(modeloLista.getElementAt(i).getId())) {
                    indicesParaSelecionar.add(i);
                }
            }
            // Converte a lista de Integer para um array de int
            listaAlunos.setSelectedIndices(indicesParaSelecionar.stream().mapToInt(i -> i).toArray());
            
        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Erro ao carregar vínculos do responsável.");
        }
    }

    private void salvar() {
        try {
            boolean isNew = (responsavelParaEditar == null);
            if (isNew) {
                responsavelParaEditar = new Usuario();
            }

            responsavelParaEditar.setNome(campoNome.getText());
            responsavelParaEditar.setLogin(campoLogin.getText());
            // Só atualiza a senha se o campo foi preenchido
            String senha = new String(campoSenha.getPassword());
            if (!senha.trim().isEmpty()) {
                responsavelParaEditar.setSenha(senha);
            } else if (isNew) {
                throw new IllegalArgumentException("A senha é obrigatória para novos usuários.");
            }

            List<Aluno> alunosSelecionados = listaAlunos.getSelectedValuesList();

            responsavelController.salvar(responsavelParaEditar, alunosSelecionados);
            
            JOptionPane.showMessageDialog(this, "Responsável salvo com sucesso!");
            salvo = true;
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar responsável: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isSalvo() {
        return salvo;
    }
}