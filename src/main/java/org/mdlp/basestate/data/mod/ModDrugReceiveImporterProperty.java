package org.mdlp.basestate.data.mod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.mdlp.basestate.data.DrugProperty;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 29.05.2017
 * @package org.mdlp.basestate.data.mod
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModDrugReceiveImporterProperty extends DrugProperty {
    // Дата и время совершения операции
    @NonNull
    @JsonProperty("op_date")
    private BrokenOpDate opDate;

    // Реквизиты документа приёмки: номер
    @NonNull
    @JsonProperty("doc_num")
    private String docNum;

    // Реквизиты документа приёмки: дата
    @NonNull
    @JsonProperty("doc_date")
    private String docDate;

    // Идентификатор ввозимой партии
    @NonNull
    @JsonProperty("delivery_id")
    private String deliveryId;

    @NonNull
    @JsonProperty("gtd_info")
    private GtdInfo gtdInfo;

    @NonNull
    @JsonProperty("goods_decision")
    private GoodsDecision goodsDecision;

    @Override
    public String getOpDate() {
        return opDate.getOpDate();
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GtdInfo {

        @NonNull
        @JsonProperty("customs_code")
        private String customsCode;

        @NonNull
        @JsonProperty("reg_date")
        private String regDate;

        @NonNull
        @JsonProperty("reg_number")
        private String regNumber;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoodsDecision {

        @NonNull
        @JsonProperty("decision_code")
        private String decisionCode;

        @NonNull
        @JsonProperty("decision_date")
        private String decisionDate;
    }

}

