package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import conexao.ConexaoBanco;
import modelo.Funcionario;

public class FuncionarioDAO {

    public void adicionar(Funcionario funcionario) {
        String sql = """
                INSERT INTO funcionario (nome, cpf, telefone, email)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setString(1, funcionario.getNome());
            declaracao.setString(2, funcionario.getCpf());
            declaracao.setString(3, funcionario.getTelefone());
            declaracao.setString(4, funcionario.getEmail());

            declaracao.executeUpdate();

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
    }

    public void atualizar(Funcionario funcionario) {
        String sql = """
                UPDATE funcionario
                SET nome = ?,
                    cpf = ?,
                    telefone = ?,
                    email = ?
                WHERE id = ?
                """;

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setString(1, funcionario.getNome());
            declaracao.setString(2, funcionario.getCpf());
            declaracao.setString(3, funcionario.getTelefone());
            declaracao.setString(4, funcionario.getEmail());
            declaracao.setLong(5, funcionario.getId());

            declaracao.executeUpdate();

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM funcionario WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setLong(1, id);
            declaracao.executeUpdate();

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
    }

    public Funcionario buscarPorId(Long id) {
        String sql = "SELECT * FROM funcionario WHERE id = ?";

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setLong(1, id);
            ResultSet resultado = declaracao.executeQuery();

            if (resultado.next()) {
                return mapearFuncionario(resultado);
            }

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
        return null;
    }

    public List<Funcionario> buscarPorNome(String nome) {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM funcionario WHERE nome LIKE ?";

        try (Connection conexao = ConexaoBanco.getConexao();
             PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setString(1, "%" + nome + "%");
            ResultSet resultado = declaracao.executeQuery();

            while (resultado.next()) {
                funcionarios.add(mapearFuncionario(resultado));
            }

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
        return funcionarios;
    }

    public List<Funcionario> buscarTodos() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM funcionario";

        try (Connection conexao = ConexaoBanco.getConexao();
             Statement declaracao = conexao.createStatement();
             ResultSet resultado = declaracao.executeQuery(sql)) {

            while (resultado.next()) {
                funcionarios.add(mapearFuncionario(resultado));
            }

        } catch (SQLException excecao) {
            excecao.printStackTrace();
        }
        return funcionarios;
    }

    private Funcionario mapearFuncionario(ResultSet resultado) throws SQLException {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(resultado.getLong("id"));
        funcionario.setNome(resultado.getString("nome"));
        funcionario.setCpf(resultado.getString("cpf"));
        funcionario.setTelefone(resultado.getString("telefone"));
        funcionario.setEmail(resultado.getString("email"));
        return funcionario;
    }
}