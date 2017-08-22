package org.mdlp.basestate.data.statistic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mdlp.basestate.data.OperationDate;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationEntry {

    @JsonProperty("system_subj_id")
    private String id;

    @JsonProperty("ORG_NAME")
    private String orgName;

    @JsonProperty("MIDDLE_NAME")
    private String middleName;

    @JsonProperty("OGRN")
    private String OGRN;

    @JsonProperty("KPP")
    private String KPP;

    @JsonProperty("LAST_NAME")
    private String lastName;

    @JsonProperty("FIRST_NAME")
    private String firstName;

    @JsonProperty("inn")
    private String INN;

    @JsonProperty("op_date")
    private OperationDate regDate;

    @JsonProperty("branches")
    private List<String> branches;

    @JsonProperty("safe_warehouses")
    private List<String> safeWarehouses;
}
