package org.mdlp.web.rest.get_kiz_list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RestShortKiz {

    @JsonProperty("id")
    private String id;

    @JsonProperty("SGTIN")
    private String SGTIN;

    @JsonProperty("seriesNumber")
    private String seriesNumber;

    @JsonProperty("ownerINN")
    private String ownerINN;

    @JsonProperty("ownerTitle")
    private String ownerTitle;

    @JsonProperty("GTIN")
    private String GTIN;

    @JsonProperty("name")
    private String name;

    @JsonProperty("statusId")
    private String statusId;

    @JsonProperty("releaseOperationDate")
    private LocalDateTime releaseOperationDate;

    @JsonProperty("lastOperationDate")
    private LocalDateTime lastOperationDate;

    @JsonProperty("sscc")
    private String sscc;

    @JsonProperty("federalSubjectName")
    private String federalSubjectName;

}
