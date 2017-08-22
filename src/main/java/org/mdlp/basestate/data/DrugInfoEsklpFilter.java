package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author PVBorisov (Pavel.V.Borisov@firstlinesoftware.com)
 * @version 21.06.2017
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugInfoEsklpFilter extends PagedFilter {

    // Номер регистрационного удостоверения
    @JsonProperty(value = "REG_ID")
    private String regId;

    // Наименование держателя регистрационного удостоверения
    @JsonProperty("REG_HOLDER")
    private String regHolder;

    // Международное непатентованное наименование, или группировочное, или химическое наименование (строковое представление)
    @JsonProperty("PROD_NAME")
    private String prodName;

    // Торговое наименование
    @JsonProperty(value = "PROD_SELL_NAME")
    private String prodSellName;

    // Код держателя РУ
    @JsonProperty(value = "REG_HOLDER_CODE")
    private String regHolderCode;

    // Дата государственной регистрации (с)
    @JsonProperty(value = "REG_DATE")
    private String regDate;

    // Дата окончания действия регистрационного удостоверения (по)
    @JsonProperty(value = "REG_END_DATE")
    private String regEndDate;

    // Статусы действия регистрационного удостоверения
    @JsonProperty(value = "REG_STATUS")
    private String regStatus;
}
