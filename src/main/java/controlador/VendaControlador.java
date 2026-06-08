package controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Cliente;
import modelo.Funcionario;
import modelo.Veiculo;
import modelo.Venda;
import servico.ClienteServico;
import servico.FuncionarioServico;
import servico.VeiculoServico;
import servico.VendaServico;

import java.time.LocalDate;
import java.util.List;

public class VendaControlador {

    // Componentes do formulário (FXML)
    @FXML private ComboBox<Cliente> cbCliente;
    @FXML private ComboBox<Funcionario> cbFuncionario;
    @FXML private ComboBox<Veiculo> cbVeiculo;
    @FXML private DatePicker dpDataVenda;
    @FXML private ComboBox<String> cbFormaPagamento;
    @FXML private TextField txtValorFinal;

    // Componentes da Tabela (FXML)
    @FXML private TableView<Venda> tabelaVendas;
    @FXML private TableColumn<Venda, Long> colId;
    @FXML private TableColumn<Venda, Long> colIdCliente;
    @FXML private TableColumn<Venda, Long> colIdFuncionario;
    @FXML private TableColumn<Venda, Long> colIdVeiculo;
    @FXML private TableColumn<Venda, LocalDate> colData;
    @FXML private TableColumn<Venda, String> colFormaPagamento;
    @FXML private TableColumn<Venda, Double> colValorFinal;

    // Serviços necessários para alimentar os combos e gerenciar as vendas
    private final VendaServico vendaServico = new VendaServico();
    private final ClienteServico clienteServico = new ClienteServico();
    private final FuncionarioServico funcionarioServico = new FuncionarioServico();
    private final VeiculoServico veiculoServico = new VeiculoServico();

    private ObservableList<Venda> obsVendas = FXCollections.observableArrayList();
    private Venda vendaSelecionada;

    @FXML
    public void initialize() {
        // Configura as colunas da tabela
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        colIdFuncionario.setCellValueFactory(new PropertyValueFactory<>("idFuncionario"));
        colIdVeiculo.setCellValueFactory(new PropertyValueFactory<>("idVeiculo"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataVenda"));
        colFormaPagamento.setCellValueFactory(new PropertyValueFactory<>("formaPagamento"));
        colValorFinal.setCellValueFactory(new PropertyValueFactory<>("valorFinal"));

        // Inicializa componentes da tela
        cbFormaPagamento.setItems(FXCollections.observableArrayList("Dinheiro", "Cartão de Crédito", "Cartão de Débito", "Pix", "Financiamento"));
        dpDataVenda.setValue(LocalDate.now());

        carregarComboboxes();
        atualizarTabela();

        // Listener para carregar dados ao clicar na tabela
        tabelaVendas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                carregarVendaParaEdicao(newValue);
            }
        });
    }

    private void carregarComboboxes() {
        try {
            cbCliente.setItems(FXCollections.observableArrayList(clienteServico.buscarTodos()));
            cbFuncionario.setItems(FXCollections.observableArrayList(funcionarioServico.buscarTodos()));
            cbVeiculo.setItems(FXCollections.observableArrayList(veiculoServico.buscarTodos()));
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao carregar dados dos formulários: " + e.getMessage());
        }
    }

    @FXML
    private void acaoSalvar() {
        try {
            if (vendaSelecionada == null) {
                vendaSelecionada = new Venda();
            }

            if (cbCliente.getValue() == null || cbFuncionario.getValue() == null || cbVeiculo.getValue() == null) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro de Validação", "Selecione um Cliente, Funcionário e Veículo.");
                return;
            }

            // Atribui os IDs dos objetos selecionados nos combos
            vendaSelecionada.setIdCliente(cbCliente.getValue().getId());
            vendaSelecionada.setIdFuncionario(cbFuncionario.getValue().getId());
            vendaSelecionada.setIdVeiculo(cbVeiculo.getValue().getId());
            vendaSelecionada.setDataVenda(dpDataVenda.getValue());
            vendaSelecionada.setFormaPagamento(cbFormaPagamento.getValue());

            try {
                vendaSelecionada.setValorFinal(Double.parseDouble(txtValorFinal.getText()));
            } catch (NumberFormatException e) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro de Validação", "O valor final deve ser um número válido.");
                return;
            }

            if (vendaSelecionada.getId() == null) {
                vendaServico.adicionar(vendaSelecionada);
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Venda registrada com sucesso!");
            } else {
                vendaServico.atualizar(vendaSelecionada);
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Venda atualizada com sucesso!");
            }

            limparFormulario();
            atualizarTabela();

        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void acaoExcluir() {
        Venda selecionada = tabelaVendas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Aviso", "Selecione uma venda na tabela para excluir.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir a venda ID: " + selecionada.getId() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                vendaServico.deletar(selecionada.getId());
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Venda excluída com sucesso!");
                limparFormulario();
                atualizarTabela();
            } catch (Exception e) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro ao Excluir", "Não foi possível excluir a venda: " + e.getMessage());
            }
        }
    }

    @FXML
    private void acaoLimpar() {
        limparFormulario();
    }

    private void atualizarTabela() {
        try {
            List<Venda> lista = vendaServico.buscarTodos();
            obsVendas = FXCollections.observableArrayList(lista);
            tabelaVendas.setItems(obsVendas);
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Conexão", "Erro ao carregar vendas: " + e.getMessage());
        }
    }

    private void carregarVendaParaEdicao(Venda venda) {
        this.vendaSelecionada = venda;
        
        // Seleciona de volta nos combos buscando pelos IDs correspondentes
        cbCliente.getItems().stream().filter(c -> c.getId().equals(venda.getIdCliente())).findFirst().ifPresent(cbCliente::setValue);
        cbFuncionario.getItems().stream().filter(f -> f.getId().equals(venda.getIdFuncionario())).findFirst().ifPresent(cbFuncionario::setValue);
        cbVeiculo.getItems().stream().filter(v -> v.getId().equals(venda.getIdVeiculo())).findFirst().ifPresent(cbVeiculo::setValue);
        
        dpDataVenda.setValue(venda.getDataVenda());
        cbFormaPagamento.setValue(venda.getFormaPagamento());
        txtValorFinal.setText(String.valueOf(venda.getValorFinal()));
    }

    private void limparFormulario() {
        cbCliente.setValue(null);
        cbFuncionario.setValue(null);
        cbVeiculo.setValue(null);
        dpDataVenda.setValue(LocalDate.now());
        cbFormaPagamento.setValue(null);
        txtValorFinal.clear();
        vendaSelecionada = null;
        tabelaVendas.getSelectionModel().clearSelection();
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}