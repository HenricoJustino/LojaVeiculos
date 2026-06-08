package modelo;

public class Veiculo {

    private Long id;
    private String marca;
    private String modelo;
    private String cor;
    private Integer ano;
    private Double preco;
    private String status;

    public Veiculo() {
    }

    public Veiculo(Long id, String marca, String modelo,
                   String color, Integer ano,
                   Double preco, String status) {

        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.cor = color;
        this.ano = ano;
        this.preco = preco;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}