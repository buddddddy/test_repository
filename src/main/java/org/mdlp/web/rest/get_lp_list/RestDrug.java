package org.mdlp.web.rest.get_lp_list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RestDrug {

    @JsonProperty("GTIN")
    private String gtin;

    @JsonProperty("producerINN")
    private String producerInn;

    @JsonProperty("name")
    private String name;

    @JsonProperty("form")
    private String form;

    @JsonProperty("regOwnerName")
    private String regOwnerName;

    @JsonProperty("regNumber")
    private String regNumber;

    @JsonProperty("regDate")
    private LocalDateTime regDate;

    @JsonProperty("gnvlp")
    private boolean gnvlp;

    @JsonProperty("regExpirationDate")
    private LocalDateTime regExpirationDate;

    @JsonProperty("regExpirationDateIsUnlimited")
    private boolean regExpirationDateIsUnlimited;

    @JsonProperty("maxPrice")
    private String maxPrice;

}
