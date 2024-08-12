package com.maxchauo.springserver.service.product;

import com.maxchauo.springserver.dto.product.JsonDto;
import com.maxchauo.springserver.dto.product.MessageDto;
import com.maxchauo.springserver.dto.product.ProductDto;
import com.maxchauo.springserver.dto.product.SearchResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    boolean addCompleteProduct(ProductDto product);

    boolean uploadProduct(ProductDto product, MultipartFile mainImage, List<MultipartFile> images);

    MessageDto<List<JsonDto>> getJsonsByCategory(String category, Long paging);

    MessageDto<List<SearchResponseDto>> getJsonsByKeyWord(String keyword, Long paging);

    MessageDto<JsonDto> getJsonById(Long id);
}
