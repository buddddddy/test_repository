package org.mdlp.basestate.data.processor.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.mdlp.Utils;
import org.mdlp.basestate.data.*;
import org.mdlp.basestate.data.kiz.ContaintmentHierarchy;
import org.mdlp.basestate.data.mod.*;
import org.mdlp.basestate.data.processor.*;
import org.mdlp.basestate.data.processor.command.KizCommand;
import org.mdlp.basestate.data.processor.command.KizEventCommand;
import org.mdlp.data.ListItems;
import org.mdlp.data.document.Operation;
import org.mdlp.service.KizService;
import org.mdlp.utils.JsonUtils;
import org.mdlp.wsdl.Documents;
import org.mdlp.wsdl.KizInfo;
import org.mdlp.wsdl.QueryKizInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by SSuvorov on 30.03.2017.
 */
@Component
class KizProcessorImpl extends AbstractProcessor implements KizProcessor {

    @Value("${basestate.publicControlEndpoint}")
    private String publicControlKizEndpoint;

    @Value("${basestate.containtmentHierarchyEndpoint}")
    private String containtmentHierarchyEndpoint;

    @Value("${basestate.currentStateKizEndpoint}")
    private String currentStateKizEndpoint;

    @Value("${basestate.historyEndpoint}")
    private String historyEndpoint;

    @Value("${basestate.kizFilterEndpoint}")
    private String kizFilterEndpoint;

    @Value("${basestate.kizAuditEndpoint}")
    private String kizAuditEndpoint;

    @Value("${basestate.requestStatusEndpoint}")
    private String requestStatusEndpoint;

    @Value("${basestate.requestResultEndpoint}")
    private String requestResultEndpoint;


    @Value("${basestate.requestErrorEndpoint}")
    private String requestErrorEndpoint;

    @Autowired
    protected RestUtil restUtil;

    private static Map<Operation, Class> deserializerMap;

    //TODO разобраться с opDate - приходит из ядра странное значение. когда починят, удалить
    private static Map<Operation, Class> secondDeserializerMap;


    static {
        deserializerMap = new HashMap<>();
        deserializerMap.put(Operation.move_order_s, DrugShipment.ShipmentKizMetadata.class);
        deserializerMap.put(Operation.receive_order_s, DrugAcceptance.AcceptanceKizMetadata.class);

        //TODO разобраться с opDate - приходит из ядра странное значение. когда починят, удалить
        secondDeserializerMap = new HashMap<>();
        secondDeserializerMap.put(Operation.register_end_packing, ModDrugPrimaryMarkingProperty.class);
        secondDeserializerMap.put(Operation.register_product_emission, ModDrugEmissionProperty.class);
        secondDeserializerMap.put(Operation.move_order_s, ModDrugShipmentProperty.class);
        secondDeserializerMap.put(Operation.receive_order_s, ModDrugAcceptanceProperty.class);
        secondDeserializerMap.put(Operation.retail_sale, ModDrugSaleProperty.class);
        secondDeserializerMap.put(Operation.health_care, ModHealthCareProperty.class);
        secondDeserializerMap.put(Operation.move_owner, ModDrugMoveOwnerProperty.class);
        secondDeserializerMap.put(Operation.receive_owner, ModDrugReceiveOwnerProperty.class);
        secondDeserializerMap.put(Operation.control_samples, ModDrugControlSamplesProperty.class);
        secondDeserializerMap.put(Operation.register_foreign_product_emission, ModDrugForeignEmissionProperty.class);
        secondDeserializerMap.put(Operation.receive_importer, ModDrugReceiveImporterProperty.class);
        secondDeserializerMap.put(Operation.recipe, ModDrugRecipeProperty.class);
        secondDeserializerMap.put(Operation.withdrawal, ModDrugWithdrawalProperty.class);
        secondDeserializerMap.put(Operation.foreign_shipment, ModDrugForeignShipmentProperty.class);
        secondDeserializerMap.put(Operation.foreign_import, ModDrugForeignImportProperty.class);
        secondDeserializerMap.put(Operation.fts_data, ModDrugFtsDataProperty.class);
        secondDeserializerMap.put(Operation.move_place, ModDrugMovePlaceProperty.class);
        secondDeserializerMap.put(Operation.move_destruction, ModDrugMoveDestructionProperty.class);
        secondDeserializerMap.put(Operation.destruction, ModDrugDestructionProperty.class);
        secondDeserializerMap.put(Operation.reexport, ModDrugWithdrawalProperty.class);
        secondDeserializerMap.put(Operation.relabeling, ModDrugRelabelingPropertyProperty.class);
        secondDeserializerMap.put(Operation.move_info, ModDrugMoveInfoProperty.class);
        secondDeserializerMap.put(Operation.recall, RecallOperation.class);

    }

