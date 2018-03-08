package com.github.cmonkey.webflux;

import com.github.cmonkey.webflux.entity.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TestRestClient {

    @Test
    public void testRest(){
        final User user = new User();
        user.setId(10L);
        user.setName("cmonkey");

        final WebClient client = WebClient.create("http://localhost:8080/users");

        final Mono<User> mono = client.post()
                .uri("")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .exchange()
                .flatMap(response -> response.bodyToMono(User.class));

        System.out.println(mono.block());
    }
}
