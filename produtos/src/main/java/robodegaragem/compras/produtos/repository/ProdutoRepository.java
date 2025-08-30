package robodegaragem.compras.produtos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import robodegaragem.compras.produtos.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
