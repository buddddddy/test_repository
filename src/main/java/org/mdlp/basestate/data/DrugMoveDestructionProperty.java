package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.mdlp.basestate.data.address.Address;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 05.07.2017
 * @package org.mdlp.basestate.data
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugMoveDestructionProperty extends DrugProperty {

    // Дата и время совершения операции
    @JsonProperty("op_date")
    private String opDate;

    // ИНН/КПП организации, осуществляющей уничтожение
    @NonNull
    @JsonProperty("destruction_org_id")
    private DestructionOrgId destruction_org_id;

    // Адрес места осуществления деятельности организации, осуществляющей уничтожение
    @NonNull
    @JsonProperty("destruction_address")
    private Address destructionAddress;

    // Номер договора передачи на уничтожение
    @NonNull
    @JsonProperty("contract_num")
    private String contractNum;

    // Дата договора передачи на уничтожение
    @NonNull
    @JsonProperty("contract_date")
    private String contractDate;

    // Номер акта передачи на уничтожение
    @NonNull
    @JsonProperty("act_num")
    private String actNum;

    // Дата акта передачи на уничтожение
    @NonNull
    @JsonProperty("act_date")
    private String actDate;

    @Override
    public String getOpDate() {
        return opDate;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DestructionOrgId {

        // Причина (основание) передачи на уничтожение
        @JsonProperty("inn")
        private String inn;

        // Реквизиты решения Росздравнадзора о выводе из оборота
        @JsonProperty("kpp")
        private String kpp;
    }
}
