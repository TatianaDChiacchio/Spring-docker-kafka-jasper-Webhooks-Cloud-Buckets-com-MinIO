package robodegaragem.icompras.pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import robodegaragem.icompras.pedidos.model.Pedido;

import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Pedido findByCodigoAndChavePagamento(Long codigo, String chavePagamento);
}
