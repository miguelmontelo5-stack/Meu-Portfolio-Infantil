package br.com.meuportfolio.ui;

import br.com.meuportfolio.controller.MidiaController;
import br.com.meuportfolio.model.Midia;
import br.com.meuportfolio.model.Turma;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TelaGerenciamentoPortfolioTurma extends JDialog {

    private Turma turma;
    private MidiaController midiaController;
    private JPanel painelGaleria;

    public TelaGerenciamentoPortfolioTurma(Frame owner, Turma turma) {
        super(owner, "Portfólio da Turma: " + turma.getNome(), true);
        this.turma = turma;
        this.midiaController = new MidiaController();

        setSize(800, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoAdicionar = new JButton("Adicionar Mídia à Turma");
        painelBotoes.add(botaoAdicionar);
        add(painelBotoes, BorderLayout.NORTH);

        painelGaleria = new JPanel(new WrapLayout(FlowLayout.LEFT, 15, 15));
        JScrollPane scrollPane = new JScrollPane(painelGaleria);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel painelFechar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botaoFechar = new JButton("Fechar");
        painelFechar.add(botaoFechar);
        add(painelFechar, BorderLayout.SOUTH);

        botaoAdicionar.addActionListener(e -> adicionarNovaMidia());
        botaoFechar.addActionListener(e -> dispose());

        carregarMidias();
    }

    private void carregarMidias() {
        painelGaleria.removeAll();
        try {
            List<Midia> midias = midiaController.listarPorTurma(turma.getId());
            for (Midia midia : midias) {
                painelGaleria.add(criarCardMidia(midia));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar mídias da turma.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        painelGaleria.revalidate();
        painelGaleria.repaint();
    }
    
    private void adicionarNovaMidia() {
        JFileChooser seletorArquivo = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imagens e Documentos", "jpg", "jpeg", "png", "gif", "pdf", "docx");
        seletorArquivo.setFileFilter(filtro);
        
        int resultado = seletorArquivo.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = seletorArquivo.getSelectedFile();
            String descricao = JOptionPane.showInputDialog(this, "Digite a descrição para esta mídia:");
            
            if (descricao != null && !descricao.trim().isEmpty()) {
                try {
                    Midia novaMidia = new Midia();
                    novaMidia.setDescricao(descricao);
                    novaMidia.setTurma(this.turma); // Vincula a mídia à TURMA
                    
                    String nomeArquivo = arquivoSelecionado.getName().toLowerCase();
                    if(nomeArquivo.endsWith(".jpg") || nomeArquivo.endsWith(".png") || nomeArquivo.endsWith(".gif")) {
                        novaMidia.setTipoMidia(Midia.TipoMidia.FOTO);
                    } else { 
                        novaMidia.setTipoMidia(Midia.TipoMidia.AUDIO);
                    }

                    midiaController.salvar(novaMidia, arquivoSelecionado);
                    JOptionPane.showMessageDialog(this, "Mídia salva com sucesso!");
                    carregarMidias();

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao salvar mídia: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private JPanel criarCardMidia(Midia midia) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEtchedBorder());
        card.setPreferredSize(new Dimension(180, 220));

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

        JPanel painelAcoesCard = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JButton botaoEditar = new JButton("Editar");
        JButton botaoExcluir = new JButton("Excluir");
        painelAcoesCard.add(botaoEditar);
        painelAcoesCard.add(botaoExcluir);
        card.add(painelAcoesCard, BorderLayout.NORTH);
        
        botaoExcluir.addActionListener(e -> excluirMidia(midia));
        botaoEditar.addActionListener(e -> editarDescricaoMidia(midia));
        
        labelImagem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                visualizarMidia(midia);
            }
        });

        return card;
    }
    
    private void excluirMidia(Midia midia) {
        Object[] options = {"Sim, excluir", "Não"};
        int confirmacao = JOptionPane.showOptionDialog(this,
                "Tem certeza que deseja excluir esta mídia?", "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
        
        if (confirmacao == 0) {
            try {
                midiaController.excluir(midia.getId());
                JOptionPane.showMessageDialog(this, "Mídia excluída com sucesso!");
                carregarMidias();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao excluir mídia: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarDescricaoMidia(Midia midia) {
        String novaDescricao = JOptionPane.showInputDialog(this, "Edite a descrição:", midia.getDescricao());
        if (novaDescricao != null && !novaDescricao.trim().equals(midia.getDescricao())) {
            try {
                midiaController.atualizarDescricao(midia.getId(), novaDescricao);
                JOptionPane.showMessageDialog(this, "Descrição atualizada com sucesso!");
                carregarMidias();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao atualizar descrição: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
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