package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by SSuvorov on 30.03.2017.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestResult {
    // Код завершения операции
    @JsonProperty("code")
    private int code;

    // Количество переданных КИЗов
    @JsonProperty("kizCount")
    private int kizCount;

    // Количество непринятых КИЗов
    @JsonProperty("brokenKizCount")
    private int brokenKizCount;

    // Массив КИЗов, которые не были приняты ИС
    @JsonProperty("brokenKiz")
    private BrokenKiz brokenKiz;

    @Data
    @NoArgsConstructor
    public static class BrokenKiz {
        @JsonProperty("brokenKizType")
        private int brokenKizType;

        @JsonProperty("kizs")
        private List<Kiz> brokenKizs;

        @Data
        @NoArgsConstructor
        public static class Kiz {
            @JsonProperty("sign_type")
            private int signType;

            @JsonProperty("sign")
            private String sign;
        }
    }
}
