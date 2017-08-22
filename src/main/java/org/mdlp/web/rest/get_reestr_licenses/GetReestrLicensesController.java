package org.mdlp.web.rest.get_reestr_licenses;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mdlp.Utils;
import org.mdlp.basestate.data.License;
import org.mdlp.basestate.data.LicenseFilter;
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
 * Created by PVBorisov on 19.06.2017.
 * emailto: Pavel.V.Borisov@firstlinesoftware.com
 */
@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_reestr_licenses")
public class GetReestrLicensesController {

    @NotNull
    private final LicenseService licenseService;

    @Autowired
    public GetReestrLicensesController(@NotNull LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @RequestMapping("")
    public @NotNull RestItemList<License> action(
            @RequestParam(value = "INN", required = false) String inn,
            @RequestParam(value = "ORG_NAME", required = false) String orgName,
            @RequestParam(value = "L_NUM", required = false) String licenseNumber,
            @RequestParam(value = "START_DATE", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "END_DATE", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "L_STATUS", required = false) @Nullable Set<String> licenseStatuses,
            @RequestParam(value = "nPage", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        LicenseFilter filter = new LicenseFilter();
        filter.setInn(inn);
        filter.setOrgName(orgName);
        filter.setLicenseNumber(licenseNumber);
        filter.setStartDate(Utils.convertLocalDateToLocalDateTimeMin(startDate));
        filter.setEndDate(Utils.convertLocalDateToLocalDateTimeMin(endDate));
        if (licenseStatuses != null) {
            filter.setLicenseStatus(LicenseStatusEnum.getByCode(licenseStatuses.stream().findFirst().get()).getValue());
        }
        int offset = (pageNumber - 1) * pageSize;
        filter.setPaginationOffset(offset);
        filter.setPaginationLimit(pageSize);

        ListItems<License> licenses = licenseService.getLicenseList(filter, false);
        RestItemList<License> restItemList = getRestItemList(licenses);

        return Utils.setTotalAndLazyMode(restItemList, pageNumber, pageSize);
    }

    private RestItemList<License> getRestItemList(ListItems<License> licenses) {
        RestItemList<License> restItemList = new RestItemList<>();
        restItemList.setList(licenses.getList());
        restItemList.setTotal(licenses.getTotal());
        restItemList.setLazyMode(false);
        return restItemList;
    }
}
