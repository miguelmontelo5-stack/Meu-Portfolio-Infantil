package br.com.meuportfolio.controller;

import br.com.meuportfolio.dao.MidiaDAO;
import br.com.meuportfolio.dao.MidiaDAOImpl;
import br.com.meuportfolio.model.Aluno;
import br.com.meuportfolio.model.Midia;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

public class MidiaController {

    private MidiaDAO midiaDAO;
    private static final String PASTA_MIDIAS = "C:/portfolio_midias/";

    public MidiaController() {
        this.midiaDAO = new MidiaDAOImpl();
        new File(PASTA_MIDIAS).mkdirs();
    }

    public List<Midia> listarPorAluno(int alunoId) throws Exception {
        return midiaDAO.listarPorAluno(alunoId);
    }
    
    public List<Midia> listarPorTurma(int turmaId) throws Exception {
        return midiaDAO.listarPorTurma(turmaId);
    }

    public void salvar(Midia midia, File arquivoOriginal) throws Exception {
        if (arquivoOriginal == null || !arquivoOriginal.exists()) {
            throw new IllegalArgumentException("O arquivo original não foi selecionado ou não existe.");
        }
        if (midia.getDescricao() == null || midia.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição é obrigatória.");
        }

        String nomeArquivo = System.currentTimeMillis() + "_" + arquivoOriginal.getName();
        Path caminhoDestino = Paths.get(PASTA_MIDIAS + nomeArquivo);
        Files.copy(arquivoOriginal.toPath(), caminhoDestino, StandardCopyOption.REPLACE_EXISTING);

        midia.setCaminhoArquivo(caminhoDestino.toString());
        midia.setDataEnvio(LocalDate.now());

        midiaDAO.salvar(midia);
    }
    
    public void atualizarDescricao(int midiaId, String novaDescricao) throws Exception {
        if (novaDescricao == null || novaDescricao.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição não pode ser vazia.");
        }
        Midia midia = midiaDAO.buscarPorId(midiaId);
        if (midia != null) {
            midia.setDescricao(novaDescricao);
            midiaDAO.atualizar(midia);
        } else {
            throw new IllegalArgumentException("Mídia não encontrada para atualização.");
        }
    }

    public void excluir(int midiaId) throws Exception {
        Midia midia = midiaDAO.buscarPorId(midiaId);
        if (midia != null) {
            Files.deleteIfExists(Paths.get(midia.getCaminhoArquivo()));
            midiaDAO.excluir(midiaId);
        } else {
            throw new IllegalArgumentException("Mídia não encontrada para exclusão.");
        }
    }
}