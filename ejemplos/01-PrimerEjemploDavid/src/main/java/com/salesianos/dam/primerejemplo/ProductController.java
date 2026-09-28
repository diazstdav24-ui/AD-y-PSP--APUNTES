package com.salesianos.dam.primerejemplo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    List<Product> products = productRepository.getProducts();



    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product){

        return ResponseEntity.status(201)
                .body(productRepository.addProduct(product));

        //ResponseEntity.badRequest().build, cod 400
    }

    @GetMapping
    ResponseEntity<List<Product>> getProducts(){

        List<Product> result = productRepository.getProducts();

        if (result.isEmpty()){
            //return ResponseEntity.status(404).build;
            return ResponseEntity.notFound().build();

        }

        return ResponseEntity.ok(result);

    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String name){

        productRepository.deleteProducts(name);

        return ResponseEntity.noContent().build();
    }







}
