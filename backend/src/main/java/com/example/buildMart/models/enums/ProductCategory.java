package com.example.buildMart.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductCategory {
    PAINT_AND_COATINGS("Paint & Coatings"),
    WOOD_AND_LUMBER("Wood & Lumber"),
    CEMENT_AND_CONCRETE("Cement & Concrete"),
    BRICKS_AND_BLOCKS("Bricks & Blocks"),
    STEEL_AND_METAL("Steel & Metal");
    private final String label;

    ProductCategory(String label) { this.label = label; }

    public String getLabel() { return label; }
}
