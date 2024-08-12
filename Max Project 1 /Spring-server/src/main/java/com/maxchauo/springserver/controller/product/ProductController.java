package com.maxchauo.springserver.controller.product;

import com.maxchauo.springserver.dto.product.MessageDto;
import com.maxchauo.springserver.dto.product.ProductDto;
import com.maxchauo.springserver.exception.BadRequestException;
import com.maxchauo.springserver.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/1.0/products")
@CrossOrigin
public class ProductController {


    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<String> createProduct(@ModelAttribute ProductDto product) {
        boolean addResult = productService.addCompleteProduct(product);
        boolean uploadSuccess = productService.uploadProduct(product, product.getMainImage(), product.getImages());

        if (addResult && uploadSuccess) {
            return ResponseEntity.ok("Create product successfully");
        } else {
            return ResponseEntity.badRequest().body("Create product failed.");
        }
    }


    @GetMapping("{category}")
    public ResponseEntity<?> getProduct(
            @PathVariable(value = "category") String category,
            @RequestParam(value = "paging", defaultValue = "0") Long paging) {


        MessageDto<?> messageDto = productService.getJsonsByCategory(category, paging);
        if (messageDto == null || messageDto.getData() == null) {
            throw new BadRequestException("Invalid product category.");
        } else {
            return ResponseEntity.ok().body(messageDto);
        }
    }


    @GetMapping("/search")
    public ResponseEntity<?> searchProduct(
            @RequestParam(value = "keyword", required = true) String keyword,
            @RequestParam(value = "paging", defaultValue = "0") Long paging
    ) {
        MessageDto<?> messageDto = productService.getJsonsByKeyWord(keyword, paging);
        if (messageDto == null || messageDto.getData() == null) {
            throw new BadRequestException("Invalid product keyword.");
        }
        return ResponseEntity.ok(messageDto);
    }

    @GetMapping("/details")
    public ResponseEntity<?> getDetails(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "paging", defaultValue = "0") Long paging
    ) {
        MessageDto<?> messageDto = productService.getJsonById(id);
        if (messageDto == null || messageDto.getData() == null) {
            throw new BadRequestException("Invalid id.");
        }
        return ResponseEntity.ok(messageDto);
    }
}
