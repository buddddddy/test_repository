package org.mdlp.basestate.data.aggregation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mdlp.basestate.data.CoreDrugOperation;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author PVBorisov (Pavel.V.Borisov@firstlinesoftware.com)
 * @version 28.06.2017
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugUnitUnpack extends CoreDrugOperation {

    @JsonProperty("properties")
    private DrugUnitUnpackProperty drugUnitUnpackProperty;

    @NotNull
    @JsonProperty("kizs")
    protected List<UnitUnpackKiz> kizs = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UnitUnpackKiz {
        @NotNull
        @JsonProperty("sign_type")
        protected int signType;

        @NotNull
        @JsonProperty("sign")
        protected String sign;

        @NotNull
        @JsonProperty("metadata")
        protected UnitUnpackKizMetadata metadata;

        public UnitUnpackKiz(String sign, int signType, UnitUnpackKizMetadata metadata) {
            this.sign = sign;
            this.signType = signType;
            this.metadata = metadata;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UnitUnpackKizMetadata {

        // Тип расформирования
        @JsonProperty("recursive")
        private int recursive;

        public UnitUnpackKizMetadata(int recursive) {
            this.recursive = recursive;
        }
    }
}
