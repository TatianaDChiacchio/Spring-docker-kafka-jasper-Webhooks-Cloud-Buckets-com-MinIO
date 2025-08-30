package robodegaragem.icompras.pedidos.model;

import lombok.Data;
import robodegaragem.icompras.pedidos.model.enums.TipoPagamento;

@Data
public class DadosPagamento {
    private String dados;
    private TipoPagamento tipoPagamento;
}
