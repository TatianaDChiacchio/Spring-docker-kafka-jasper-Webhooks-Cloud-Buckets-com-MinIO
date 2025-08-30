package robodegaragem.icompras.faturamento.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import robodegaragem.icompras.faturamento.bucket.BucketFile;
import robodegaragem.icompras.faturamento.bucket.BucketService;
import robodegaragem.icompras.faturamento.model.Pedido;
import robodegaragem.icompras.faturamento.publisher.FaturamentoPublisher;

import java.io.ByteArrayInputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeradorNotaFiscalService {

    private final NotaFiscalService notaFiscalService;
    private final BucketService bucketService;
    private final FaturamentoPublisher publisher;

    public void gerar( Pedido pedido ) {
        log.info( "Gerando nota fiscal para o pedido {} ", pedido.codigo() );

        try {
            byte[] byteArray = notaFiscalService.gerarNota( pedido );

            String nomeArquivo = String.format( "notafiscal_pedido_%d.pdf", pedido.codigo() );

            var file = new BucketFile(
                    nomeArquivo, new ByteArrayInputStream( byteArray ), MediaType.APPLICATION_PDF, byteArray.length );

            bucketService.upload( file );

            String url = bucketService.getUrl( nomeArquivo );
            publisher.publicar( pedido, url );

            log.info( "Gerada a nota fiscal, nome do arquivo: {}", nomeArquivo );

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }
}
