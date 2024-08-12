package com.maxchauo.springserver.service.campaign;

import com.maxchauo.springserver.dao.campaign.CampaignDao;
import com.maxchauo.springserver.dto.campaign.CampaignDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface CampaignService {
    CampaignDto insertCampaign(CampaignDto campaignDto, MultipartFile file);

    List<CampaignDto> showAllCampaigns();
}
