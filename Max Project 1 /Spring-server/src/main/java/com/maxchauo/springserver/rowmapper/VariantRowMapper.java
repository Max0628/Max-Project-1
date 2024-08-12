package com.maxchauo.springserver.rowmapper;

import com.maxchauo.springserver.dto.product.VariantResponseDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VariantRowMapper implements RowMapper<VariantResponseDto> {

    @Override
    public VariantResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        VariantResponseDto variantResponseDto = new VariantResponseDto();
        variantResponseDto.setColorCode(rs.getString("color_code"));
        variantResponseDto.setSize(rs.getString("size"));
        variantResponseDto.setStock(rs.getLong("stock"));
        return variantResponseDto;
    }
}
