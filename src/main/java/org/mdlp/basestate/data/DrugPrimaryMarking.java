package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SSuvorov on 30.03.2017.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugPrimaryMarking extends CoreDrugOperation {
    @NotNull
    @JsonProperty("properties")
    private DrugPrimaryMarkingProperty drugPrimaryMarkingProperty;

    @NotNull
    @JsonProperty("kizs")
    protected List<PrimaryMarkingKiz> kizs = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PrimaryMarkingKiz {
        @NotNull
        @JsonProperty("sign_type")
        protected int signType;

        @NotNull
        @JsonProperty("sign")
        protected String sign;

        public PrimaryMarkingKiz(String sign, int signType) {
            this.signType = signType;
            this.sign = sign;
        }
    }
}
