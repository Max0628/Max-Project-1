package com.maxchauo.springserver.rowmapper;

import com.maxchauo.springserver.dto.product.JsonDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JsonRowMapper implements RowMapper<JsonDto> {


    @Override
    public JsonDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        JsonDto jsonDto = new JsonDto();
        jsonDto.setId(rs.getLong("id"));
        jsonDto.setCategory(rs.getString("category"));
        jsonDto.setTitle(rs.getString("title"));
        jsonDto.setDescription(rs.getString("description"));
        jsonDto.setPrice(rs.getLong("price"));
        jsonDto.setTexture(rs.getString("texture"));
        jsonDto.setWash(rs.getString("wash"));
        jsonDto.setPlace(rs.getString("place"));
        jsonDto.setNote(rs.getString("note"));
        jsonDto.setStory(rs.getString("story"));
        jsonDto.setColors(new ArrayList<>());
        jsonDto.setSizes(new ArrayList<>());
        jsonDto.setVariants(new ArrayList<>());
        jsonDto.setMainImage(rs.getString("main_image"));
        jsonDto.setImages(new ArrayList<>());
        return jsonDto;
    }
}
