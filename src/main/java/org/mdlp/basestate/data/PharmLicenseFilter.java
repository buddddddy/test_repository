package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author PVBorisov (Pavel.V.Borisov@firstlinesoftware.com)
 * @version 29.06.2017
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PharmLicenseFilter extends PagedFilter {

    // ИНН организации
    @JsonProperty("inn")
    private String inn;

    // Название организации
    @JsonProperty("ORG_NAME")
    private String orgName;

    // Номер лицензии
    @JsonProperty("L_NUM")
    private String licenseNumber;

    // дата начала действия лицензии
    @JsonProperty("START_DATE")
    private String startDate;

    // дата окончания действия лицензии
    @JsonProperty("END_DATE")
    private String endDate;

    // Статусы лицензии
    @JsonProperty("L_STATUS")
    private String licenseStatus;
}
