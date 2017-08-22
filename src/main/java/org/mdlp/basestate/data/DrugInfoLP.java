package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by SSuvorov on 05.04.2017.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugInfoLP {
    @JsonProperty(value = "system_subj_id")
    private String systemSubjId;

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "gtin")
    private String gtin;

    @JsonProperty(value = "regNum")
    private String regNum;

    @JsonProperty(value = "regDate")
    private OperationDate regDate;
}
