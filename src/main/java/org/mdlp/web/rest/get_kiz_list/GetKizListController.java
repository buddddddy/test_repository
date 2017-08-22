package org.mdlp.web.rest.get_kiz_list;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mdlp.Utils;
import org.mdlp.basestate.data.KizInternalStateEnum;
import org.mdlp.basestate.data.processor.RestProcessor;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.data.ListItems;
import org.mdlp.service.KizService;
import org.mdlp.web.rest.ParentRestController;
import org.mdlp.web.rest.RestItemList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_kiz_list")
public class GetKizListController {

    @Value("${basestate.kizFilterEndpoint}")
    private String kizFilterEndpoint;

    @NotNull
    private final KizService kizService;

    @NotNull
    private final RestProcessor restProcessor;

    @Autowired
    public GetKizListController(@NotNull KizService kizService, @NotNull RestProcessor restProcessor) {
        this.kizService = kizService;
        this.restProcessor = restProcessor;
    }

    @NotNull
    @RequestMapping("")
    public  RestItemList<RestShortKiz> action(
            @RequestParam(value = "SGTIN", required = false) String sgtin,
            @RequestParam(value = "seriesNumber", required = false) String batch,
            @RequestParam(value = "GTIN", required = false) String gtin,
            @RequestParam(value = "SSCC", required = false) String sscc,
            @RequestParam(value = "name", required = false) String productNameFragment,
            @RequestParam(value = "ownerInn", required = false) String producerInnFragment,
            @Nullable @RequestParam(value = "statuses", required = false) Set<Integer> statusIds,
            @RequestParam(value = "releaseDateFrom", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate releaseDateFrom,
            @RequestParam(value = "releaseDateTo", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate releaseDateTo,

            @RequestParam(value = "lastDateFrom", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate lastDateFrom,
            @RequestParam(value = "lastDateTo", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate lastDateTo,

            @RequestParam(value = "nPage", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize
    ) {
        KizService.KizInfosFilter filter = new KizService.KizInfosFilter();
        filter.setSgtin(sgtin);
        filter.setGtin(gtin);
        filter.setBatch(batch);
        filter.setDrugTitleFragment(productNameFragment);
        filter.setProducerInnFragment(producerInnFragment);
        if (statusIds != null) {
            filter.setStatuses(statusIds.stream().map(KizInternalStateEnum::getByCode).collect(Collectors.toSet()));
        }
        filter.setDateFrom(Utils.convertLocalDateToLocalDateTimeMin(lastDateFrom));
        filter.setDateTo(Utils.convertLocalDateToLocalDateTimeMax(lastDateTo));
        filter.setReleaseDateFrom(Utils.convertLocalDateToLocalDateTimeMin(releaseDateFrom));
        filter.setReleaseDateTo(Utils.convertLocalDateToLocalDateTimeMax(releaseDateTo));
        filter.setNPage(pageNumber);
        filter.setPageSize(pageSize);
        filter.setSscc(sscc);


        ListItems<KizService.KizInfo> shortKizs = kizService.getShortKizs(filter, false);
        RestItemList<RestShortKiz> restItemList = getRestShortKizList(shortKizs);

        return Utils.setTotalAndLazyMode(restItemList, pageNumber, pageSize);
    }

    private RestItemList<RestShortKiz> getRestShortKizList(ListItems<KizService.KizInfo> shortKizs) {
        RestItemList<RestShortKiz> restItemList = new RestItemList<>();
        List<RestShortKiz> collect = shortKizs.getList().stream()
                .map(item -> {
                    RestShortKiz restItem = new RestShortKiz();
                    restItem.setId(item.getId());
                    restItem.setName(item.getTitle());
                    restItem.setOwnerINN(item.getMemberId());
                    restItem.setOwnerTitle(item.getMemberTitle());
                    restItem.setSGTIN(item.getId());
                    restItem.setSeriesNumber(item.getSeriesNumber());
                    restItem.setStatusId(String.valueOf(item.getStatus().getCode()));
                    restItem.setReleaseOperationDate(item.getReleaseOperationDate());
                    restItem.setLastOperationDate(item.getLastOperationDate());
                    restItem.setGTIN(item.getGtin());
                    restItem.setSscc(item.getSscc());
                    restItem.setFederalSubjectName(item.getFederalSubjectName());
                    return restItem;
                })
                .collect(Collectors.toList());
        restItemList.setList(collect);
        restItemList.setTotal(shortKizs.getTotal());
        restItemList.setLazyMode(false);
        return restItemList;
    }

}
