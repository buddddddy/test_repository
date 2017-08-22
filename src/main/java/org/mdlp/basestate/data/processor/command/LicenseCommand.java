package org.mdlp.basestate.data.processor.command;

import org.mdlp.basestate.data.AsyncResult;
import org.mdlp.basestate.data.License;
import org.mdlp.basestate.data.PagedResult;
import org.mdlp.basestate.data.processor.RestUtil;
import org.mdlp.service.DrugsService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * Created by PVBorisov on 20.06.2017.
 * emailto: Pavel.V.Borisov@firstlinesoftware.com
 */
public class LicenseCommand extends AsyncRestCommand<PagedResult<License>, PagedResult<License>> {

    public LicenseCommand(String requestId, String requestStatusEndpoint, String requestResultEndpoint, String requestErrorEndpoint, RestUtil restUtil) {
        super(requestId, requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil);
    }

    @Override
    protected AsyncResult<PagedResult<License>> getResult(AsyncResult<PagedResult<License>> asyncResult) {
        return asyncResult;
    }

    @Override
    protected ResponseEntity<PagedResult<License>> getResult() {
        return restUtil.getTemplate()
                .exchange(requestResultEndpoint + requestId, HttpMethod.GET, null, new ParameterizedTypeReference<PagedResult<License>>() {
                });
    }
}
