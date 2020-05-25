package br.fernando.oficinalicaoum.DAO;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import br.fernando.oficinalicaoum.modelo.Pedido;


public class DAO {

    private List<Pedido> pedidos;

    public List<Pedido> adicionaPedido() {
        pedidos = new ArrayList<Pedido>();
        for (int i = 1; i < 11; i++) {
            Random random = new Random();
            Double valor = random.nextDouble() * 1000;
            Pedido pedido = new Pedido(i, valor);
            pedidos.add(pedido);
        }
        return pedidos;
    }


}
