package robodegaragem.compras.clientes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import robodegaragem.compras.clientes.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
