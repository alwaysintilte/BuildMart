package com.example.buildMart.models;

public class Category {
    private String name;
    private Long quantity;
    private String image;
    public Category() {}
    public Category(String name, Long quantity, String image) {
        this.name = name;
        this.quantity = quantity;
        this.image = image;
    }
    public String getName() {
        return name;
    }
    public Long getQuantity() {
        return quantity;
    }
    public String getImage() {
        return image;
    }
}
