package me.jumen.demospringbootrest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RestRunner implements ApplicationRunner {

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @Autowired
    WebClient.Builder webClientBuilder;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("======================================");
        System.out.println("RestTemplate Test");
        System.out.println("======================================");
        RestTemplate restTemplate = restTemplateBuilder.build();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // TODO /hello
        String helloResult = restTemplate.getForObject("http://localhost:8080/hello", String.class);
        System.out.println(helloResult);

        // TODO /world
        String worldObject = restTemplate.getForObject("http://localhost:8080/world", String.class);
        System.out.println(worldObject);

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        System.out.println("======================================");
        System.out.println("WebClient Test");
        System.out.println("======================================");

        /* WebClient non-blocking */
        WebClient webClient = webClientBuilder.build();

        Mono<String> helloMono = webClient.get().uri("http://localhost:8080/hello").retrieve().bodyToMono(String.class);
        helloMono.subscribe(s->{
            /* async callback */
            System.out.println(s);
            if(stopWatch.isRunning()) {
                stopWatch.stop();
            }

            System.out.println(stopWatch.prettyPrint());
            stopWatch.start();
        });

        /* WebClient non-blocking */
        Mono<String> worldMono = webClient.get().uri("http://localhost:8080/world").retrieve().bodyToMono(String.class);
        worldMono.subscribe(s->{
            /* async callback */
            System.out.println(s);
            if(stopWatch.isRunning()) {
                stopWatch.stop();
            }

            System.out.println(stopWatch.prettyPrint());
            stopWatch.start();
        });
    }
}
