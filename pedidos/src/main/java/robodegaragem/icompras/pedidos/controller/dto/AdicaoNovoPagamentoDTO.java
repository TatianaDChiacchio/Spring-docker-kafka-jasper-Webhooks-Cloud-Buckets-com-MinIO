package robodegaragem.icompras.pedidos.controller.dto;

import robodegaragem.icompras.pedidos.model.enums.TipoPagamento;

public record AdicaoNovoPagamentoDTO(
        Long codigoPedido, String formaPagamento, String numeroCartao) {
}
