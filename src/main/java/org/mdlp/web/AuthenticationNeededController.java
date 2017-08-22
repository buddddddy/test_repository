package org.mdlp.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class AuthenticationNeededController {

    @Value("${web.indexpage}")
    @NotNull
    private String rootUrl;

    @RequestMapping(method = RequestMethod.GET)
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect(rootUrl);
    }
}
