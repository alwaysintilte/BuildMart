package com.example.buildMart.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private String category;
    private String image;
    private Double rating;
    private Double price;
    private Double discount;
    private String description;
    private Map<String,String> technicalSpecifications;
    private List<String> relatedProducts;

    public Product() {}

    public Product(String id, String name, String category, String image, Double rating, Double price, Double discount, String description, Map<String, String> technicalSpecifications, List<String> relatedProducts) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.image = image;
        this.rating = rating;
        this.price = price;
        this.discount = discount;
        this.description = description;
        this.technicalSpecifications = technicalSpecifications;
        this.relatedProducts = relatedProducts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategories(String category) {
        this.category = category;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getTechnicalSpecifications() {
        return technicalSpecifications;
    }

    public void setTechnicalSpecifications(Map<String, String> technicalSpecifications) {
        this.technicalSpecifications = technicalSpecifications;
    }

    public List<String> getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(List<String> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }
}
