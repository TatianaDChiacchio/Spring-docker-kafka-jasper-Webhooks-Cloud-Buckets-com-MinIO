package robodegaragem.icompras.pedidos.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import robodegaragem.icompras.pedidos.model.enums.StatusPedido;
import robodegaragem.icompras.pedidos.repository.PedidoRepository;

@Service
@RequiredArgsConstructor
public class AtualizacaoStatusPedidoService {

    private final PedidoRepository repository;

    @Transactional
    public void atualizarStatus(
            Long codigo, StatusPedido status, String urlNotaFiscal, String rastreio){

        repository.findById(codigo).ifPresent(pedido -> {
            pedido.setStatus(status);

            if(urlNotaFiscal != null){
                pedido.setUrlNotaFiscal(urlNotaFiscal);
            }

            if(rastreio != null){
                pedido.setCodigoRastreio(rastreio);
            }
        });
    }
}
