package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by PVBorisov on 21.06.2017.
 * emailto: Pavel.V.Borisov@firstlinesoftware.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugInfoGs1Filter extends PagedFilter {

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

    @JsonProperty("WEB_90001589_from")
    private String web90001589From;

    @JsonProperty("WEB_90001589_to")
    private String web90001589To;
}
