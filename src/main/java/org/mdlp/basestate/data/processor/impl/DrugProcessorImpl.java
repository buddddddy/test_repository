package org.mdlp.basestate.data.processor.impl;


import com.google.common.collect.Lists;
import org.mdlp.Utils;
import org.mdlp.basestate.data.*;
import org.mdlp.basestate.data.aggregation.DrugUnitAppend;
import org.mdlp.basestate.data.aggregation.DrugUnitExtract;
import org.mdlp.basestate.data.aggregation.DrugUnitUnpack;
import org.mdlp.basestate.data.introduction.*;
import org.mdlp.basestate.data.packing.DrugUnitPack;
import org.mdlp.basestate.data.processor.AbstractProcessor;
import org.mdlp.basestate.data.processor.CoreResponse;
import org.mdlp.basestate.data.processor.DrugProcessor;
import org.mdlp.basestate.data.processor.RestUtil;
import org.mdlp.basestate.data.processor.command.*;
import org.mdlp.basestate.data.withdrawal.DrugHealthCare;
import org.mdlp.basestate.data.withdrawal.DrugRecipe;
import org.mdlp.basestate.data.withdrawal.DrugWithdrawal;
import org.mdlp.data.ListItems;
import org.mdlp.service.DrugsService;
import org.mdlp.wsdl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.mdlp.basestate.data.BasestateUtil.convert;


/**
 * Created by SSuvorov on 30.03.2017.
 */
@Component
class DrugProcessorImpl extends AbstractProcessor implements DrugProcessor {

    @Value("${basestate.primaryMarkingEndpoint}")
    private String primaryMarkingEndpoint;

    @Value("${basestate.shipmentEndpoint}")
    private String shipmentEndpoint;

    @Value("${basestate.acceptanceEndpoint}")
    private String acceptanceEndpoint;

    @Value("${basestate.moveInfoEndpoint}")
    private String moveInfoEndpoint;

    @Value("${basestate.saleEndpoint}")
    private String saleEndpoint;

    @Value("${basestate.withdrawalEndpoint}")
    private String withdrawalEndpoint;

    @Value("${basestate.emissionEndpoint}")
    private String emissionEndpoint;

    @Value("${basestate.controlSamplesEndpoint}")
    private String controlSamplesEndpoint;

    @Value("${basestate.moveOwnerEndpoint}")
    private String moveOwnerEndpoint;

    @Value("${basestate.receiveOwnerEndpoint}")
    private String receiveOwnerEndpoint;

    @Value("${basestate.receiveImporterEndpoint}")
    private String receiveImporterEndpoint;

    @Value("${basestate.recipeEndpoint}")
    private String recipeEndpoint;

    @Value("${basestate.healthCareEndpoint}")
    private String healthCareEndpoint;

    @Value("${basestate.unitPackEndpoint}")
    private String unitPackEndpoint;

    @Value("${basestate.unitUnpackEndpoint}")
    private String unitUnpackEndpoint;

    @Value("${basestate.unitExtractEndpoint}")
    private String unitExtractEndpoint;

    @Value("${basestate.foreignEmissionEndpoint}")
    private String foreignerEmissionEndpoint;

    @Value("${basestate.foreignShipmentEndpoint}")
    private String foreignShipmentEndpoint;

    @Value("${basestate.foreignImportEndpoint}")
    private String foreignImportEndpoint;

    @Value("${basestate.ftsDataEndpoint}")
    private String ftsDataEndpoint;

    @Value("${basestate.movePlaceEndpoint}")
    private String movePlaceEndpoint;

    @Value("${basestate.recallEndpoint}")
    private String recallEndpoint;

    @Value("${basestate.moveDestructionEndpoint}")
    private String moveDestructionEndpoint;

    @Value("${basestate.destructionEndpoint}")
    private String destructionEndpoint;

    @Value("${basestate.relabelingEndpoint}")
    private String relabelingEndpoint;

    @Value("${basestate.unitAppendEndpoint}")
    private String unitAppendEndpoint;

    @Value("${basestate.requestStatusEndpoint}")
    private String requestStatusEndpoint;

