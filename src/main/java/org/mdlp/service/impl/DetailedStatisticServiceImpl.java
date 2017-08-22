package org.mdlp.service.impl;

import org.jetbrains.annotations.NotNull;
import org.mdlp.Utils;
import org.mdlp.basestate.data.processor.RestProcessor;
import org.mdlp.basestate.data.statistic.DetailedStatistic;
import org.mdlp.basestate.data.statistic.SummaryStatistic;
import org.mdlp.service.StatisticService;
import org.mdlp.web.rest.get_region_analitycs.RegionAnalyticsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 09.06.2017
 * @package org.mdlp.service.impl
 */
@Component
public class DetailedStatisticServiceImpl implements StatisticService {

    private final @NotNull RestProcessor restProcessor;

    @Autowired
    public DetailedStatisticServiceImpl(@NotNull RestProcessor restProcessor) {
        this.restProcessor = restProcessor;
    }

    @Override
    public RegionAnalyticsInfo getStatistic(StatisticFilter statisticFilter, boolean async) {

        RegionAnalyticsInfo regionAnalyticsInfo = new RegionAnalyticsInfo();

        statisticFilter.setQueryId(UUID.randomUUID().toString());
        SummaryStatistic[] summaryStatistics = restProcessor.getSummaryStatistics(statisticFilter, false);
        getSummaryStatistic(summaryStatistics, regionAnalyticsInfo);

        // for detailed statistic we need info by month previous before start date
        setFilterForDetailedStat(statisticFilter);
        statisticFilter.setQueryId(UUID.randomUUID().toString());
        DetailedStatistic[] detailedStatistics = restProcessor.getDetailedStatistics(statisticFilter, false);
        getDetailedStatistic(detailedStatistics, regionAnalyticsInfo);

        return regionAnalyticsInfo;
    }

    @Override
    public RegionAnalyticsInfo getAsyncStatistic(StatisticFilter statisticFilter, boolean async) throws Exception {

        RegionAnalyticsInfo regionAnalyticsInfo = new RegionAnalyticsInfo();

        StatisticFilter sumFilter = new StatisticFilter();
        sumFilter.setFederalCode(statisticFilter.getFederalCode());
        sumFilter.setStartDate(statisticFilter.getStartDate());
        sumFilter.setEndDate(statisticFilter.getEndDate());
        sumFilter.setQueryId(UUID.randomUUID().toString());
        Future<SummaryStatistic[]> futureSummaryStatistics = restProcessor.getAsyncSummaryStatistics(sumFilter, false);

        // for detailed statistic we need info by month previous before start date
        setFilterForDetailedStat(statisticFilter);
        statisticFilter.setQueryId(UUID.randomUUID().toString());

        Future<DetailedStatistic[]> futureDetailedStatistics = restProcessor.getAsyncDetailedStatistics(statisticFilter, false);

        SummaryStatistic[] summaryStatistics = futureSummaryStatistics.get();
        DetailedStatistic[] detailedStatistics = futureDetailedStatistics.get();

        getSummaryStatistic(summaryStatistics, regionAnalyticsInfo);
        getDetailedStatistic(detailedStatistics, regionAnalyticsInfo);

        return regionAnalyticsInfo;
    }

    @Override
    public RegionAnalyticsInfo getSummaryStatistic(StatisticFilter statisticFilter, boolean async) {
        RegionAnalyticsInfo regionAnalyticsInfo = new RegionAnalyticsInfo();

        statisticFilter.setQueryId(UUID.randomUUID().toString());
        SummaryStatistic[] summaryStatistics = restProcessor.getSummaryStatistics(statisticFilter, false);
        getSummaryStatistic(summaryStatistics, regionAnalyticsInfo);
        return regionAnalyticsInfo;
    }

    private void setFilterForDetailedStat(StatisticFilter statisticFilter) {
        statisticFilter.setStartDate(Utils.getFirstDecemberLastYear());
        Calendar instance2 = Calendar.getInstance();
        instance2.setTime(Utils.sliceToEndDate(new Date()));
        statisticFilter.setEndDate(Utils.convertLongDateToCore(instance2));
    }

