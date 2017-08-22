package org.mdlp.basestate.data.processor.command;

import org.mdlp.basestate.data.AsyncResult;
import org.mdlp.basestate.data.DrugInfoGs1;
import org.mdlp.basestate.data.PagedResult;
import org.mdlp.basestate.data.processor.RestUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author PVBorisov (Pavel.V.Borisov@firstlinesoftware.com)
 * @version 21.06.2017
 */
public class DrugInfoGs1Command extends AsyncRestCommand<PagedResult<DrugInfoGs1>, PagedResult<DrugInfoGs1>> {

    public DrugInfoGs1Command(String requestId, String requestStatusEndpoint, String requestResultEndpoint, String requestErrorEndpoint, RestUtil restUtil) {
        super(requestId, requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil);
    }

    @Override
    protected AsyncResult<PagedResult<DrugInfoGs1>> getResult(AsyncResult<PagedResult<DrugInfoGs1>> asyncResult) {
        return asyncResult;
    }

    @Override
    protected ResponseEntity<PagedResult<DrugInfoGs1>> getResult() {
        return restUtil.getTemplate()
            .exchange(requestResultEndpoint + requestId, HttpMethod.GET, null, new ParameterizedTypeReference<PagedResult<DrugInfoGs1>>() {
            });
    }
}
