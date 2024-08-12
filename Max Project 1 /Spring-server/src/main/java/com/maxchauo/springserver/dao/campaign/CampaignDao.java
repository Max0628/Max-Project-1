package com.maxchauo.springserver.dao.campaign;

import com.maxchauo.springserver.dto.campaign.CampaignDto;

import java.util.List;
import java.util.Map;

public interface CampaignDao {
    CampaignDto insertCampaign(CampaignDto campaignDto);

    List<CampaignDto> showAllCampaign();
}
