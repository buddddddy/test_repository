package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @version 13.07.2017
 * @package org.mdlp.basestate.data
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugMoveInfoProperty extends DrugProperty {

    // Дата и время совершения операции
    @JsonProperty("op_date")
    protected String opDate;

    // Идентификатор получателя лекарственных препаратов в ИС "Маркировка"
    @JsonProperty("consignee_id")
    protected String consigneeId;

    // Регистрационный номер документа отгрузки
    @JsonProperty("doc_num")
    protected String docNum;

    // Дата регистрации документа отгрузки
    @JsonProperty("doc_date")
    protected String docDate;

    @Override
    public String getOpDate() {
        return opDate;
    }

    @Override
    @JsonIgnore
    public String getReceiverId() {
        return getConsigneeId();
    }
}
