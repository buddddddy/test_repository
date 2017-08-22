package org.mdlp.basestate.data.kiz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.mdlp.basestate.data.OperationDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 27.07.2017
 * @package org.mdlp.basestate.data.kiz
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContaintmentHierarchy {

    @NonNull
    @JsonProperty("code")
    private int code;

    @JsonProperty("up")
    private List<KizInfo> upKizInfos;

    @JsonProperty("down")
    private List<KizInfo> downKizInfos;

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KizInfo {

        @JsonProperty("sign")
        private String sign;

        @JsonProperty("system_subj_id")
        private String systemSubjId;

        @JsonProperty("op_date")
        private OperationDate operationDate;

    }
}
