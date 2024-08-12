package com.maxchauo.springserver.dao.product;


import com.maxchauo.springserver.dto.product.*;

import java.util.List;

public interface Dao {

    Long insertProduct(ProductDto productDto);

    Long insertColor(String name, String code);

    Long insertSize(String size);

    Long insertVariant(String colorCode, String size, Long sizeId, Long colorId, Long stock,Long product_id);

    Long insertImage(ImageDto image);

    List<ColorDto> getColorsByProductId(Long productId);

    List<String> getSizesByProductId(Long productId);

    List<VariantResponseDto> getVariantsByProductId(Long productId);

    List<String> getImagesByProductId(Long productId);

    List<JsonDto> getJsonDtoByCategory(String category, Long paging);

    List<SearchResponseDto> getSearchResponseByKeyword(String keyword, Long paging);

    JsonDto getJsonDtoById(Long id);
}