    @Value("${basestate.requestResultEndpoint}")
    private String requestResultEndpoint;

    @Value("${basestate.requestErrorEndpoint}")
    private String requestErrorEndpoint;

    @Value("${basestate.reestrEsklpFilterEndpoint}")
    private String reestrEsklpFilterEndpoint;

    @Value("${basestate.reestrEsklpStatusEndpoint}")
    private String reestrEsklpStatusEndpoint;

    @Value("${basestate.reestrEsklpResultEndpoint}")
    private String reestrEsklpResultEndpoint;

    @Value("${basestate.reestrEsklpErrorEndpoint}")
    private String reestrEsklpErrorEndpoint;

    @Value("${basestate.reestrEsklpDetail}")
    private String reestrEsklpDetail;

    @Value("${basestate.reestrGs1FilterEndpoint}")
    private String reestrGs1FilterEndpoint;

    @Value("${basestate.reestrGs1StatusEndpoint}")
    private String reestrGs1StatusEndpoint;

    @Value("${basestate.reestrGs1ResultEndpoint}")
    private String reestrGs1ResultEndpoint;

    @Value("${basestate.reestrGs1ErrorEndpoint}")
    private String reestrGs1ErrorEndpoint;

    @Value("${basestate.reestrGs1Detail}")
    private String reestrGs1Detail;

    @Value("${basestate.reestrLP}")
    private String reestrLP;

    @Value("${basestate.reestrLPDetail}")
    private String reestrLPDetail;

    @Autowired
    protected RestUtil restUtil;

    private Map<Class<?>, ParameterizedTypeReference> typeReferences = new HashMap<>();

    public DrugProcessorImpl() {
        typeReferences.put(DrugInfoEsklp.class, new ParameterizedTypeReference<List<DrugInfoEsklp>>() {
        });
        typeReferences.put(DrugInfoEsklpId.class, new ParameterizedTypeReference<List<DrugInfoEsklpId>>() {
        });
        typeReferences.put(DrugInfoGs1.class, new ParameterizedTypeReference<List<DrugInfoGs1>>() {
        });
        typeReferences.put(DrugInfoGs1Id.class, new ParameterizedTypeReference<List<DrugInfoGs1Id>>() {
        });
    }

