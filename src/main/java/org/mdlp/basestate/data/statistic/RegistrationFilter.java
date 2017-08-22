package org.mdlp.basestate.data.statistic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mdlp.basestate.data.PagedFilter;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationFilter extends PagedFilter {

    @NotNull
    @JsonProperty("federal_subject_code")
    private String federalCode;

    // Наименование организации
    @JsonProperty("org_name")
    private String orgName;

    // ИНН
    @JsonProperty("inn")
    private String INN;

    // КПП
    @JsonProperty("kpp")
    private String KPP;

    // ОГРН
    @JsonProperty("ogrn")
    private String OGRN;

    // Начало временного диапазона
    @JsonProperty("start_date")
    private String startDate;

    // Конец временного диапазона
    @JsonProperty("end_date")
    private String endDate;
}
