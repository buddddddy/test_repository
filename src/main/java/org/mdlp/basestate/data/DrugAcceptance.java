package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SSuvorov on 30.03.2017.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugAcceptance extends CoreDrugOperation {
    @JsonProperty("properties")
    private DrugAcceptanceProperty drugAcceptanceProperty;

    @JsonProperty("kizs")
    protected List<AcceptanceKiz> kizs = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AcceptanceKiz {
        @JsonProperty("sign_type")
        protected int signType;

        @JsonProperty("sign")
        protected String sign;

        @JsonProperty("metadata")
        protected AcceptanceKizMetadata metadata;

        public AcceptanceKiz(String sign, int signType, AcceptanceKizMetadata metadata) {
            this.signType = signType;
            this.sign = sign;
            this.metadata = metadata;
        }
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper=true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AcceptanceKizMetadata extends CoreDrugMetadata {
        // Сумма НДС (если сделка облагается НДС) (в копейках)
        @JsonProperty("vat_value")
        private Long vatValue;

        // Отпускная цена производителя (в копейках)
        @JsonProperty("cost")
        private Long cost;

        public AcceptanceKizMetadata(Long vatValue, Long cost) {
            this.vatValue = vatValue;
            this.cost = cost;
        }


        @JsonIgnore
        public Long getPriceInRubbles() {
            return cost/100;
        }
    }
}
