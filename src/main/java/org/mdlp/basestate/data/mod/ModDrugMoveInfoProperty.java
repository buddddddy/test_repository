package org.mdlp.basestate.data.mod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mdlp.basestate.data.DrugMoveInfoProperty;
import org.mdlp.basestate.data.OperationDate;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 18.07.2017
 * @package org.mdlp.basestate.data.mod
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModDrugMoveInfoProperty extends DrugMoveInfoProperty {


    // Дата и время совершения операции
    @JsonProperty("op_date")
    protected OperationDate opDate;

    @Override
    public String getOpDate() {
        return opDate.getOpDate();
    }

    @Override
    public String getMemberId() {
        return getSystemSubjId();
    }

    @Override
    public String getReceiverId() {
        return getConsigneeId();
    }
}
