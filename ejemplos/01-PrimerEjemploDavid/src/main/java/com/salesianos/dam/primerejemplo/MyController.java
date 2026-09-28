package com.salesianos.dam.primerejemplo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@RequestMapping("/api")
public class MyController {


    @GetMapping("/hello")
    public Greeting hello (@RequestParam(defaultValue = "world")String name){

        return new Greeting("Hello", name);
    }



    record Greeting(String greeting, String name){
    }


    //Los records son clases que estan orientadas a aglutinar una serie de propiedasd, que son inmutables y tienen
    //directamente unos metodos getter que no empiezan por get


}
