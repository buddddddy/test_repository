package org.mdlp.web.rest.get_kiz_full_info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RestFullKiz {

    @JsonProperty("id")
    private String id;

    @JsonProperty("ownerName")
    private String name;

    @JsonProperty("producer")
    private String producer;

    @JsonProperty("INN")
    private String INN;

    @JsonProperty("KPP")
    private String KPP;

    @JsonProperty("SGTIN")
    private String SGTIN;

    @JsonProperty("producerINN")
    private String producerINN;

    @JsonProperty("packerINN")
    private String packerINN;

    @JsonProperty("orderType")
    private String orderType;

    @JsonProperty("seriesNumber")
    private String seriesNumber;

    @JsonProperty("producerKPP")
    private String producerKPP;

    @JsonProperty("packerKPP")
    private String packerKPP;

    @JsonProperty("sscc")
    private String sscc;

    @JsonProperty("GTIN")
    private String GTIN;

    @JsonProperty("lpName")
    private String lpName;

    @JsonProperty("expirationDate")
    private String expirationDate;

    @JsonProperty("dosageForm")
    private String dosageForm;

    @JsonProperty("registrationCertificate")
    private String registrationCertificate;

    @JsonProperty("tradeName")
    private String tradeName;

    @JsonProperty("dosage")
    private String dosage;

    @JsonProperty("packageCompleteness")
    private String packageCompleteness;

}
