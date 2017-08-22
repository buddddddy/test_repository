package org.mdlp.basestate.data.mod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mdlp.basestate.data.DrugProperty;
import org.mdlp.basestate.data.OperationDate;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 05.07.2017
 * @package org.mdlp.basestate.data.mod
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModDrugForeignImportProperty extends DrugProperty {

    // Дата и время совершения операции
    @JsonProperty("op_date")
    private OperationDate opDate;

    // Идентификатор продавца лекарственных препаратов в ИС "Маркировка"
    @JsonProperty("seller_id")
    private String sellerId;

    // Реквизиты документа отгрузки: номер
    @JsonProperty("invoice_num")
    private String invoiceNum;

    // Реквизиты документа отгрузки: дата
    @JsonProperty("invoice_date")
    private String invoiceDate;

    // Отпускная цена производителя
    @JsonProperty("cost")
    private Long cost;

    // Сумма НДС (если сделка облагается НДС)
    @JsonProperty("vat_value")
    private Long vat_value;

    @Override
    public String getOpDate() {
        return opDate.getOpDate();
    }
}
