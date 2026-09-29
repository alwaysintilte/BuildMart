package com.example.buildMart.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "promocodes")
public class PromoCode {
    @Id
    private String id;
    private String code;
    private Double value;

    public PromoCode() {}

    public PromoCode(String id, String code, Double value) {
        this.id = id;
        this.code = code;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
