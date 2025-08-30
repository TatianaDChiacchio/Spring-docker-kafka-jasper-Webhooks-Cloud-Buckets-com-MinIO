package robodegaragem.icompras.pedidos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import robodegaragem.icompras.pedidos.controller.dto.AdicaoNovoPagamentoDTO;
import robodegaragem.icompras.pedidos.controller.dto.NovoPedidoDTO;
import robodegaragem.icompras.pedidos.controller.mappers.PedidoMapper;
import robodegaragem.icompras.pedidos.model.ErroResposta;
import robodegaragem.icompras.pedidos.model.Pedido;
import robodegaragem.icompras.pedidos.model.exceptions.ItemNaoEncontradoException;
import robodegaragem.icompras.pedidos.model.exceptions.ValidationException;
import robodegaragem.icompras.pedidos.publisher.DetalhePedidoMapper;
import robodegaragem.icompras.pedidos.publisher.representation.DetalhePedidoRepresentation;
import robodegaragem.icompras.pedidos.service.PedidoService;

@RestController
@RequestMapping("pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final DetalhePedidoMapper detalhePedidoMapper;

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody NovoPedidoDTO dto) {

        try {
            Pedido pedido = pedidoService.criarPedido( dto );
            return ResponseEntity.ok( pedido );
        } catch ( ValidationException e ){
            var erro = new ErroResposta("Erro validação", e.getField(), e.getMessage() );
            return ResponseEntity.badRequest().body( erro );
        }

    }


    @PostMapping("pagamentos")
    public ResponseEntity<Object> adicionarNovoPagamento(
            @RequestBody AdicaoNovoPagamentoDTO dto){
        try {
            pedidoService.adicionarNovoPagamento( dto.codigoPedido(), dto.formaPagamento(), dto.numeroCartao() );
            return ResponseEntity.noContent().build();
        } catch (ItemNaoEncontradoException e){
            var erro = new ErroResposta(
                    "Item não encontrado", "codigoPedido", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @GetMapping("{codigo}")
    public ResponseEntity<DetalhePedidoRepresentation> obterDetalhesPedido(
            @PathVariable( "codigo" ) Long codigo ){
        return pedidoService
                .carregarDadosCompletosPedido( codigo )
                .map( detalhePedidoMapper::map )
                .map( ResponseEntity::ok )
                .orElseGet( () -> ResponseEntity.notFound().build() );
    }
}
