package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class DrugForeignImport extends CoreDrugOperation {
    @JsonProperty("properties")
    private DrugForeignImportProperty drugForeignImportProperty;

    @JsonProperty("kizs")
    protected List<ForeignImportKiz> kizs = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForeignImportKiz {
        @JsonProperty("sign_type")
        protected int signType;

        @JsonProperty("sign")
        protected String sign;

        @JsonProperty("metadata")
        protected ForeignImportKizMetadate foreignImportKizMetadate;

        public ForeignImportKiz(String sign, int signType) {
            this.signType = signType;
            this.sign = sign;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown =  true)
    public static class ForeignImportKizMetadate {
        // Отпускная цена производителя
        @JsonProperty("cost")
        private Long cost;

        // Сумма НДС (если сделка облагается НДС)
        @JsonProperty("vat_value")
        private Long vat_value;
    }
}
