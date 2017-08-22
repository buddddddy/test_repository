package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.mdlp.basestate.data.mod.BrokenOpDate;

import javax.validation.constraints.NotNull;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 23.05.2017
 * @package org.mdlp.basestate.data
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Kiz {
    // URL файла изображения товара (GTIN) в формате png
    @NotNull
    @JsonProperty("PROD_IMAGE")
    private String prodImage;

    // Бренд (торговая марка)
    @NotNull
    @JsonProperty("PROD_NAME")
    private String prodName;

    //Наименование держателя регистрационного удостоверения
    @NotNull
    @JsonProperty("TR_PARTNER_NAME")
    private String trPartnerName;

    //Полное наименование товара
    @NotNull
    @JsonProperty("PROD_DESC_FULL")
    private String prodDescFull;

    //Описание вложенной немаркированнной (первичной) упаковки
    @NotNull
    @JsonProperty("PROD_COVER_EXT_DESC")
    private String prodCoverExtDesc;

    //Номер производственной серии
    @NotNull
    @JsonProperty("BATCH")
    private String batch;

    //Срок годности
    @NotNull
    @JsonProperty("EXP_DATE")
    private String expDate;

    @JsonProperty("CURRENT_OWNER")
    private String currentOwner;

    //Состояние КиЗа
    @NotNull
    @JsonProperty("sgtin_state")
    private SgtinState sgtinState;

    //Идентификационный код вторичной (потребительской) упаковки
    @JsonProperty("sgtin")
    private String sgtin;

    //Идентификационный номер товара (GTIN)
    @JsonProperty("gtin")
    private String gtin;

    //Идентификационный код третичной упаковки, имеющей во вложении указанный код вторичной (потребительской упаковки)
    @JsonProperty("sscc")
    private String sscc;

    @JsonProperty("inn")
    private String inn;

    @JsonProperty("federal_subject_code")
    private String federalSubjectCode;

    @JsonProperty("federal_subject_name")
    private FederalSubjectName federalSubjectName;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FederalSubjectName {

        @NotNull
        @JsonProperty("id")
        private String id;

        @NotNull
        @JsonProperty("federal_subject_code")
        private String federalSubjectCode;

        @NotNull
        @JsonProperty("federal_subject_name")
        private String federalSubjectName;
    }

    @Nullable
    @JsonProperty("release_op_date")
    private BrokenOpDate releaseOperationDate;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SgtinState {
        // Текущее состояние
        @NotNull
        @JsonProperty("internalState")
        private String internalState;

        // Дата и время совершения операции
        @NotNull
        @JsonProperty("op_date")
        private BrokenOpDate opDate;

        public SgtinState(String internalState, BrokenOpDate opDate) {
            this.internalState = internalState;
            this.opDate = opDate;
        }
    }
}