    @PostConstruct
    private void initialize() {
        currentStateKizEndpoint = Utils.createEndpointUrl(currentStateKizEndpoint, baseUrl, coreVersion);
        historyEndpoint = Utils.createEndpointUrl(historyEndpoint, baseUrl, coreVersion);
        kizFilterEndpoint = Utils.createEndpointUrl(kizFilterEndpoint, baseUrl, coreVersion);
        kizAuditEndpoint = Utils.createEndpointUrl(kizAuditEndpoint, baseUrl, coreVersion);
        requestStatusEndpoint = Utils.createEndpointUrl(requestStatusEndpoint, baseUrl, coreVersion);
        requestResultEndpoint = Utils.createEndpointUrl(requestResultEndpoint, baseUrl, coreVersion);
        requestErrorEndpoint = Utils.createEndpointUrl(requestErrorEndpoint, baseUrl, coreVersion);
        publicControlKizEndpoint = Utils.createEndpointUrl(publicControlKizEndpoint, baseUrl, coreVersion);
        containtmentHierarchyEndpoint = Utils.createEndpointUrl(containtmentHierarchyEndpoint, baseUrl, coreVersion);
    }

    @Override
    public Kiz getPublicControlKiz(String kizId) throws IOException {
        String uri = publicControlKizEndpoint.replace("{kizId}", kizId);
        ResponseEntity<Kiz> response = restUtil.getTemplate().exchange(uri,
                HttpMethod.GET, null, new ParameterizedTypeReference<Kiz>() {
                });
        Kiz kiz = response.getBody();
        return kiz;
    }

    @Override
    public KizService.KizEvent getCurrentKizState(String kizId) throws IOException {
        String uri = currentStateKizEndpoint.replace("{kizId}", kizId);
        ResponseEntity<KizState> response = restUtil.getTemplate().exchange(uri,
                HttpMethod.GET, null, new ParameterizedTypeReference<KizState>() {
                });
        KizState state = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        KizService.KizEvent event = new KizService.KizEvent();
        Operation operation = Operation.findByCoreNumber(state.getStateNumber());
        if (operation != null) {
            CoreDrugProperty property = deserializeProperty(mapper, state, operation);
            if (property != null) {
                event.setDateTime(Utils.parseCoreDateTimeWith3Ms(property.getOpDate()));
                event.setOperation(operation);
                event.setMemberId(property.getMemberId());
                event.setReceiverMemberId(property.getReceiverId());
            }
        }
        return event;
    }

    @Override
    public KizService.KizEvents getKizEvents(String kizId) throws IOException {
        String uri = historyEndpoint.replace("{kizId}", kizId);
        LOG.error("URI: " + uri);
        ResponseEntity<List<KizState>> response = restUtil.getTemplate().exchange(uri,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<KizState>>() {
                });
        List<KizState> states = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        List<KizService.KizEvent> events = new ArrayList<>();
        for (KizState state : states) {
            KizService.KizEvent event = new KizService.KizEvent();
            Operation operation = Operation.findByCoreNumber(state.getStateNumber());
            if (operation != null) {
                processCoreOperation(mapper, events, state, event, operation);
            }
        }
        return new KizService.KizEvents(events, events.size());
    }

