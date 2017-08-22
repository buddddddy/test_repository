package org.mdlp.web.rest.get_reestr_item_licenses;

import org.jetbrains.annotations.NotNull;
import org.mdlp.basestate.data.License;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.service.LicenseService;
import org.mdlp.web.rest.ParentRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by PVBorisov on 20.06.2017.
 * emailto: Pavel.V.Borisov@firstlinesoftware.com
 */
@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_reestr_item_licenses")
public class GetReestrItemLicensesController {

    private final @NotNull LicenseService licenseService;

    @Autowired
    public GetReestrItemLicensesController(@NotNull LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @RequestMapping("")
    public @NotNull License action(@RequestParam(value = "L_NUM", required = true) String licenseNumber) {
        return licenseService.getLicense(licenseNumber, false);
    }
}
