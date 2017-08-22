package org.mdlp.basestate.data.withdrawal;

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
 * @version 25.05.2017
 * @package org.mdlp.basestate.data.withdrawal
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugHealthCareProperty extends DrugProperty {
    // Дата и время совершения операции
    @NonNull
    @JsonProperty("op_date")
    private String opDate;

    // Номер документа
    @NonNull
    @JsonProperty("use_doc_num")
    private String useDocNum;

    // Дата документа
    @NonNull
    @JsonProperty("use_doc_date")
    private String useDocTime;

    @Override
    public String getOpDate() {
        return opDate;
    }
}
