package org.mdlp.web.rest.get_events_list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RestKizEventsList {

    @JsonProperty("list")
    private List<RestKizEvent> list;

    @JsonProperty("total")
    private long total;

}
