package robodegaragem.icompras.faturamento.bucket;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import robodegaragem.icompras.faturamento.config.props.MinioProps;
//import java.util.concurrent.TimeUnit;
@Component
@RequiredArgsConstructor
public class BucketService {

    // client faz as operações dentro do bucket
    private final MinioClient client;
    private final MinioProps props;

    public void upload( BucketFile file ){
        try {
            var object = PutObjectArgs
                    .builder()
                    .bucket( props.getBucketName() )
                    .object( file.name() )
                    .stream( file.is(), file.size(), -1 )
                    .contentType( file.type().toString() ) //application/pdf image/jpeg
                    .build();

            client.putObject( object );
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    public String getUrl( String fileName ){
        try {
            var object = GetPresignedObjectUrlArgs.builder()
                    .method( Method.GET )
                    .bucket( props.getBucketName() )
                    .object( fileName )
        //            .expiry( 7, TimeUnit.DAYS )
                    .build();

            return client.getPresignedObjectUrl( object );

        } catch ( Exception e ){
            throw new RuntimeException( e );
        }
    }
}
