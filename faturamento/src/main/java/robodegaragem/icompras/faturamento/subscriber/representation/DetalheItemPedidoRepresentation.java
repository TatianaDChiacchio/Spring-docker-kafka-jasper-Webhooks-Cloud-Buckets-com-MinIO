package robodegaragem.icompras.faturamento.subscriber.representation;

import java.math.BigDecimal;

import java.math.BigDecimal;

public record DetalheItemPedidoRepresentation(
        Long codigoProduto,
        String nome,
        Integer quantidade,
        BigDecimal valorUnitario,
        BigDecimal total ) {
}
