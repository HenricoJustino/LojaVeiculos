package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {

    // URL de conexão apontando para o seu banco 'loja_veiculos'
    private static final String URL = "jdbc:mysql://localhost:3306/loja_veiculos";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    /**
     * Estabelece e retorna uma conexão com o banco de dados MySQL.
     * @return Connection objeto de conexão ativa
     * @throws SQLException caso ocorra algum erro na conexão
     */
    public static Connection getConexao() throws SQLException {
        try {
            // Garante o carregamento do driver do MySQL (boa prática em projetos JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver do MySQL não foi encontrado no projeto.", e);
        }
    }
}