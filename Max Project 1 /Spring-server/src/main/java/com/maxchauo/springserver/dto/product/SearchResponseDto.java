package com.maxchauo.springserver.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonPropertyOrder({"id", "category", "title", "description", "price", "texture", "wash", "place",
        "note","story", "main_image", "images", "variants", "colors", "sizes"})
public class SearchResponseDto {
    private Long id;
    private String category;
    private String title;
    private String description;
    private long price;
    private String texture;
    private String wash;
    private String place;
    private String note;
    private String story;
    @JsonProperty("main_image")
    private String mainImage;
    private List<String> images = new ArrayList<>();
    private List<VariantResponseDto> variants = new ArrayList<>();
    private List<ColorDto> colors = new ArrayList<>();
    private List<String> sizes = new ArrayList<>();
}
