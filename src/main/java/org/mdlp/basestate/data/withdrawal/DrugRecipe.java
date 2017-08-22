package org.mdlp.basestate.data.withdrawal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mdlp.basestate.data.CoreDrugOperation;
import org.mdlp.basestate.data.introduction.DrugReceiveImporterProperty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 25.05.2017
 * @package org.mdlp.basestate.data.withdrawal
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugRecipe extends CoreDrugOperation {
    @JsonProperty("properties")
    private DrugRecipeProperty drugRecipeProperty;

    @NotNull
    @JsonProperty("kizs")
    protected List<RecipeKiz> kizs = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecipeKiz {
        @NotNull
        @JsonProperty("sign_type")
        protected int signType;

        @NotNull
        @JsonProperty("sign")
        protected String sign;

        public RecipeKiz(String sign, int signType) {
            this.signType = signType;
            this.sign = sign;
        }
    }
}
