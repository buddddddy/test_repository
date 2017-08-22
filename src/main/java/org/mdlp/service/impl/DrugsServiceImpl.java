package org.mdlp.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.mdlp.Utils;
import org.mdlp.basestate.data.*;
import org.mdlp.basestate.data.processor.DrugProcessor;
import org.mdlp.basestate.data.processor.RestProcessor;
import org.mdlp.data.ListItems;
import org.mdlp.service.DrugsService;
import org.mdlp.service.MembersService;
import org.mdlp.web.rest.get_lp_list.RestDrug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableMap;

@Component
public class DrugsServiceImpl implements DrugsService {

    private static final String RESOURCE = "drugs.json";

    private static final TypeReference LIST_TYPE = new TypeReference<List<DrugInfo>>() {
    };

    @NotNull
    private final List<DrugInfo> drugs;

    @NotNull
    private final Map<String, String> typeReverseMapping;

    @NotNull
    private final Map<String, String> typeMapping;

    private final @NotNull RestProcessor restProcessor;

    private final @NotNull DrugProcessor drugProcessor;
    @NotNull
    private final MembersService membersService;

    @Autowired
    @SneakyThrows
    public DrugsServiceImpl(@NotNull ObjectMapper objectMapper, @NotNull RestProcessor restProcessor, @NotNull DrugProcessor drugProcessor, @NotNull MembersService membersService) {
        this.restProcessor = restProcessor;
        this.drugProcessor = drugProcessor;
        this.membersService = membersService;
        URL resourceURL = this.getClass().getClassLoader().getResource(RESOURCE);
        if (null == resourceURL) throw new IllegalStateException("Resource " + RESOURCE + " not found");
        drugs = objectMapper.readValue(resourceURL, LIST_TYPE);

        typeReverseMapping = new HashMap<>();
        typeMapping = new HashMap<>();
        int typeId = 0;
        for (DrugInfo info : drugs) {
            if (!typeReverseMapping.containsKey(info.getType())) {
                ++typeId;
                typeReverseMapping.put(info.getType(), String.valueOf(typeId));
                typeMapping.put(String.valueOf(typeId), info.getType());
            }
        }
    }

    @NotNull
    @Override
    public Map<String, String> getTypeMapping() {
        return unmodifiableMap(typeMapping);
    }

    @NotNull
    @Override
    public DrugInfos find(@NotNull DrugsFilter filter, @NotNull Pageable pageable) {
        List<DrugInfo> allResults = drugs.stream()
                .filter(info -> Utils.contains(info.getGtin(), filter.getGtinFragment()))
                .filter(info -> Utils.contains(info.getTitle(), filter.getTitleFragment()))
                .filter(info -> Utils.contains(info.getProducerInn(), filter.getProducerInnFragment()))
                .filter(info -> Utils.contains(info.getProducerTitle(), filter.getProducerTitleFragment()))
                .filter(info -> Utils.contains(info.getRegOwnerName(), filter.getRegOwnerNameFragment()))
                .filter(info -> {
                    if (null == filter.getTypeIds() || filter.getTypeIds().isEmpty()) return true;
                    String typeId = typeReverseMapping.get(info.getType());
                    return filter.getTypeIds().contains(typeId);
                })
                .collect(Collectors.toList());

        List<DrugInfo> results = allResults.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        return new DrugInfos(results, allResults.size());
    }

    @NotNull
    @Override
    public Optional<DrugInfo> findByGtin(@NotNull String gtin) {
        return drugs.stream().filter(item -> Objects.equals(item.getGtin(), gtin)).findFirst();
    }

    @Override
    public ListItems<RestDrug> getDrugInfos(DrugsFilter filter, boolean async) {
        DrugInfos infos = restProcessor.getDrugInfos(async);

        if (infos == null) {
            return null;
        }

        List<RestDrug> collect =
                infos.getList().stream()
                        .map(item -> {
                            RestDrug restItem = new RestDrug();
                            restItem.setGtin(item.getGtin());
                            Optional<MembersService.MemberInfo> info = membersService.findById(item.getSystemSubjectId());
                            if (info.isPresent()) {
                                restItem.setProducerInn(info.get().getInn());
                            }
                            restItem.setName(item.getTitle());
                            restItem.setForm(item.getType());
                            restItem.setRegOwnerName(item.getRegOwnerName());
                            restItem.setRegExpirationDate(null != item.getRegExpirationDate() ? item.getRegExpirationDate().atStartOfDay() : null);
                            restItem.setRegExpirationDateIsUnlimited(item.isRegExpirationDateIsUnlimited());
                            restItem.setRegNumber(item.getRegNumber());
                            restItem.setRegDate(item.getRegDate());
                            restItem.setGnvlp(item.isGnvlp());
                            restItem.setMaxPrice(item.getMaxPrice());
                            return restItem;
                        })
                        .collect(Collectors.toList());

        List<RestDrug> filteredDrugs = filter(collect, filter);

        ListItems<RestDrug> listItems = new ListItems<>(filteredDrugs, filteredDrugs.size());
        return listItems;

    }

    @Override
    public  ListItems<DrugInfoGs1> getDrugInfoGs1List(DrugInfoGs1Filter filter, boolean async) {
        ListItems<DrugInfoGs1> gs1Infos = Optional.ofNullable(drugProcessor.getDrugInfoGs1List(filter, async))
                .orElseGet(() -> new ListItems<>(new ArrayList<>(), 0));

        return gs1Infos;
    }

    @Override
    public DrugInfoGs1 getDrugInfoGs1(String gtin, boolean async) {
        return drugProcessor.getDrugInfoGs1(gtin, async);
    }

    @Override
    public ListItems<DrugInfoEsklp> getDrugInfoEsklpList(DrugInfoEsklpFilter filter, boolean async) {
        ListItems<DrugInfoEsklp> esklpInfos = Optional.ofNullable(drugProcessor.getDrugInfoEsklpList(filter, async))
                .orElseGet(() -> new ListItems<>(new ArrayList<>(), 0));

        return esklpInfos;
    }

    @Override
    public DrugInfoEsklp getDrugInfoEsklp(String regNum, boolean async) {
        return drugProcessor.getDrugInfoEsklp(regNum, async);
    }

    private List<RestDrug> filter(List<RestDrug> collect, DrugsFilter filter) {
        List<RestDrug> allResults = collect.stream()
                .filter(info -> Utils.contains(info.getGtin(), filter.getGtinFragment()))
                .filter(info -> Utils.contains(info.getName(), filter.getTitleFragment()))
                .filter(info -> Utils.contains(info.getProducerInn(), filter.getProducerInnFragment()))
                .filter(info -> Utils.contains(info.getRegOwnerName(), filter.getRegOwnerNameFragment()))
                .filter(info -> (Utils.contains(info.getMaxPrice(), filter.getMaxPrice())))
                .collect(Collectors.toList());

        return allResults;
    }
}
