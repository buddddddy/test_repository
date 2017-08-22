package org.mdlp.web.rest;

import org.mdlp.core.mvc.ParentController;
import org.mdlp.web.AuthenticationNeededController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ParentController(AuthenticationNeededController.class)
@RequestMapping("/rest")
public class ParentRestController {
}
