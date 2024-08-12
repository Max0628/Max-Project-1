package com.maxchauo.springserver.rowmapper;

import com.maxchauo.springserver.dto.product.SearchResponseDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchResponseRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        SearchResponseDto  searchResponseDto= new SearchResponseDto();
        searchResponseDto.setId(rs.getLong("id"));
        searchResponseDto.setCategory(rs.getString("category"));
        searchResponseDto.setTitle(rs.getString("title"));
        searchResponseDto.setDescription(rs.getString("description"));
        searchResponseDto.setPrice(rs.getLong("price"));
        searchResponseDto.setTexture(rs.getString("texture"));
        searchResponseDto.setWash(rs.getString("wash"));
        searchResponseDto.setPlace(rs.getString("place"));
        searchResponseDto.setNote(rs.getString("note"));
        searchResponseDto.setStory(rs.getString("story"));
        searchResponseDto.setMainImage(rs.getString("main_image"));
        searchResponseDto.setImages(new ArrayList<>());
        searchResponseDto.setVariants(new ArrayList<>());
        searchResponseDto.setColors(new ArrayList<>());
        searchResponseDto.setSizes(new ArrayList<>());
        return searchResponseDto;
    }
}
