package main;

import javafx.application.Application;

public class Main {
    
    public static void main(String[] args) {
        // Chama o ciclo de vida do JavaFX apontando para a classe da janela
        Application.launch(GerenciadorJanela.class, args);
    }
}