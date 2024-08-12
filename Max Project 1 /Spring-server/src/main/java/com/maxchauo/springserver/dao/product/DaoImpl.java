package com.maxchauo.springserver.dao.product;

import com.maxchauo.springserver.dto.product.*;
import com.maxchauo.springserver.exception.BadRequestException;
import com.maxchauo.springserver.rowmapper.ColorRowMapper;
import com.maxchauo.springserver.rowmapper.JsonRowMapper;
import com.maxchauo.springserver.rowmapper.SearchResponseRowMapper;
import com.maxchauo.springserver.rowmapper.VariantRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class DaoImpl implements Dao {

    private static final Logger log = LoggerFactory.getLogger(DaoImpl.class);
    @Autowired
    private NamedParameterJdbcTemplate template;
    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Autowired
    private SqlInitializationAutoConfiguration sqlInitializationAutoConfiguration;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public Long insertProduct(ProductDto productDto) {
        String sql = "INSERT INTO product (category, title, description, price, texture, wash, place, note, story, main_image) " +
                "VALUES (:category, :title, :description, :price, :texture, :wash, :place, :note, :story, :mainImage)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("category", productDto.getCategory());
        params.addValue("title", productDto.getTitle());
        params.addValue("description", productDto.getDescription());
        params.addValue("price", productDto.getPrice());
        params.addValue("texture", productDto.getTexture());
        params.addValue("wash", productDto.getWash());
        params.addValue("place", productDto.getPlace());
        params.addValue("note", productDto.getNote());
        params.addValue("story", productDto.getStory());
        params.addValue("mainImage", productDto.getMainImagePath());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, params, keyHolder, new String[]{"id"});
        return keyHolder.getKey().longValue();
    }

    @Override
    public Long insertColor(String name, String code) {

        String sql = "INSERT INTO color (name, code) VALUES (:namePlaceHolder, :codePlaceHolder) ";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("namePlaceHolder", name);
        params.addValue("codePlaceHolder", code);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, params, keyHolder, new String[]{"id"});
        return keyHolder.getKey().longValue();
    }

    @Override
    public Long insertSize(String size) {//外部傳入一個參數size
        String checkSql = "SELECT id FROM size WHERE size = :size ";
        Map<String, Object> checkMap = new HashMap<>();
        checkMap.put("size", size);//
        List<Long> existingIds = template.queryForList(checkSql, new MapSqlParameterSource(checkMap), Long.class);
        if (!existingIds.isEmpty()) {
            return existingIds.get(0);
        } else {
            String sql = "INSERT INTO size(size) VALUES(:size) ";
            Map<String, Object> map = new HashMap<>();
            map.put("size", size);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            template.update(sql, new MapSqlParameterSource(map), keyHolder, new String[]{"id"});
            return keyHolder.getKey().longValue();
        }
    }

    @Override
    public Long insertVariant(String colorCode, String size, Long sizeId, Long colorId, Long stock, Long product_id) {
        String sql = "INSERT  INTO variant (color_code,size,size_id,color_id, stock, product_id) VALUES (:colorCode, :size,:size_id,:color_id, :stock, :product_id) ";
        Map<String, Object> map = new HashMap<>();
        map.put("colorCode", colorCode);
        map.put("size", size);
        map.put("size_id", sizeId);
        map.put("color_id", colorId);
        map.put("stock", stock);
        map.put("product_id", product_id);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, new MapSqlParameterSource(map), keyHolder);
        return keyHolder.getKey().longValue();
    }



    @Override
    public Long insertImage(ImageDto image) {
        String sql = "INSERT INTO image(url,product_id) VALUES (:url,:product_id) ";
        Map<String, Object> map = new HashMap<>();
        map.put("url", image.getUrl());
        map.put("product_id", image.getProductId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, new MapSqlParameterSource(map), keyHolder);
        return keyHolder.getKey().longValue();
    }


    //<---------------Tables connection---------------->
    @Override
    public List<ColorDto> getColorsByProductId(Long productId) {

        String sql = "SELECT DISTINCT c.id, c.name, c.code FROM color c " +
                "INNER JOIN variant v ON c.id = v.color_id " +
                "WHERE v.product_id = :productId ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);

        return template.query(sql, params, new ColorRowMapper());
    }

    @Override
    public List<String> getSizesByProductId(Long productId) {
        String sql = "SELECT DISTINCT s.size,s.id FROM size s " +
                "INNER JOIN variant v ON s.id = v.size_id " +
                "WHERE v.product_id = :productId ";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productId", productId);
        List<String> result = template.query(sql, param, (rs, rowNum) -> rs.getString("size"));
        return result;
    }


    @Override
    public List<VariantResponseDto> getVariantsByProductId(Long productId) {
        String sql = "SELECT DISTINCT v.color_code,v.size,v.stock,v.product_id FROM variant v " +
                "WHERE v.product_id = :product_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("product_id", productId);
        return template.query(sql, params, new VariantRowMapper());
    }

    @Override
    public List<String> getImagesByProductId(Long productId) {
        String sql = "SELECT DISTINCT i.url FROM image i WHERE i.product_id = :productId ";
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productId", productId);
        return template.query(sql, param, (rs, rowNum) -> rs.getString("url"));
    }

    public List<JsonDto> getJsonDtoByCategory(String category, Long paging) {
        String sql;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("offset", paging * 6);
        params.addValue("limit", 7);

        if (category == null || category.isEmpty() || category.equals("all")) {
            sql = "SELECT * FROM product LIMIT :limit OFFSET :offset ";
        } else if (category.equals("men") || category.equals("women") || category.equals("accessories")) {
            sql = "SELECT * FROM product WHERE category = :category LIMIT :limit OFFSET :offset ";

            params.addValue("category", category);
        } else {
            throw new BadRequestException("Invalid category");
        }
        return namedParameterJdbcTemplate.query(sql, params, new JsonRowMapper());
    }


    @Override
    public List<SearchResponseDto> getSearchResponseByKeyword(String keyword, Long paging) {
        String sql;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("offset", paging * 6);
        params.addValue("limit", 7);
        params.addValue("keyword", keyword);
        if (!keyword.toString().isEmpty()) {
            sql = "SELECT * FROM product WHERE title LIKE CONCAT('%', :keyword, '%') LIMIT :limit OFFSET :offset";
        } else {
            throw new BadRequestException("Invalid keyword");
        }
        return namedParameterJdbcTemplate.query(sql, params, new SearchResponseRowMapper());
    }


    @Override
    public JsonDto getJsonDtoById(Long id) {
        String sql;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        if (!id.toString().isEmpty()) {
            sql = "SELECT * FROM product WHERE id = :id ";
        } else {
            throw new BadRequestException("Invalid keyword");
        }
        return namedParameterJdbcTemplate.queryForObject(sql, params, new JsonRowMapper());
    }
}



