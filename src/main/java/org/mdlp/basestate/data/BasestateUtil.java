package org.mdlp.basestate.data;

import org.mdlp.Utils;
import org.mdlp.basestate.data.address.Address;
import org.mdlp.basestate.data.aggregation.*;
import org.mdlp.basestate.data.introduction.*;
import org.mdlp.basestate.data.packing.DrugUnitPack;
import org.mdlp.basestate.data.packing.DrugUnitPackProperty;
import org.mdlp.basestate.data.withdrawal.*;
import org.mdlp.wsdl.*;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

import static org.mdlp.Utils.convertLongDateToCore;
import static org.mdlp.Utils.convertShortDateToCore;
import static org.mdlp.Utils.toPennies;

/**
 * Created by SSuvorov on 30.03.2017.
 */
public class BasestateUtil {
    public static List<DrugPrimaryMarking> convert(RegisterEndPacking document) {
        List<DrugPrimaryMarking> primaryMarkings = new ArrayList<>();
        if (document.getSigns() != null) {
            DrugPrimaryMarking primaryMarking = new DrugPrimaryMarking();
            DrugPrimaryMarkingProperty property = new DrugPrimaryMarkingProperty();
            property.setSystemSubjId(document.getSubjectId());
            property.setExpDate(convertShortDateToCore(document.getExpirationDate()));
            property.setOpDate(convertLongDateToCore(document.getOperationDate()));
            property.setOwnerId(document.getSubjectId());
            property.setOrderType(document.getOrderType());
            property.setBatch(document.getSeriesNumber());
            property.setHsCode(document.getTnvedCode());

            property.setGtin(document.getGtin());
            if (document.getSigns() != null && document.getSigns().getSgtins() != null) {
                for (String sgtin : document.getSigns().getSgtins()) {
                    DrugPrimaryMarking.PrimaryMarkingKiz kiz = new DrugPrimaryMarking.PrimaryMarkingKiz(sgtin, 0);
                    primaryMarking.getKizs().add(kiz);
                }
            }

            primaryMarking.setDrugPrimaryMarkingProperty(property);
            primaryMarkings.add(primaryMarking);
        }

        return primaryMarkings;
    }

