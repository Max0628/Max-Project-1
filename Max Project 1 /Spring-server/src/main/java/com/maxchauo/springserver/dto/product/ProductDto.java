package com.maxchauo.springserver.dto.product;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class ProductDto {
    private  Long id;
    private String category;
    private String title;
    private String description;
    private long price;
    private String texture;
    private String wash;
    private String place;
    private String note;
    private String story;
    private List<ColorDto> colors = new ArrayList<>();
    private List<String> sizes = new ArrayList<>();
    private List<VariantDto> variants = new ArrayList<>();
    private List<VariantDto> variantIns = new ArrayList<>();
    private MultipartFile mainImage;
    private List<MultipartFile> images = new ArrayList<>();
    private String mainImagePath;
}
