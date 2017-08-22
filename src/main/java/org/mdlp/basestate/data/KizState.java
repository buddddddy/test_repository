package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by SSuvorov on 06.04.2017.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KizState {
    @JsonProperty("stateStatus")
    private int stateStatus;
    @JsonProperty("stateNumber")
    private Integer stateNumber;
    @JsonProperty("properties")
    private JsonNode properties;
    @JsonProperty("metadata")
    private JsonNode metadata;
    @JsonProperty("timestamp")
    private long timestamp;
    @JsonIgnore
    private DrugProperty drugProperty;

    public String getPropertiesJson() {
        return properties == null ? null : properties.toString();
    }

    public String getMetadataJson() {
        return metadata == null ? null : metadata.toString();
    }
}
