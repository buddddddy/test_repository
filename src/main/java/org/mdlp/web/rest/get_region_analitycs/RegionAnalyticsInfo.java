package org.mdlp.web.rest.get_region_analitycs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 08.06.2017
 * @package org.mdlp.web.rest.get_region_analitycs
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RegionAnalyticsInfo {

    @JsonProperty("members")
    private int membersQuantity;

    @JsonProperty("drugs")
    private int drugsQuantity;

    @JsonProperty("sales")
    private int salesQuantity;

    @JsonProperty("dynamics")
    private List<Dynamics> dynamics = new ArrayList<>();

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class Dynamics {

        @JsonProperty("monthYear")
        private String monthYear;

        @JsonProperty("rates")
        private Rates rates;


        @Data
        @RequiredArgsConstructor
        @AllArgsConstructor
        public static class Rates {

            @JsonProperty("members")
            private ValuesPair membersValuesPairs;

            @JsonProperty("drugs")
            private ValuesPair drugsValuesPairs;

            @JsonProperty("sales")
            private ValuesPair salesValuesPairs;


            @Data
            @RequiredArgsConstructor
            @AllArgsConstructor
            public static class ValuesPair {

                @JsonProperty("currentValue")
                private int currentValue;

                @JsonProperty("previousValue")
                private int previousValue;

            }
        }
    }

}
