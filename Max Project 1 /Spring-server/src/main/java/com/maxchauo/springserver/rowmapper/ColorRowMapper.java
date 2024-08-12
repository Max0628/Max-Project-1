package com.maxchauo.springserver.rowmapper;

import com.maxchauo.springserver.dto.product.ColorDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColorRowMapper implements RowMapper<ColorDto> {


    @Override
    public ColorDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        ColorDto colorDto = new ColorDto();
        colorDto.setCode(rs.getString("code"));
        colorDto.setName(rs.getString("name"));
        return colorDto;
    }
}
