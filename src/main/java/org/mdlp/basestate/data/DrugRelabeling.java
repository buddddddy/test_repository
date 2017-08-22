package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 05.07.2017
 * @package org.mdlp.basestate.data
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugRelabeling extends CoreDrugOperation {
    @JsonProperty("properties")
    private DrugRelabelingProperty drugRelabelingProperty;

    @JsonProperty("kizs")
    protected List<RelabelingKiz> kizs = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RelabelingKiz {
        @JsonProperty("sign_type")
        protected int signType;

        @JsonProperty("sign")
        protected String sign;

        @JsonProperty("metadata")
        protected RelabelingKizMetadata relabelingKizMetadata;

        public RelabelingKiz(String sign, int signType, RelabelingKizMetadata relabelingKizMetadata) {
            this.signType = signType;
            this.sign = sign;
            this.relabelingKizMetadata = relabelingKizMetadata;
        }
    }


    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RelabelingKizMetadata {

        // Идентификационный код новой вторичной (потребительской) упаковки
        @NonNull
        @JsonProperty("sgtin_new")
        private String sgtinNew;

    }
}
