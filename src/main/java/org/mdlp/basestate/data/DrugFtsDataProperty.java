package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
public class DrugFtsDataProperty extends DrugProperty {

    // Дата и время совершения операции
    @JsonProperty("op_date")
    private String opDate;

    // Вид документа подтверждения соответствия
    @NonNull
    @JsonProperty("doc_type")
    private Integer docType;

    // Регистрационный номер документа подтверждения соответствия
    @NonNull
    @JsonProperty("doc_num")
    private String docNum;


    // Дата регистрации документа подтверждения соответствия
    @NonNull
    @JsonProperty("doc_date")
    private String docDate;

    @NonNull
    @JsonProperty("control_type")
    private int controlType;

    @NonNull
    @JsonProperty("custom_procedure_code")
    private int customProcedureCode;

    @NonNull
    @JsonProperty("gtd_info")
    private GtdInfo gtdInfo;

    @NonNull
    @JsonProperty("goods_desision")
    private GoodsDecision goodsDecision;

    @Override
    public String getOpDate() {
        return opDate;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GtdInfo {

        //Код таможенного органа
        @NonNull
        @JsonProperty("customs_code")
        private String customsCode;

        //Дата регистрации декларации на товары
        @NonNull
        @JsonProperty("reg_date")
        private String regDate;

        //Регистрационный номер декларации на товары
        @NonNull
        @JsonProperty("reg_number")
        private String regNumber;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoodsDecision {

        //Код принятого решения
        @NonNull
        @JsonProperty("decision_code")
        private String decisionCode;

        //Дата и время принятия решения
        @NonNull
        @JsonProperty("decision_date")
        private String decisionDate;
    }
}
