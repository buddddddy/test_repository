package org.mdlp.web.rest.get_info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 31.05.2017
 * @package org.mdlp.web.rest.get_user_info
 */
@Data
public class AppInfo {

    @JsonProperty("logo")
    private String logoUrl;

    @JsonProperty("title")
    private String title;

    @JsonProperty("footer")
    private String footer;

    @JsonProperty("version")
    private String version;
}
