package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SSuvorov on 30.03.2017.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugSale extends CoreDrugOperation {
    @JsonProperty("properties")
    private DrugProperty drugSaleProperty;

    @NotNull
    @JsonProperty("kizs")
    protected List<SaleKiz> kizs = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SaleKiz {
        @JsonProperty("sign_type")
        protected int signType;

        @JsonProperty("sign")
        protected String sign;

        @JsonProperty("metadata")
        protected SaleKizMetadata metadata;

        public SaleKiz(String sign, int signType, SaleKizMetadata metadata) {
            this.signType = signType;
            this.sign = sign;
            this.metadata = metadata;
        }
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper=true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SaleKizMetadata extends CoreDrugMetadata {
        // Розничная цена (в копейках)
        @JsonProperty("price")
        private Long price;

        // НДС в копейках
        @JsonProperty("vat_value")
        private Long vat;

//        // Дата продажи
//        @JsonProperty("date")
//        private String date;

        // Документ продажи
        @JsonProperty("docs")
        private List<SaleDoc> docs = new ArrayList<>();

        @Override
        @JsonIgnore
        public Long getPriceInRubbles() {
            return price/100;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SaleDoc {
        // Вид документа
        @JsonProperty("doc_type")
        private int docType;

        // Наименование документа
        @JsonProperty("doc_name")
        private String docName;

        // Номер кассового чека
        @JsonProperty("doc_num")
        private String docNum;

        // Дата и время выдачи кассового чека
        @JsonProperty("doc_date")
        private String docDate;
    }
}