    public static DrugShipment convert(MoveOrder document) {
        DrugShipment shipment = new DrugShipment();

        DrugShipmentProperty property = new DrugShipmentProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setConsigneeId(document.getReceiverId());
        property.setAcceptType(document.getAcceptType());
        property.setDocDate(convertShortDateToCore(document.getDocDate()));
        property.setDocNum(document.getDocNum());

        if (document.getOrderDetails() != null && document.getOrderDetails().getUnions() != null) {
            for (MoveOrder.OrderDetails.Union union : document.getOrderDetails().getUnions()) {
                DrugShipment.ShipmentKizMetadata metadata = new DrugShipment.ShipmentKizMetadata();
                metadata.setCost(toPennies(union.getCost()));
                metadata.setVatValue(toPennies(union.getVatValue()));
                DrugShipment.ShipmentKiz kiz = new DrugShipment.ShipmentKiz();
                if (union.getSgtin() != null) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                    kiz.setSign(union.getSgtin());
                } else if (union.getSscc() != null) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                    kiz.setSign(union.getSscc());
                }
                kiz.setMetadata(metadata);
                shipment.getKizs().add(kiz);
            }
        }

        shipment.setDrugShipmentProperty(property);

        return shipment;
    }

    public static DrugAcceptance convert(ReceiveOrder document) {
        DrugAcceptance acceptance = new DrugAcceptance();

        DrugAcceptanceProperty property = new DrugAcceptanceProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setReceivingType(document.getReceiveType());
        property.setDocNum(document.getDocNum());
        property.setDocDate(convertShortDateToCore(document.getDocDate()));
        property.setAcceptType(document.getAcceptType());
        property.setSellerId(document.getShipperId());

        if (document.getOrderDetails() != null && document.getOrderDetails().getUnions() != null) {
            for (ReceiveOrder.OrderDetails.Union union : document.getOrderDetails().getUnions()) {
                DrugAcceptance.AcceptanceKizMetadata metadata = new DrugAcceptance.AcceptanceKizMetadata();
                metadata.setCost(toPennies(union.getCost()));
                metadata.setVatValue(toPennies(union.getVatValue()));
                DrugAcceptance.AcceptanceKiz kiz = new DrugAcceptance.AcceptanceKiz();
                if (union.getSgtin() != null) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                    kiz.setSign(union.getSgtin());
                } else if (union.getSscc() != null) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                    kiz.setSign(union.getSscc());
                }
                kiz.setMetadata(metadata);
                acceptance.getKizs().add(kiz);
            }
        }

        acceptance.setDrugAcceptanceProperty(property);

        return acceptance;
    }

    public static DrugMoveInfo convert(MoveInfo document) {
        DrugMoveInfo drugMoveInfo = new DrugMoveInfo();

        DrugMoveInfoProperty property = new DrugMoveInfoProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setConsigneeId(document.getReceiverId());
        property.setDocDate(convertShortDateToCore(document.getDocDate()));
        property.setDocNum(document.getDocNum());

        List<DrugMoveInfo.DrugMoveGtin> gtins = new ArrayList<>();
        if (document.getOrderDetails() != null && document.getOrderDetails().getDetails() != null && !document.getOrderDetails().getDetails().isEmpty()) {
            for (MoveInfo.OrderDetails.Detail detail : document.getOrderDetails().getDetails()) {
                DrugMoveInfo.DrugMoveGtin gtin = new DrugMoveInfo.DrugMoveGtin();
                gtin.setGtin(detail.getGtin());
                gtin.setBatch(detail.getSeriesNumber());
                gtin.setCost(detail.getCount());
                gtins.add(gtin);
            }
        }

        drugMoveInfo.setGtins(gtins);
        drugMoveInfo.setDrugMoveInfoProperty(property);
        return drugMoveInfo;
    }

    public static DrugSale convert(RetailSale document) {
        DrugSale drugSale = new DrugSale();

        DrugSaleProperty property = new DrugSaleProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOperationDate(Utils.convertLongDateToCore(document.getOperationDate()));

        if (document.getSales() != null && document.getSales().getDetails() != null) {
            for (RetailSale.Sales.Detail detail : document.getSales().getDetails()) {
                DrugSale.SaleKizMetadata metadata = new DrugSale.SaleKizMetadata();
                metadata.setPrice(toPennies(detail.getCost()));
                metadata.setVat(toPennies(detail.getVatValue()));

                DrugSale.SaleKiz kiz = new DrugSale.SaleKiz();
                kiz.setMetadata(metadata);
                kiz.setSignType(0);
                kiz.setSign(detail.getSgtin());
                drugSale.getKizs().add(kiz);

                if (detail.getSaleDocs() != null) {
                    List<DrugSale.SaleDoc> saleDocs = new ArrayList<>();
                    for (RetailSale.Sales.Detail.SaleDocs.Doc doc : detail.getSaleDocs().getDocs()) {
                        DrugSale.SaleDoc saleDoc = new DrugSale.SaleDoc();
                        saleDoc.setDocType(DocumentTypeEnum.getByCode(doc.getDocType()).getCode());
                        saleDoc.setDocName(doc.getDocName());
                        saleDoc.setDocNum(doc.getDocNumber());
                        saleDoc.setDocDate(convertShortDateToCore(doc.getDocDate()));
                        saleDocs.add(saleDoc);
                        break;
                    }
                    metadata.setDocs(saleDocs);
                }
            }
        }
        drugSale.setDrugSaleProperty(property);

        return drugSale;
    }

    public static DrugEmission convert(RegisterProductEmission document) {
        DrugEmission emission = new DrugEmission();

        DrugEmissionProperty property = new DrugEmissionProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setDocDate(convertShortDateToCore(document.getDocDate()));
        property.setDocNum(document.getDocNum());
        property.setDocType(document.getConfirmDoc());

        emission.setDrugEmissionProperty(property);

        if (document.getSigns() != null) {
            for (JAXBElement<String> orderDetails : document.getSigns().getSgtinsAndSsccs()) {
                DrugEmission.PrimaryEmissionKiz kiz = new DrugEmission.PrimaryEmissionKiz();
                kiz.setSign(orderDetails.getValue());
                if (orderDetails.getName().getLocalPart().equals("sgtin")) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                } else if (orderDetails.getName().getLocalPart().equals("sscc")) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                }

                emission.getKizs().add(kiz);
            }
        }
        return emission;
    }

    public static DrugControlSamples convert(RegisterControlSamples document) {
        DrugControlSamples controlSamples = new DrugControlSamples();

        DrugControlSamplesProperty property = new DrugControlSamplesProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setControlType(document.getControlSamplesType());

        controlSamples.setDrugControlSamplesProperty(property);

        if (document.getSigns() != null) {
            for (String sgtin : document.getSigns().getSgtins()) {

                DrugControlSamples.ControlSamplesKiz kiz = new DrugControlSamples.ControlSamplesKiz();
                kiz.setSign(sgtin);
                kiz.setSignType(SignTypeEnum.SGTIN.getCode());

                controlSamples.getKizs().add(kiz);
            }
        }
        return controlSamples;
    }

    public static DrugMoveOwner convert(MoveOwner document) {
        DrugMoveOwner moveOwner = new DrugMoveOwner();

        DrugMoveOwnerProperty property = new DrugMoveOwnerProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setOwnerId(document.getOwnerId());
        property.setDocNum(document.getDocNum());
        property.setDocDate(Utils.convertShortDateToCore(document.getDocDate()));
        property.setRelocation(document.isStorageChange() ? 0 : 1);

        moveOwner.setDrugMoveOwnerProperty(property);

        if (document.getOrderDetails() != null) {
            for (JAXBElement<String> orderDetails : document.getOrderDetails().getSgtinsAndSsccs()) {
                DrugMoveOwner.MoveOwnerKiz kiz = new DrugMoveOwner.MoveOwnerKiz();
                kiz.setSign(orderDetails.getValue());
                if (orderDetails.getName().getLocalPart().equals("sgtin")) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                } else if (orderDetails.getName().getLocalPart().equals("sscc")) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                }

                moveOwner.getKizs().add(kiz);
            }
        }
        return moveOwner;
    }

    public static DrugReceiveOwner convert(ReceiveOwner document) {
        DrugReceiveOwner receiveOwner = new DrugReceiveOwner();

        DrugReceiveOwnerProperty property = new DrugReceiveOwnerProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setDocNum(document.getDocNum());
        property.setDocDate(Utils.convertShortDateToCore(document.getDocDate()));

        receiveOwner.setDrugReceiveOwnerProperty(property);

        if (document.getOrderDetails() != null) {
            for (JAXBElement<String> orderDetails : document.getOrderDetails().getSgtinsAndSsccs()) {

                DrugReceiveOwner.ReceiveOwnerKiz kiz = new DrugReceiveOwner.ReceiveOwnerKiz();
                kiz.setSign(orderDetails.getValue());
                if (orderDetails.getName().getLocalPart().equals("sgtin")) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                } else if (orderDetails.getName().getLocalPart().equals("sscc")) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                }

                receiveOwner.getKizs().add(kiz);
            }
        }
        return receiveOwner;
    }

    public static DrugReceiveImporter convert(ReceiveImporter document) {
        DrugReceiveImporter receiveImporter = new DrugReceiveImporter();

        DrugReceiveImporterProperty property = new DrugReceiveImporterProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setDocNum(document.getDocNum());
        property.setDocDate(Utils.convertShortDateToCore(document.getDocDate()));
        receiveImporter.setDrugReceiveImporterProperty(property);

        if (document.getOrderDetails() != null) {
            for (JAXBElement<String> orderDetails : document.getOrderDetails().getSgtinsAndSsccs()) {

                DrugReceiveImporter.ReceiveImporterKiz kiz = new DrugReceiveImporter.ReceiveImporterKiz();
                kiz.setSign(orderDetails.getValue());
                if (orderDetails.getName().getLocalPart().equals("sgtin")) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                } else if (orderDetails.getName().getLocalPart().equals("sscc")) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                }

                receiveImporter.getKizs().add(kiz);
            }
        }
        return receiveImporter;
    }

    public static DrugRecipe convert(Recipe document) {
        DrugRecipe recipe = new DrugRecipe();

        DrugRecipeProperty property = new DrugRecipeProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setPrescriptionNum(document.getDocNum());
        property.setPrescriptionDate(Utils.convertShortDateToCore(document.getDocDate()));

        recipe.setDrugRecipeProperty(property);

        if (document.getOrderDetails() != null) {
            for (String orderDetails : document.getOrderDetails().getSgtins()) {

                DrugRecipe.RecipeKiz kiz = new DrugRecipe.RecipeKiz();
                kiz.setSign(orderDetails);
                kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                recipe.getKizs().add(kiz);
            }
        }
        return recipe;
    }

    public static DrugWithdrawal convert(Withdrawal document) {
        DrugWithdrawal withdrawal = new DrugWithdrawal();

        DrugWithdrawalProperty property = new DrugWithdrawalProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setWithdrawalType(document.getWithdrawalType());
        property.setDocNum(document.getDocNum());
        property.setDocDate(Utils.convertShortDateToCore(document.getDocDate()));

        withdrawal.setDrugWithdrawalProperty(property);

        if (document.getOrderDetails() != null) {
            for (JAXBElement<String> orderDetails : document.getOrderDetails().getSgtinsAndSsccs()) {

                DrugWithdrawal.WithdrawalKiz kiz = new DrugWithdrawal.WithdrawalKiz();
                kiz.setSign(orderDetails.getValue());
                if (orderDetails.getName().getLocalPart().equals("sgtin")) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                } else if (orderDetails.getName().getLocalPart().equals("sscc")) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                }

                withdrawal.getKizs().add(kiz);
            }
        }
        return withdrawal;
    }

    public static DrugHealthCare convert(HealthCare document) {
        DrugHealthCare healthCare = new DrugHealthCare();

        DrugHealthCareProperty property = new DrugHealthCareProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setUseDocNum(document.getDocNum());
        property.setUseDocTime(Utils.convertShortDateToCore(document.getDocDate()));

        healthCare.setDrugHealthCareProperty(property);

        if (document.getOrderDetails() != null) {
            for (JAXBElement<String> orderDetails : document.getOrderDetails().getSgtinsAndSsccs()) {

                DrugHealthCare.HeathCareKiz kiz = new DrugHealthCare.HeathCareKiz();
                kiz.setSign(orderDetails.getValue());
                if (orderDetails.getName().getLocalPart().equals("sgtin")) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                } else if (orderDetails.getName().getLocalPart().equals("sscc")) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                }

                healthCare.getKizs().add(kiz);
            }
        }
        return healthCare;
    }

    public static DrugUnitPack convert(UnitPack document) {
        DrugUnitPack unitPack = new DrugUnitPack();

        DrugUnitPackProperty property = new DrugUnitPackProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setSscc(document.getSscc());

        unitPack.setDrugUnitPackProperty(property);

        if (document.getContent() != null) {
            for (JAXBElement<String> orderDetails : document.getContent().getSgtinsAndSsccs()) {

                DrugUnitPack.UnitPackKiz kiz = new DrugUnitPack.UnitPackKiz();
                kiz.setSign(orderDetails.getValue());
                if (orderDetails.getName().getLocalPart().equals("sgtin")) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                } else if (orderDetails.getName().getLocalPart().equals("sscc")) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                }
                unitPack.getKizs().add(kiz);
            }
        }
        return unitPack;
    }

    public static DrugUnitUnpack convert(UnitUnpack document) {
        DrugUnitUnpack unitUnpack = new DrugUnitUnpack();

        DrugUnitUnpackProperty property = new DrugUnitUnpackProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));

        DrugUnitUnpack.UnitUnpackKizMetadata metadata = new DrugUnitUnpack.UnitUnpackKizMetadata();
        metadata.setRecursive(Utils.toInteger(document.getIsRecursive()));
        DrugUnitUnpack.UnitUnpackKiz kiz = new DrugUnitUnpack.UnitUnpackKiz(document.getSscc(), 1, metadata);
        unitUnpack.getKizs().add(kiz);

        unitUnpack.setDrugUnitUnpackProperty(property);
        return unitUnpack;
    }

    public static DrugUnitExtract convert(UnitExtract document) {
        DrugUnitExtract unitExtract = new DrugUnitExtract();

        DrugUnitExtractProperty property = new DrugUnitExtractProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));

        unitExtract.setDrugUnitExtractProperty(property);

        if (document.getContent() != null) {
            for (JAXBElement<String> orderDetails : document.getContent().getSgtinsAndSsccs()) {

                DrugUnitExtract.UnitExtractKiz kiz = new DrugUnitExtract.UnitExtractKiz();
                kiz.setSign(orderDetails.getValue());
                if (orderDetails.getName().getLocalPart().equals("sgtin")) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                } else if (orderDetails.getName().getLocalPart().equals("sscc")) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                }
                unitExtract.getKizs().add(kiz);
            }
        }
        return unitExtract;
    }

    public static DrugForeignerEmission convert(ForeignEmission document) {
        DrugForeignerEmission foreignerEmission = new DrugForeignerEmission();

        DrugForeignerEmissionProperty property = new DrugForeignerEmissionProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
//        property.setControlId(document.getQualityContolId());
        property.setHsCode(document.getTnvedCode());
        property.setGtin(document.getGtin());
        property.setBatch(document.getSeriesNumber());
        property.setExpDate(Utils.convertShortDateToCore(document.getExpirationDate()));

        foreignerEmission.setDrugForeignerEmissionProperty(property);

        if (document.getSigns() != null && document.getSigns().getSgtins() != null) {
            for (String sgtin : document.getSigns().getSgtins()) {

                DrugForeignerEmission.ForeignerEmissionKiz kiz = new DrugForeignerEmission.ForeignerEmissionKiz();
                kiz.setSign(sgtin);
                kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                foreignerEmission.getKizs().add(kiz);
            }
        }
        return foreignerEmission;
    }

    public static DrugForeignShipment convert(ForeignShipment document) {
        DrugForeignShipment drugForeignShipment = new DrugForeignShipment();

        DrugForeignShipmentProperty property = new DrugForeignShipmentProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setSellerId(document.getSellerId());
        property.setConsumerId(document.getReceiverId());
        property.setInvoiceNum(document.getDocNum());
        property.setInvoiceDate(Utils.convertShortDateToCore(document.getDocDate()));
        drugForeignShipment.setDrugForeignShipmentProperty(property);

        if (document.getOrderDetails() != null) {
            for (ForeignShipment.OrderDetails.Union union : document.getOrderDetails().getUnions()) {
                DrugForeignShipment.ForeignShipmentKiz kiz = new DrugForeignShipment.ForeignShipmentKiz();
                if (union.getSgtin() != null) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                    kiz.setSign(union.getSgtin());
                } else if (union.getSscc() != null) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                    kiz.setSign(union.getSscc());
                }

                DrugForeignShipment.DrugForeignShipmentMetadata metadata = new DrugForeignShipment.DrugForeignShipmentMetadata();
                metadata.setCost(toPennies(union.getCost()));
                kiz.setMetadata(metadata);

                drugForeignShipment.getKizs().add(kiz);
            }
        }
        return drugForeignShipment;
    }

    public static DrugForeignImport convert(ForeignImport document) {
        DrugForeignImport drugForeignImport = new DrugForeignImport();

        DrugForeignImportProperty property = new DrugForeignImportProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setSellerId(document.getSellerId());
        property.setInvoiceNum(document.getDocNum());
        property.setInvoiceDate(Utils.convertShortDateToCore(document.getDocDate()));
        drugForeignImport.setDrugForeignImportProperty(property);

        if (document.getOrderDetails() != null) {
            for (ForeignImport.OrderDetails.Union union : document.getOrderDetails().getUnions()) {
                DrugForeignImport.ForeignImportKiz kiz = new DrugForeignImport.ForeignImportKiz();
                if (union.getSgtin() != null) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                    kiz.setSign(union.getSgtin());
                } else if (union.getSscc() != null) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                    kiz.setSign(union.getSscc());
                }

                DrugForeignImport.ForeignImportKizMetadate metadate = new DrugForeignImport.ForeignImportKizMetadate();
                metadate.setCost(toPennies(union.getCost()));
                kiz.setForeignImportKizMetadate(metadate);

                drugForeignImport.getKizs().add(kiz);
            }
        }
        return drugForeignImport;
    }

    public static DrugFtsData convert(FtsData document) {
        DrugFtsData drugFtsData = new DrugFtsData();

        DrugFtsDataProperty property = new DrugFtsDataProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setDocType(document.getConfirmDoc());
        property.setDocNum(document.getDocNum());
        property.setDocDate(Utils.convertShortDateToCore(document.getDocDate()));

        DrugFtsDataProperty.GtdInfo gtdInfo = new DrugFtsDataProperty.GtdInfo();
        gtdInfo.setCustomsCode(document.getFtsInfo().getCustomsCode());
        gtdInfo.setRegDate(Utils.convertShortDateToCore(document.getFtsInfo().getRegistrationDate()));
        gtdInfo.setRegNumber(document.getFtsInfo().getGtdNumber());
        property.setGtdInfo(gtdInfo);
        property.setCustomProcedureCode(document.getCustomProcedureCode());
        property.setControlType(document.getConfirmDoc());
        drugFtsData.setDrugFtsDataProperty(property);

        if (document.getOrderDetails() != null) {
            for (JAXBElement<String> orderDetails : document.getOrderDetails().getSgtinsAndSsccs()) {

                DrugFtsData.FtsDataKiz kiz = new DrugFtsData.FtsDataKiz();
                kiz.setSign(orderDetails.getValue());
                if (orderDetails.getName().getLocalPart().equals("sgtin")) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                } else if (orderDetails.getName().getLocalPart().equals("sscc")) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                }

                drugFtsData.getKizs().add(kiz);
            }
        }
        return drugFtsData;
    }

    public static DrugMovePlace convert(MovePlace document) {
        DrugMovePlace drugMovePlace = new DrugMovePlace();

        DrugMovePlaceProperty property = new DrugMovePlaceProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setConsigneeId(document.getReceiverId());
        property.setStorageType(document.getStockType());
        property.setDocNum(document.getDocNum());
        property.setDocDate(Utils.convertShortDateToCore(document.getDocDate()));

        drugMovePlace.setDrugMovePlaceProperty(property);

        if (document.getOrderDetails() != null) {
            for (JAXBElement<String> orderDetails : document.getOrderDetails().getSgtinsAndSsccs()) {

                DrugMovePlace.MovePlaceKiz kiz = new DrugMovePlace.MovePlaceKiz();
                kiz.setSign(orderDetails.getValue());
                if (orderDetails.getName().getLocalPart().equals("sgtin")) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                } else if (orderDetails.getName().getLocalPart().equals("sscc")) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                }

                drugMovePlace.getKizs().add(kiz);
            }
        }
        return drugMovePlace;
    }

    public static RecallOperation convert(Recall document) {
        RecallOperation recall = new RecallOperation();
        recall.setSystemSubjectId(document.getSubjectId());
        recall.setActionToCancelId(String.valueOf(document.getSessionUi()));
        recall.setCancelReason(document.getReason());
        return recall;
    }

    public static DrugMoveDestruction convert(MoveDestruction document) {
        DrugMoveDestruction drugMoveDestruction = new DrugMoveDestruction();

        DrugMoveDestructionProperty property = new DrugMoveDestructionProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        DrugMoveDestructionProperty.DestructionOrgId destructionOrgId = new DrugMoveDestructionProperty.DestructionOrgId();
        if (document.getDestructionOrg().getUl() != null) {
            destructionOrgId.setInn(document.getDestructionOrg().getUl().getInn());
            destructionOrgId.setKpp(document.getDestructionOrg().getUl().getKpp());
        } else if (document.getDestructionOrg().getFl() != null) {
            destructionOrgId.setInn(document.getDestructionOrg().getFl().getInn());
        }
        property.setDestruction_org_id(destructionOrgId);
        Address address = new Address();
        address.setAoguid(document.getDestructionOrg().getAddres().getAoguid());
        address.setHouseguid(document.getDestructionOrg().getAddres().getHouseguid());
        address.setRoom(document.getDestructionOrg().getAddres().getFlat());
        property.setDestructionAddress(address);
        property.setContractNum(document.getDocNum());
        property.setContractDate(Utils.convertShortDateToCore(document.getDocDate()));
        property.setActNum(document.getActNumber());
        property.setActDate(Utils.convertShortDateToCore(document.getActDate()));

        drugMoveDestruction.setDrugMoveDestructionProperty(property);

        if (document.getOrderDetails() != null) {
            for (MoveDestruction.OrderDetails.Detail orderDetails : document.getOrderDetails().getDetails()) {

                DrugMoveDestruction.MoveDestructionKiz kiz = new DrugMoveDestruction.MoveDestructionKiz();
                if (orderDetails.getSgtin() != null) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                    kiz.setSign(orderDetails.getSgtin());
                } else if (orderDetails.getSscc() != null) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                    kiz.setSign(orderDetails.getSscc());
                }
                DrugMoveDestruction.MoveDestructionKizMetadata metadata = new DrugMoveDestruction.MoveDestructionKizMetadata();
                metadata.setDecision(orderDetails.getDecision());
                metadata.setReason(orderDetails.getDestructionType());
                kiz.setMoveDestructionKizMetadata(metadata);

                drugMoveDestruction.getKizs().add(kiz);
            }
        }
        return drugMoveDestruction;
    }

    public static DrugDestruction convert(Destruction document) {
        DrugDestruction drugDestruction = new DrugDestruction();

        DrugDestructionProperty property = new DrugDestructionProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setDestructionMethod(document.getDestructionMethod());
        DrugDestructionProperty.DestructionOrgId destructionOrgId = new DrugDestructionProperty.DestructionOrgId();
        if (document.getDestructionOrg().getUl() != null) {
            destructionOrgId.setInn(document.getDestructionOrg().getUl().getInn());
            destructionOrgId.setKpp(document.getDestructionOrg().getUl().getKpp());
        } else if (document.getDestructionOrg().getFl() != null) {
            destructionOrgId.setInn(document.getDestructionOrg().getFl().getInn());
        }
        property.setDestruction_org_id(destructionOrgId);
//        Address address = new Address();
//        address.setAoguid(document.getDestructionOrg().getAddres().getAoguid());
//        address.setHouseguid(document.getDestructionOrg().getAddres().getHouseguid());
//        address.setRoom(document.getDestructionOrg().getAddres().getFlat());
//        property.setDestructionAddress(address);
        property.setActNum(document.getDocNum());
        property.setActDate(Utils.convertShortDateToCore(document.getDocDate()));

        drugDestruction.setDrugDestructionProperty(property);

        if (document.getOrderDetails() != null) {
            for (JAXBElement<String> orderDetails : document.getOrderDetails().getSgtinsAndSsccs()) {

                DrugDestruction.DestructionKiz kiz = new DrugDestruction.DestructionKiz();
                kiz.setSign(orderDetails.getValue());
                if (orderDetails.getName().getLocalPart().equals("sgtin")) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                } else if (orderDetails.getName().getLocalPart().equals("sscc")) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                }

                drugDestruction.getKizs().add(kiz);
            }
        }
        return drugDestruction;
    }

    public static DrugWithdrawal convert(Reexport document) {
        DrugWithdrawal withdrawal = new DrugWithdrawal();

        DrugWithdrawalProperty property = new DrugWithdrawalProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setWithdrawalType(document.getReexportType());
        property.setDocNum(document.getDocNum());
        property.setDocDate(Utils.convertShortDateToCore(document.getDocDate()));

        withdrawal.setDrugWithdrawalProperty(property);

        if (document.getOrderDetails() != null) {
            for (JAXBElement<String> orderDetails : document.getOrderDetails().getSgtinsAndSsccs()) {

                DrugWithdrawal.WithdrawalKiz kiz = new DrugWithdrawal.WithdrawalKiz();
                kiz.setSign(orderDetails.getValue());
                if (orderDetails.getName().getLocalPart().equals("sgtin")) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                } else if (orderDetails.getName().getLocalPart().equals("sscc")) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                }

                withdrawal.getKizs().add(kiz);
            }
        }
        return withdrawal;
    }

    public static DrugRelabeling convert(Relabeling document) {
        DrugRelabeling relabeling = new DrugRelabeling();

        DrugRelabelingProperty property = new DrugRelabelingProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));

        relabeling.setDrugRelabelingProperty(property);

        if (document.getRelabelingDetail() != null) {
            for (Relabeling.RelabelingDetail.Detail orderDetails : document.getRelabelingDetail().getDetails()) {

                DrugRelabeling.RelabelingKiz kiz = new DrugRelabeling.RelabelingKiz();
                kiz.setSign(orderDetails.getOldSgtin());
                kiz.setSignType(SignTypeEnum.SGTIN.getCode());

                DrugRelabeling.RelabelingKizMetadata metadata = new DrugRelabeling.RelabelingKizMetadata();
                metadata.setSgtinNew(orderDetails.getNewSgtin());
                kiz.setRelabelingKizMetadata(metadata);

                relabeling.getKizs().add(kiz);
            }
        }
        return relabeling;
    }

    public static DrugUnitAppend convert(UnitAppend document) {
        DrugUnitAppend drugUnitAppend = new DrugUnitAppend();

        DrugUnitAppendProperty property = new DrugUnitAppendProperty();
        property.setSystemSubjId(document.getSubjectId());
        property.setOpDate(convertLongDateToCore(document.getOperationDate()));
        property.setSscc(document.getSscc());

        drugUnitAppend.setDrugUnitAppendProperty(property);

        if (document.getContent() != null) {
            for (JAXBElement<String> orderDetails : document.getContent().getSgtinsAndSsccs()) {

                DrugUnitAppend.UnitAppendKiz kiz = new DrugUnitAppend.UnitAppendKiz();
                kiz.setSign(orderDetails.getValue());
                if (orderDetails.getName().getLocalPart().equals("sgtin")) {
                    kiz.setSignType(SignTypeEnum.SGTIN.getCode());
                } else if (orderDetails.getName().getLocalPart().equals("sscc")) {
                    kiz.setSignType(SignTypeEnum.SSCC.getCode());
                }

                drugUnitAppend.getKizs().add(kiz);
            }
        }
        return drugUnitAppend;
    }

}
