package controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Funcionario;
import servico.FuncionarioServico;

import java.util.List;

public class FuncionarioControlador {

    // Componentes do formulário (FXML)
    @FXML private TextField txtNome;
    @FXML private TextField txtCpf;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEmail;

    // Componentes da Tabela (FXML)
    @FXML private TableView<Funcionario> tabelaFuncionarios;
    @FXML private TableColumn<Funcionario, Long> colId;
    @FXML private TableColumn<Funcionario, String> colNome;
    @FXML private TableColumn<Funcionario, String> colCpf;
    @FXML private TableColumn<Funcionario, String> colTelefone;
    @FXML private TableColumn<Funcionario, String> colEmail;

    private final FuncionarioServico funcionarioServico = new FuncionarioServico();
    private ObservableList<Funcionario> obsFuncionarios = FXCollections.observableArrayList();
    private Funcionario funcionarioSelecionado;

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
        tabelaFuncionarios.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                carregarFuncionarioParaEdicao(newValue);
            }
        });
    }

    @FXML
    private void acaoSalvar() {
        try {
            if (funcionarioSelecionado == null) {
                funcionarioSelecionado = new Funcionario();
            }

            funcionarioSelecionado.setNome(txtNome.getText());
            funcionarioSelecionado.setCpf(txtCpf.getText());
            funcionarioSelecionado.setTelefone(txtTelefone.getText());
            funcionarioSelecionado.setEmail(txtEmail.getText());

            if (funcionarioSelecionado.getId() == null) {
                funcionarioServico.adicionar(funcionarioSelecionado);
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Funcionário cadastrado com sucesso!");
            } else {
                funcionarioServico.atualizar(funcionarioSelecionado);
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Funcionário atualizado com sucesso!");
            }

            limparFormulario();
            atualizarTabela();

        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    private void acaoExcluir() {
        Funcionario selecionado = tabelaFuncionarios.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Aviso", "Selecione um funcionário na tabela para excluir.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir o funcionário " + selecionado.getNome() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                funcionarioServico.deletar(selecionado.getId());
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Funcionário excluído com sucesso!");
                limparFormulario();
                atualizarTabela();
            } catch (Exception e) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro ao Excluir", "Não foi possível excluir o funcionário: " + e.getMessage());
            }
        }
    }

    @FXML
    private void acaoLimpar() {
        limparFormulario();
    }

    private void atualizarTabela() {
        try {
            List<Funcionario> lista = funcionarioServico.buscarTodos();
            obsFuncionarios = FXCollections.observableArrayList(lista);
            tabelaFuncionarios.setItems(obsFuncionarios);
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Conexão", "Erro ao carregar funcionários: " + e.getMessage());
        }
    }

    private void carregarFuncionarioParaEdicao(Funcionario funcionario) {
        this.funcionarioSelecionado = funcionario;
        txtNome.setText(funcionario.getNome());
        txtCpf.setText(funcionario.getCpf());
        txtTelefone.setText(funcionario.getTelefone());
        txtEmail.setText(funcionario.getEmail());
    }

    private void limparFormulario() {
        txtNome.clear();
        txtCpf.clear();
        txtTelefone.clear();
        txtEmail.clear();
        funcionarioSelecionado = null;
        tabelaFuncionarios.getSelectionModel().clearSelection();
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}