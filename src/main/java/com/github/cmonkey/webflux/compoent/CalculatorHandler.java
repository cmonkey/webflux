package com.github.cmonkey.webflux.compoent;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.function.BiFunction;

@Component
public class CalculatorHandler {

    public Mono<ServerResponse> add(final ServerRequest request){
        return calculator(request, (v1 , v2) -> v1 + v2);
    }

    public Mono<ServerResponse> subtract(final ServerRequest request){
        return calculator(request, (v1, v2) -> v1 - v2);
    }

    public Mono<ServerResponse> multiply(final ServerRequest request){
        return calculator(request, (v1, v2) -> v1 * v2);
    }

    public Mono<ServerResponse> divide(final ServerRequest request){
        return calculator(request, (v1, v2) -> v1 / v2);
    }

    public Mono<ServerResponse> calculator(ServerRequest request,
                                          BiFunction<Integer, Integer, Integer> calFunc){

        Tuple2<Integer, Integer> operands = extractOperands(request);

        return ServerResponse.ok()
                .body(Mono.just(calFunc.apply(operands.getT1(), operands.getT2())), Integer.class);

    }

    private Tuple2<Integer,Integer> extractOperands(ServerRequest request) {

        return Tuples.of(parseOperands(request, "v1"), parseOperands(request, "v2"));
    }

    private int parseOperands(ServerRequest request, String v1) {
        return Integer.parseInt(request.queryParam(v1).orElse("0"));
    }

}
