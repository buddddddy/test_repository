package org.mdlp.basestate.data.processor;

import org.mdlp.basestate.data.statistic.DetailedStatistic;
import org.mdlp.basestate.data.statistic.SummaryStatistic;
import org.mdlp.service.KizService;
import org.mdlp.service.StatisticService;

import java.io.IOException;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 09.06.2017
 * @package org.mdlp.basestate.data.processor
 */
public interface StatisticProcessor {

    SummaryStatistic[] getSummaryStatistic(StatisticService.StatisticFilter filter);

    DetailedStatistic[] getDetailedStatistic(StatisticService.StatisticFilter filter);

}
