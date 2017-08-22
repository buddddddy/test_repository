package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by SSuvorov on 30.03.2017.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugSaleProperty extends DrugProperty{
//    // Тип вывода из оборота
//    @JsonProperty("withdrawal_type")
//    private Integer withdrawalType;
//
//    // Адрес места осуществления деятельности субъекта обращения
//    @JsonProperty("subject_address")
//    private Address subjectAddress;


    @JsonProperty("op_date")
    private String operationDate;
}
