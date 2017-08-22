package org.mdlp.web.rest.get_reestr_item_esklp;

import org.jetbrains.annotations.NotNull;
import org.mdlp.basestate.data.DrugInfoEsklp;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.service.DrugsService;
import org.mdlp.web.rest.ParentRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author PVBorisov (Pavel.V.Borisov@firstlinesoftware.com)
 * @version 21.06.2017
 */
@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_reestr_item_esklp")
public class GetReestrItemEsklpController {

    private final @NotNull DrugsService drugsService;

    @Autowired
    public GetReestrItemEsklpController(@NotNull DrugsService drugsService) {
        this.drugsService = drugsService;
    }

    @RequestMapping("")
    public @NotNull DrugInfoEsklp action(@RequestParam(value = "ID", required = true) String id) {
        return drugsService.getDrugInfoEsklp(id, false);
    }
}
