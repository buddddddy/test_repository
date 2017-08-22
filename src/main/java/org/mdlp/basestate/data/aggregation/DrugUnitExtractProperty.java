package org.mdlp.basestate.data.aggregation;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @version 27.06.2017
 * @package org.mdlp.basestate.data.aggregation
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugUnitExtractProperty extends DrugProperty {

    // Идентификатор субъекта обращения (субъекта учёта) в ИС "Маркировка товаров"
    @NonNull
    @JsonIgnore
    private String memberId;

    // Дата и время совершения операции
    @NonNull
    @JsonProperty("op_date")
    private String opDate;

    @Override
    public String getOpDate() {
        return opDate;
    }
}
