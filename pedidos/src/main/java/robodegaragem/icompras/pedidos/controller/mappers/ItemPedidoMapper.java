package robodegaragem.icompras.pedidos.controller.mappers;

import org.mapstruct.Mapper;
import robodegaragem.icompras.pedidos.controller.dto.ItemPedidoDTO;
import robodegaragem.icompras.pedidos.model.ItemPedido;


@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {

    default ItemPedido map(ItemPedidoDTO dto) {
        if (dto == null) return null;
        ItemPedido item = new ItemPedido();
        item.setCodigoProduto(dto.codigoProduto());
        item.setNome(dto.nome());
        item.setQuantidade(dto.quantidade());
        item.setValorUnitario(dto.valorUnitario());
        return item;
    }
}
