package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by SSuvorov on 05.04.2017.
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugInfoGs1 {

    @JsonProperty("PROD_COVER_GTIN")
    private String prodCoverGtin;

    @JsonProperty("PROD_NAME")
    private String prodName;

    @JsonProperty("GS1_MEMBER_GLN")
    private String gs1MemberGln;

    @JsonProperty("TR_PARTNER_NAME")
    private String trPartnerName;

    @JsonProperty("WEB_90000175")
    private String web90000175;

    @JsonProperty("WEB_90001589")
    private OperationDate web90001589;

    @JsonProperty("PROD_COVER_TYPE_DICT")
    private String prodCoverTypeDict;

    @JsonProperty("PROD_COVER_EXT_DESC")
    private String prodCoverExtDesc;

    @JsonProperty("PROD_GCPCL_BRICK")
    private String prodGcpclBrick;

    @JsonProperty("TR_PARTNER_ROLE")
    private String trPartnerRole;

    @JsonProperty("PROD_COVER_MATERIAL")
    private String prodCoverMaterial;

    @JsonProperty("PROD_DESC_FULL")
    private String prodDescFull;

    @JsonProperty("gtin")
    private String gtin;

    @JsonProperty("PROD_GCPCL_SEG")
    private String prodGcpclSeg;

    @JsonProperty("PROD_CUST_COVER_CODES")
    private String prodCustCoverCodes;

    @JsonProperty("PROD_DESC")
    private String prodDesc;

    @JsonProperty("PROD_CODED_FROM_1C")
    private String prodCodedFrom1C;

    @JsonProperty("PROD_MEASURE")
    private String prodMeasure;

    @JsonProperty("TR_PARTNER_GLN")
    private String trPartnerGln;

    @JsonProperty("PROD_TM_CHANGED_BY_GS46")
    private String prodTmChangedByGS46;

    @JsonProperty("TNVED_1")
    private String tnved1;

    @JsonProperty("TR_PARTNER_ADDR")
    private String trPartnerAddr;

    @JsonProperty("id")
    private String id;

    @JsonProperty("TNVED_3")
    private String tnved3;

    @JsonProperty("TNVED_4")
    private String tnved4;

    @JsonProperty("TNVED_5")
    private String tnved5;

    @JsonProperty("WEB_90000164")
    private String web90000164;
    @JsonProperty("WEB_90001681")
    private String web90001681;

    @JsonProperty("PROD_PVN")
    private String prodPvn;

    @JsonProperty("MEAS_CUBISCAN")
    private String measCubiscan;

    @JsonProperty("PROD_COVER_PREFIX")
    private String prodCoverPrefix;

    @JsonProperty("PROD_GCPCL_CLASS")
    private String prodGcpclClass;

    @JsonProperty("CFG_ID")
    private String cfgId;

    @JsonProperty("WEB_90001591")
    private String web90001591;

    @JsonProperty("TNVED_2")
    private String tnved2;

    @JsonProperty("PROD_COUNT")
    private String prodCount;

    @JsonProperty("PROD_GCPCL_FAMILY")
    private String prodGcpclFamily;

    @JsonProperty("PUBLICATION_DATE")
    private String publicationDate;

    @JsonProperty("PROD_HAS_INNER_PACK")
    private String prodHasInnerPack;

    @JsonProperty("WEB_90001590")
    private String web90001590;

    @JsonProperty("PROD_MEAS_ILAB")
    private String prodMeasIlab;

    @JsonProperty("CFG_VAL")
    private String cfgVal;

    @JsonProperty("TR_PARTNER_NAME_SERVICE_PROVIDER")
    private String trPartnerNameServiceProvider;

    @JsonProperty("TR_PARTNER_ADDR_SERVICE_PROVIDER")
    private String trPartnerAddrServiceProvider;

    @JsonProperty("TR_PARTNER_ROLE_SERVICE_PROVIDER")
    private String trPartnerRoleServiceProvider;

    @JsonProperty("TR_PARTNER_GLN_SERVICE_PROVIDER")
    private String trPartnerGlnServiceProvider;
}

