package org.mdlp.web.rest.get_kiz_short_info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RestShortKiz {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

}
