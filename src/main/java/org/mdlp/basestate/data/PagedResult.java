package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * User: PTikhomirov
 * Date: 28.07.2017
 * Time: 12:36
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagedResult<T> {
    @JsonProperty("filtered_records_count")
    private Long filteredTotalCount;
    @JsonProperty("filtered_records")
    private T[] filteredRecords;
}
