package org.mdlp.basestate.data.processor.command;

import org.mdlp.basestate.data.AsyncResult;
import org.mdlp.basestate.data.RequestError;
import org.mdlp.basestate.data.RequestResult;
import org.mdlp.basestate.data.processor.RestUtil;
import org.mdlp.utils.ApplicationConstants;
import org.mdlp.wsdl.OperationResultTypeEnum;
import org.mdlp.wsdl.Result;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 18.08.2017
 * @package org.mdlp.basestate.data.processor.command
 */
public class RecallOperationCommand extends AsyncRestCommand<RequestResult, Result> {

    public RecallOperationCommand(String requestId, String requestStatusEndpoint, String requestResultEndpoint, String requestErrorEndpoint, RestUtil restUtil) {
        super(requestId, requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil);
    }

    @Override
    public AsyncResult<Result> getResult(AsyncResult<RequestResult> asyncResult) {
        AsyncResult<Result> asyncResult1 = new AsyncResult<>();
        if (asyncResult.isOperationResult()) {
            Result result = new Result();
            asyncResult1.setResult(result);
            if (asyncResult.getResult() != null) {
                processResult(asyncResult, result);
            } else if (asyncResult.getError() != null) {
                result.setOperationResult(OperationResultTypeEnum.REJECTED);
            }

        }

        return asyncResult1;
    }

    @Override
    protected ResponseEntity<RequestResult> getResult() {
        return restUtil.getTemplate()
                .exchange(requestResultEndpoint + requestId, HttpMethod.GET, null, new ParameterizedTypeReference<RequestResult>() {
                });
    }

    protected void processResult(AsyncResult asyncResult, final Result result) {
        final RequestResult result1 = (RequestResult) asyncResult.getResult();
        setAcceptedTime(result);
        switch (result1.getCode()) {
            case 0:
                result.setOperationResult(OperationResultTypeEnum.ACCEPTED);
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 100:
                result.setOperationResult(OperationResultTypeEnum.REJECTED);
                fillErrors(result, result1.getCode());
                break;
        }
    }

    private void fillErrors(Result result, int resultCode) {
        RequestError requestError = getErrorDescription();
        Result.Errors error = new Result.Errors();
        error.setErrorDesc(requestError.getErrorDescription().getError() != null ? requestError.getErrorDescription().getError() : ApplicationConstants.ERRORS_DESCRIPTION.get(resultCode));
        error.setErrorCode("04_" + requestError.getErrorDescription().getCode());
        result.getErrors().add(error);
    }

}