    private void processCoreOperation(ObjectMapper mapper, List<KizService.KizEvent> events,
                                      KizState state, KizService.KizEvent event, Operation operation) throws IOException {
        if (operation.equals(Operation.recall)) {
            RecallOperation recallOperation = mapper.readValue(state.getPropertiesJson(), RecallOperation.class);
            KizService.KizEvent lastActiveOperation = getLastActiveOperation(events);
            if (lastActiveOperation != null) {
                KizService.CancellationInfo cancellationInfo = lastActiveOperation.getCancellationInfo();
                cancellationInfo.setCancelled(true);
                cancellationInfo.setInitiatorId(recallOperation.getSystemSubjectId());
            }
        } else {
            if (deserializerMap.get(operation) != null) {
                CoreDrugMetadata coreDrugMetadata = deserializeMetadata(mapper, state, operation);
                if (coreDrugMetadata != null) {
                    event.setCost(coreDrugMetadata.getPriceInRubbles());
                }
            }
            CoreDrugProperty property = deserializeProperty(mapper, state, operation);
            if (property != null) {
                event.setDateTime(Utils.parseCoreDateTimeWith3Ms(property.getOpDate()));
                event.setOperation(operation);
                event.setMemberId(property.getMemberId());
                event.setReceiverMemberId(property.getReceiverId());
                event.setCancellationInfo(new KizService.CancellationInfo());
                events.add(event);
            }
        }
    }

    private KizService.KizEvent getLastActiveOperation(List<KizService.KizEvent> events) {
        Optional<KizService.KizEvent> reduce = events.stream().filter(item -> !item.getCancellationInfo().isCancelled()).reduce((a, b) -> b);
        return reduce.isPresent()? reduce.get() : null;
    }

    @Override
    public KizService.KizEvents getKizEvents(KizService.KizEventsFilter filter) throws IOException {

        KizEventFilter kizEventFilter = new KizEventFilter();
        kizEventFilter.setQueryId(UUID.randomUUID().toString());
        kizEventFilter.setPaginationOffset(0);
        kizEventFilter.setPaginationLimit(10_000);
        kizEventFilter.setStartDate(filter.getDateFrom());
        kizEventFilter.setEndDate(filter.getDateTo());

        CoreResponse response = restUtil.getTemplate().postForObject(kizAuditEndpoint, kizEventFilter, CoreResponse.class);
        KizStateNew[] states = (KizStateNew[]) getResult(new KizEventCommand(response.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));

        ObjectMapper mapper = new ObjectMapper();
        List<KizService.KizEvent> events = new ArrayList<>();
        for (KizStateNew state : states) {
            Operation operation = Operation.findByCoreNumber(state.getStateNumber());
            if (operation != null) {
                CoreDrugProperty property = deserializeProperty(mapper, state, operation);
                if (property != null) {
                    KizService.KizEvent event = new KizService.KizEvent();
                    event.setDateTime(Utils.parseCoreDateTimeWith3Ms(state.getOpDate().getOpDate()));
                    event.setOperation(operation);
                    event.setMemberId(property.getMemberId());
                    event.setReceiverMemberId(property.getReceiverId());
                    events.add(event);
                }
            }
        }

        return new KizService.KizEvents(events, events.size());
    }

    @Override
    public Documents processContainmentHierarchyRequest(QueryKizInfo queryKizInfo, DrugProcessor drugProcessor) throws IOException {
        KizInfo kizInfo = new KizInfo();
        kizInfo.setActionId(211);

        if (queryKizInfo.getSgtin() != null) {
            Kiz kiz = getPublicControlKiz(queryKizInfo.getSgtin());
            if (kiz != null ) {
                KizInfo.Result result = new KizInfo.Result();
                result.setFound(true);
                result.setSgtin(queryKizInfo.getSgtin());
                kizInfo.setResult(result);

                KizInfo.Sgtin resultSgtin = new KizInfo.Sgtin();
                resultSgtin.setGtin(kiz.getGtin());
                resultSgtin.setExpirationDate(LocalDate.parse(kiz.getExpDate(), DateTimeFormatter.ISO_LOCAL_DATE).format(DateTimeFormatter.ofPattern("dd.MM.YYYY")));
                resultSgtin.setSeriesNumber(kiz.getBatch());
                resultSgtin.setSscc(kiz.getSscc());

                DrugInfoGs1 drugInfoGs1 = drugProcessor.getDrugInfoGs1(kiz.getGtin(), false);
                resultSgtin.setTnvedCode(drugInfoGs1.getTnved1());

                kizInfo.setSgtin(resultSgtin);

            }
        } else if (queryKizInfo.getSsccUp() != null) {
            ContaintmentHierarchy containmentHierarchy = getContainmentHierarchy(queryKizInfo.getSsccUp());
            if (containmentHierarchy != null && containmentHierarchy.getCode() == 0) {
                KizInfo.Result result = new KizInfo.Result();
                result.setFound(true);
                result.setSscc(queryKizInfo.getSsccUp());
                kizInfo.setResult(result);
                KizInfo.SsccUp ssccUp = new KizInfo.SsccUp();
                kizInfo.setSsccUp(ssccUp);
                int level = 1;
                for (int i = containmentHierarchy.getUpKizInfos().size() - 1; i >= 0; i--) {
                    if (!containmentHierarchy.getUpKizInfos().get(i).getSign().equals(queryKizInfo.getSsccUp())) {
                        KizInfo.SsccUp.Info info = new KizInfo.SsccUp.Info();
                        ssccUp.getInfos().add(info);
                        info.setSscc(containmentHierarchy.getUpKizInfos().get(i).getSign());
                        info.setLevel(level);
                        level++;
                    }
                }
            }
        } else if (queryKizInfo.getSsccDown() != null) {
            String containmentHierarchy = getContainmentHierarchyInString(queryKizInfo.getSsccDown());
            KizInfo.Result result = new KizInfo.Result();

            result.setSscc(queryKizInfo.getSsccDown());
            kizInfo.setResult(result);
            KizInfo.SsccDown ssccDown = new KizInfo.SsccDown();
            kizInfo.setSsccDown(ssccDown);

            JsonNode jsonNode = JsonUtils.toJsonNode(containmentHierarchy);
            JsonNode downNode = jsonNode.path("down");

            String sign = downNode.path("sign").textValue();
            if (sign != null) {
                result.setFound(true);
                processDown(downNode, ssccDown, sign);
            } else {
                result.setFound(false);
            }
        }

        Documents documents = new Documents().withKizInfo(kizInfo);
        return documents;

    }

