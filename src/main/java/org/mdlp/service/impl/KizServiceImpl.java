package org.mdlp.service.impl;

import org.jetbrains.annotations.NotNull;
import org.mdlp.basestate.data.Kiz;
import org.mdlp.basestate.data.processor.RestProcessor;
import org.mdlp.data.ListItems;
import org.mdlp.service.DrugsService;
import org.mdlp.service.KizService;
import org.mdlp.service.MembersService;
import org.mdlp.web.rest.get_kiz_full_info.RestFullKiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class KizServiceImpl implements KizService {

    @NotNull
    private final RestProcessor restProcessor;

    @Autowired
    public KizServiceImpl(@NotNull RestProcessor restProcessor) {
        this.restProcessor = restProcessor;
    }

    @Override
    public ListItems<KizInfo> getShortKizs(KizInfosFilter filter, boolean async) {

        ListItems<KizInfo> kizInfos = Optional.ofNullable(restProcessor.getKizs(filter, false))
                .orElseGet(() -> new ListItems<>(new ArrayList<>(), 0));

        return kizInfos;
    }

    @Override
    public RestFullKiz getKizInfoById(String kizId) {
        //getInfoFromPublicControl
        RestFullKiz restFullKiz = new RestFullKiz();
        Kiz kiz = restProcessor.getPublicControlKiz(kizId, false);
        KizEvent kizEvent = restProcessor.getCurrentStateKizEvent(kizId, false);
        KizInfosFilter filter = new KizInfosFilter();
        filter.setSgtin(kizId);
        ListItems<KizInfo> shortKizsList = getShortKizs(filter, false);
        List<KizInfo> shortKizs = shortKizsList.getList();

        if (kizEvent != null && shortKizs != null && !shortKizs.isEmpty()) {
            String memberIdInn = shortKizs.get(0).getMemberId();
            String gtin = shortKizs.get(0).getGtin();
            MembersService.MemberInfo contractor = restProcessor.getContractorByInn(memberIdInn, false);
            DrugsService.DrugInfo drugInfo = restProcessor.getDrugInfo(gtin, false);

            restFullKiz.setId(kizId);
            restFullKiz.setSGTIN(kizId);
            restFullKiz.setSeriesNumber(kiz.getBatch());
            restFullKiz.setExpirationDate(kiz.getExpDate());
            restFullKiz.setSscc(kiz.getSscc());
            restFullKiz.setGTIN(gtin);

            restFullKiz.setName(contractor != null ? contractor.getName() : "Нет данных");
            restFullKiz.setINN(contractor != null ? contractor.getInn() : "Нет данных");
            restFullKiz.setKPP(contractor != null ? contractor.getKpp() : "Нет данных");

            restFullKiz.setProducer(drugInfo != null ? drugInfo.getProducerTitle() : "Нет данных");
            restFullKiz.setProducerINN(drugInfo != null ? drugInfo.getProducerInn() : "Нет данных");
            restFullKiz.setPackerINN(drugInfo != null ? drugInfo.getProducerInn() : "Нет данных");
            restFullKiz.setLpName(kiz.getProdName());
            restFullKiz.setDosageForm(drugInfo != null ? drugInfo.getType() : "Нет данных");
            restFullKiz.setTradeName(drugInfo != null ? drugInfo.getCommercialTitle() : "Нет данных");
            restFullKiz.setDosage(drugInfo != null ? drugInfo.getDosage() : "Нет данных");

        }

        return restFullKiz;

    }
}
