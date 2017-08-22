package org.mdlp.basestate.data.statistic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 09.06.2017
 * @package org.mdlp.basestate.data.statistic
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailedStatistic {

    @JsonProperty("month")
    private String month;

    @JsonProperty("stats")
    private List<Stats> stats;


    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Stats {

        @JsonProperty("stats_type")
        private StatsTypeEnum statsType;

        @JsonProperty("value")
        private int value;

        public static enum StatsTypeEnum {
            STATS_LP,
            STATS_RETAIL_SALE,
            STATS_MED_CARE,
            STATS_DISCOUNT_PRESCRIPTION_SALE,
            STATS_SYSID,
            STATS_DESTRUCTION,
            STATS_PUBLIC_CONTROL,
            STATS_PUBLIC_CONTROL_VIOLATION
        }
    }
}
