package servico;

import dao.VeiculoDAO;
import modelo.Veiculo;

import java.util.List;

public class VeiculoServico {

    private final VeiculoDAO veiculoDAO;

    public VeiculoServico() {
        this.veiculoDAO = new VeiculoDAO();
    }

    public void adicionar(Veiculo veiculo) {

        if (veiculo == null) {
            throw new IllegalArgumentException("O veículo não pode ser nulo.");
        }

        veiculoDAO.adicionar(veiculo);
    }

    public void atualizar(Veiculo veiculo) {

        if (veiculo == null) {
            throw new IllegalArgumentException("O veículo não pode ser nulo.");
        }

        if (veiculo.getId() == null) {
            throw new IllegalArgumentException("O ID do veículo é obrigatório.");
        }

        veiculoDAO.atualizar(veiculo);
    }

    public void deletar(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        veiculoDAO.deletar(id);
    }

    public Veiculo buscarPorId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        return veiculoDAO.buscarPorId(id);
    }

    public List<Veiculo> buscarPorModelo(String modelo) {

        if (modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException("O modelo não pode estar vazio.");
        }

        return veiculoDAO.buscarPorModelo(modelo);
    }

    public List<Veiculo> buscarTodos() {
        return veiculoDAO.buscarTodos();
    }
}