package br.fernando.oficinalicaoum.modelo;

public class Pedido {

    private Integer id;
    private Double valor;

    public Pedido(Integer id, Double valor){
        this.id = id;
        this.valor= valor;
    }
    public Pedido(){

    }

    public Integer getId() {
        return id;
    }

    public Double getValor() {
        return valor;
    }
}
