package robodegaragem.icompras.pedidos.validator;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import robodegaragem.icompras.pedidos.client.ClientesClient;
import robodegaragem.icompras.pedidos.client.ProdutosClient;
import robodegaragem.icompras.pedidos.client.representation.ClienteRepresentation;
import robodegaragem.icompras.pedidos.client.representation.ProdutoRepresentation;
import robodegaragem.icompras.pedidos.model.ItemPedido;
import robodegaragem.icompras.pedidos.model.Pedido;
import robodegaragem.icompras.pedidos.model.exceptions.ValidationException;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoValidator {

    private final ProdutosClient produtosClient;
    private final ClientesClient clientesClient;
//
    public void validar(Pedido pedido){

        Long codigoCliente = pedido.getCodigoCliente();
        validarCliente(codigoCliente);
        pedido.getItens().forEach(this::validarItem);
   }
//
    private void validarCliente(Long codigoCliente){
        try{
            ClienteRepresentation cliente = clientesClient.obterDados(codigoCliente).getBody();
            log.info("Cliente de código {} encontrado: {}", cliente.codigo(), cliente.nome());

            if(!cliente.ativo()){
                throw new ValidationException("codigoCliente", "Cliente Inativo.");
            }

        } catch (FeignException.NotFound e){
            throw new ValidationException("codigoCliente", String.format("Cliente de código %d não encontrado.", codigoCliente ) );
        }

    }
//
    private void validarItem(ItemPedido item){
        try {
            ProdutoRepresentation produto = produtosClient.obterDados( item.getCodigoProduto() ).getBody();
            log.info("Produto de código {} encontrado: {}", produto.codigo(), produto.nome() );

            if(!produto.ativo()){
                throw new ValidationException("codigoProduto", "Produto inativo.");
            }

        } catch (FeignException.NotFound e){
            throw new ValidationException("codigoProduto", String.format("Produto de código %d não encontrado.", item.getCodigoProduto() ) );
        }
    }
}