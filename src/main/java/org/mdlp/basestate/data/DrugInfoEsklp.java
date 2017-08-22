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
public class DrugInfoEsklp {

    // Номер регистрационного удостоверения
    @JsonProperty(value = "reg_id")
    private String regId;

    // Международное непатентованное наименование, или группировочное, или химическое наименование (строковое представление)
    @JsonProperty("PROD_NAME")
    private String prodName;

    // Код держателя РУ
    @JsonProperty(value = "REG_HOLDER_CODE")
    private String regHolderCode;

    // Статус действия регистрационного удостоверения
    @JsonProperty(value = "REG_STATUS")
    private String regStatus;

    // Первичная упаковка (строковое представление)
    @JsonProperty(value = "PROD_PACK_1_NAME")
    private String prodPack1Name;

    // Первичная упаковка
    @JsonProperty(value = "PROD_PACK_1_ID")
    private String prodPack1Id;

    // Ед. измерения массы/объема в первичной упаковке
    @JsonProperty(value = "PROD_PACK_1_ED")
    private String prodPpack1Ed;

    // Ед. измерения массы/объема в первичной упаковке (строковое представление)
    @JsonProperty(value = "PROD_PACK_1_ED_NAME")
    private String prodPpack1EdName;

    // Наименование упаковщика во вторичную/третичную упаковку
    @JsonProperty(value = "PACK_2_3_NAME")
    private String pack23Name;

    // Код налогоплательщика упаковщика во вторичную/третичную упаковку для резидентов РФ
    @JsonProperty(value = "PACK_2_3_CODE")
    private String pack23Code;

    // Код налогоплательщика упаковщика во вторичную/третичную упаковку в стране регистрации
    @JsonProperty(value = "PACK_2_3_CODE_F")
    private String pack23CodeF;

    // Страна регистрации упаковщика во вторичную/третичную упаковку
    @JsonProperty(value = "COUNTRY_PACK_2_3")
    private String countryPack23;

    // Код налогоплательщика стадии выпускающий контроль качества для резидентов РФ
    @JsonProperty(value = "QA_CODE")
    private String qaCode;

    // Код налогоплательщика стадии выпускающий контроль качества в стране регистрации
    @JsonProperty(value = "QA_CODE_F")
    private String qaCodeF;

    // Вторичная (потребительская) упаковка
    @JsonProperty(value = "PROD_PACK_2_ID")
    private String prodPack2Id;

    // Вторичная (потребительская) упаковка (строковое представление)
    @JsonProperty(value = "PROD_PACK_2_NAME")
    private String prodPack2Name;

    // Страна регистрации производителя стадии выпускающий контроль качества
    @JsonProperty(value = "QA_COUNTRY")
    private String qaCountry;

    // Страна регистрации держателя регистрационного удостоверения
    @JsonProperty(value = "REG_COUNTRY")
    private String regCountry;

    // Наименование производителя стадии выпускающий контроль качества
    @JsonProperty(value = "QA_NAME")
    private String qaName;

    // Масса/объем в первичной упаковке
    @JsonProperty(value = "PROD_PACK_1_SIZE")
    private String prodPack1Size;

    // Количество единиц измерения дозировки лекарственного препарата
    @JsonProperty(value = "PROD_D")
    private String prodD;

    // Адрес стадии выпускающий контроль качества (строкой)
    @JsonProperty(value = "QA_ADDRESS_NAME")
    private String qaAddressName;

    // Адрес фасовщика/упаковщика во вторичную/третичную упаковку (по ФИАС для резидентов РФ)
    @JsonProperty(value = "ADDRESS_FIAS")
    private String addressFias;

    // Адрес стадии выпускающий контроль качества (по ФИАС для резидентов РФ)
    @JsonProperty(value = "QA_ADDRESS_FIAS")
    private String qaAddressFias;

    // Адрес фасовщика/упаковщика во вторичную/третичную упаковку
    @JsonProperty(value = "ADDRESS")
    private String address;

    // Код налогоплательщика держателя регистрационного удостоверения в стране регистрации или его аналог
    @JsonProperty(value = "REG_HOLDER_CODE_F")
    private String regHolderCodeF;

    // Код ТН ВЭД
    @JsonProperty(value = "TN_VED")
    private String tnVed;

    // Количество единиц измерения дозировки лекарственного препарата (строковое представление)
    @JsonProperty(value = "PROD_D_NAME")
    private String prodDName;

    @JsonProperty(value = "PROD_FORM_NAME")
    private String prodFormName;

    @JsonProperty(value = "PROD_ID")
    private String prodId;

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "PROD_PACK_1")
    private String prodPack1;

    @JsonProperty(value = "PROD_SELL_NAME")
    private String prodSellName;

    @JsonProperty(value = "MAX_GNVLP")
    private String maxGnvlp;

    @JsonProperty(value = "PROD_PACK_1_2")
    private String prodPack12;

    @JsonProperty(value = "REG_END_DATE")
    private OperationDate regEndDate;

    @JsonProperty(value = "REG_DATE")
    private OperationDate regDate;

    @JsonProperty("REG_HOLDER")
    private String regHolder;

    @JsonProperty("GNVLP")
    private String gnvlp;

}
