package org.mdlp.basestate.data.mod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mdlp.basestate.data.DrugProperty;

/**
 * Created by SSuvorov on 30.03.2017.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModDrugAcceptanceProperty extends DrugProperty {
    // Дата и время совершения операции
    @JsonProperty("op_date")
    private BrokenOpDate opDate;

    // Тип операции приёмки на склад
    @JsonProperty("receiving_type")
    private int receivingType;

    // Адрес места осуществления деятельности покупателя (получателя)
    @JsonProperty("seller_id")
    private String sellerId;

    // Реквизиты документа приёмки: номер
    @JsonProperty("doc_num")
    private String docNum;

    // Реквизиты документа приёмки: дата
    @JsonProperty("doc_date")
    private String docDate;

    @Override
    public String getOpDate() {
        return opDate.getOpDate();
    }

    @Override
    @JsonIgnore
    public String getReceiverId() {
        return sellerId;
    }
}
