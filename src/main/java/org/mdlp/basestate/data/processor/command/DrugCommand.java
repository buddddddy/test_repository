package org.mdlp.basestate.data.processor.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.mdlp.basestate.data.AsyncResult;
import org.mdlp.basestate.data.RequestError;
import org.mdlp.basestate.data.RequestResult;
import org.mdlp.basestate.data.processor.RestUtil;
import org.mdlp.utils.AppContext;
import org.mdlp.wsdl.OperationResultTypeEnum;
import org.mdlp.wsdl.Result;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 25.05.2017
 * @package org.mdlp.basestate.data.processor.command
 */
public class DrugCommand extends AsyncRestCommand<RequestResult, Result> {

    public DrugCommand(String requestId, String requestStatusEndpoint, String requestResultEndpoint, String requestErrorEndpoint, RestUtil restUtil) {
        super(requestId, requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil);
    }

    @Override
    public AsyncResult<Result> getResult(AsyncResult<RequestResult> asyncResult) {
        AsyncResult<Result> asyncResult1 = new AsyncResult<>();
        if (asyncResult.isOperationResult()) {
            Result result = new Result();
            asyncResult1.setResult(result);
            if (asyncResult.getResult() != null) {
                result.setSessionUi(String.valueOf(AppContext.getInstance().getNextId()));
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
                .exchange(requestResultEndpoint + requestId, HttpMethod.GET, null, new ParameterizedTypeReference<RequestResult>() {});
    }

    protected void processResult(AsyncResult asyncResult, final Result result) {
        final RequestResult result1 = (RequestResult) asyncResult.getResult();
        setAcceptedTime(result);
        switch (result1.getCode()) {
            case 0:
                result.setOperationResult(OperationResultTypeEnum.ACCEPTED);
                break;
            case 4:
                result.setOperationComment("Операция недоступна");
            case 1:
            case 2:
            case 10:
            case 11:
            case 15:
            case 16:
            case 20:
            case 21:
            case 30:
            case 40:
            case 50:
            case 60:
            case 100:
                if ((result1.getBrokenKizCount() != 0 && result1.getBrokenKizCount() == result1.getKizCount()) ||
                        (result1.getBrokenKiz() == null && result1.getKizCount() == 0)) {
                    result.setOperationResult(OperationResultTypeEnum.REJECTED);
                } else {
                    result.setOperationResult(OperationResultTypeEnum.PARTIAL);
                }
                List<Result.Errors> errors = new ArrayList<>();

                RequestError requestError = getErrorDescription();
                if (result1.getBrokenKiz() != null) {
                    for (RequestResult.BrokenKiz.Kiz kiz : result1.getBrokenKiz().getBrokenKizs()) {
                        Result.Errors error = new Result.Errors();
                        error.setErrorDesc(requestError.getErrorDescription().getError());
                        error.setObjectId(kiz.getSign());
                        error.setErrorCode("04_" + requestError.getErrorDescription().getCode());
                        errors.add(error);
                    }
                }

                result.getErrors().addAll(errors);
        }
    }
}