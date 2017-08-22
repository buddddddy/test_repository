package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mdlp.basestate.data.mod.BrokenOpDate;

import javax.validation.constraints.NotNull;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 24.05.2017
 * @package org.mdlp.basestate.data
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KizStateNew {

    @NotNull
    @JsonProperty("stateNumber")
    private Integer stateNumber;

    @NotNull
    @JsonProperty("code")
    private Integer code;

    @NotNull
    @JsonProperty("op_date")
    private BrokenOpDate opDate;

    @NotNull
    @JsonProperty("req_body")
    private ReqBody reqBody;

    @JsonIgnore
    private DrugProperty drugProperty;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReqBody {

        @NotNull
        @JsonProperty("stateNumber")
        private Integer stateNumber;

        @JsonProperty("properties")
        private JsonNode properties;

        @JsonIgnore
        private DrugProperty drugProperty;

        public String getPropertiesJson() {
            return properties == null ? null : properties.toString();
        }
    }
}


