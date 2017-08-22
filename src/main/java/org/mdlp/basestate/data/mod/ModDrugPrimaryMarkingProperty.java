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
public class ModDrugPrimaryMarkingProperty extends DrugProperty{
    // Дата и время совершения операции
    @JsonProperty("op_date")
    private BrokenOpDate opDate;

    // Тип производственного заказа
    @JsonProperty("order_type")
    private int orderType;

    // Номер производственной серии
    @JsonProperty("batch")
    private String batch;

    // Идентификатор собственника в ИС "Маркировка товаров"
    @JsonProperty("owner_id")
    private String ownerId;

    // Срок годности
    @JsonProperty("exp_date")
    private String expDate;

    // Идентификационный код потребительских упаковок
    @JsonProperty("gtin")
    private String gtin;

    // Код ТН ВЭД
    @JsonProperty("hs_code")
    private String hsCode;

    @Override
    public String getOpDate() {
        return opDate.getOpDate();
    }
}
