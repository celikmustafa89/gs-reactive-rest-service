package hello;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import reactor.core.publisher.Mono;

@Component
public class GreetingHandler {

    private KafkaProducer kafkaProducer;

    @PostConstruct
    public void initializeKafkaProducer(){

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        kafkaProducer = new KafkaProducer(properties);
    }

    public Mono<ServerResponse> hello(ServerRequest request) {
        Map<String, Object> attributes = request.attributes();

        try{

            int i = 0;
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                System.out.println(entry.getKey()+"-"+entry.getValue());
                kafkaProducer.send(new ProducerRecord("test", entry.getKey(), entry.getValue().toString() ));
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
        }



        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromObject("Hello, Spring-->!"));
    }

    public Mono<ServerResponse> post(ServerRequest request) {

        try{
            Mono<String> stringMono = request.bodyToMono(String.class);
            kafkaProducer.send(new ProducerRecord("test", "key", stringMono.toString() ));

        }catch (Exception e){
            e.printStackTrace();
        }finally {
        }

        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromObject("I am post request"));
    }

    @PreDestroy
    public void destroy(){
        kafkaProducer.close();
    }

}