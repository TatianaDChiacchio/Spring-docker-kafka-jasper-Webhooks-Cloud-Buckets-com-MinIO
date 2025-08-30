package robodegaragem.icompras.pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import robodegaragem.icompras.pedidos.model.PagamentoPedido;

public interface PagamentoPedidoRepository extends JpaRepository<PagamentoPedido, Long> {
}
