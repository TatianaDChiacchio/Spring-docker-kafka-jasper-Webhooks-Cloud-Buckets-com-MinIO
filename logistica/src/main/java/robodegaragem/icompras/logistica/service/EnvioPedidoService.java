package robodegaragem.icompras.logistica.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import robodegaragem.icompras.logistica.model.AtualizacaoEnvioPedido;
import robodegaragem.icompras.logistica.model.StatusPedido;
import robodegaragem.icompras.logistica.publisher.EnvioPedidoPublisher;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EnvioPedidoService {

    private final EnvioPedidoPublisher publisher;

    public void enviar(Long codigoPedido, String urlNotaFiscal){
        var codigoRastreio = gerarCodigoRastreio();
        var atualizacaoRepresentation =
                new AtualizacaoEnvioPedido( codigoPedido, StatusPedido.ENVIADO, codigoRastreio );
        publisher.enviar( atualizacaoRepresentation );
    }

    private String gerarCodigoRastreio(){
        // AB123456789BR

        var random = new Random();

        char letra1 = (char) ('A' + random.nextInt(26));
        char letra2 = (char) ('A' + random.nextInt(26));

        int numeros = 100000000 + random.nextInt(900000000);

        return "" + letra1 + letra2 + numeros + "BR";

    }
}