    private void processDown(JsonNode downNode, KizInfo.SsccDown ssccDown, String parentSscc) throws IOException {
        if (downNode.isArray()) {
            for (Iterator<JsonNode> elements = downNode.elements(); elements.hasNext();) {
                JsonNode node = elements.next();
                String sign = process(ssccDown, parentSscc, node);

                List<JsonNode> childs = node.findValues("childs");
                for (JsonNode childNode : childs) {
                    processDown(childNode, ssccDown, sign);
                }
            }
        } else {
            String sign = process(ssccDown, parentSscc, downNode);
            List<JsonNode> childs = downNode.findValues("childs");

            for (JsonNode node : childs) {
                processDown(node, ssccDown, sign);
            }
        }
    }

    private String process(KizInfo.SsccDown ssccDown, String parentSscc, JsonNode node) throws IOException {
        String sign = node.path("sign").textValue();
        if (sign != null) {
            KizService.KizInfosFilter filter = new KizService.KizInfosFilter();
            filter.setSscc(sign);
            filter.setPageSize(25_000);//TODO: вынести в параметр
            filter.setNPage(1);
            ListItems<KizService.KizInfo> kizs = getKizs(filter);
            if (kizs != null && kizs.getList() != null && !kizs.getList().isEmpty()) {
                for (KizService.KizInfo kizInfo1 : kizs.getList()) {
                    KizInfo.SsccDown.Tree tree = new KizInfo.SsccDown.Tree();
                    tree.setSgtin(kizInfo1.getId());
                    tree.setParentSscc(sign);
                    ssccDown.getTrees().add(tree);
                }
            }
            if (!sign.equals(parentSscc)) {
                KizInfo.SsccDown.Tree tree = new KizInfo.SsccDown.Tree();
                tree.setSscc(sign);
                tree.setParentSscc(parentSscc);
                ssccDown.getTrees().add(tree);

            }
        }
        return sign;
    }

    private ContaintmentHierarchy getContainmentHierarchy(String kizId) {
        String uri = containtmentHierarchyEndpoint.replace("{kizId}", kizId);
        ResponseEntity<ContaintmentHierarchy> response = restUtil.getTemplate().exchange(uri,
                HttpMethod.GET, null, new ParameterizedTypeReference<ContaintmentHierarchy>() {
                });
        ContaintmentHierarchy containtmentHierarchy = response.getBody();
        return containtmentHierarchy;
    }