    private void getDetailedStatistic(DetailedStatistic[] detailedStatistics, RegionAnalyticsInfo regionAnalyticsInfo) {
        if (detailedStatistics != null && detailedStatistics.length > 0) {
            for (DetailedStatistic detailedStatistic : detailedStatistics) {
                RegionAnalyticsInfo.Dynamics dynamics = new RegionAnalyticsInfo.Dynamics();
                RegionAnalyticsInfo.Dynamics.Rates rates = new RegionAnalyticsInfo.Dynamics.Rates();
                dynamics.setRates(rates);
                dynamics.setMonthYear(Utils.convertCoreDateToShort(detailedStatistic.getMonth()));

                for (DetailedStatistic.Stats stats : detailedStatistic.getStats()) {
                    switch (stats.getStatsType()) {
                        case STATS_SYSID:
                            RegionAnalyticsInfo.Dynamics.Rates.ValuesPair valuesPair = new RegionAnalyticsInfo.Dynamics.Rates.ValuesPair(stats.getValue(), stats.getValue());
                            rates.setMembersValuesPairs(valuesPair);
                            break;
                        case STATS_RETAIL_SALE:
                            RegionAnalyticsInfo.Dynamics.Rates.ValuesPair valuesPair1 = new RegionAnalyticsInfo.Dynamics.Rates.ValuesPair(stats.getValue(), stats.getValue());
                            rates.setSalesValuesPairs(valuesPair1);
                            break;
                        case STATS_LP:
                            RegionAnalyticsInfo.Dynamics.Rates.ValuesPair valuesPair2 = new RegionAnalyticsInfo.Dynamics.Rates.ValuesPair(stats.getValue(), stats.getValue());
                            rates.setDrugsValuesPairs(valuesPair2);
                            break;
                    }
                }
                regionAnalyticsInfo.getDynamics().add(dynamics);
            }
            fillPreviousValues(regionAnalyticsInfo);
            boolean isDynamicEmpty = isDynamicsEmpty(regionAnalyticsInfo.getDynamics());
            if (isDynamicEmpty) {
                regionAnalyticsInfo.setDynamics(null);
            }
        }
    }

    private boolean isDynamicsEmpty(List<RegionAnalyticsInfo.Dynamics> dynamics) {
        boolean isEmpty = true;
        for (RegionAnalyticsInfo.Dynamics dyn : dynamics) {
            if (dyn.getRates().getDrugsValuesPairs().getCurrentValue() != 0 || dyn.getRates().getDrugsValuesPairs().getPreviousValue() != 0 ||
                    dyn.getRates().getMembersValuesPairs().getCurrentValue() != 0 || dyn.getRates().getMembersValuesPairs().getPreviousValue() != 0 ||
                    dyn.getRates().getSalesValuesPairs().getCurrentValue() != 0 || dyn.getRates().getSalesValuesPairs().getPreviousValue() != 0) {
                isEmpty = false;
            }
        }

        return isEmpty;
    }

    private void fillPreviousValues(RegionAnalyticsInfo regionAnalyticsInfo) {
        int prevDrugValues;
        int prevMembersValues;
        int prevSalesValues;
        for (int i = 0; i < regionAnalyticsInfo.getDynamics().size(); i++) {
            if (i > 0) {
                prevDrugValues = regionAnalyticsInfo.getDynamics().get(i - 1).getRates().getDrugsValuesPairs().getCurrentValue();
                prevMembersValues = regionAnalyticsInfo.getDynamics().get(i - 1).getRates().getMembersValuesPairs().getCurrentValue();
                prevSalesValues = regionAnalyticsInfo.getDynamics().get(i - 1).getRates().getSalesValuesPairs().getCurrentValue();

                regionAnalyticsInfo.getDynamics().get(i).getRates().getDrugsValuesPairs().setPreviousValue(prevDrugValues);
                regionAnalyticsInfo.getDynamics().get(i).getRates().getMembersValuesPairs().setPreviousValue(prevMembersValues);
                regionAnalyticsInfo.getDynamics().get(i).getRates().getSalesValuesPairs().setPreviousValue(prevSalesValues);
            }
        }

        // remove first month
        regionAnalyticsInfo.getDynamics().remove(0);
    }

    private void getSummaryStatistic(SummaryStatistic[] summaryStatistics, RegionAnalyticsInfo regionAnalyticsInfo) {
        if (summaryStatistics != null && summaryStatistics.length > 0) {
            for (SummaryStatistic summaryStatistic : summaryStatistics) {
                switch (summaryStatistic.getStatsType()) {
                    case STATS_SYSID:
                        regionAnalyticsInfo.setMembersQuantity(summaryStatistic.getValue());
                        break;
                    case STATS_RETAIL_SALE:
                        regionAnalyticsInfo.setSalesQuantity(summaryStatistic.getValue());
                        break;
                    case STATS_LP:
                        regionAnalyticsInfo.setDrugsQuantity(summaryStatistic.getValue());
                        break;
                }
            }
        }
    }
}
