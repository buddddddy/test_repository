package org.mdlp.basestate.data.mod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.mdlp.basestate.data.DrugProperty;
import org.mdlp.basestate.data.OperationDate;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 05.07.2017
 * @package org.mdlp.basestate.data.mod
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModDrugMovePlaceProperty extends DrugProperty {


    // Дата и время совершения операции
    @JsonProperty("op_date")
    private OperationDate opDate;

    // Идентификатор получателя в ИС "Маркировка товаров"
    @NonNull
    @JsonProperty("consignee_id")
    private String consigneeId;

    // Тип хранения
    @NonNull
    @JsonProperty("storage_type")
    private Integer storageType;

    // Номер документа, подтверждающего перемещение
    @NonNull
    @JsonProperty("doc_num")
    private String docNum;

    // Дата документа, подтверждающего перемещение
    @NonNull
    @JsonProperty("doc_date")
    private String docDate;

    @Override
    public String getOpDate() {
        return opDate.getOpDate();
    }

    @Override
    public String getReceiverId() {
        return getConsigneeId();
    }
}
