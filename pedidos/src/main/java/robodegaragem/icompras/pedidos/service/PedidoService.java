package robodegaragem.icompras.pedidos.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import robodegaragem.icompras.pedidos.client.ClientesClient;
import robodegaragem.icompras.pedidos.client.ProdutosClient;
import robodegaragem.icompras.pedidos.client.ServicoBancarioClient;
import robodegaragem.icompras.pedidos.controller.dto.NovoPedidoDTO;
import robodegaragem.icompras.pedidos.controller.mappers.PedidoMapper;
import robodegaragem.icompras.pedidos.model.DadosPagamento;
import robodegaragem.icompras.pedidos.model.ItemPedido;
import robodegaragem.icompras.pedidos.model.PagamentoPedido;
import robodegaragem.icompras.pedidos.model.Pedido;
import robodegaragem.icompras.pedidos.model.enums.StatusPedido;
import robodegaragem.icompras.pedidos.model.enums.TipoPagamento;
import robodegaragem.icompras.pedidos.model.exceptions.ItemNaoEncontradoException;
import robodegaragem.icompras.pedidos.publisher.PagamentoPublisher;
import robodegaragem.icompras.pedidos.repository.ItemPedidoRepository;
import robodegaragem.icompras.pedidos.repository.PedidoRepository;
import robodegaragem.icompras.pedidos.validator.PedidoValidator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final PedidoValidator validator;


    private final ServicoBancarioClient servicoBancarioClient;
    private final ClientesClient apiClientes;
    private final ProdutosClient apiProdutos;
    private final PagamentoPublisher pagamentoPublisher;

    @Transactional
    public Pedido criarPedido(NovoPedidoDTO dto) {
        Pedido pedido = pedidoMapper.map( dto );
        realizarPersistência( pedido );
        pedido.setChavePagamento( servicoBancarioClient.solicitarPagamento( pedido ) );
        return pedido;
    }

    private void realizarPersistência(Pedido pedido) {
        validator.validar( pedido );
        pedidoRepository.save( pedido );
        itemPedidoRepository.saveAll(pedido.getItens());
    }

    @Transactional()
    public Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }
    public void atualizarStatusPagamento(
            Long codigoPedido, String chavePagamento, boolean sucesso, String observacoes) {

        Optional<Pedido> pedidoEncontrado = Optional.ofNullable( pedidoRepository
                .findByCodigoAndChavePagamento( codigoPedido, chavePagamento ) );

        if ( pedidoEncontrado.isEmpty() ) {
            log.error( String.format( "Pedido não encontrado para o código %d e chave pgmto %s",
                    codigoPedido, chavePagamento ) );
            return;
        }
        Pedido pedido = pedidoEncontrado.get();

        if( sucesso ){
            prepararEPublicarPedidoPago( pedido );
            pedido.setStatus( StatusPedido.PAGO );
            pedido.setObservacoes( observacoes );

        } else {
            pedido.setStatus( StatusPedido.ERRO_PAGAMENTO );
            pedido.setObservacoes( observacoes );
        }
        pedidoRepository.save( pedido );


    }

    private void prepararEPublicarPedidoPago(Pedido pedido) {
        pedido.setStatus(StatusPedido.PAGO);
        carregarDadosCliente(pedido);
        carregarItensPedido(pedido);
        pagamentoPublisher.publicar(pedido);
    }

    @Transactional
    public void adicionarNovoPagamento(
            Long codigoPedido, String formaPagamento, String numeroCartao){

        var pedidoEncontrado = pedidoRepository.findById( codigoPedido );

        if( pedidoEncontrado.isEmpty() ){
            throw new ItemNaoEncontradoException("Pedido não encontrado para o código informado.");
        }

        var pedido = pedidoEncontrado.get();

        PagamentoPedido dadosPagamento = new PagamentoPedido();
        dadosPagamento.setFormaPagamento( formaPagamento );
        dadosPagamento.setNumeroCartao( numeroCartao );

        pedido.setPagamento( dadosPagamento );
        pedido.setStatus( StatusPedido.REALIZADO );
        pedido.setObservacoes( "Novo pagamento realizado, aguardando o processamento." );

        pedido.setChavePagamento( servicoBancarioClient.solicitarPagamento( pedido ) );

        pedidoRepository.save( pedido );

    }

    public Optional<Pedido> carregarDadosCompletosPedido( Long codigo ){
        Optional<Pedido> pedido = pedidoRepository.findById( codigo );
        pedido.ifPresent( this::carregarDadosCliente );
        pedido.ifPresent( this::carregarItensPedido );
        return pedido;
    }

    private void carregarDadosCliente( Pedido pedido ){
        Long codigoCliente = pedido.getCodigoCliente();
        pedido.setDadosCliente( apiClientes.obterDados( codigoCliente ).getBody() );
    }

    private void carregarItensPedido( Pedido pedido ){
        List<ItemPedido> itens = itemPedidoRepository.findByPedido( pedido );
        pedido.setItens( itens );
        pedido.getItens().forEach( this::carregarDadosProduto );
    }

    private void carregarDadosProduto( ItemPedido item ){
        Long codigoProduto = item.getCodigoProduto();
        item.setNome( apiProdutos.obterDados( codigoProduto ).getBody().nome() );
    }
}
