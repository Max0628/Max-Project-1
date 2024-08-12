package com.maxchauo.springserver.rowmapper;

import com.maxchauo.springserver.dto.product.ImageDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageRowMapper implements RowMapper<ImageDto> {

    @Override
    public ImageDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        ImageDto imageDto = new ImageDto();
        imageDto.setUrl(rs.getString("url"));
        return imageDto;
    }
}
