package controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Cliente;
import servico.ClienteServico;

import java.util.List;

public class ClienteControlador {

    // Componentes do formulário (FXML)
    @FXML private TextField txtNome;
    @FXML private TextField txtCpf;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEmail;

    // Componentes da Tabela (FXML)
    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableColumn<Cliente, Long> colId;
    @FXML private TableColumn<Cliente, String> colNome;
    @FXML private TableColumn<Cliente, String> colCpf;
    @FXML private TableColumn<Cliente, String> colTelefone;
    @FXML private TableColumn<Cliente, String> colEmail;

    private final ClienteServico clienteServico = new ClienteServico();
    private ObservableList<Cliente> obsClientes = FXCollections.observableArrayList();
    private Cliente clienteSelecionado;

    @FXML
    public void initialize() {
        // Configura as colunas da tabela
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        atualizarTabela();

        // Listener para carregar dados ao clicar na linha da tabela
        tabelaClientes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                carregarClienteParaEdicao(newValue);
            }
        });
    }

    @FXML
    private void acaoSalvar() {
        try {
            if (clienteSelecionado == null) {
                clienteSelecionado = new Cliente();
            }

            clienteSelecionado.setNome(txtNome.getText());
            clienteSelecionado.setCpf(txtCpf.getText());
            clienteSelecionado.setTelefone(txtTelefone.getText());
            clienteSelecionado.setEmail(txtEmail.getText());

            if (clienteSelecionado.getId() == null) {
                clienteServico.adicionar(clienteSelecionado);
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Cliente cadastrado com sucesso!");
            } else {
                clienteServico.atualizar(clienteSelecionado);
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Cliente atualizado com sucesso!");
            }

            limparFormulario();
            atualizarTabela();

        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void acaoExcluir() {
        Cliente selecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Aviso", "Selecione um cliente na tabela para excluir.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir o cliente " + selecionado.getNome() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                clienteServico.deletar(selecionado.getId());
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Cliente excluído com sucesso!");
                limparFormulario();
                atualizarTabela();
            } catch (Exception e) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro ao Excluir", "Não foi possível excluir o cliente: " + e.getMessage());
            }
        }
    }

    @FXML
    private void acaoLimpar() {
        limparFormulario();
    }

    private void atualizarTabela() {
        try {
            List<Cliente> lista = clienteServico.buscarTodos();
            obsClientes = FXCollections.observableArrayList(lista);
            tabelaClientes.setItems(obsClientes);
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Conexão", "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void carregarClienteParaEdicao(Cliente cliente) {
        this.clienteSelecionado = cliente;
        txtNome.setText(cliente.getNome());
        txtCpf.setText(cliente.getCpf());
        txtTelefone.setText(cliente.getTelefone());
        txtEmail.setText(cliente.getEmail());
    }

    private void limparFormulario() {
        txtNome.clear();
        txtCpf.clear();
        txtTelefone.clear();
        txtEmail.clear();
        clienteSelecionado = null;
        tabelaClientes.getSelectionModel().clearSelection();
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}