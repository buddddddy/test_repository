package org.mdlp.basestate.data;

import lombok.Data;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 24.05.2017
 * @package org.mdlp.basestate.data
 */
@Data
public class AsyncResult<T> {

    private boolean operationResult;
    private T result;
    private Object error;

    public void setResult(T result) {
        this.result = result;
        this.operationResult = true;
    }

    public void setError(Object error) {
        this.error = error;
        this.operationResult = true;
    }

}
