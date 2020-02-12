package controller;

import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * Project: gs-reactive-rest-service Created by Mustafa Çelik on 12/23/19, Mon Contact:
 * celikmustafa89@gmail.com
 */
@RestController
public class MyController {

    @GetMapping("/ali")
    public Publisher<String> home() {

        return Mono.just("Home page");
    }
}