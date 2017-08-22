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
public class ModHealthCareProperty extends DrugProperty {
    // Дата и время совершения операции
    @NonNull
    @JsonProperty("op_date")
    private BrokenOpDate opDate;

    // Вид документа
    @JsonProperty("use_doc_type")
    private String useDocType;

    // Номер документа
    @NonNull
    @JsonProperty("use_doc_num")
    private String useDocNum;

    // Дата документа
    @NonNull
    @JsonProperty("use_doc_time")
    private String useDocTime;

    @Override
    public String getOpDate() {
        return opDate.getOpDate();
    }
}