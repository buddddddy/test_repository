package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 13.07.2017
 * @package org.mdlp.basestate.data
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugMoveInfo extends CoreDrugOperation {

    @JsonProperty("properties")
    private DrugMoveInfoProperty drugMoveInfoProperty;

    @JsonProperty("gtins")
    private List<DrugMoveGtin> gtins;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DrugMoveGtin {
        // Идентификационный номер товара (GTIN)
        @NonNull
        @JsonProperty("gtin")
        private String gtin;

        // Номер производственной серии
        @NonNull
        @JsonProperty("batch")
        private String batch;

        // Количество вторичных (потребительских) упаковок
        @NonNull
        @JsonProperty("count")
        private Integer cost;
    }
}
