package com.maxchauo.springserver.controller.campaign;

import com.maxchauo.springserver.dto.campaign.CampaignDto;
import com.maxchauo.springserver.exception.ServerErrorException;
import com.maxchauo.springserver.service.campaign.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("api/1.0/marketing/campaigns")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @PostMapping("/add")
    public ResponseEntity<?> addCampaign(
            @RequestParam("productId") Long productId,
            @RequestParam("story") String story,
            @RequestParam("file") MultipartFile file) {
        try {
            CampaignDto campaignDto = new CampaignDto();
            campaignDto.setProductId(productId);
            campaignDto.setStory(story);
            CampaignDto result = campaignService.insertCampaign(campaignDto, file);
            return ResponseEntity.ok(result);
        } catch (ServerErrorException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllCampaigns() {
        List<CampaignDto> campaigns = campaignService.showAllCampaigns();
        return ResponseEntity.ok(Map.of("data", campaigns));
    }
}
