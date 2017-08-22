package org.mdlp.basestate.data.packing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mdlp.basestate.data.CoreDrugOperation;
import org.mdlp.basestate.data.withdrawal.DrugHealthCareProperty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 25.05.2017
 * @package org.mdlp.basestate.data.packing
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugUnitPack extends CoreDrugOperation {
    @JsonProperty("properties")
    private DrugUnitPackProperty drugUnitPackProperty;

    @NotNull
    @JsonProperty("kizs")
    protected List<UnitPackKiz> kizs = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UnitPackKiz {
        @NotNull
        @JsonProperty("sign_type")
        protected int signType;

        @NotNull
        @JsonProperty("sign")
        protected String sign;

        public UnitPackKiz(String sign, int signType) {
            this.signType = signType;
            this.sign = sign;
        }
    }
}
