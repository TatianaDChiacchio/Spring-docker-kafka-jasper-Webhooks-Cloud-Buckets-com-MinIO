package robodegaragem.icompras.pedidos.controller.dto;

public record RecebimentoCallbackPagamentoDTO(
        Long codigo, String chavePagamento,
        boolean status, String observacoes) {
}
