package robodegaragem.icompras.faturamento.subscriber.representation;


import java.math.BigDecimal;
import java.util.List;

public record DetalhePedidoRepresentation(
        Long codigo,
        Long codigoCliente,
        String nome,
        String cpf,
        String logradouro,
        String numero,
        String bairro,
        String email,
        String telefone,
        String dataPedido,
        BigDecimal total,
        String urlNotaFiscal,
        String codigoRastreio,
        List<DetalheItemPedidoRepresentation> itens
) {
}
