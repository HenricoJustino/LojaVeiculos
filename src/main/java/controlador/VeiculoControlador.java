package controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Veiculo;
import servico.VeiculoServico;

import java.util.List;

public class VeiculoControlador {

    // Componentes do formulário (FXML)
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtCor;
    @FXML private TextField txtAno;
    @FXML private TextField txtPreco;
    @FXML private ComboBox<String> cbStatus;

    // Componentes da Tabela (FXML)
    @FXML private TableView<Veiculo> tabelaVeiculos;
    @FXML private TableColumn<Veiculo, Long> colId;
    @FXML private TableColumn<Veiculo, String> colMarca;
    @FXML private TableColumn<Veiculo, String> colModelo;
    @FXML private TableColumn<Veiculo, String> colCor;
    @FXML private TableColumn<Veiculo, Integer> colAno;
    @FXML private TableColumn<Veiculo, Double> colPreco;
    @FXML private TableColumn<Veiculo, String> colStatus;

    // Instância da camada de serviço
    private final VeiculoServico veiculoServico = new VeiculoServico();
    
    // Lista observável que alimenta a tabela do JavaFX
    private ObservableList<Veiculo> obsVeiculos = FXCollections.observableArrayList();
    
    // Objeto auxiliar para saber se estamos editando ou criando um novo
    private Veiculo veiculoSelecionado;

    /**
     * O método initialize() roda automaticamente assim que a tela FXML é carregada.
     */
    @FXML
    public void initialize() {
        // 1. Configura as colunas da tabela com os atributos do Modelo
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colCor.setCellValueFactory(new PropertyValueFactory<>("cor"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("ano"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // 2. Preenche as opções do ComboBox de Status
        cbStatus.setItems(FXCollections.observableArrayList("Disponível", "Vendido", "Em Manutenção"));
        cbStatus.setValue("Disponível"); // Valor padrão

        // 3. Carrega os dados do banco na tabela
        atualizarTabela();

        // 4. Escuta cliques na tabela para carregar os dados de volta para o formulário (Edição)
        tabelaVeiculos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                carregarVeiculoParaEdicao(newValue);
            }
        });
    }

    /**
     * Ação do botão "Salvar" (Serve tanto para Salvar Novo quanto para Atualizar Existente)
     */
    @FXML
    private void acaoSalvar() {
        try {
            if (veiculoSelecionado == null) {
                veiculoSelecionado = new Veiculo();
            }

            veiculoSelecionado.setMarca(txtMarca.getText());
            veiculoSelecionado.setModelo(txtModelo.getText());
            veiculoSelecionado.setCor(txtCor.getText());
            
            try {
                veiculoSelecionado.setAno(Integer.parseInt(txtAno.getText()));
                veiculoSelecionado.setPreco(Double.parseDouble(txtPreco.getText()));
            } catch (NumberFormatException e) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro de Validação", "Ano e Preço devem ser valores numéricos válidos.");
                return;
            }
            
            veiculoSelecionado.setStatus(cbStatus.getValue());

            if (veiculoSelecionado.getId() == null) {
                veiculoServico.adicionar(veiculoSelecionado);
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Veículo cadastrado com sucesso!");
            } else {
                veiculoServico.atualizar(veiculoSelecionado);
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Veículo atualizado com sucesso!");
            }

            limparFormulario();
            atualizarTabela();

        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    /**
     * Ação do botão "Excluir"
     */
    @FXML
    private void acaoExcluir() {
        Veiculo selecionado = tabelaVeiculos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Aviso", "Selecione um veículo na tabela para excluir.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir o veículo " + selecionado.getModelo() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                veiculoServico.deletar(selecionado.getId());
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Veículo excluído com sucesso!");
                limparFormulario();
                atualizarTabela();
            } catch (Exception e) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro ao Excluir", "Não foi possível excluir o veículo: " + e.getMessage());
            }
        }
    }

    /**
     * Ação do botão "Limpar" ou Cancelar
     */
    @FXML
    private void acaoLimpar() {
        limparFormulario();
    }

    /**
     * Busca os dados atualizados do banco através do Serviço e joga na tela
     */
    private void atualizarTabela() {
        try {
            List<Veiculo> lista = veiculoServico.buscarTodos();
            obsVeiculos = FXCollections.observableArrayList(lista);
            tabelaVeiculos.setItems(obsVeiculos);
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Conexão", "Erro ao carregar veículos do banco: " + e.getMessage());
        }
    }

    /**
     * Preenche os campos do formulário para permitir alteração
     */
    private void carregarVeiculoParaEdicao(Veiculo veiculo) {
        this.veiculoSelecionado = veiculo;
        txtMarca.setText(veiculo.getMarca());
        txtModelo.setText(veiculo.getModelo());
        txtCor.setText(veiculo.getCor());
        txtAno.setText(String.valueOf(veiculo.getAno()));
        txtPreco.setText(String.valueOf(veiculo.getPreco()));
        cbStatus.setValue(veiculo.getStatus());
    }

    /**
     * Reseta o formulário para o estado inicial
     */
    private void limparFormulario() {
        txtMarca.clear();
        txtModelo.clear();
        txtCor.clear();
        txtAno.clear();
        txtPreco.clear();
        cbStatus.setValue("Disponível");
        veiculoSelecionado = null;
        tabelaVeiculos.getSelectionModel().clearSelection();
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}