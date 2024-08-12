package com.maxchauo.springserver.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageDto<T> {

    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("next_paging")
    private Integer nextPage;
}
