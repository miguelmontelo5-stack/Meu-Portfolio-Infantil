package br.com.meuportfolio.ui;

import br.com.meuportfolio.controller.MidiaController;
import br.com.meuportfolio.model.Aluno;
import br.com.meuportfolio.model.Midia;
import br.com.meuportfolio.model.Turma;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Tela de SOMENTE LEITURA para o Responsável visualizar os portfólios.
 */
public class TelaVisualizacaoPortfolio extends JDialog {

    private MidiaController midiaController;
    private JPanel painelGaleria;
    private Aluno aluno; // Pode ser nulo se o portfólio for da turma
    private Turma turma; // Pode ser nulo se o portfólio for do aluno

    // Construtor para o portfólio do ALUNO
    public TelaVisualizacaoPortfolio(Dialog owner, Aluno aluno) {
        super(owner, "Portfólio de " + aluno.getNome(), true);
        this.aluno = aluno;
        this.midiaController = new MidiaController();
        initComponents();
        carregarMidias();
    }

    // Construtor para o portfólio da TURMA
    public TelaVisualizacaoPortfolio(Dialog owner, Turma turma) {
        super(owner, "Portfólio da Turma: " + turma.getNome(), true);
        this.turma = turma;
        this.midiaController = new MidiaController();
        initComponents();
        carregarMidias();
    }

    private void initComponents() {
        setSize(800, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // Título da Janela (adicionado para clareza)
        JLabel labelTitulo = new JLabel(getTitle(), SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(labelTitulo, BorderLayout.NORTH);

        painelGaleria = new JPanel(new WrapLayout(FlowLayout.LEFT, 15, 15));
        JScrollPane scrollPane = new JScrollPane(painelGaleria);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel painelFechar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botaoFechar = new JButton("Fechar");
        painelFechar.add(botaoFechar);
        add(painelFechar, BorderLayout.SOUTH);

        botaoFechar.addActionListener(e -> dispose());
    }

    private void carregarMidias() {
        painelGaleria.removeAll();
        try {
            List<Midia> midias;
            if (aluno != null) {
                midias = midiaController.listarPorAluno(aluno.getId());
            } else {
                midias = midiaController.listarPorTurma(turma.getId());
            }

            for (Midia midia : midias) {
                painelGaleria.add(criarCardMidia(midia));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar mídias.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        painelGaleria.revalidate();
        painelGaleria.repaint();
    }
    
    private JPanel criarCardMidia(Midia midia) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEtchedBorder());
        card.setPreferredSize(new Dimension(180, 180));

        JLabel labelImagem;
        if (midia.getTipoMidia() == Midia.TipoMidia.FOTO) {
            ImageIcon icone = new ImageIcon(midia.getCaminhoArquivo());
            Image imagemPequena = icone.getImage().getScaledInstance(170, 120, Image.SCALE_SMOOTH);
            labelImagem = new JLabel(new ImageIcon(imagemPequena));
        } else {
            Icon iconeGenerico = UIManager.getIcon("FileView.fileIcon");
            labelImagem = new JLabel(iconeGenerico);
            labelImagem.setHorizontalAlignment(SwingConstants.CENTER);
            labelImagem.setText(new File(midia.getCaminhoArquivo()).getName());
            labelImagem.setVerticalTextPosition(SwingConstants.BOTTOM);
            labelImagem.setHorizontalTextPosition(SwingConstants.CENTER);
        }
        
        labelImagem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.add(labelImagem, BorderLayout.CENTER);
        
        JTextArea textoDescricao = new JTextArea(midia.getDescricao());
        textoDescricao.setLineWrap(true);
        textoDescricao.setWrapStyleWord(true);
        textoDescricao.setEditable(false);
        card.add(new JScrollPane(textoDescricao), BorderLayout.SOUTH);
        
        labelImagem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                visualizarMidia(midia);
            }
        });

        return card;
    }

    private void visualizarMidia(Midia midia) {
        if (midia.getTipoMidia() == Midia.TipoMidia.FOTO) {
            ImageIcon iconeOriginal = new ImageIcon(midia.getCaminhoArquivo());
            JDialog dialogoImagem = new JDialog(this, "Visualizar Mídia", true);
            JLabel labelImagemGrande = new JLabel(iconeOriginal);
            dialogoImagem.add(new JScrollPane(labelImagemGrande));
            dialogoImagem.pack();
            dialogoImagem.setLocationRelativeTo(this);
            dialogoImagem.setVisible(true);
        } else {
            try {
                Desktop.getDesktop().open(new File(midia.getCaminhoArquivo()));
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Não foi possível abrir o arquivo.\nVerifique se você tem um programa instalado para abrir este tipo de arquivo.",
                    "Erro ao Abrir",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}