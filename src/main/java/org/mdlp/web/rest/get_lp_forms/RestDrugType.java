package org.mdlp.web.rest.get_lp_forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RestDrugType {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

}
