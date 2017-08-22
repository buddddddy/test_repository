package org.mdlp.core.security.user;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 31.05.2017
 * @package org.mdlp.core.security.user
 */
@Data
@AllArgsConstructor
public class User {

    private String login;
    private String fio;
    private String password;
    private String role;
}
