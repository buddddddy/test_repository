package org.mdlp.basestate.data.aggregation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mdlp.basestate.data.CoreDrugOperation;
import org.mdlp.basestate.data.packing.DrugUnitPackProperty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 27.06.2017
 * @package org.mdlp.basestate.data.aggregation
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugUnitExtract extends CoreDrugOperation {

    @JsonProperty("properties")
    private DrugUnitExtractProperty drugUnitExtractProperty;

    @NotNull
    @JsonProperty("kizs")
    protected List<UnitExtractKiz> kizs = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UnitExtractKiz {
        @NotNull
        @JsonProperty("sign_type")
        protected int signType;

        @NotNull
        @JsonProperty("sign")
        protected String sign;

        public UnitExtractKiz(String sign, int signType) {
            this.signType = signType;
            this.sign = sign;
        }
    }
}
