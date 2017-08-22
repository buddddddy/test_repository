package org.mdlp.basestate.data.processor.command;

import org.mdlp.basestate.data.AsyncResult;
import org.mdlp.basestate.data.PagedResult;
import org.mdlp.basestate.data.PharmLicense;
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
 * @version 29.06.2017
 */
public class PharmLicenseCommand extends AsyncRestCommand<PagedResult<PharmLicense>, PagedResult<PharmLicense>> {

    public PharmLicenseCommand(String requestId, String requestStatusEndpoint, String requestResultEndpoint, String requestErrorEndpoint, RestUtil restUtil) {
        super(requestId, requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil);
    }

    @Override
    protected AsyncResult<PagedResult<PharmLicense>> getResult(AsyncResult<PagedResult<PharmLicense>> asyncResult) {
        return asyncResult;
    }

    @Override
    protected ResponseEntity<PagedResult<PharmLicense>> getResult() {
        return restUtil.getTemplate()
                .exchange(requestResultEndpoint + requestId, HttpMethod.GET, null, new ParameterizedTypeReference<PagedResult<PharmLicense>>() {
                });
    }
}
