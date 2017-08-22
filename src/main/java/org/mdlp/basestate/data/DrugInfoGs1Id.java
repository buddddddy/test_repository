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
public class DrugInfoGs1Id {
    @JsonProperty(value = "gtin")
    private String gtin;

    @JsonProperty(value = "id")
    private String id;
}

