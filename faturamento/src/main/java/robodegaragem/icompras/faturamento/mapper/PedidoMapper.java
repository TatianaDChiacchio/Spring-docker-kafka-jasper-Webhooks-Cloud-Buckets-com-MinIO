package robodegaragem.icompras.faturamento.mapper;

import org.springframework.stereotype.Component;
import robodegaragem.icompras.faturamento.model.Cliente;
import robodegaragem.icompras.faturamento.model.ItemPedido;
import robodegaragem.icompras.faturamento.model.Pedido;
import robodegaragem.icompras.faturamento.subscriber.representation.DetalheItemPedidoRepresentation;
import robodegaragem.icompras.faturamento.subscriber.representation.DetalhePedidoRepresentation;

import java.util.List;

@Component
public class PedidoMapper {

    public Pedido map(DetalhePedidoRepresentation representation){
        Cliente cliente = new Cliente(
                representation.nome(), representation.cpf(), representation.logradouro(),
                representation.numero(), representation.bairro(), representation.email(),
                representation.telefone()
        );

        List<ItemPedido> itens = representation.itens()
                .stream().map(this::mapItem).toList();

        return new Pedido(representation.codigo(), cliente,
                representation.dataPedido(), representation.total(), itens );
    }

    private ItemPedido mapItem( DetalheItemPedidoRepresentation representation ){
        return new ItemPedido( representation.codigoProduto(), representation.nome(),
                representation.valorUnitario(), representation.quantidade(),
                representation.total() );
    }
}
