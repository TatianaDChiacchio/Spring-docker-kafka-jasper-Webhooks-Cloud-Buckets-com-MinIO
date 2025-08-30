package robodegaragem.icompras.pedidos.publisher;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import robodegaragem.icompras.pedidos.controller.mappers.ItemPedidoMapper;
import robodegaragem.icompras.pedidos.model.Pedido;
import robodegaragem.icompras.pedidos.publisher.representation.DetalhePedidoRepresentation;

@Mapper(componentModel = "spring", uses = {ItemPedidoMapper.class})
public interface DetalhePedidoMapper {

    @Mapping(source = "dadosCliente.nome", target = "nome")
    @Mapping(source = "dadosCliente.cpf", target = "cpf")
    @Mapping(source = "dadosCliente.logradouro", target = "logradouro")
    @Mapping(source = "dadosCliente.numero", target = "numero")
    @Mapping(source = "dadosCliente.bairro", target = "bairro")
    @Mapping(source = "dadosCliente.email", target = "email")
    @Mapping(source = "dadosCliente.telefone", target = "telefone")
    @Mapping(source = "dataPedido", target = "dataPedido", dateFormat = "yyyy-MM-dd")
        // As propriedades 'codigo', 'codigoCliente', 'total', 'status', 'urlNotaFiscal', 'codigoRastreio'
        // serão mapeadas implicitamente devido aos nomes e tipos compatíveis.
        // A propriedade 'itens' será mapeada automaticamente usando o ItemPedidoMapper.
    DetalhePedidoRepresentation map(Pedido pedido);
}