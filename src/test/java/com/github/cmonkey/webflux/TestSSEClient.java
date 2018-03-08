package com.github.cmonkey.webflux;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

public class TestSSEClient {
    public TestSSEClient() {
    }

    @Test
    public void testSSE() {

        final WebClient client = WebClient.create();

        client.get()
                .uri("http://localhost:8080/sse/randomNumbers")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .flatMapMany(response -> response.body(BodyExtractors.toFlux(
                        new ParameterizedTypeReference<ServerSentEvent<String>>() {
                        }
                )))
                .filter(sse -> Objects.nonNull(sse.data()))
                .map(ServerSentEvent::data)
                .buffer(20)
                .doOnNext(System.out::println)
                .blockFirst();
    }
}