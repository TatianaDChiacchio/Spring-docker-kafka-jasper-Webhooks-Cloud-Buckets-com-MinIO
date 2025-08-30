package robodegaragem.icompras.pedidos.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import robodegaragem.icompras.pedidos.model.Pedido;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagamentoPublisher {

    private final DetalhePedidoMapper mapper;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${icompras.config.kafka.topics.pedidos-pagos}")
    private String topico;

    public void publicar(Pedido pedido){
        log.info( "Publicando pedido pago {}", pedido.getCodigo() );

        try {
            // transformar o objeto mapper.map( pedido ) em um json
            var json = objectMapper.writeValueAsString( mapper.map( pedido ) );
            kafkaTemplate.send( topico, "dados", json );
            // esse deserialização feita pelo writeValueAsString pode dar erro durante esse processo , gerando o erro JsonProcessingException
        } catch ( JsonProcessingException e) {
            log.error( "Erro ao processar o json", e );
        } catch ( RuntimeException e ){
            log.error( "Erro técnico ao publicar no tópico de pedidos", e );
        }

    }

}
