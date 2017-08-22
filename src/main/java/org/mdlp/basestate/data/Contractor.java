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
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contractor extends BaseContractor {
    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private int type;

    @JsonProperty("inn")
    private String inn;

    @JsonProperty("kpp")
    private String kpp;

    @JsonProperty("regNum")
    private String regNum;

    @JsonProperty("regDate")
    private OperationDate regDate;

    public String getRegDate() {
        return regDate.getOpDate();
    }
}
