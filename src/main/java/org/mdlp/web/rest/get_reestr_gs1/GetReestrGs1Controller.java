package org.mdlp.web.rest.get_reestr_gs1;

import org.jetbrains.annotations.NotNull;
import org.mdlp.Utils;
import org.mdlp.basestate.data.DrugInfoGs1;
import org.mdlp.basestate.data.DrugInfoGs1Filter;
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
@RequestMapping("/get_reestr_gs1")
public class GetReestrGs1Controller {

    private final @NotNull DrugsService drugsService;

    @Autowired
    public GetReestrGs1Controller(@NotNull DrugsService drugsService) {
        this.drugsService = drugsService;
    }

    @RequestMapping("")
    public @NotNull RestItemList<DrugInfoGs1> action(
        @RequestParam(value = "PROD_COVER_GTIN", required = false) String prodCoverGtin,
        @RequestParam(value = "PROD_NAME", required = false) String prodName,
        @RequestParam(value = "GS1_MEMBER_GLN", required = false) String gs1MemberGln,
        @RequestParam(value = "TR_PARTNER_NAME", required = false) String trPartnerName,
        @RequestParam(value = "WEB_90000175", required = false) String web90000175,
        @RequestParam(value = "WEB_90001589_from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate web90001589From,
        @RequestParam(value = "WEB_90001589_to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate web90001589To,
        @RequestParam(value = "nPage", defaultValue = "1") int pageNumber,
        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {

        DrugInfoGs1Filter filter = new DrugInfoGs1Filter();
        filter.setProdCoverGtin(prodCoverGtin);
        filter.setProdName(prodName);
        filter.setGs1MemberGln(gs1MemberGln);
        filter.setTrPartnerName(trPartnerName);
        filter.setWeb90000175(web90000175);
        filter.setWeb90001589From(Utils.convertLocalDateToLocalDateTimeMin(web90001589From));
        filter.setWeb90001589To(Utils.convertLocalDateToLocalDateTimeMin(web90001589To));

        int offset = (pageNumber - 1) * pageSize;
        filter.setPaginationOffset(offset);
        filter.setPaginationLimit(pageSize);

        ListItems<DrugInfoGs1> drugs = drugsService.getDrugInfoGs1List(filter, false);
        RestItemList<DrugInfoGs1> drugInfoGs1RestItemList = getRestDrugInfosGs1(drugs);
        return Utils.setTotalAndLazyMode(drugInfoGs1RestItemList, pageNumber, pageSize);
    }

    private RestItemList<DrugInfoGs1> getRestDrugInfosGs1(ListItems<DrugInfoGs1> drugInfoGs1ListItems) {
        RestItemList<DrugInfoGs1> restItemList = new RestItemList<>();
        restItemList.setList(drugInfoGs1ListItems.getList());
        restItemList.setTotal(drugInfoGs1ListItems.getTotal());
        return restItemList;
    }
}
