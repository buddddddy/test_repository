package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created by PVBorisov on 20.06.2017.
 * emailto: Pavel.V.Borisov@firstlinesoftware.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class PagedFilter extends BaseFilter {

    // Начальный элемент списка (при пагинации)
    @NotNull
    @JsonProperty("pagination_offset")
    private int paginationOffset;

    // Количество возвращаемых элементов списка (при пагинации)
    @NotNull
    @JsonProperty("pagination_limit")
    private int paginationLimit;
}
