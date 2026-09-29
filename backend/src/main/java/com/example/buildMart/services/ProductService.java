package com.example.buildMart.services;

import com.example.buildMart.models.Category;
import com.example.buildMart.models.Product;
import com.example.buildMart.repositories.ProductCustomRepository;
import com.example.buildMart.repositories.interfaces.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCustomRepository productCustomRepository;
    @Autowired
    public ProductService(ProductRepository productRepository, ProductCustomRepository productCustomRepository){
        this.productRepository = productRepository;
        this.productCustomRepository = productCustomRepository;
    }
    public Page<Product> findAllByPage(Integer page, Integer size, String sort){
        return productCustomRepository.findAllByPage(page, size, sort);
    }
    public Optional<Product> findById(String id){
        return productRepository.findById(id);
    }
    public List<Category> findAllCategories(){
        return productCustomRepository.findAllCategories();
    }
    public List<Product> findByDiscount(){
        return  productCustomRepository.findByDiscount();
    }
    public Page<Product> findAllByParams(Float rating, Float minPrice, Float maxPrice,  String category, Integer page, Integer size, String sort){
        return productCustomRepository.findByParams(rating, minPrice, maxPrice,  category, page, size, sort);
    }
}
