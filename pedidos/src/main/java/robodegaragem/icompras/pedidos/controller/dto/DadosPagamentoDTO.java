package robodegaragem.icompras.pedidos.controller.dto;

import robodegaragem.icompras.pedidos.model.enums.TipoPagamento;

public record DadosPagamentoDTO(
        String formaPagamento,
        String numeroCartao
) {}

