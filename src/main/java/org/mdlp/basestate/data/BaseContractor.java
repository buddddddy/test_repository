package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mdlp.basestate.data.mod.BrokenOpDate;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 18.05.2017
 * @package org.mdlp.basestate.data.processor
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseContractor {

    @JsonProperty("id")
    private String id;

    @JsonProperty("op_date")
    private BrokenOpDate opDate;

    public String getOpDate() {
        return opDate != null ? opDate.getOpDate() : null;
    }
}
