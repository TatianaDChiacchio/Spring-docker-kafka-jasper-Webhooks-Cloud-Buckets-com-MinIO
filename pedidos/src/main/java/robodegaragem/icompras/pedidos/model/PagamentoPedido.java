package robodegaragem.icompras.pedidos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "pagamento_pedido")
public class PagamentoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String formaPagamento;
    private String numeroCartao;

    @OneToOne(mappedBy = "pagamento")
    @JsonIgnore
    private Pedido pedido;
}
