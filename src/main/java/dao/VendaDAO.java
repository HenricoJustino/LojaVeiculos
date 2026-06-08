package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import conexao.ConexaoBanco;
import modelo.Venda;

public class VendaDAO {

    public void adicionar(Venda venda) {
        String sql = """
                INSERT INTO venda (id_cliente, id_funcionario, id_veiculo, data_venda, forma_pagamento, valor_final)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setLong(1, venda.getIdCliente());
            declaracao.setLong(2, venda.getIdFuncionario());
            declaracao.setLong(3, venda.getIdVeiculo());
            declaracao.setDate(4, Date.valueOf(venda.getDataVenda()));
            declaracao.setString(5, venda.getFormaPagamento());
            declaracao.setDouble(6, venda.getValorFinal());

            declaracao.executeUpdate();

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
    }

    public void atualizar(Venda venda) {
        String sql = """
                UPDATE venda
                SET id_cliente = ?,
                    id_funcionario = ?,
                    id_veiculo = ?,
                    data_venda = ?,
                    forma_pagamento = ?,
                    valor_final = ?
                WHERE id = ?
                """;

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setLong(1, venda.getIdCliente());
            declaracao.setLong(2, venda.getIdFuncionario());
            declaracao.setLong(3, venda.getIdVeiculo());
            declaracao.setDate(4, Date.valueOf(venda.getDataVenda()));
            declaracao.setString(5, venda.getFormaPagamento());
            declaracao.setDouble(6, venda.getValorFinal());
            declaracao.setLong(7, venda.getId());

            declaracao.executeUpdate();

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM venda WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setLong(1, id);
            declaracao.executeUpdate();

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
    }

    public Venda buscarPorId(Long id) {
        String sql = "SELECT * FROM venda WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setLong(1, id);
            ResultSet resultado = declaracao.executeQuery();

            if (resultado.next()) {
                return mapearVenda(resultado);
            }

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
        return null;
    }

    public List<Venda> buscarTodos() {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM venda";

        try (Connection conexao = ConexaoBanco.getConexao();
             Statement declaracao = conexao.createStatement();
             ResultSet resultado = declaracao.executeQuery(sql)) {

            while (resultado.next()) {
                vendas.add(mapearVenda(resultado));
            }

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
        return vendas;
    }

    private Venda mapearVenda(ResultSet resultado) throws SQLException {
        Venda venda = new Venda();
        venda.setId(resultado.getLong("id"));
        venda.setIdCliente(resultado.getLong("id_cliente"));
        venda.setIdFuncionario(resultado.getLong("id_funcionario"));
        venda.setIdVeiculo(resultado.getLong("id_veiculo"));
        
        Date data = resultado.getDate("data_venda");
        if (data != null) {
            venda.setDataVenda(data.toLocalDate());
        }
        
        venda.setFormaPagamento(resultado.getString("forma_pagamento"));
        venda.setValorFinal(resultado.getDouble("valor_final"));
        return venda;
    }
}