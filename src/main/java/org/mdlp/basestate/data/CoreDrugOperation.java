package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 23.05.2017
 * @package org.mdlp.basestate.data
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class CoreDrugOperation {

    @NotNull
    @JsonProperty("query_id")
    private String queryId;

    public CoreDrugOperation() {
        queryId = UUID.randomUUID().toString();
    }
}
