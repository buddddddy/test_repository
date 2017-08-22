package org.mdlp.basestate.data.mod;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import org.mdlp.basestate.data.DrugProperty;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 29.05.2017
 * @package org.mdlp.basestate.data.mod
 */
public class ModDrugRecipeProperty extends DrugProperty {
    // Дата и время совершения операции
    @NonNull
    @JsonProperty("op_date")
    private BrokenOpDate opDate;

    // Номер льготного рецепта
    @NonNull
    @JsonProperty("prescription_num")
    private String prescriptionNum;

    // Дата регистрации льготного рецепта
    @NonNull
    @JsonProperty("prescription_date")
    private String prescriptionDate;

    @Override
    public String getOpDate() {
        return opDate.getOpDate();
    }
}
