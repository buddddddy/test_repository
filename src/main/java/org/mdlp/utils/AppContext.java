package org.mdlp.utils;

import org.mdlp.core.security.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 22.05.2017
 * @package org.mdlp.utils
 */
public class AppContext {

    private static volatile AppContext instance = null;

    private AtomicInteger counter = new AtomicInteger(0);
    private List<User> users = new ArrayList<>();

    private AppContext() {
    }

    public static AppContext getInstance() {
        if (instance == null) {
            synchronized (AppContext.class) {
                if (instance == null) {
                    instance = new AppContext();
                }
            }
        }
        return instance;
    }

    public int getNextId() {
        return counter.incrementAndGet();
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUserByLogin(String login) {
        return users.stream().filter(user -> user.getLogin().equals(login)).findFirst().get();
    }
}
