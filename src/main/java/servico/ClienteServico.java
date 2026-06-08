package servico;

import dao.ClienteDAO;
import modelo.Cliente;

import java.util.List;

public class ClienteServico {

    private final ClienteDAO clienteDAO;

    public ClienteServico() {
        this.clienteDAO = new ClienteDAO();
    }

    public void adicionar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("O cliente não pode ser nulo.");
        }
        clienteDAO.adicionar(cliente);
    }

    public void atualizar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("O cliente não pode ser nulo.");
        }
        if (cliente.getId() == null) {
            throw new IllegalArgumentException("O ID do cliente é obrigatório para atualização.");
        }
        clienteDAO.atualizar(cliente);
    }

    public void deletar(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        clienteDAO.deletar(id);
    }

    public Cliente buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        return clienteDAO.buscarPorId(id);
    }

    public List<Cliente> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome não pode estar vazio.");
        }
        return clienteDAO.buscarPorNome(nome);
    }

    public List<Cliente> buscarTodos() {
        return clienteDAO.buscarTodos();
    }
}