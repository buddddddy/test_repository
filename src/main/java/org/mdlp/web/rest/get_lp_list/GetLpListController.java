package org.mdlp.web.rest.get_lp_list;

import org.jetbrains.annotations.NotNull;
import org.mdlp.Utils;
import org.mdlp.basestate.data.processor.RestProcessor;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.data.ListItems;
import org.mdlp.service.DrugsService;
import org.mdlp.service.DrugsService.DrugsFilter;
import org.mdlp.web.rest.ParentRestController;
import org.mdlp.web.rest.RestItemList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Math.min;

@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_lp_list")
public class GetLpListController {

    @NotNull
    private final DrugsService drugsService;

    @NotNull
    private final RestProcessor restProcessor;


    @Value("${basestate.useReestr}")
    private boolean useReestr;

    @Autowired
    public GetLpListController(@NotNull DrugsService drugsService,
                               @NotNull RestProcessor restProcessor) {
        this.drugsService = drugsService;
        this.restProcessor = restProcessor;
    }

    @RequestMapping("")
    public @NotNull RestItemList<RestDrug> action(
        @RequestParam(value = "GTIN", required = false) String gtinFragment,
        @RequestParam(value = "name", required = false) String titleFragment,
        @RequestParam(value = "producerInn", required = false) String producerInnFragment,
        @RequestParam(value = "regOwnerName", required = false) String regOwnerNameFragment,
        @RequestParam(value = "forms", required = false) Set<String> typeIds,
        @RequestParam(value = "expDateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
        @RequestParam(value = "expDateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
        @RequestParam(value = "maxPrice", required = false) String maxPrice,

        @RequestParam(value = "nPage", defaultValue = "1") int pageNumber,
        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize
    ) {
        DrugsFilter filter = new DrugsFilter();
        filter.setGtinFragment(gtinFragment);
        filter.setTitleFragment(titleFragment);
        filter.setProducerInnFragment(producerInnFragment);
        filter.setRegOwnerNameFragment(regOwnerNameFragment);
        filter.setTypeIds(typeIds);
        filter.setDateFrom(Utils.convertLocalDateToLocalDateTimeMin(dateFrom));
        filter.setDateTo(Utils.convertLocalDateToLocalDateTimeMax(dateTo));
        filter.setMaxPrice(maxPrice);

//        Pageable pageable = new PageRequest(pageNumber - 1, min(pageSize, 10000));
//
//        DrugInfos infos = drugsService.find(filter, pageable);

        ListItems<RestDrug> restDrugs;

        if(useReestr) {
            restDrugs = drugsService.getDrugInfos(filter, false);
        } else {
            restDrugs = drugsService.getDrugInfos(filter, true);
        }
        RestItemList<RestDrug> restDrugsList = getRestDrugs(restDrugs);
        return Utils.setTotalAndLazyMode(restDrugsList, pageNumber, pageSize);
    }

    private RestItemList<RestDrug> getRestDrugs(ListItems<RestDrug> restDrugListItems) {
        RestItemList<RestDrug> restDrugsList = new RestItemList<>();
        List<RestDrug> restDrugs = restDrugListItems.getList();
        restDrugsList.setList(restDrugs);
        restDrugsList.setTotal(restDrugListItems.getTotal());
        return restDrugsList;
    }

    @NotNull
    @RequestMapping("/core")
    public RestItemList action(
            @RequestParam(defaultValue = "false") boolean async) {
        ListItems<RestDrug> restDrugs = drugsService.getDrugInfos(new DrugsFilter(), async);

        if (restDrugs == null) {
            return new RestItemList();
        }

        RestItemList<RestDrug> restDrugsList = getRestDrugs(restDrugs);
        return restDrugsList;
    }
}
