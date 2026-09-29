package com.example.buildMart.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private Map<String, Integer> productQuantity;
    private List<Product> products;
    public Cart(){
        productQuantity = new HashMap<>();
        products = new ArrayList<>();
    }

    public Map<String, Integer> getProductQuantity() {
        return productQuantity;
    }
    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product, Integer quantity){
        products.add(product);
        productQuantity.put(product.getId(),quantity);
    }
}