    @PostConstruct
    private void initialize() {
        primaryMarkingEndpoint = Utils.createEndpointUrl(primaryMarkingEndpoint, baseUrl, coreVersion);
        shipmentEndpoint = Utils.createEndpointUrl(shipmentEndpoint, baseUrl, coreVersion);
        acceptanceEndpoint = Utils.createEndpointUrl(acceptanceEndpoint, baseUrl, coreVersion);
        moveInfoEndpoint = Utils.createEndpointUrl(moveInfoEndpoint, baseUrl, coreVersion);
        saleEndpoint = Utils.createEndpointUrl(saleEndpoint, baseUrl, coreVersion);
        withdrawalEndpoint = Utils.createEndpointUrl(withdrawalEndpoint, baseUrl, coreVersion);
        emissionEndpoint = Utils.createEndpointUrl(emissionEndpoint, baseUrl, coreVersion);
        controlSamplesEndpoint = Utils.createEndpointUrl(controlSamplesEndpoint, baseUrl, coreVersion);
        moveOwnerEndpoint = Utils.createEndpointUrl(moveOwnerEndpoint, baseUrl, coreVersion);
        receiveOwnerEndpoint = Utils.createEndpointUrl(receiveOwnerEndpoint, baseUrl, coreVersion);
        receiveImporterEndpoint = Utils.createEndpointUrl(receiveImporterEndpoint, baseUrl, coreVersion);
        recipeEndpoint = Utils.createEndpointUrl(recipeEndpoint, baseUrl, coreVersion);
        healthCareEndpoint = Utils.createEndpointUrl(healthCareEndpoint, baseUrl, coreVersion);
        foreignShipmentEndpoint = Utils.createEndpointUrl(foreignShipmentEndpoint, baseUrl, coreVersion);
        foreignImportEndpoint = Utils.createEndpointUrl(foreignImportEndpoint, baseUrl, coreVersion);
        ftsDataEndpoint = Utils.createEndpointUrl(ftsDataEndpoint, baseUrl, coreVersion);
        movePlaceEndpoint = Utils.createEndpointUrl(movePlaceEndpoint, baseUrl, coreVersion);
        recallEndpoint = Utils.createEndpointUrl(recallEndpoint, baseUrl, coreVersion);
        moveDestructionEndpoint = Utils.createEndpointUrl(moveDestructionEndpoint, baseUrl, coreVersion);
        destructionEndpoint = Utils.createEndpointUrl(destructionEndpoint, baseUrl, coreVersion);
        relabelingEndpoint = Utils.createEndpointUrl(relabelingEndpoint, baseUrl, coreVersion);
        unitPackEndpoint = Utils.createEndpointUrl(unitPackEndpoint, baseUrl, coreVersion);
        unitExtractEndpoint = Utils.createEndpointUrl(unitExtractEndpoint, baseUrl, coreVersion);
        unitUnpackEndpoint = Utils.createEndpointUrl(unitUnpackEndpoint, baseUrl, coreVersion);
        unitAppendEndpoint = Utils.createEndpointUrl(unitAppendEndpoint, baseUrl, coreVersion);
        foreignerEmissionEndpoint = Utils.createEndpointUrl(foreignerEmissionEndpoint, baseUrl, coreVersion);
        requestStatusEndpoint = Utils.createEndpointUrl(requestStatusEndpoint, baseUrl, coreVersion);
        requestResultEndpoint = Utils.createEndpointUrl(requestResultEndpoint, baseUrl, coreVersion);
        requestErrorEndpoint = Utils.createEndpointUrl(requestErrorEndpoint, baseUrl, coreVersion);
        reestrEsklpFilterEndpoint = Utils.createEndpointUrl(reestrEsklpFilterEndpoint, baseUrl, coreVersion);
        reestrEsklpStatusEndpoint = Utils.createEndpointUrl(reestrEsklpStatusEndpoint, baseUrl, coreVersion);
        reestrEsklpResultEndpoint = Utils.createEndpointUrl(reestrEsklpResultEndpoint, baseUrl, coreVersion);
        reestrEsklpErrorEndpoint = Utils.createEndpointUrl(reestrEsklpErrorEndpoint, baseUrl, coreVersion);
        reestrEsklpDetail = Utils.createEndpointUrl(reestrEsklpDetail, baseUrl, coreVersion);
        reestrGs1FilterEndpoint = Utils.createEndpointUrl(reestrGs1FilterEndpoint, baseUrl, coreVersion);
        reestrGs1StatusEndpoint = Utils.createEndpointUrl(reestrGs1StatusEndpoint, baseUrl, coreVersion);
        reestrGs1ResultEndpoint = Utils.createEndpointUrl(reestrGs1ResultEndpoint, baseUrl, coreVersion);
        reestrGs1ErrorEndpoint = Utils.createEndpointUrl(reestrGs1ErrorEndpoint, baseUrl, coreVersion);
        reestrGs1Detail = Utils.createEndpointUrl(reestrGs1Detail, baseUrl, coreVersion);
        reestrLP = Utils.createEndpointUrl(reestrLP, baseUrl, coreVersion);
        reestrLPDetail = Utils.createEndpointUrl(reestrLPDetail, baseUrl, coreVersion);
    }

    @Override
    public Documents process(RegisterEndPacking document, Documents documents) {
        List<DrugPrimaryMarking> primaryMarkings = convert(document);

        Result finalResult = null;
        for (DrugPrimaryMarking primaryMarking : primaryMarkings) {
            CoreResponse coreResponse = restUtil.getTemplate().postForObject(primaryMarkingEndpoint,
                    primaryMarking, CoreResponse.class);

            Result restResult = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
            if (finalResult == null) {
                finalResult = restResult;
            } else {
                if (!finalResult.getOperationResult().equals(restResult.getOperationResult())) {
                    finalResult.setOperationResult(OperationResultTypeEnum.PARTIAL);
                }
            }
        }
        finalResult.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(finalResult);
    }

