package com.github.cmonkey.webflux;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;

public class TestWSClinet {

    @Test
    public void testEcho(){
        final WebSocketClient client = new ReactorNettyWebSocketClient();

        client.execute(URI.create("ws://localhost:8080/echo"),
                session ->
                    session.send(Flux.just(session.textMessage("hello")))
                            .thenMany(session.receive().take(1).map(WebSocketMessage::getPayloadAsText))
                            .doOnNext(System.out::println)
                            .then())
                            .block(Duration.ofMillis(500));
    }

    @Test
    public void testSSE(){

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
