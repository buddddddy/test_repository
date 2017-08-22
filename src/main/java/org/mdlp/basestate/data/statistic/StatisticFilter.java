package org.mdlp.basestate.data.statistic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 09.06.2017
 * @package org.mdlp.basestate.data.statistic
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticFilter {

    // Уникальный идентификатор запроса
    @NotNull
    @JsonProperty("query_id")
    private String queryId;

    @NotNull
    @JsonProperty("federal_subject_code")
    private String federalCode;

    // Начало временного диапазона
    @JsonProperty("start_date")
    private String startDate;

    // Конец временного диапазона
    @JsonProperty("end_date")
    private String endDate;
}
