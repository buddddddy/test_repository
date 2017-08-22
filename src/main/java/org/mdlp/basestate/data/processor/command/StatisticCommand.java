package org.mdlp.basestate.data.processor.command;

import org.mdlp.basestate.data.AsyncResult;
import org.mdlp.basestate.data.Kiz;
import org.mdlp.basestate.data.processor.RestUtil;
import org.mdlp.basestate.data.statistic.DetailedStatistic;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 09.06.2017
 * @package org.mdlp.basestate.data.processor.command
 */
public class StatisticCommand extends AsyncRestCommand<DetailedStatistic[], DetailedStatistic[]> {

    public StatisticCommand(String requestId, String requestStatusEndpoint, String requestResultEndpoint, String requestErrorEndpoint, RestUtil restUtil) {
        super(requestId, requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil);
    }

    @Override
    protected AsyncResult<DetailedStatistic[]> getResult(AsyncResult<DetailedStatistic[]> asyncResult) {
        return asyncResult;
    }

    @Override
    protected ResponseEntity<DetailedStatistic[]> getResult() {
        return restUtil.getTemplate()
                .exchange(requestResultEndpoint + requestId, HttpMethod.GET, null, new ParameterizedTypeReference<DetailedStatistic[]>() {});
    }
}
