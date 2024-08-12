package com.maxchauo.springserver.dao.campaign;

import com.maxchauo.springserver.dto.campaign.CampaignDto;
import com.maxchauo.springserver.exception.BadRequestException;
import com.maxchauo.springserver.rowmapper.CampaignRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CampaignDaoImpl implements CampaignDao {

    @Autowired
    private NamedParameterJdbcTemplate template;
    @Autowired
    private SqlInitializationAutoConfiguration sqlInitializationAutoConfiguration;


    @Override
    public CampaignDto insertCampaign(CampaignDto campaignDto) {
        Long productId = campaignDto.getProductId();
        String story = campaignDto.getStory();
        String picture = campaignDto.getPicture();
        String sql = "INSERT INTO campaign (product_id,picture,story) VALUES(:productId,:picture,:story) ";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
        params.addValue("picture", picture);
        params.addValue("story", story);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = template.update(sql, params, keyHolder, new String[]{"id"});
        if (result > 0) {
            Number generatedId = keyHolder.getKey();
            if (generatedId != null) {
                campaignDto.setId(generatedId.longValue());
            }
            return campaignDto;
        } else {
            throw new BadRequestException("Failed to insert campaign");
        }
    }

    @Override
    public List<CampaignDto> showAllCampaign() {
        String sql = "SELECT * FROM campaign";
        return template.query(sql, new CampaignRowMapper());
    }
}


