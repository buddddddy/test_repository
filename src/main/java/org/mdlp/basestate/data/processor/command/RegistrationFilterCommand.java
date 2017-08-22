package org.mdlp.basestate.data.processor.command;

import org.mdlp.basestate.data.AsyncResult;
import org.mdlp.basestate.data.PagedResult;
import org.mdlp.basestate.data.processor.RestUtil;
import org.mdlp.basestate.data.statistic.RegistrationEntry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * User: PTikhomirov
 * Date: 09.06.2017
 * Time: 15:40
 */
public class RegistrationFilterCommand extends AsyncRestCommand<PagedResult<RegistrationEntry>, PagedResult<RegistrationEntry>> {

        public RegistrationFilterCommand(String requestId, String requestEndpoint, String resultEndpoint, String requestErrorEndpoint, RestUtil restUtil) {
            super(requestId, requestEndpoint, resultEndpoint, requestErrorEndpoint, restUtil);
        }

        @Override
        protected AsyncResult<PagedResult<RegistrationEntry>> getResult(AsyncResult<PagedResult<RegistrationEntry>> asyncResult) {
            return asyncResult;
        }

        @Override
        protected ResponseEntity<PagedResult<RegistrationEntry>> getResult() {
            return restUtil.getTemplate()
                    .exchange(requestResultEndpoint + requestId, HttpMethod.GET, null, new ParameterizedTypeReference<PagedResult<RegistrationEntry>>() {
                    });
        }
}
