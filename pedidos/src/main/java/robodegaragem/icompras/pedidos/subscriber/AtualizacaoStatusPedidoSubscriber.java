package robodegaragem.icompras.pedidos.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import robodegaragem.icompras.pedidos.service.AtualizacaoStatusPedidoService;
import robodegaragem.icompras.pedidos.subscriber.representation.AtualizacaoStatusPedidoRepresentation;

@Slf4j
@Component
@RequiredArgsConstructor
public class AtualizacaoStatusPedidoSubscriber {

    private final AtualizacaoStatusPedidoService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = {
            "${icompras.config.kafka.topics.pedidos-faturados}",
            "${icompras.config.kafka.topics.pedidos-enviados}"
    })
    public void receberAtualizacao(String json){
        log.info("Recebendo atualizacao de status: {}", json);

        try {
            var atualizacaoStatus =
                    objectMapper.readValue(json, AtualizacaoStatusPedidoRepresentation.class);

            service.atualizarStatus(
                    atualizacaoStatus.codigo(),
                    atualizacaoStatus.status(),
                    atualizacaoStatus.urlNotaFiscal(),
                    atualizacaoStatus.codigoRastreio()
            );

            log.info("Pedido atualizado com sucesso!");

        } catch (Exception e){
            log.error("Erro ao atualizar status pedido: {}", e.getMessage());
        }
    }
}