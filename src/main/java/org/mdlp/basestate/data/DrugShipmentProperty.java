package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by SSuvorov on 30.03.2017.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugShipmentProperty extends DrugProperty {
    // Дата и время совершения операции
    @JsonProperty("op_date")
    private String opDate;

    @JsonProperty("accept_type")
    private Integer acceptType;

    @JsonProperty("shipment_type")
    private int shipmentType = 1;

    // Идентификатор покупателя лекарственных препаратов в ИС "Маркировка"
    @JsonProperty("consignee_id")
    private String consigneeId;

    @JsonProperty("source_type")
    private int sourceType = 1;

    // Реквизиты документа приёмки: номер
    @JsonProperty("doc_num")
    private String docNum;

    // Реквизиты документа приёмки: дата
    @JsonProperty("doc_date")
    private String docDate;

    @Override
    public String getOpDate() {
        return opDate;
    }

    @Override
    @JsonIgnore
    public String getReceiverId() {
        return getSystemSubjId();
    }
}
