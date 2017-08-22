package org.mdlp.web.rest.get_lp_forms;

import org.mdlp.core.mvc.ParentController;
import org.mdlp.service.DrugsService;
import org.mdlp.web.rest.ParentRestController;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_lp_forms")
public class GetLpFormsController {

    @NotNull
    private final DrugsService drugsService;

    @Autowired
    public GetLpFormsController(@NotNull DrugsService drugsService) {
        this.drugsService = drugsService;
    }

    @NotNull
    @RequestMapping("")
    public List<RestDrugType> action() {
        List<RestDrugType> result = new ArrayList<>();
        drugsService.getTypeMapping().forEach((id, title) -> {
            result.add(new RestDrugType(id, title));
        });
        return result;
    }

}
