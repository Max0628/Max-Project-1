package com.maxchauo.springserver.service.campaign;

import com.maxchauo.springserver.dao.campaign.CampaignDao;
import com.maxchauo.springserver.dto.campaign.CampaignDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class CampaignServiceImpl implements CampaignService {

    @Autowired
    private CampaignDao campaignDao;

    @Value("${file.upload-dir-campaign}")
    private String uploadDirCampaign;

    @Value("${elastic.ip.prefix.campaign}")
    private String campaignElasticIpPrefix;

    @Override
    public CampaignDto insertCampaign(CampaignDto campaignDto, MultipartFile file) {
        try {
            String fileUrl = uploadFile(file);//get the prefix+filename string
            campaignDto.setPicture(fileUrl); //store in mysql
            return campaignDao.insertCampaign(campaignDto);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CampaignDto> showAllCampaigns() {
        return campaignDao.showAllCampaign();
    }

    //tools
    private String uploadFile(MultipartFile file) throws IOException {
        //check if the file is empty
        if (file.isEmpty()) {
            throw new IOException("File is empty.");
        }

        //check the directory is existed or not
        File dir = new File(uploadDirCampaign);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        String fileName = file.getOriginalFilename(); //get the filename
        Path filePath = Paths.get(uploadDirCampaign, fileName); //saving file as filename & path
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return campaignElasticIpPrefix + fileName;
    }
}