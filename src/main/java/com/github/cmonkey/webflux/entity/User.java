package com.github.cmonkey.webflux.entity;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class User {
    private long id;
    private String name;
}
