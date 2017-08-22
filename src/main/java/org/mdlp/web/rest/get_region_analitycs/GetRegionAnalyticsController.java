package org.mdlp.web.rest.get_region_analitycs;

import org.jetbrains.annotations.NotNull;
import org.mdlp.Utils;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.service.StatisticService;
import org.mdlp.web.rest.ParentRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 08.06.2017
 * @package org.mdlp.web.rest.get_region_analitycs
 */
@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_region_analytics")
public class GetRegionAnalyticsController {

    protected static final Logger LOG = LoggerFactory.getLogger(GetRegionAnalyticsController.class);

    private final @NotNull StatisticService statisticService;

    @Autowired
    public GetRegionAnalyticsController(@NotNull StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @RequestMapping("")
    @NotNull
    public RegionAnalyticsInfo getRegionAnalytics(
            @RequestParam(value = "code", required = true) String regionCode
    ) {
        StatisticService.StatisticFilter filter = new StatisticService.StatisticFilter();
        filter.setStartDate(Utils.getFirstJanuaryCurrentYear());
        Calendar instance = Calendar.getInstance();
        instance.setTime(Utils.sliceToEndDate(new Date()));
        filter.setEndDate(Utils.convertLongDateToCore(instance));
        filter.setFederalCode(regionCode);
        RegionAnalyticsInfo regionAnalyticsInfo = new RegionAnalyticsInfo();
        if (regionCode.equals("00")) {
            regionAnalyticsInfo = statisticService.getSummaryStatistic(filter, false);
        } else {
            try {
                regionAnalyticsInfo = statisticService.getAsyncStatistic(filter, false);
            } catch (Exception e) {
                LOG.error("Can't retrieve statistic from core!", e);
            }
        }

        return regionAnalyticsInfo;
    }


}
