package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import conexao.ConexaoBanco;
import modelo.Veiculo;

public class VeiculoDAO {

    public void adicionar(Veiculo veiculo) {

        String sql = """
                INSERT INTO veiculo
                (marca, modelo, cor, ano, preco, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao =
                     conexao.prepareStatement(sql)) {

            declaracao.setString(1, veiculo.getMarca());
            declaracao.setString(2, veiculo.getModelo());
            declaracao.setString(3, veiculo.getCor());
            declaracao.setInt(4, veiculo.getAno());
            declaracao.setDouble(5, veiculo.getPreco());
            declaracao.setString(6, veiculo.getStatus());

            declaracao.executeUpdate();

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
    }

    public void atualizar(Veiculo veiculo) {

        String sql = """
                UPDATE veiculo
                SET marca = ?,
                    modelo = ?,
                    cor = ?,
                    ano = ?,
                    preco = ?,
                    status = ?
                WHERE id = ?
                """;

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao =
                     conexao.prepareStatement(sql)) {

            declaracao.setString(1, veiculo.getMarca());
            declaracao.setString(2, veiculo.getModelo());
            declaracao.setString(3, veiculo.getCor());
            declaracao.setInt(4, veiculo.getAno());
            declaracao.setDouble(5, veiculo.getPreco());
            declaracao.setString(6, veiculo.getStatus());
            declaracao.setLong(7, veiculo.getId());

            declaracao.executeUpdate();

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
    }

    public void deletar(Long id) {

        String sql = "DELETE FROM veiculo WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao =
                     conexao.prepareStatement(sql)) {

            declaracao.setLong(1, id);

            declaracao.executeUpdate();

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
    }

    public Veiculo buscarPorId(Long id) {

        String sql = "SELECT * FROM veiculo WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao =
                     conexao.prepareStatement(sql)) {

            declaracao.setLong(1, id);

            ResultSet resultado = declaracao.executeQuery();

            if (resultado.next()) {
                return mapearVeiculo(resultado);
            }

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }

        return null;
    }

    public List<Veiculo> buscarPorModelo(String modelo) {

        List<Veiculo> veiculos = new ArrayList<>();

        String sql =
                "SELECT * FROM veiculo WHERE modelo LIKE ?";

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao =
                     conexao.prepareStatement(sql)) {

            declaracao.setString(1, "%" + modelo + "%");

            ResultSet resultado = declaracao.executeQuery();

            while (resultado.next()) {
                veiculos.add(mapearVeiculo(resultado));
            }

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }

        return veiculos;
    }

    public List<Veiculo> buscarTodos() {

        List<Veiculo> veiculos = new ArrayList<>();

        String sql = "SELECT * FROM veiculo";

        try (Connection conexao = ConexaoBanco.getConexao();
             Statement declaracao =
                     conexao.createStatement();
             ResultSet resultado =
                     declaracao.executeQuery(sql)) {

            while (resultado.next()) {
                veiculos.add(mapearVeiculo(resultado));
            }

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }

        return veiculos;
    }

    private Veiculo mapearVeiculo(ResultSet resultado)
            throws SQLException {

        Veiculo veiculo = new Veiculo();

        veiculo.setId(resultado.getLong("id"));
        veiculo.setMarca(resultado.getString("marca"));
        veiculo.setModelo(resultado.getString("modelo"));
        veiculo.setCor(resultado.getString("cor"));
        veiculo.setAno(resultado.getInt("ano"));
        veiculo.setPreco(resultado.getDouble("preco"));
        veiculo.setStatus(resultado.getString("status"));

        return veiculo;
    }
}