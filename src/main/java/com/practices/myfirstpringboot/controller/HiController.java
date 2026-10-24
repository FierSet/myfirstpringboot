package com.practices.myfirstpringboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api-regards")
public class HiController {

    @GetMapping("/hi")
    public String sayHi() {
        return "Hi, welcome to my first Spring Boot application!";
    }

    @GetMapping("/helloname/{name}/{age}")
    public String sayHelloName(@PathVariable String name, @PathVariable String age)
    {
        return "Hello, " + name + "! You are " + age + " years old.";
    }

}
