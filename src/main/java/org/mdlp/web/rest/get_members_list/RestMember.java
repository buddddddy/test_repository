package org.mdlp.web.rest.get_members_list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RestMember {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("INN")
    private String INN;

    @JsonProperty("OGRN")
    private String OGRN;

    @JsonProperty("email")
    private String email;

    @JsonProperty("certOwnerFIO")
    private String certOwnerFIO;

    @JsonProperty("KPP")
    private String kpp;

    @JsonProperty("REG_DATE")
    private String regDate;

    @JsonProperty("isResident")
    private boolean isResident;

    @JsonProperty("registryType")
    private String registryType;
}
