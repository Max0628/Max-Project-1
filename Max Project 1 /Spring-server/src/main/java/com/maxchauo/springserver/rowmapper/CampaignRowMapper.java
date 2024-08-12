package com.maxchauo.springserver.rowmapper;

import com.maxchauo.springserver.dto.campaign.CampaignDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CampaignRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        CampaignDto campaignDto = new CampaignDto();
        campaignDto.setId(rs.getLong("id"));
        campaignDto.setProductId(rs.getLong("product_id"));
        campaignDto.setPicture(rs.getString("picture"));
        campaignDto.setStory(rs.getString("story"));
        return campaignDto;
    }
}
