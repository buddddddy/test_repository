package org.mdlp.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mdlp.basestate.data.KizInternalStateEnum;
import org.mdlp.data.ListItems;
import org.mdlp.data.document.Operation;
import org.mdlp.web.rest.get_kiz_full_info.RestFullKiz;
import wsdl.WsdlType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface KizService {

    ListItems<KizInfo> getShortKizs(KizService.KizInfosFilter filter, boolean async);

    RestFullKiz getKizInfoById(String kizId);

    @Data
    @NoArgsConstructor
    class KizEvent {

        @NotNull
        private String id;

        @NotNull
        private Operation operation;

        @NotNull
        private LocalDateTime dateTime;

        @Nullable
        private String memberId;

        @Nullable
        private String receiverMemberId;

        @Nullable
        private Long cost;

        @Nullable
        private String from;

        @Nullable
        private String to;

        @Nullable
        private WsdlType document;

        private CancellationInfo cancellationInfo;
    }

    @Data
    @NoArgsConstructor
    class CancellationInfo {

        private boolean cancelled = false;

        private String initiatorId;

    }

    @Data
    class KizEvents {

        @NotNull
        private final List<KizEvent> list;

        private final long total;

    }

    @Data
    class KizEventsFilter {

        @Nullable
        private Set<String> operations = new HashSet<>();

        private String kizId;

        private String memberInnFragment;

        private String memberNameFragment;

        private String dateFrom;

        private String dateTo;

        private int nPage;

        private int pageSize;

    }

    @Data
    class KizInfo {

        @NotNull
        private final String id;

        @Nullable
        private final String gtin;

        @Nullable
        private final String memberId;

        @Nullable
        private final String memberTitle;

        @Nullable
        private final String seriesNumber;

        @Nullable
        private final LocalDate expirationDate;

        @Nullable
        private final String tnved;

        @Nullable
        private final String sscc;

        @NotNull
        private final KizInternalStateEnum status;

        @Nullable
        private final Operation lastOperation;

        @Nullable
        private final LocalDateTime releaseOperationDate;

        @NotNull
        private final LocalDateTime lastOperationDate;

        @Nullable
        private String title;

        @Nullable
        private final String federalSubjectName;
    }

    @Data
    class KizInfos {

        @NotNull
        private final List<KizInfo> list;

        private final long total;

    }

    @Data
    class KizInfosFilter {

        private String sgtin;

        private String sscc;

        @Nullable
        private Set<String> gtins = new HashSet<>();

        private String gtin;

        private String batch;

        private String seriesNumberFragment;

        private String producerInnFragment;

        private String drugTitleFragment;

        @Nullable
        private Set<KizInternalStateEnum> statuses = new HashSet<>();

        private String dateFrom;

        private String dateTo;

        private String releaseDateFrom;

        private String releaseDateTo;

        private int nPage;

        private int pageSize;
    }

}
