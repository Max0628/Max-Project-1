package com.maxchauo.springserver.rowmapper;

import com.maxchauo.springserver.dto.product.SizeDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SizeRowMapper implements RowMapper<SizeDto> {
    @Override
    public SizeDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        SizeDto sizeDto = new SizeDto();
        sizeDto.setSize(rs.getString("size"));
        return sizeDto;
    }
}
