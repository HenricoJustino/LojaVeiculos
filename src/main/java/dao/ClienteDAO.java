package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import conexao.ConexaoBanco;
import modelo.Cliente;

public class ClienteDAO {

    public void adicionar(Cliente cliente) {
        String sql = """
                INSERT INTO cliente (nome, cpf, telefone, email)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setString(1, cliente.getNome());
            declaracao.setString(2, cliente.getCpf());
            declaracao.setString(3, cliente.getTelefone());
            declaracao.setString(4, cliente.getEmail());

            declaracao.executeUpdate();

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
    }

    public void atualizar(Cliente cliente) {
        String sql = """
                UPDATE cliente
                SET nome = ?,
                    cpf = ?,
                    telefone = ?,
                    email = ?
                WHERE id = ?
                """;

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setString(1, cliente.getNome());
            declaracao.setString(2, cliente.getCpf());
            declaracao.setString(3, cliente.getTelefone());
            declaracao.setString(4, cliente.getEmail());
            declaracao.setLong(5, cliente.getId());

            declaracao.executeUpdate();

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setLong(1, id);
            declaracao.executeUpdate();

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
    }

    public Cliente buscarPorId(Long id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setLong(1, id);
            ResultSet resultado = declaracao.executeQuery();

            if (resultado.next()) {
                return mapearCliente(resultado);
            }

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
        return null;
    }

    public List<Cliente> buscarPorNome(String nome) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE nome LIKE ?";

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setString(1, "%" + nome + "%");
            ResultSet resultado = declaracao.executeQuery();

            while (resultado.next()) {
                clientes.add(mapearCliente(resultado));
            }

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
        return clientes;
    }

    public List<Cliente> buscarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conexao = ConexaoBanco.getConexao();
             Statement declaracao = conexao.createStatement();
             ResultSet resultado = declaracao.executeQuery(sql)) {

            while (resultado.next()) {
                clientes.add(mapearCliente(resultado));
            }

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
        return clientes;
    }

    private Cliente mapearCliente(ResultSet resultado) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(resultado.getLong("id"));
        cliente.setNome(resultado.getString("nome"));
        cliente.setCpf(resultado.getString("cpf"));
        cliente.setTelefone(resultado.getString("telefone"));
        cliente.setEmail(resultado.getString("email"));
        return cliente;
    }
}