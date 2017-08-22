package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by SSuvorov on 30.03.2017.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrokenKiz {
    // Тип ошибки
    @JsonProperty("brokenKizType")
    private int brokenKisType;

//    // Массив КИЗов соотвествующих ошибке
//    @JsonProperty("kizs")
//    private List<Drug.Kiz> kizs;
}
