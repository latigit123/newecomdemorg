package com.example.ecom.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
public class ProductsController {

    record Product(int id, String sku, String name, int price){}

    @GetMapping("/api/products")
    public List<Product> products(){
        return List.of(
            new Product(1,"SKU-RED-TSHIRT","Red T‑Shirt", 499),
            new Product(2,"SKU-BLUE-MUG","Blue Coffee Mug", 299),
            new Product(3,"SKU-YOGA-MAT","Yoga Mat", 899)
        );
    }
}
