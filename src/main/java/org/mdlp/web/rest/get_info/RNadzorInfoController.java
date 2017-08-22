package org.mdlp.web.rest.get_info;

import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.core.security.user.User;
import org.mdlp.utils.AppContext;
import org.mdlp.web.rest.ParentRestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 31.05.2017
 * @package org.mdlp.web.rest.get_info
 */
@RestController
@ParentController(ParentRestController.class)
@Profile("RNADZOR")
public class RNadzorInfoController {

    @Value("${app.title.rnadzor}")
    private String title;
    @Value("${app.footer.rnadzor}")
    private String footer;
    @Value("${app.logo.rnadzor}")
    private String logo;
    @Value(("${app.version}"))
    private String version;

    @RequestMapping("/get_app_info")
    public @NotNull AppInfo getAppInfo() {
        AppInfo appInfo = new AppInfo();
        appInfo.setLogoUrl(logo);
        appInfo.setTitle(title);
        appInfo.setFooter(footer);
        appInfo.setVersion(version);
        return appInfo;
    }


    @RequestMapping("/get_user_info")
    public @NotNull UserInfo action() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<String> username = (Optional) auth.getPrincipal();

        if (!username.isPresent()) {
            throw new BadCredentialsException("Invalid Domain User Credentials");
        }

        String login = username.get(); //get logged in username
        User user = AppContext.getInstance().getUserByLogin(login);

        UserInfo userInfo = new UserInfo();
        userInfo.setId("id");
        userInfo.setUserName(user.getFio());
        return userInfo;
    }

}
