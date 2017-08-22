package org.mdlp.web.rest.get_reestr_item_pharm_licenses;

import org.jetbrains.annotations.NotNull;
import org.mdlp.basestate.data.License;
import org.mdlp.basestate.data.PharmLicense;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.service.LicenseService;
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
 * @version 29.06.2017
 */
@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_reestr_item_pharm_licenses")
public class GetReestrIitemPharmLicensesController {

    private final @NotNull LicenseService licenseService;

    @Autowired
    public GetReestrIitemPharmLicensesController(@NotNull LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @RequestMapping("")
    public @NotNull PharmLicense action(@RequestParam(value = "L_NUM", required = true) String licenseNumber) {
        return licenseService.getPharmLicense(licenseNumber, false);
    }
}
