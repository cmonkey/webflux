package com.github.cmonkey.webflux.controller;

import com.github.cmonkey.webflux.entity.User;
import com.github.cmonkey.webflux.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
public class FluxController {
    @Resource
    private UserService userService;
    @PostMapping("/users")
    public Mono<User> users(@RequestBody  User user){
        return userService.createOrUpdate(user);
    }

    @GetMapping("/users/{id}")
    public Mono<User> findId(@PathVariable("id") long id){
        return userService.getById(id);
    }

    @GetMapping("/users/all")
    public Flux<User> findAll(){
        return userService.list();
    }
}
