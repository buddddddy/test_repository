package org.mdlp.basestate.data.mod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.mdlp.basestate.data.DrugProperty;
import org.mdlp.basestate.data.OperationDate;
import org.mdlp.basestate.data.withdrawal.DrugWithdrawalProperty;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 01.07.2017
 * @package org.mdlp.basestate.data.mod
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModDrugWithdrawalProperty extends DrugProperty {

    // Дата и время совершения операции
    @JsonProperty("op_date")
    private OperationDate opDate;

    // Тип вывода из оборота
    @JsonProperty("withdrawal_type")
    private int withdrawalType;

    // Регистрационный номер документа
    @NonNull
    @JsonProperty("doc_num")
    private String docNum;

    // Дата регистрации документа
    @NonNull
    @JsonProperty("doc_date")
    private String docDate;

    @Override
    public String getOpDate() {
        return opDate.getOpDate();
    }
}
