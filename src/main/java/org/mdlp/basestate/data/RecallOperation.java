package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 15.08.2017
 * @package org.mdlp.basestate.data
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecallOperation extends CoreDrugOperation {

    @JsonProperty("system_subj_id")
    private String systemSubjectId;

    @JsonProperty("action_to_cancel_id")
    private String actionToCancelId;

    @JsonProperty("cancel_reason")
    private String cancelReason;
}
