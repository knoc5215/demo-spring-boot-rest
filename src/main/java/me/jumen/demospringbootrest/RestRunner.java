package me.jumen.demospringbootrest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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

    /* RestTemplate global customize */
    @Bean
    public RestTemplateCustomizer restTemplateCustomizer() {
        return restTemplate -> restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    /* WebClient global customize */
    @Bean
    public WebClientCustomizer webClientCustomizer() {
        return webClientBuilder -> webClientBuilder.baseUrl("http://localhost:8080");
    }



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

//        WebClient webClient = webClientBuilder.baseUrl("http://localhost:8080").build();    // WebClient local customize
        WebClient webClient = webClientBuilder.build();                                       // use WebClient global customize

        Mono<String> helloMono = webClient.get().uri("/hello").retrieve().bodyToMono(String.class);
        helloMono.subscribe(s -> {
            /* async callback */
            System.out.println(s);
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }

            System.out.println(stopWatch.prettyPrint());
            stopWatch.start();
        });

        /* WebClient non-blocking */
        Mono<String> worldMono = webClient.get().uri("/world").retrieve().bodyToMono(String.class);
        worldMono.subscribe(s -> {
            /* async callback */
            System.out.println(s);
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }

            System.out.println(stopWatch.prettyPrint());
            stopWatch.start();
        });
    }
}
