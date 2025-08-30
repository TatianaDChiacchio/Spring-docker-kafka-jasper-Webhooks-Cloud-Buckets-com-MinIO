package robodegaragem.icompras.pedidos.controller.mappers;

import robodegaragem.icompras.pedidos.controller.dto.DadosPagamentoDTO;
import robodegaragem.icompras.pedidos.controller.dto.ItemPedidoDTO;
import robodegaragem.icompras.pedidos.controller.dto.NovoPedidoDTO;
import robodegaragem.icompras.pedidos.model.ItemPedido;
import robodegaragem.icompras.pedidos.model.PagamentoPedido;
import robodegaragem.icompras.pedidos.model.Pedido;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import robodegaragem.icompras.pedidos.model.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.mapstruct.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", uses = { ItemPedidoMapper.class })
public interface PedidoMapper {

    @Mapping(target = "pagamento", source = "dadosPagamento", qualifiedByName = "mapPagamento")
    @Mapping(target = "codigoCliente", ignore = true) // vai ser preenchido no afterMapping
    Pedido map(NovoPedidoDTO dto);

    @Named("mapPagamento")
    static PagamentoPedido mapPagamento(DadosPagamentoDTO dto) {
        if (dto == null) return null;
        PagamentoPedido pagamento = new PagamentoPedido();
        pagamento.setFormaPagamento(dto.formaPagamento());
        pagamento.setNumeroCartao(dto.numeroCartao());
        return pagamento;
    }

    @AfterMapping
    default void afterMapping(@MappingTarget Pedido pedido, NovoPedidoDTO dto) {
        // Preenche codigoCliente manualmente
        pedido.setCodigoCliente(dto.codigoCliente());

        // Define status e data do pedido automaticamente
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setDataPedido(LocalDateTime.now());

        // Vincula cada item ao pedido
        if (pedido.getItens() != null) {
            pedido.getItens().forEach(item -> item.setPedido(pedido));
        }

        // Vincula o pagamento ao pedido
        if (pedido.getPagamento() != null) {
            pedido.getPagamento().setPedido(pedido);
        }

        // Calcula o total do pedido
        pedido.setTotal(calcularTotal(pedido));
    }

    private static BigDecimal calcularTotal(Pedido pedido) {
        if (pedido.getItens() == null) return BigDecimal.ZERO;

        return pedido.getItens().stream()
                .map(item -> item.getValorUnitario()
                        .multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
