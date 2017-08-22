package org.mdlp.web.rest.get_info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 31.05.2017
 * @package org.mdlp.web.rest
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String userName;

    @JsonProperty("link")
    private String userUrl = "#";


}
