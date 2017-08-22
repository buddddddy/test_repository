package org.mdlp.basestate.data;

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
 * @version 24.05.2017
 * @package org.mdlp.basestate.data
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KizEventFilter {
    // Уникальный идентификатор запроса
    @NotNull
    @JsonProperty("query_id")
    private String queryId;

    // Начальный элемент списка (при пагинации)
    @NotNull
    @JsonProperty("pagination_offset")
    private int paginationOffset;

    // Количество возвращаемых элементов списка (при пагинации)
    @NotNull
    @JsonProperty("pagination_limit")
    private int paginationLimit;

    // Начало временного диапазона
    @JsonProperty("start_date")
    private String startDate;

    // Конец временного диапазона
    @JsonProperty("end_date")
    private String endDate;

    // Состояние идентификационного кода вторичной упаковки
    @JsonProperty("sgtin_state")
    private String sgtinState;
}
