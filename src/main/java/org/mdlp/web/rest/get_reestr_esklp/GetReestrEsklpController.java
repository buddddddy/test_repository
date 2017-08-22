package org.mdlp.web.rest.get_reestr_esklp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mdlp.Utils;
import org.mdlp.basestate.data.DrugInfoEsklp;
import org.mdlp.basestate.data.DrugInfoEsklpFilter;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.data.ListItems;
import org.mdlp.service.DrugsService;
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
 * @version 21.06.2017
 */
@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_reestr_esklp")
public class GetReestrEsklpController {

    private final @NotNull DrugsService drugsService;

    @Autowired
    public GetReestrEsklpController(@NotNull DrugsService drugsService) {
        this.drugsService = drugsService;
    }

    @RequestMapping("")
    public @NotNull RestItemList<DrugInfoEsklp> action(
        @RequestParam(value = "REG_ID", required = false) String regId,
        @RequestParam(value = "REG_HOLDER", required = false) String regHolder,
        @RequestParam(value = "PROD_NAME", required = false) String prodName,
        @RequestParam(value = "PROD_SELL_NAME", required = false) String prodSellName,
        @RequestParam(value = "REG_HOLDER_CODE", required = false) String regHolderCode,
        @RequestParam(value = "REG_DATE", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate regDate,
        @RequestParam(value = "REG_END_DATE", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate regEndDate,
        @RequestParam(value = "REG_STATUS", required = false) @Nullable Set<String> regStatuses,
        @RequestParam(value = "nPage", defaultValue = "1") int pageNumber,
        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        DrugInfoEsklpFilter filter = new DrugInfoEsklpFilter();
        filter.setRegId(regId);
        filter.setRegHolder(regHolder);
        filter.setProdName(prodName);
        filter.setProdSellName(prodSellName);
        filter.setRegHolderCode(regHolderCode);
        filter.setRegDate(Utils.convertLocalDateToLocalDateTimeMin(regDate));
        filter.setRegEndDate(Utils.convertLocalDateToLocalDateTimeMin(regEndDate));
        if (regStatuses != null) {
            filter.setRegStatus(EsklpStatusEnum.getByCode(regStatuses.stream().findFirst().get()).getValue());
        }
        int offset = (pageNumber - 1) * pageSize;
        filter.setPaginationOffset(offset);
        filter.setPaginationLimit(pageSize);

        ListItems<DrugInfoEsklp> licenses = drugsService.getDrugInfoEsklpList(filter, false);
        RestItemList<DrugInfoEsklp> drugInfoEsklpRestItemList = getRestDrugInfoEsklp(licenses);
        return Utils.setTotalAndLazyMode(drugInfoEsklpRestItemList, pageNumber, pageSize);
    }

    private  RestItemList getRestDrugInfoEsklp(ListItems<DrugInfoEsklp> drugInfoEsklpListItems) {
        RestItemList<DrugInfoEsklp> drugInfoEsklpRestItemList = new RestItemList<>();
        drugInfoEsklpRestItemList.setList(drugInfoEsklpListItems.getList());
        drugInfoEsklpRestItemList.setTotal(drugInfoEsklpListItems.getTotal());
        return drugInfoEsklpRestItemList;
    }
}
