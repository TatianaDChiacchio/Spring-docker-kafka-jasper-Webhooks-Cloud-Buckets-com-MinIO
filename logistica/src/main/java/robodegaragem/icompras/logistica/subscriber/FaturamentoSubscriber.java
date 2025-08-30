package robodegaragem.icompras.logistica.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import robodegaragem.icompras.logistica.service.EnvioPedidoService;
import robodegaragem.icompras.logistica.subscriber.representation.AtualizacaoFaturamentoRepresentation;

@Component
@Slf4j
@RequiredArgsConstructor
public class FaturamentoSubscriber {

    private final ObjectMapper objectMapper;
    private final EnvioPedidoService service;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}",
            topics = "${icompras.config.kafka.topics.pedidos-faturados}")
    public void listen(String json){
        log.info("Recebendo pedido para envio: {} ", json);

        try {
            var representation =
                    objectMapper.readValue(json, AtualizacaoFaturamentoRepresentation.class);

            service.enviar(representation.codigo(), representation.urlNotaFiscal());

            log.info("Pedido processado com sucesso! Código: {}", representation.codigo());

        } catch (Exception e){
            log.error("Erro ao preparar pedido para envio", e);
        }

    }
}
