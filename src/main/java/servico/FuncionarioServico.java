package servico;

import dao.FuncionarioDAO;
import modelo.Funcionario;

import java.util.List;

public class FuncionarioServico {

    private final FuncionarioDAO funcionarioDAO;

    public FuncionarioServico() {
        this.funcionarioDAO = new FuncionarioDAO();
    }

    public void adicionar(Funcionario funcionario) {
        if (funcionario == null) {
            throw new IllegalArgumentException("O funcionário não pode ser nulo.");
        }
        funcionarioDAO.adicionar(funcionario);
    }

    public void atualizar(Funcionario funcionario) {
        if (funcionario == null) {
            throw new IllegalArgumentException("O funcionário não pode ser nulo.");
        }
        if (funcionario.getId() == null) {
            throw new IllegalArgumentException("O ID do funcionário é obrigatório para atualização.");
        }
        funcionarioDAO.atualizar(funcionario);
    }

    public void deletar(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        funcionarioDAO.deletar(id);
    }

    public Funcionario buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        return funcionarioDAO.buscarPorId(id);
    }

    public List<Funcionario> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome não pode estar vazio.");
        }
        return funcionarioDAO.buscarPorNome(nome);
    }

    public List<Funcionario> buscarTodos() {
        return funcionarioDAO.buscarTodos();
    }
}