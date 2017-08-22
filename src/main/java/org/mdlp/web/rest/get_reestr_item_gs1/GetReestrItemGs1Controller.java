package org.mdlp.web.rest.get_reestr_item_gs1;

import org.jetbrains.annotations.NotNull;
import org.mdlp.basestate.data.DrugInfoGs1;
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
@RequestMapping("/get_reestr_item_gs1")
public class GetReestrItemGs1Controller {

    private final @NotNull DrugsService drugsService;

    @Autowired
    public GetReestrItemGs1Controller(@NotNull DrugsService drugsService) {
        this.drugsService = drugsService;
    }

    @RequestMapping("")
    public @NotNull DrugInfoGs1 action(@RequestParam(value = "gtin", required = true) String gtin) {
        return drugsService.getDrugInfoGs1(gtin, false);
    }
}
