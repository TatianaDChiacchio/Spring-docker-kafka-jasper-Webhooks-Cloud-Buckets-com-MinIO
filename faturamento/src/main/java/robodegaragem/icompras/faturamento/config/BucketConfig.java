package robodegaragem.icompras.faturamento.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import robodegaragem.icompras.faturamento.config.props.MinioProps;

@Configuration
public class BucketConfig {

    @Autowired
    MinioProps props;

    @Bean
    public MinioClient bucketClient(){
        return MinioClient.builder()
                .endpoint( props.getUrl() )
                .credentials( props.getAccessKey(), props.getSecretKey() )
                .build();
    }
}