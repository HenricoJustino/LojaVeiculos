package modelo;

import java.time.LocalDate;

public class Venda {

    private Long id;
    private Long idCliente;
    private Long idFuncionario;
    private Long idVeiculo;
    private LocalDate dataVenda;
    private String formaPagamento;
    private Double valorFinal;

    public Venda() {
    }

    public Venda(Long id, Long idCliente, Long idFuncionario, Long idVeiculo, 
                 LocalDate dataVenda, String formaPagamento, Double valorFinal) {
        this.id = id;
        this.idCliente = idCliente;
        this.idFuncionario = idFuncionario;
        this.idVeiculo = idVeiculo;
        this.dataVenda = dataVenda;
        this.formaPagamento = formaPagamento;
        this.valorFinal = valorFinal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Long getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(Long idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }
}