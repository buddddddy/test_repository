package org.mdlp.core.security;

import org.mdlp.core.security.user.User;
import org.mdlp.utils.AppContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 31.05.2017
 * @package org.mdlp.core.security
 */
@Component
public class ArmConfiguration {

    @Value("#{'${app.users}'.split(';;')}")
    private List<String> users;

    public void init() {
        List<User> userList = readUsersFromProperty();
        AppContext.getInstance().setUsers(userList);
    }

    private List<User> readUsersFromProperty() {
        List<User> userList = new ArrayList<>();
        for (String user : users) {
            String[] strings = user.split("\\|");
            User us = new User(strings[0], strings[1], strings[2], strings[3]);
            userList.add(us);
        }
        return userList;
    }
}
