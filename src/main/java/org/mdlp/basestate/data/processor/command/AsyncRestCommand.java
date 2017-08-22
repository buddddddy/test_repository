package org.mdlp.basestate.data.processor.command;

import org.mdlp.basestate.data.AsyncResult;
import org.mdlp.basestate.data.RequestError;
import org.mdlp.basestate.data.RequestStatus;
import org.mdlp.basestate.data.processor.AbstractProcessor;
import org.mdlp.basestate.data.processor.RestUtil;
import org.mdlp.wsdl.Result;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 25.05.2017
 * @package org.mdlp.basestate.data.processor.command
 */
public abstract class AsyncRestCommand<T, P> {

    protected RestUtil restUtil;

    protected String requestStatusEndpoint;

    protected String requestResultEndpoint;

    protected String requestErrorEndpoint;

    protected String requestId;

    public AsyncRestCommand(String requestId, String requestStatusEndpoint, String requestResultEndpoint, String requestErrorEndpoint, RestUtil restUtil) {
        this.requestId = requestId;
        this.requestStatusEndpoint = requestStatusEndpoint;
        this.requestResultEndpoint = requestResultEndpoint;
        this.requestErrorEndpoint = requestErrorEndpoint;
        this.restUtil = restUtil;
    }

    public String getRequestId() {
        return requestId;
    }

    public AsyncResult<P> execute() {
        AsyncResult<T> asyncResult = baseExecute();
        return getResult(asyncResult);
    }

    protected abstract AsyncResult<P> getResult(AsyncResult<T> asyncResult);

    protected AsyncResult<T> baseExecute() {
        AsyncResult<T> result = new AsyncResult<T>();
        ResponseEntity<RequestStatus> status = restUtil.getTemplate()
                .exchange(requestStatusEndpoint + requestId, HttpMethod.GET, null, new ParameterizedTypeReference<RequestStatus>() {
                });
        if (status != null && status.getBody().getStatus() != AbstractProcessor.REQUEST_PROCESSING) {
            switch (status.getBody().getStatus()) {
                case AbstractProcessor.REQUEST_SUCCESS: {
                    ResponseEntity<T> requestResult = getResult();
                    result.setResult(requestResult.getBody());
                    break;
                }
                case AbstractProcessor.REQUEST_ERROR: {
                    ResponseEntity<RequestError> error = restUtil.getTemplate().getForEntity(requestErrorEndpoint + requestId, RequestError.class);
                    result.setError(error);
                    break;
                }
            }
        }
        return result;
    }

    protected abstract ResponseEntity<T> getResult();

    protected RequestError getErrorDescription() {
        ResponseEntity<RequestError> requestError = restUtil.getTemplate()
                .exchange(requestErrorEndpoint + requestId, HttpMethod.GET, null, new ParameterizedTypeReference<RequestError>() {
                });
        return requestError.getBody();
    }

    protected void setAcceptedTime(Result result) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        result.setAcceptTime(cal);
    }

}
