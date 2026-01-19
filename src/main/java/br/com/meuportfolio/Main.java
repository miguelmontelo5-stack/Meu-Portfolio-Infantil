package br.com.meuportfolio;

import br.com.meuportfolio.ui.TelaSelecaoUsuario;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
       
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               new TelaSelecaoUsuario().setVisible(true);
            }
        });
    }
}