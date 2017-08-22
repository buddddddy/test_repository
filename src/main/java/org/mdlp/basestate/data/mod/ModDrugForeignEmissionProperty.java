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
 * @version 29.05.2017
 * @package org.mdlp.basestate.data.mod
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModDrugForeignEmissionProperty  extends DrugProperty {
    // Дата и время совершения операции
    @NonNull
    @JsonProperty("op_date")
    private BrokenOpDate opDate;

    // Идентификатор производителя, осуществившего выпускающий контроль качества, в ИС "Маркировка"
    @NonNull
    @JsonProperty("control_id")
    private String controlId;

    // Код ТН ВЭД
    @NonNull
    @JsonProperty("hs_code")
    private String hsCode;

    // Идентификационный номер товара (GTIN)
    @NonNull
    @JsonProperty("gtin")
    private String gtin;

    // Номер производственной серии
    @NonNull
    @JsonProperty("batch")
    private String batch;

    // Срок годности
    @NonNull
    @JsonProperty("exp_date")
    private String expDate;

    @Override
    public String getOpDate() {
        return opDate.getOpDate();
    }


}
