package org.mdlp.service;

import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import org.mdlp.web.rest.get_region_analitycs.RegionAnalyticsInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 09.06.2017
 * @package org.mdlp.service
 */
public interface StatisticService {

    /**
     * Get full statistic by region. Detailed + summary
     *
     * @param statisticFilter - statistic filter
     * @param async - async flag
     * @return region analytic info
     */
    RegionAnalyticsInfo getStatistic(StatisticFilter statisticFilter, boolean async);

    RegionAnalyticsInfo getSummaryStatistic(StatisticFilter statisticFilter, boolean async);

    RegionAnalyticsInfo getAsyncStatistic(StatisticFilter statisticFilter, boolean async) throws Exception;

    @Data
    class StatisticFilter {

        private String queryId;

        private String federalCode;

        private String startDate;

        private String endDate;
    }
}
