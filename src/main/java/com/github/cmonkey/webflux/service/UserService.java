package com.github.cmonkey.webflux.service;

import com.github.cmonkey.webflux.entity.User;
import com.github.cmonkey.webflux.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final Map<Long, User> data = new ConcurrentHashMap<>();

    public Flux<User> list() {
        return Flux.fromIterable(this.data.values());
    }

    public Flux<User> getById(final Flux<Long> ids) {
        return ids.flatMap(id -> Mono.justOrEmpty(this.data.get(id)));
    }

    public Mono<User> getById(final long id) {
        return Mono.justOrEmpty(this.data.get(id))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException()));
    }

    public Flux<User> createOrUpdate(final Flux<User> users) {
        return users.doOnNext(user -> this.data.put(user.getId(), user));
    }

    public Mono<User> createOrUpdate(final User user) {
        this.data.put(user.getId(), user);
        return Mono.just(user);
    }

    public Mono<User> delete(final long id) {
        return Mono.justOrEmpty(this.data.remove(id));
    }
}