    @Override
    public Documents process(MoveOrder document, Documents documents) {
        DrugShipment shipment = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(shipmentEndpoint, shipment, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(ReceiveOrder document, Documents documents) {
        DrugAcceptance acceptance = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(acceptanceEndpoint, acceptance, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(MoveInfo document, Documents documents) {
        DrugMoveInfo drugMoveInfo = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(moveInfoEndpoint, drugMoveInfo, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(RetailSale document, Documents documents) {
        DrugSale sale = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(saleEndpoint, sale, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(Withdrawal document, Documents documents) {
        DrugWithdrawal sale = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(withdrawalEndpoint, sale, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    public Documents process(RegisterProductEmission document, Documents documents) {
        DrugEmission emission = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(emissionEndpoint, emission, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(RegisterControlSamples document, Documents documents) {
        DrugControlSamples controlSamples = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(controlSamplesEndpoint, controlSamples, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(MoveOwner document, Documents documents) {
        DrugMoveOwner moveOwner = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(moveOwnerEndpoint, moveOwner, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(ReceiveOwner document, Documents documents) {
        DrugReceiveOwner drugReceiveOwner = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(receiveOwnerEndpoint, drugReceiveOwner, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(ReceiveImporter document, Documents documents) {
        DrugReceiveImporter drugReceiveImporter = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(receiveImporterEndpoint, drugReceiveImporter, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(Recipe document, Documents documents) {
        DrugRecipe drugRecipe = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(recipeEndpoint, drugRecipe, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(HealthCare document, Documents documents) {
        DrugHealthCare drugHealthCare = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(healthCareEndpoint, drugHealthCare, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(UnitPack document, Documents documents) {
        DrugUnitPack drugUnitPack = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(unitPackEndpoint, drugUnitPack, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(UnitUnpack document, Documents documents) {
        DrugUnitUnpack drugUnitUnpack = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(unitUnpackEndpoint, drugUnitUnpack, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(UnitExtract document, Documents documents) {
        DrugUnitExtract drugUnitExtract = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(unitExtractEndpoint, drugUnitExtract, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }


    @Override
    public Documents process(ForeignEmission document, Documents documents) {
        DrugForeignerEmission drugForeignerEmission = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(foreignerEmissionEndpoint, drugForeignerEmission, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(ForeignShipment document, Documents documents) {
        DrugForeignShipment drugForeignShipment = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(foreignShipmentEndpoint, drugForeignShipment, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(ForeignImport document, Documents documents) {
        DrugForeignImport drugForeignImport = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(foreignImportEndpoint, drugForeignImport, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(FtsData document, Documents documents) {
        DrugFtsData drugFtsData = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(ftsDataEndpoint, drugFtsData, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(MovePlace document, Documents documents) {
        DrugMovePlace drugMovePlace = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(movePlaceEndpoint, drugMovePlace, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(MoveDestruction document, Documents documents) {
        DrugMoveDestruction drugMoveDestruction = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(moveDestructionEndpoint, drugMoveDestruction, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(Destruction document, Documents documents) {
        DrugDestruction drugDestruction = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(destructionEndpoint, drugDestruction, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(Reexport document, Documents documents) {
        DrugWithdrawal drugWithdrawal = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(withdrawalEndpoint, drugWithdrawal, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(Relabeling document, Documents documents) {
        DrugRelabeling drugRelabeling = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(relabelingEndpoint, drugRelabeling, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(UnitAppend document, Documents documents) {
        DrugUnitAppend drugUnitAppend = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(unitAppendEndpoint, drugUnitAppend, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new DrugCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        return documents.withResult(result4Wsdl);
    }

    @Override
    public Documents process(Recall document, Documents documents) {
        RecallOperation operationRecall = convert(document);

        CoreResponse coreResponse = restUtil.getTemplate().postForObject(recallEndpoint, operationRecall, CoreResponse.class);

        Result result4Wsdl = getResult4Wsdl(documents.getResult(), new RecallOperationCommand(coreResponse.getQueryId(), requestStatusEndpoint, requestResultEndpoint, requestErrorEndpoint, restUtil));
        result4Wsdl.setOperation(String.valueOf(document.getActionId()));
        if (result4Wsdl.getErrors() != null && !result4Wsdl.getErrors().isEmpty()) {
            result4Wsdl.getErrors().get(0).setObjectId(operationRecall.getActionToCancelId());
        }
        return documents.withResult(result4Wsdl);
    }

    @Override
    public DrugsService.DrugInfos getDrugInfos() {
        List<JsonId> lpIds = getLPIds();
        List<DrugInfoLP> lps = getLps(lpIds);
        Map<DrugInfoLP, DrugsService.DrugInfo> drugs = new HashMap<>();
        processDrugInfosGs1(lps, drugs);
        processDrugInfosEsklp(lps, drugs);
        return new DrugsService.DrugInfos(Lists.newArrayList(drugs.values()), drugs.values().size());
    }

    //TODO need refactoring
    @Override
    public DrugsService.DrugInfo getDrugInfo(String gtin) {
        List<JsonId> lpIds = getLPIds();
        List<DrugInfoLP> lps = getLps(lpIds);
        List<DrugInfoLP> collect = lps.stream().filter(info -> info.getGtin().equals(gtin)).collect(Collectors.toList());

        Map<DrugInfoLP, DrugsService.DrugInfo> drugs = new HashMap<>();
        processDrugInfosGs1(collect, drugs);
        processDrugInfosEsklp(collect, drugs);
        DrugsService.DrugInfo drugInfo = null;
        if (drugs.values().size() > 0) {
            drugInfo = new ArrayList<>(drugs.values()).get(0);
        }
        return drugInfo;
    }

    @Override
    public ListItems<DrugInfoGs1> getDrugInfoGs1List(DrugInfoGs1Filter filter, boolean async) {
        return executeCommand(async, new AsyncCommand<ListItems<DrugInfoGs1>>() {
            @Override
            public ListItems<DrugInfoGs1> execute() throws IOException {
                filter.setQueryId(UUID.randomUUID().toString());
                CoreResponse response = restUtil.getTemplate().postForObject(reestrGs1FilterEndpoint, filter, CoreResponse.class);
                PagedResult<DrugInfoGs1> infos = (PagedResult<DrugInfoGs1>) getResult(new DrugInfoGs1Command(response.getQueryId(),
                        reestrGs1StatusEndpoint, reestrGs1ResultEndpoint, reestrGs1ErrorEndpoint, restUtil));

                List<DrugInfoGs1> list = Arrays.asList(infos.getFilteredRecords());
                return new ListItems<>(list, infos.getFilteredTotalCount() == null ? -1 : infos.getFilteredTotalCount());
            }
        });
    }

    @Override
    public DrugInfoGs1 getDrugInfoGs1(String gtin, boolean async) {
        return executeCommand(async, new AsyncCommand<DrugInfoGs1>() {
            @Override
            public DrugInfoGs1 execute() throws IOException {
                String uri = reestrGs1Detail + gtin;
                ResponseEntity<DrugInfoGs1> response = restUtil.getTemplate().exchange(uri,
                        HttpMethod.GET, null, DrugInfoGs1.class);
                return response.getBody();
            }
        });
    }

    @Override
    public ListItems<DrugInfoEsklp> getDrugInfoEsklpList(DrugInfoEsklpFilter filter, boolean async) {
        return executeCommand(async, new AsyncCommand<ListItems<DrugInfoEsklp>>() {
            @Override
            public ListItems<DrugInfoEsklp> execute() throws IOException {
                filter.setQueryId(UUID.randomUUID().toString());
                CoreResponse response = restUtil.getTemplate().postForObject(reestrEsklpFilterEndpoint, filter, CoreResponse.class);
                PagedResult<DrugInfoEsklp> infos = (PagedResult<DrugInfoEsklp>) getResult(new DrugInfoEsklpCommand(response.getQueryId(),
                    reestrEsklpStatusEndpoint, reestrEsklpResultEndpoint, reestrEsklpErrorEndpoint, restUtil));
                List<DrugInfoEsklp> list = Arrays.asList(infos.getFilteredRecords());
                return new ListItems<>(list, infos.getFilteredTotalCount() == null ? -1 : infos.getFilteredTotalCount());
            }
        });
    }

    @Override
    public DrugInfoEsklp getDrugInfoEsklp(String regNum, boolean async) {
        return executeCommand(async, new AsyncCommand<DrugInfoEsklp>() {
            @Override
            public DrugInfoEsklp execute() throws IOException {
                ResponseEntity<DrugInfoEsklp> response = restUtil.getTemplate().exchange(Utils.createURI(reestrEsklpDetail, regNum),
                    HttpMethod.GET, null, DrugInfoEsklp.class);
                return response.getBody();
            }
        });
    }

    private List<DrugInfoLP> getLps(List<JsonId> lpIds) {
        List<DrugInfoLP> lps = new ArrayList<>();
        for (JsonId jsonId : lpIds) {
            ResponseEntity<DrugInfoLP> response = restUtil.getTemplate().exchange(reestrLPDetail + jsonId.getId(),
                    HttpMethod.GET, null, DrugInfoLP.class);
            lps.add(response.getBody());
        }
        return lps;
    }

    private List<JsonId> getLPIds() {
        ResponseEntity<List<JsonId>> response = restUtil.getTemplate().exchange(reestrLP,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<JsonId>>() {
                });
        return response.getBody();
    }

    private void processDrugInfosGs1(List<DrugInfoLP> lps,
                                     Map<DrugInfoLP, DrugsService.DrugInfo> drugs) {
        for (DrugInfoLP drugInfoLP : lps) {
            ResponseEntity<DrugInfoGs1> response = restUtil.getTemplate().exchange(
                    reestrGs1Detail + drugInfoLP.getGtin(),
                    HttpMethod.GET, null, DrugInfoGs1.class);
            DrugInfoGs1 gs1 = response.getBody();

            DrugsService.DrugInfo info = drugs.get(drugInfoLP);
            if (info == null) {
                info = new DrugsService.DrugInfo();
                drugs.put(drugInfoLP, info);
            }
            info.setCommercialTitle(gs1.getProdName());
            info.setGtin(drugInfoLP.getGtin());
            info.setProducerTitle(gs1.getTrPartnerName());
            info.setRegOwnerName(gs1.getTrPartnerName());
            info.setTitle(gs1.getProdName());
            info.setSystemSubjectId(drugInfoLP.getSystemSubjId());
            info.setDosage(gs1.getWeb90001681());
        }
    }

    private void processDrugInfosEsklp(List<DrugInfoLP> lps,
                                       Map<DrugInfoLP, DrugsService.DrugInfo> drugs) {
        for (DrugInfoLP drugInfoLP : lps) {
            ResponseEntity<DrugInfoEsklp> response = restUtil.getTemplate().exchange(
                    Utils.createURI(reestrEsklpDetail, drugInfoLP.getRegNum()),
                    HttpMethod.GET, null, DrugInfoEsklp.class);
            DrugInfoEsklp esklp = response.getBody();

            DrugsService.DrugInfo info = drugs.get(drugInfoLP);
            if (info == null) {
                info = new DrugsService.DrugInfo();
                drugs.put(drugInfoLP, info);
            }
            info.setTitle(esklp.getProdSellName());
            info.setItemsInPackage(esklp.getProdPack1());
            info.setMaxPrice(esklp.getMaxGnvlp());
            info.setRegNumber(esklp.getRegId());
            info.setRegDate(Utils.parseCoreDateTimeWith3Ms(esklp.getRegDate() != null ? esklp.getRegDate().getOpDate() : null));
            info.setGnvlp(Utils.fromNumeralString(esklp.getGnvlp()));
            info.setRegExpirationDate(Utils.parseCoreDate(esklp.getRegEndDate() != null ? esklp.getRegEndDate().getOpDate() : null));
            info.setRegExpirationDateIsUnlimited(esklp.getRegEndDate() == null ||
                    esklp.getRegEndDate().getOpDate().isEmpty());
            info.setType(esklp.getProdFormName());
        }
    }

    protected Result getResult4Wsdl(Result result, AsyncRestCommand command) {
        Result coreResult = (Result) getOperationsResult(command);

        coreResult.setActionId(result.getActionId());
        coreResult.setSessionUi(command.getRequestId());

        return coreResult;
    }
}