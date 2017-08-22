package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class DrugProperty extends CoreDrugProperty {
    // Идентификатор субъекта обращения в ИС "Маркировка товаров"
    @JsonProperty("system_subj_id")
    private String systemSubjId;

    @Override
    @JsonIgnore
    public String getMemberId() {
        return getSystemSubjId();
    }

    @Override
    @JsonIgnore
    public String getReceiverId() {
        return null;
    }

    @Override
    public String getOpDate() {
        return null;
    }
}
