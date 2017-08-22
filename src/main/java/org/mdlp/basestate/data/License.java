package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * Created by PVBorisov on 20.06.2017.
 * emailto: Pavel.V.Borisov@firstlinesoftware.com
 */
@Data
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
public class License {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("inn")
    private String inn;

    @NotNull
    @JsonProperty("ORG_NAME")
    private String orgName;

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

    @NotNull
    @JsonProperty("L_STATUS")
    private String licenseStatus;

    // Адреса мест осуществления деятельности
    @NotNull
    @JsonProperty("ADDRESS")
    private Address address;

    @NotNull
    @JsonProperty("OGRN")
    private String OGRN;

    @JsonProperty("DESC")
    private String description;

    // дата последнего изменения лицензии
    @JsonProperty("CHANGE_DATE")
    private OperationDate changeDate;

    // Перечень работ/услуг согласно лицензии
    // TODO: по идеи должен уйти в Address
    @NotNull
    @JsonProperty("WORK_LIST")
    private String workList;

    // Перечень работ/услуг согласно лицензии 2
    // TODO: по идеи должен уйти в Address
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


