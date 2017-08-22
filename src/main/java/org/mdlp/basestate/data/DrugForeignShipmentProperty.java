package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class DrugForeignShipmentProperty extends DrugProperty {
    // Дата и время совершения операции
    @JsonProperty("op_date")
    private String opDate;

    // Идентификатор продавца лекарственных препаратов в ИС "Маркировка"
    @JsonProperty("seller_id")
    private String sellerId;

    // Идентификатор покупателя лекарственных препаратов в ИС "Маркировка"
    @JsonProperty("consumer_id")
    private String consumerId;

    // Реквизиты документа отгрузки: номер
    @JsonProperty("invoice_num")
    private String invoiceNum;

    // Реквизиты документа отгрузки: дата
    @JsonProperty("invoice_date")
    private String invoiceDate;

    @Override
    public String getOpDate() {
        return opDate;
    }
}
