package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by SSuvorov on 05.04.2017.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractorEGRUL {
    @JsonProperty("POS_NAME")
    private String posName;

    @JsonProperty("inn")
    private String inn;

    @JsonProperty("ORG_NAME")
    private String orgName;

    @JsonProperty("KPP")
    private String kpp;

    @JsonProperty("LAST_NAME")
    private String lastName;

    @JsonProperty("MIDDLE_NAME")
    private String middleName;

    @JsonProperty("FIRST_NAME")
    private String firstName;

    @JsonProperty("OGRN")
    private String ogrn;

    @JsonProperty("id")
    private String id;

    private String registryType;
}
