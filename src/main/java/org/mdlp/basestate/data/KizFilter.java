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
 * @version 23.05.2017
 * @package org.mdlp.basestate.data
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KizFilter {

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

    @JsonProperty("release_start_date")
    private String startReleaseOperationDate;

    @JsonProperty("release_end_date")
    private String endReleaseOperationDate;

    // Состояние идентификационного кода вторичной упаковки
    @JsonProperty("sgtin_state")
    private String sgtinState;

    // Идентификатор субъекта обращения (субъекта учёта) в ИС "Маркировка товаров
    @JsonProperty("system_subj_id")
    private String systemSubjId;

    // ИНН организации
    @JsonProperty("inn")
    private String inn;

    // SGTIN (полное вхождение)
    @JsonProperty("sgtin")
    private String sgtin;

    // GTIN (полное вхождение)
    @JsonProperty("gtin")
    private String gtin;

    // Номер серии (полное вхождение)
    @JsonProperty("batch")
    private String batch;

    // наименование КИЗ (частичное вхождение; регистронезависимое)
    @JsonProperty("product_name")
    private String drugTitle;

    @JsonProperty("sscc")
    private String sscc;
}
