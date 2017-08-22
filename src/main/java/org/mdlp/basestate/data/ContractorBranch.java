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
 * @version 18.05.2017
 * @package org.mdlp.basestate.data
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractorBranch extends BaseContractor {

    @JsonProperty("system_subj_id")
    private String systemSubjId;

    @JsonProperty("branch_id")
    private String branchId;
}
