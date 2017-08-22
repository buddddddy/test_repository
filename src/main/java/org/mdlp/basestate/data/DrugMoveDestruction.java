package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 05.07.2017
 * @package org.mdlp.basestate.data
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugMoveDestruction extends CoreDrugOperation {
    @JsonProperty("properties")
    private DrugMoveDestructionProperty drugMoveDestructionProperty;

    @JsonProperty("kizs")
    protected List<MoveDestructionKiz> kizs = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MoveDestructionKiz {
        @JsonProperty("sign_type")
        protected int signType;

        @JsonProperty("sign")
        protected String sign;

        @JsonProperty("metadata")
        protected MoveDestructionKizMetadata moveDestructionKizMetadata;

        public MoveDestructionKiz(String sign, int signType, MoveDestructionKizMetadata moveDestructionKizMetadata) {
            this.signType = signType;
            this.sign = sign;
            this.moveDestructionKizMetadata = moveDestructionKizMetadata;
        }
    }


    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MoveDestructionKizMetadata {

        // Причина (основание) передачи на уничтожение
        @JsonProperty("reason")
        private int reason;

        // Реквизиты решения Росздравнадзора о выводе из оборота
        @JsonProperty("decision")
        private String decision;

        public MoveDestructionKizMetadata(int reason, String decision) {
            this.reason = reason;
            this.decision = decision;
        }
    }
}
