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
 * @version 26.05.2017
 * @package org.mdlp.basestate.data.mod
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModDrugReceiveOwnerProperty extends DrugProperty {
    // Дата и время совершения операции
    @NonNull
    @JsonProperty("op_date")
    private BrokenOpDate opDate;

    // Регистрационный номер документа, подтверждающего перемещение
    @NonNull
    @JsonProperty("doc_num")
    private String docNum;

    // Дата регистрации документа, подтверждающего перемещение
    @NonNull
    @JsonProperty("doc_date")
    private String docDate;

    @Override
    public String getOpDate() {
        return opDate.getOpDate();
    }
}
