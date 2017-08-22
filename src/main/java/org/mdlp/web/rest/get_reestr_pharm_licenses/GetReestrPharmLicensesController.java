package org.mdlp.web.rest.get_reestr_pharm_licenses;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mdlp.Utils;
import org.mdlp.basestate.data.License;
import org.mdlp.basestate.data.PharmLicense;
import org.mdlp.basestate.data.PharmLicenseFilter;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.data.ListItems;
import org.mdlp.service.LicenseService;
import org.mdlp.web.rest.ParentRestController;
import org.mdlp.web.rest.RestItemList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
@RequestMapping("/get_reestr_pharm_licenses")
public class GetReestrPharmLicensesController {

    private final @NotNull LicenseService licenseService;

    @Autowired
    public GetReestrPharmLicensesController(@NotNull LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @RequestMapping("")
    public @NotNull RestItemList<PharmLicense> action(
            @RequestParam(value = "INN", required = false) String inn,
            @RequestParam(value = "ORG_NAME", required = false) String orgName,
            @RequestParam(value = "L_NUM", required = false) String licenseNumber,
            @RequestParam(value = "START_DATE", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "END_DATE", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "L_STATUS", required = false) @Nullable Set<String> licenseStatuses,
            @RequestParam(value = "nPage", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        PharmLicenseFilter filter = new PharmLicenseFilter();
        filter.setInn(inn);
        filter.setOrgName(orgName);
        filter.setLicenseNumber(licenseNumber);
        filter.setStartDate(Utils.convertLocalDateToLocalDateTimeMin(startDate));
        filter.setEndDate(Utils.convertLocalDateToLocalDateTimeMin(endDate));
        if (licenseStatuses != null) {
            filter.setLicenseStatus(PharmLicenseStatusEnum.getByCode(licenseStatuses.stream().findFirst().get()).getValue());
        }
        int offset = (pageNumber - 1) * pageSize;
        filter.setPaginationOffset(offset);
        filter.setPaginationLimit(pageSize);

        ListItems<PharmLicense> licenses = licenseService.getPharmLicenseList(filter, false);
        RestItemList<PharmLicense> restItemList = getRestItemList(licenses);

        return Utils.setTotalAndLazyMode(restItemList, pageNumber, pageSize);
    }

    private RestItemList<PharmLicense> getRestItemList(ListItems<PharmLicense> licenses) {
        RestItemList<PharmLicense> restItemList = new RestItemList<>();
        restItemList.setList(licenses.getList());
        restItemList.setTotal(licenses.getTotal());
        restItemList.setLazyMode(false);
        return restItemList;
    }
}
