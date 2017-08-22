package org.mdlp.web.rest.get_kiz_full_info;

import org.jetbrains.annotations.NotNull;
import org.mdlp.basestate.data.processor.RestProcessor;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.service.DrugsService;
import org.mdlp.service.KizService;
import org.mdlp.service.MembersService;
import org.mdlp.web.rest.ParentRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mdlp.Utils.formatDate;

@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_kiz_full_info")
public class GetKizFullInfoController {

    @NotNull
    private final KizService kizService;

    private final @NotNull RestProcessor restProcessor;

    @Autowired
    public GetKizFullInfoController(@NotNull KizService kizService, @NotNull RestProcessor restProcessor) {
        this.restProcessor = restProcessor;
        this.kizService = kizService;
    }

    @NotNull
    @RequestMapping("")
    public RestFullKiz action(@NotNull @RequestParam(value = "kizId") String id) {
        RestFullKiz result =kizService.getKizInfoById(id);


//
//
//        KizService.KizInfos kizs = restProcessor.getKizs(new KizService.KizInfosFilter(), false);
//        KizService.KizInfo kizInfo = kizs.getList().stream()
//                .filter(item -> item.getId().equals(id))
//                .findFirst()
//                .get();
//        result.setId(kizInfo.getId());
//        result.setSGTIN(kizInfo.getId());
//        result.setSeriesNumber(kizInfo.getSeriesNumber());
//        result.setExpirationDate(formatDate(kizInfo.getExpirationDate()));
//        result.setGTIN(kizInfo.getGtin());
//
//        if (isNotEmpty(kizInfo.getMemberId())) {
//            MembersService.MemberInfo contractor = restProcessor.getContractor(kizInfo.getMemberId(), false);
//            if (contractor != null) {
//                result.setName(contractor.getName());
//                result.setINN(contractor.getInn());
//                result.setKPP(contractor.getKpp());
//            }
//        }
//
//        if (isNotEmpty(kizInfo.getGtin())) {
//            DrugsService.DrugInfo drugInfo = restProcessor.getDrugInfo(kizInfo.getGtin(), false);
//            if (drugInfo == null) {
//                drugInfo = restProcessor.getDrugInfo(kizInfo.getGtin().substring(0, 13), false);
//            }
//            if (drugInfo == null) {
//                drugInfo = restProcessor.getDrugInfo(kizInfo.getGtin().substring(1, 14), false);
//            }
//            if (drugInfo != null) {
//                result.setProducer(drugInfo.getProducerTitle());
//                result.setProducerINN(drugInfo.getProducerInn());
//                result.setPackerINN(drugInfo.getProducerInn());
//                result.setLpName(drugInfo.getTitle());
//                result.setDosageForm(drugInfo.getType());
//                result.setTradeName(drugInfo.getCommercialTitle());
//                result.setDosage(drugInfo.getDosage());
//            }
//        }

        return result;
    }

}
