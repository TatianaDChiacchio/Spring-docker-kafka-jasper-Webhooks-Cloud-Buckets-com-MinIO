package robodegaragem.icompras.pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import robodegaragem.icompras.pedidos.model.ItemPedido;
import robodegaragem.icompras.pedidos.model.Pedido;

import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    List<ItemPedido> findByPedido( Pedido pedido );
}
