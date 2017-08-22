package org.mdlp.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mdlp.basestate.data.*;
import org.mdlp.data.ListItems;
import org.mdlp.web.rest.get_lp_list.RestDrug;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface DrugsService {

    @NotNull Map<String, String> getTypeMapping();

    @NotNull DrugInfos find(DrugsFilter filter, Pageable pageable);

    @NotNull Optional<DrugInfo> findByGtin(@NotNull String gtin);

    @NotNull
    default DrugInfos find(DrugsFilter filter) {
        return find(filter, new PageRequest(0, Integer.MAX_VALUE));
    }

    ListItems<RestDrug> getDrugInfos(DrugsFilter filter, boolean async);

    ListItems<DrugInfoGs1> getDrugInfoGs1List(DrugInfoGs1Filter filter, boolean async);

    DrugInfoGs1 getDrugInfoGs1(String gtin, boolean async);

    ListItems<DrugInfoEsklp> getDrugInfoEsklpList(DrugInfoEsklpFilter filter, boolean async);

    DrugInfoEsklp getDrugInfoEsklp(String regNum, boolean async);

    @Data
    @NoArgsConstructor
    class DrugInfo {

        @NotNull
        private String gtin;

        @Nullable
        private String regOwnerName;

        @Nullable
        private LocalDate regExpirationDate;

        private boolean regExpirationDateIsUnlimited;

        @NotNull
        private String title;

        @Nullable
        private String commercialTitle;

        @Nullable
        private String producerInn;

        @NotNull
        private String producerTitle;

        @Nullable
        private String type;

        @Nullable
        private String itemsInPackage;

        @Nullable
        private String dosage;

        @Nullable
        private String maxPrice;

        @Nullable
        private String regNumber;

        @Nullable
        private LocalDateTime regDate;

        @Nullable
        private boolean gnvlp;

        @Nullable
        private String systemSubjectId;

    }

    @Data
    class DrugInfos {

        @NotNull
        private final List<DrugInfo> list;

        private final long total;

    }

    @Data
    class DrugsFilter {

        private String gtinFragment;

        private String titleFragment;

        private String producerInnFragment;

        private String producerTitleFragment;

        private String regOwnerNameFragment;

        private String dateFrom;

        private String dateTo;

        private String maxPrice;

        @NotNull
        private Set<String> typeIds = new HashSet<>();

        private int nPage;

        private int pageSize;

    }

}
