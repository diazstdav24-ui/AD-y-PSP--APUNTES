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




    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product){


        return ResponseEntity.status(201)
                .body(productRepository.save(product));

        //ResponseEntity.badRequest().build, cod 400
    }

    @GetMapping
    ResponseEntity<List<Product>> getProducts(){



        List<Product> result = productRepository.findAll();

        if (result.isEmpty()){
            //return ResponseEntity.status(404).build;
            return ResponseEntity.notFound().build();

        }

        return ResponseEntity.ok(result);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductoById(@PathVariable Long id){

        return ResponseEntity.of(productRepository.findById(id));

    }

    //Recordar siempre que @Pathvariable rescata cosas de la URL, y que @RequestBody obliga a que la petición lleve
    // cuerpo

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product){

        return productRepository.findById(id)
                .map(p -> {
                    p.setName(product.getName());
                    p.setPrice(product.getPrice());
                    return ResponseEntity.ok(productRepository.save(p));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){

        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();

    }

}
