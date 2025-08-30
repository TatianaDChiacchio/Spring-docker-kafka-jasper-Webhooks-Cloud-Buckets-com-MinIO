package robodegaragem.icompras.pedidos.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule( new Jdk8Module() );
        mapper.registerModule( new JavaTimeModule() );

        // incluir nos campos só o que não está nulo
        // Serialização: transformar objeto Java em json
        mapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );
        // se na classe java tiver 3 campos e no json vierem 4 campos não vai dar erro
        // deserialização: transformar um json em um objeto Java
        mapper.configure( DeserializationFeature.FAIL_ON_UNEXPECTED_VIEW_PROPERTIES, false );

        return mapper;
    }
}