    private String getContainmentHierarchyInString(String kizId) {
        String uri = containtmentHierarchyEndpoint.replace("{kizId}", kizId);
        ResponseEntity<String> response = restUtil.getTemplate().exchange(uri,
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                });
        String containtmentHierarchy = response.getBody();
        return containtmentHierarchy;
    }

    private CoreDrugProperty deserializeProperty(ObjectMapper mapper, KizStateNew state, Operation operation) {
        CoreDrugProperty property = null;
        try {
            property = (CoreDrugProperty) mapper.readValue(state.getReqBody().getPropertiesJson(), deserializerMap.get(operation));
        } catch (Exception e) {
            LOG.error("Can't deserialize into normal class!", e);
            try {
                property = (CoreDrugProperty) mapper.readValue(state.getReqBody().getPropertiesJson(), secondDeserializerMap.get(operation));
            } catch (Exception e2) {
                LOG.error("Can't deserialize into mod class!", e);
            }
        }
        return property;
    }

    private CoreDrugProperty deserializeProperty(ObjectMapper mapper, KizState state, Operation operation) {
        CoreDrugProperty property = null;
        try {
            property = (CoreDrugProperty) mapper.readValue(state.getPropertiesJson(), secondDeserializerMap.get(operation));
        } catch (Exception e) {
            LOG.error("Can't deserialize into normal class!", e);
        }
        return property;
    }

    private CoreDrugMetadata deserializeMetadata(ObjectMapper mapper, KizState state, Operation operation) {
        CoreDrugMetadata metadata = null;
        try {
            metadata = (CoreDrugMetadata) mapper.readValue(state.getMetadataJson(), deserializerMap.get(operation));
        } catch (Exception e) {
            LOG.error("Can't deserialize into normal class!", e);
        }
        return metadata;
    }


    @Override
    public ListItems<KizService.KizInfo> getKizs(KizService.KizInfosFilter filter) throws IOException {
        KizFilter kizFilter = new KizFilter();
        kizFilter.setQueryId(UUID.randomUUID().toString());
        kizFilter.setSgtin(filter.getSgtin());
        kizFilter.setGtin(filter.getGtin());
        kizFilter.setBatch(filter.getBatch());
        kizFilter.setDrugTitle(filter.getDrugTitleFragment());
        kizFilter.setInn(filter.getProducerInnFragment());
        kizFilter.setStartDate(filter.getDateFrom());
        kizFilter.setEndDate(filter.getDateTo());
        kizFilter.setStartReleaseOperationDate(filter.getReleaseDateFrom());
        kizFilter.setEndReleaseOperationDate(filter.getReleaseDateTo());
        kizFilter.setSgtinState(!filter.getStatuses().isEmpty() ? filter.getStatuses().iterator().next().name() : null);
        kizFilter.setSscc(filter.getSscc());

        kizFilter.setPaginationOffset((filter.getNPage() - 1) * filter.getPageSize());
        kizFilter.setPaginationLimit(filter.getPageSize());


        CoreResponse response = restUtil.getTemplate().postForObject(kizFilterEndpoint, kizFilter, CoreResponse.class);
        PagedResult<Kiz> kizes = (PagedResult<Kiz>) getResult(new KizCommand(response.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));

        List<KizService.KizInfo> kizInfos = Stream.of(kizes.getFilteredRecords())
                .map(this::newKizInfo)
                .collect(Collectors.toList());

        return new ListItems<>(kizInfos, kizes.getFilteredTotalCount() == null ? -1 : kizes.getFilteredTotalCount());
    }

    @NotNull
    private KizService.KizInfo newKizInfo(@NotNull Kiz kiz) {
        KizService.KizInfo kizInfo = new KizService.KizInfo(
                kiz.getSgtin(),
                kiz.getGtin(),
                kiz.getInn(),
                kiz.getCurrentOwner(),
                kiz.getBatch(),
                Utils.parseCoreDate(kiz.getExpDate()),
                null,
                kiz.getSscc(),
                KizInternalStateEnum.valueOf(kiz.getSgtinState().getInternalState()),
                null,
                kiz.getReleaseOperationDate() != null ? Utils.parseCoreDateTimeWith3Ms(kiz.getReleaseOperationDate().getOpDate()) : null,
                Utils.parseCoreDateTimeWith3Ms(kiz.getSgtinState().getOpDate().getOpDate()),
                kiz.getFederalSubjectName() != null ? kiz.getFederalSubjectName().getFederalSubjectName() : null
        );
        String title = kiz.getProdName();
        if (title == null || title.isEmpty() || "отсутствует".equals(title.toLowerCase())) {
            title = kiz.getProdDescFull();
        }
        kizInfo.setTitle(title);
        return kizInfo;
    }
}