package robodegaragem.icompras.logistica.model;

public record AtualizacaoEnvioPedido(
        Long codigo, StatusPedido status, String codigoRastreio) {
}
