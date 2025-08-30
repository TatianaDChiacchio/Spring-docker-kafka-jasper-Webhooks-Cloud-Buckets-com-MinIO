package robodegaragem.icompras.pedidos.publisher.representation;

import java.math.BigDecimal;

public record DetalheItemPedidoRepresentation(
        Long codigoProduto,
        String nome,
        Integer quantidade,
        BigDecimal valorUnitario
) {
    public BigDecimal getTotal(){
        return valorUnitario.multiply( BigDecimal.valueOf( quantidade ) );
    }

}
