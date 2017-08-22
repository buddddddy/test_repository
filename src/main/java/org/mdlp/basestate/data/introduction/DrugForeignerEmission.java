package org.mdlp.basestate.data.introduction;

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
 * @version 29.05.2017
 * @package org.mdlp.basestate.data.introduction
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugForeignerEmission extends CoreDrugOperation {
    @JsonProperty("properties")
    private DrugForeignerEmissionProperty drugForeignerEmissionProperty;

    @NotNull
    @JsonProperty("kizs")
    protected List<ForeignerEmissionKiz> kizs = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForeignerEmissionKiz {
        @NotNull
        @JsonProperty("sign_type")
        protected int signType;

        @NotNull
        @JsonProperty("sign")
        protected String sign;

        public ForeignerEmissionKiz(String sign, int signType) {
            this.signType = signType;
            this.sign = sign;
        }
    }
}
