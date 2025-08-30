package robodegaragem.icompras.pedidos.controller.dto;

import java.math.BigDecimal;

import java.math.BigDecimal;

public record ItemPedidoDTO(
        Long codigoProduto,
        String nome,
        Integer quantidade,
        BigDecimal valorUnitario
) {}
