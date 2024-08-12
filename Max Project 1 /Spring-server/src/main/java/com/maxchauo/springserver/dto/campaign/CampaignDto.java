package com.maxchauo.springserver.dto.campaign;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"product_id", "picture", "story"})
public class CampaignDto {
    @JsonIgnore
    private Long id;
    @JsonProperty("product_id")
    private Long productId;
    private String picture;
    private String story;
}
