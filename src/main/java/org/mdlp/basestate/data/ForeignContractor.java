package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 02.06.2017
 * @package org.mdlp.basestate.data
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForeignContractor extends BaseContractor {

    @JsonProperty("regDate")
    private OperationDate regDate;

    @JsonProperty("itin")
    private String itin;

    @JsonProperty("regNum")
    private String regNum;

    public String getRegDate() {
        return regDate.getOpDate();
    }
}
