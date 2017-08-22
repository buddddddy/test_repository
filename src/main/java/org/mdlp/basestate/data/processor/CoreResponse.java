package org.mdlp.basestate.data.processor;

import lombok.NoArgsConstructor;

/**
 * Created by SSuvorov on 02.05.2017.
 */
@NoArgsConstructor
public class CoreResponse {
    private String queryId;

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }
}
