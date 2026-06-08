package servico;

import dao.VendaDAO;
import modelo.Venda;

import java.util.List;

public class VendaServico {

    private final VendaDAO vendaDAO;

    public VendaServico() {
        this.vendaDAO = new VendaDAO();
    }

    public void adicionar(Venda venda) {
        if (venda == null) {
            throw new IllegalArgumentException("A venda não pode ser nula.");
        }
        if (venda.getIdCliente() == null || venda.getIdFuncionario() == null || venda.getIdVeiculo() == null) {
            throw new IllegalArgumentException("Cliente, Funcionário e Veículo são obrigatórios.");
        }
        if (venda.getDataVenda() == null) {
            throw new IllegalArgumentException("A data da venda é obrigatória.");
        }
        if (venda.getValorFinal() == null || venda.getValorFinal() <= 0) {
            throw new IllegalArgumentException("O valor final deve ser maior que zero.");
        }
        vendaDAO.adicionar(venda);
    }

    public void atualizar(Venda venda) {
        if (venda == null) {
            throw new IllegalArgumentException("A venda não pode ser nula.");
        }
        if (venda.getId() == null) {
            throw new IllegalArgumentException("O ID da venda é obrigatório para atualização.");
        }
        vendaDAO.atualizar(venda);
    }

    public void deletar(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        vendaDAO.deletar(id);
    }

    public Venda buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        return vendaDAO.buscarPorId(id);
    }

    public List<Venda> buscarTodos() {
        return vendaDAO.buscarTodos();
    }
}