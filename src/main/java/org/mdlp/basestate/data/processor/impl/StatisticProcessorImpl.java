package org.mdlp.basestate.data.processor.impl;

import org.mdlp.Utils;
import org.mdlp.basestate.data.processor.AbstractProcessor;
import org.mdlp.basestate.data.processor.CoreResponse;
import org.mdlp.basestate.data.processor.RestUtil;
import org.mdlp.basestate.data.processor.StatisticProcessor;
import org.mdlp.basestate.data.processor.command.StatisticCommand;
import org.mdlp.basestate.data.processor.command.SummaryStatisticCommand;
import org.mdlp.basestate.data.statistic.StatisticFilter;
import org.mdlp.basestate.data.statistic.DetailedStatistic;
import org.mdlp.basestate.data.statistic.SummaryStatistic;
import org.mdlp.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 09.06.2017
 * @package org.mdlp.basestate.data.processor.impl
 */
@Component
public class StatisticProcessorImpl extends AbstractProcessor implements StatisticProcessor {

    @Value("${basestate.summaryStatEndpoint}")
    private String summaryStatEndpoint;

    @Value("${basestate.detailedStatEndpoint}")
    private String detailedStatEndpoint;

    @Value("${basestate.statStatusEndpoint}")
    private String statStatusEndpoint;

    @Value("${basestate.statResultEndpoint}")
    private String statResultEndpoint;

    @Value("${basestate.statErrorEndpoint}")
    private String statErrorEndpoint;

    @Autowired
    protected RestUtil restUtil;

    @PostConstruct
    private void initialize() {
        summaryStatEndpoint= Utils.createEndpointUrl(summaryStatEndpoint, baseUrl, coreVersion);
        detailedStatEndpoint = Utils.createEndpointUrl(detailedStatEndpoint, baseUrl, coreVersion);
        statStatusEndpoint = Utils.createEndpointUrl(statStatusEndpoint, baseUrl, coreVersion);
        statResultEndpoint = Utils.createEndpointUrl(statResultEndpoint, baseUrl, coreVersion);
        statErrorEndpoint = Utils.createEndpointUrl(statErrorEndpoint, baseUrl, coreVersion);

    }

    @Override
    public SummaryStatistic[] getSummaryStatistic(StatisticService.StatisticFilter filter) {
        StatisticFilter statisticFilter = new StatisticFilter();
        statisticFilter.setQueryId(filter.getQueryId());
        statisticFilter.setFederalCode(filter.getFederalCode());
        statisticFilter.setStartDate(filter.getStartDate());
        statisticFilter.setEndDate(filter.getEndDate());

        CoreResponse response = restUtil.getTemplate().postForObject(summaryStatEndpoint, statisticFilter, CoreResponse.class);
        SummaryStatistic[] statistics = (SummaryStatistic[]) getResult(new SummaryStatisticCommand(response.getQueryId(), statStatusEndpoint, statResultEndpoint, statErrorEndpoint, restUtil));

        return statistics;
    }

    @Override
    public DetailedStatistic[] getDetailedStatistic(StatisticService.StatisticFilter filter) {

        StatisticFilter detailedStatisicFilter = new StatisticFilter();
        detailedStatisicFilter.setQueryId(filter.getQueryId());
        detailedStatisicFilter.setFederalCode(filter.getFederalCode());
        detailedStatisicFilter.setStartDate(filter.getStartDate());
        detailedStatisicFilter.setEndDate(filter.getEndDate());

        CoreResponse response = restUtil.getTemplate().postForObject(detailedStatEndpoint, detailedStatisicFilter, CoreResponse.class);
        DetailedStatistic[] statistics = (DetailedStatistic[]) getResult(new StatisticCommand(response.getQueryId(), statStatusEndpoint, statResultEndpoint, statErrorEndpoint, restUtil));

        return statistics;
    }
}
