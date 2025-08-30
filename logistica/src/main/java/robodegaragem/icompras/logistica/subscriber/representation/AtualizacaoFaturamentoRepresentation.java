package robodegaragem.icompras.logistica.subscriber.representation;

import robodegaragem.icompras.logistica.model.StatusPedido;

public record AtualizacaoFaturamentoRepresentation(
        Long codigo, StatusPedido status, String urlNotaFiscal
) {
}
