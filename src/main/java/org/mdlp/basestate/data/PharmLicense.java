package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author PVBorisov (Pavel.V.Borisov@firstlinesoftware.com)
 * @version 29.06.2017
 */
@Data
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
public class PharmLicense {

    @JsonProperty("ID")
    private String id;

    // ИНН организации
    @JsonProperty("INN")
    private String INN;

    // Наименование организации
    @NotNull
    @JsonProperty("ORG_NAME")
    private String orgName;

    // Номер лицензии
    @NotNull
    @JsonProperty("L_NUM")
    private String licenseNumber;

    @NotNull
    // дата начала действия лицензии
    @JsonProperty("START_DATE")
    private OperationDate startDate;

    // дата окончания действия лицензии
    @JsonProperty("END_DATE")
    private OperationDate endDate;

    // Статус действия лицензии
    @NotNull
    @JsonProperty("L_STATUS")
    private String licenseStatus;

    // Адреса мест осуществления деятельности
    @NotNull
    @JsonProperty("ADDRESS")
    private Address address;

    // КПП
    @NotNull
    @JsonProperty("OGRN")
    private String OGRN;

    // КПП
    @NotNull
    @JsonProperty("KPP")
    private String KPP;

    // Адрес объекта
    @NotNull
    @JsonProperty("OBJECTS")
    private String objectAddress;

    // дата последнего изменения лицензии
    @NotNull
    @JsonProperty("CHANGE_DATE")
    private String changeDate;

    // Перечень работ/услуг согласно лицензии
    // TODO: по идеи должен уйти в Address
    @NotNull
    @JsonProperty("WORK_LIST")
    private String workList;

    // Перечень работ/услуг согласно лицензии 2
    // TODO: по идеи должен уйти в Address
    @NotNull
    @JsonProperty("WORK_LIST_2")
    private String workList2;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {

        // Место осуществления деятельности согласно лицензии
        // TODO: в доке 7.0 не описан
        @JsonProperty("PLACE")
        private String place;

        // Глобальный уникальный идентификатор адресного объекта
        @JsonProperty("aoguid")
        private String aoguid;

        // Глобальный уникальный идентификатор дома
        @JsonProperty("houseguid")
        private String houseGuid;

        public Address(String place, String aoguid, String houseGuid) {
            this.place = place;
            this.aoguid = aoguid;
            this.houseGuid = houseGuid;
        }
    }
}
