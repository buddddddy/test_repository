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
public class DrugAcceptanceProperty extends DrugProperty {
    // Дата и время совершения операции
    @JsonProperty("op_date")
    private String opDate;

    // Тип операции приёмки на склад
    @JsonProperty("receiving_type")
    private int receivingType;

    // Адрес места осуществления деятельности покупателя (получателя)
    @JsonProperty("seller_id")
    private String sellerId;

    // Реквизиты документа приёмки: номер
    @JsonProperty("doc_num")
    private String docNum;

    @JsonProperty("accept_type")
    private Integer acceptType;

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
        return sellerId;
    }
}
