package com.github.cmonkey.webflux.configuration;

import com.github.cmonkey.webflux.compoent.CalculatorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Configuration
public class CalculatorRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> calcFunc(CalculatorHandler handler){

        return RouterFunctions.route(RequestPredicates.path("/calc"), request ->
                request.queryParam("operator")
                .map(operator ->
                        Mono.justOrEmpty(ReflectionUtils.findMethod(CalculatorHandler.class, operator, ServerRequest.class))
                        .flatMap(method -> (Mono<ServerResponse>)ReflectionUtils.invokeMethod(method, handler, request))
                        .switchIfEmpty(ServerResponse.badRequest().build())
                        .onErrorResume(ex -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
                .orElse(ServerResponse.badRequest().build())
        );
    }
}
