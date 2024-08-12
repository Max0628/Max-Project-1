package com.maxchauo.springserver.service.product;

import com.maxchauo.springserver.dao.product.Dao;
import com.maxchauo.springserver.dto.product.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private Dao dao;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${elastic.ip.prefix.product}")
    private String productElasticIpPrefix;

    @Override
    public boolean addCompleteProduct(ProductDto product) {
        try {
            //Make sure mainImage path created before insertion.
            MultipartFile mainImage = product.getMainImage();
            List<MultipartFile> images = product.getImages();

            if (mainImage != null && !mainImage.isEmpty()) {
                uploadProduct(product, mainImage, images);
            }

            if (product.getMainImagePath() == null || product.getMainImagePath().isEmpty()) {
                throw new IOException("Main image path is not set");
            }

            Long productId = dao.insertProduct(product);
            Map<String, Long> colorIdMap = new HashMap<>();
            Map<String, Long> sizeIdMap = new HashMap<>();

            List<VariantDto> variantDtos = product.getVariants();
            for (VariantDto variantDto : variantDtos) {
                String colorCode = variantDto.getColorCode();
                String colorName = variantDto.getColorName();
                String size = variantDto.getSize();
                Long stock = variantDto.getStock();

                if (!colorIdMap.containsKey(colorCode)) {
                    Long colorId = dao.insertColor(colorName, colorCode);
                    colorIdMap.put(colorCode, colorId);
                }
                Long colorId = colorIdMap.get(colorCode);

                if (!sizeIdMap.containsKey(size)) {
                    Long sizeId = dao.insertSize(size);
                    sizeIdMap.put(size, sizeId);

                }
                Long sizeId = sizeIdMap.get(size);

                dao.insertVariant(colorCode, size, sizeId, colorId, stock, productId);
            }

            if (product.getImages() != null) {
                for (MultipartFile file : product.getImages()) {
                    String imageUrl = saveFile(file);
                    ImageDto imageDto = new ImageDto();
                    imageDto.setProductId(productId);
                    imageDto.setUrl(imageUrl);
                    dao.insertImage(imageDto);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean uploadProduct(ProductDto product, MultipartFile mainImage, List<MultipartFile> images) {
        try {
            String mainImageFileName = mainImage.getOriginalFilename();
            Path mainImageUrlPath = Paths.get(uploadDir).resolve(mainImageFileName);
            product.setMainImagePath(productElasticIpPrefix + mainImageFileName);
            Files.copy(mainImage.getInputStream(), mainImageUrlPath, StandardCopyOption.REPLACE_EXISTING);

            for (MultipartFile image : images) {
                String imagesUrl = image.getOriginalFilename();
                Path imagePath = Paths.get(uploadDir).resolve(imagesUrl);

                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return productElasticIpPrefix + fileName;
    }

    @Override
    public MessageDto<List<JsonDto>> getJsonsByCategory(String category, Long paging) {
        List<JsonDto> jsonDtos = dao.getJsonDtoByCategory(category, paging);
        MessageDto<List<JsonDto>> messageDto = new MessageDto<>();

        Integer nextPage = null;
        if (jsonDtos.size() > 6) {
            jsonDtos.remove(jsonDtos.size() - 1);
            nextPage = paging.intValue() + 1;
        }
        messageDto.setNextPage(nextPage);

        for (JsonDto jsonDto : jsonDtos) {
            Long setId = jsonDto.getId();
            jsonDto.setColors(dao.getColorsByProductId(setId));
            jsonDto.setImages(dao.getImagesByProductId(setId));
            jsonDto.setSizes(dao.getSizesByProductId(setId));
            jsonDto.setVariants(dao.getVariantsByProductId(setId));
        }

        messageDto.setData(jsonDtos);
        return messageDto;
    }


    @Override
    public MessageDto<List<SearchResponseDto>> getJsonsByKeyWord(String keyword, Long paging) {
        List<SearchResponseDto> searchResponseDtos = dao.getSearchResponseByKeyword(keyword, paging);
        MessageDto<List<SearchResponseDto>> messageDto = new MessageDto<>();

        Integer nextPage = null;
        if (searchResponseDtos.size() > 6) {
            searchResponseDtos.remove(searchResponseDtos.size() - 1);
            nextPage = paging.intValue() + 1;
        }
        messageDto.setNextPage(nextPage);

        for (SearchResponseDto searchResponseDto : searchResponseDtos) {
            Long setId = searchResponseDto.getId();
            searchResponseDto.setColors(dao.getColorsByProductId(setId));
            searchResponseDto.setImages(dao.getImagesByProductId(setId));
            searchResponseDto.setSizes(dao.getSizesByProductId(setId));
            searchResponseDto.setVariants(dao.getVariantsByProductId(setId));
        }
        messageDto.setData(searchResponseDtos);
        return messageDto;
    }

    @Override
    public MessageDto<JsonDto> getJsonById(Long id) {
        JsonDto jsonDto = dao.getJsonDtoById(id);
        Long setId = jsonDto.getId();
        jsonDto.setColors(dao.getColorsByProductId(setId));
        jsonDto.setSizes(dao.getSizesByProductId(setId));
        jsonDto.setVariants(dao.getVariantsByProductId(setId));
        jsonDto.setImages(dao.getImagesByProductId(setId));
        MessageDto<JsonDto> jsonDtoMessageDto = new MessageDto<>();
        jsonDtoMessageDto.setData(jsonDto);
        return jsonDtoMessageDto;
    }
}









