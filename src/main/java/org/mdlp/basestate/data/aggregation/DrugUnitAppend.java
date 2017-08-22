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
 * @author SSukhanov
 * @version 05.07.2017
 * @package org.mdlp.basestate.data.aggregation
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugUnitAppend  extends CoreDrugOperation {

    @JsonProperty("properties")
    private DrugUnitAppendProperty drugUnitAppendProperty;

    @NotNull
    @JsonProperty("kizs")
    protected List<UnitAppendKiz> kizs = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UnitAppendKiz {
        @NotNull
        @JsonProperty("sign_type")
        protected int signType;

        @NotNull
        @JsonProperty("sign")
        protected String sign;

        public UnitAppendKiz(String sign, int signType) {
            this.signType = signType;
            this.sign = sign;
        }
    }
}
