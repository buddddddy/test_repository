package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created by PVBorisov on 20.06.2017.
 * emailto: Pavel.V.Borisov@firstlinesoftware.com
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseFilter {

    // Уникальный идентификатор запроса
    @NotNull
    @JsonProperty("query_id")
    private String queryId;
}
