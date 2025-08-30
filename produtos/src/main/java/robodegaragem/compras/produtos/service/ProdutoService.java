package robodegaragem.compras.produtos.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import robodegaragem.compras.produtos.model.Produto;
import robodegaragem.compras.produtos.repository.ProdutoRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;
    public Produto salvar(Produto produto){
        return repository.save(produto);
    }

    public Optional<Produto> obterPorCodigo(Long codigo){
        return repository.findById(codigo);
    }

    public void deletar(Produto produto) {
        produto.setAtivo( false );
        repository.save( produto);
    }
}
