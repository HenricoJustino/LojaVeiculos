package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import java.io.IOException;

public class GerenciadorJanela extends Application {

    @Override
    public void start(Stage palcoPrincipal) {
        try {
            palcoPrincipal.setTitle("Sistema de Gerenciamento - Loja de Veículos");

            TabPane painelAbas = new TabPane();

            painelAbas.getTabs().add(criarAba("Veículos", "/visao/VeiculoVisao.fxml"));
            painelAbas.getTabs().add(criarAba("Clientes", "/visao/ClienteVisao.fxml"));
            painelAbas.getTabs().add(criarAba("Funcionários", "/visao/FuncionarioVisao.fxml"));
            painelAbas.getTabs().add(criarAba("Vendas", "/visao/VendaVisao.fxml"));

            painelAbas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

            Scene cena = new Scene(painelAbas, 900, 600);
            palcoPrincipal.setScene(cena);
            palcoPrincipal.centerOnScreen();
            palcoPrincipal.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Tab criarAba(String titulo, String caminhoFxml) {
        Tab aba = new Tab(titulo);
        try {
            Parent conteudo = FXMLLoader.load(getClass().getResource(caminhoFxml));
            aba.setContent(conteudo);
        } catch (IOException | NullPointerException e) {
            javafx.scene.control.Label erroLabel = new javafx.scene.control.Label(
                "Tela de " + titulo + " em desenvolvimento.\nCrie o arquivo em: src/main/resources" + caminhoFxml
            );
            erroLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-padding: 20; -fx-font-size: 14px;");
            aba.setContent(erroLabel);
        }
        return aba;
    }
}