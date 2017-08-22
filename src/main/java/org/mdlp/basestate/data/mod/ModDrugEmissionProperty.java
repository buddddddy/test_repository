package org.mdlp.basestate.data.mod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mdlp.basestate.data.DrugProperty;

/**
 * Created by SSuvorov on 30.03.2017.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModDrugEmissionProperty extends DrugProperty {
    // Дата и время совершения операции
    @JsonProperty("op_date")
    private BrokenOpDate opDate;

    // Вид документа подтверждения соответствия
    @JsonProperty("doc_type")
    private int docType;

    // Регистрационный номер документа подтверждения соответствия
    @JsonProperty("doc_num")
    private String docNum;

    // Дата регистрации документа подтверждения соответствия
    @JsonProperty("doc_date")
    private String docDate;

    @Override
    public String getOpDate() {
        return opDate.getOpDate();
    }
}
