package org.mdlp.basestate.data.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 01.07.2017
 * @package org.mdlp.basestate.data.address
 */
@Data
@NoArgsConstructor
public class AddressResponse {
    @JsonProperty("address")
    private String address;

    @JsonProperty("code")
    private String code;
}
