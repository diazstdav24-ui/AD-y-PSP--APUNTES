package com.salesianos.dam.primerejemplo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long> {



 /*  private  List<Product> products;

    public ProductRepository() {
        this.products = new ArrayList<>();
    }

    public Product addProduct (Product product){

        products.add(product);

        return product;


    }

    public List<Product> getProducts(){

        return products;
    }

    public void deleteProducts(String name){

       Optional<Product> product = products.stream()
                                            .filter(p -> p.name().equals(name))
                                            .findFirst();

       products.remove(product);

    }
    public Product updateProduct (Product product){

        products.removeIf(p -> p.name().equals(product.name()));
        addProduct(product);
        return product;

    }*/


}
