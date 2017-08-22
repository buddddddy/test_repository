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
 * @version 02.06.2017
 * @package org.mdlp.basestate.data
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForeignAgentContractor extends BaseContractor {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("middle_name")
    private String middleName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("inn")
    private String inn;
}
