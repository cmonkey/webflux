package com.github.cmonkey.webflux;

import org.junit.Test;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.time.Duration;

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

}
