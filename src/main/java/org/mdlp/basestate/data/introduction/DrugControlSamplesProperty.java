package org.mdlp.basestate.data.introduction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mdlp.basestate.data.DrugProperty;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 25.05.2017
 * @package org.mdlp.basestate.data
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugControlSamplesProperty extends DrugProperty {
    // Дата и время совершения операции
    @JsonProperty("op_date")
    private String opDate;

    // Вид отбора образца
    @JsonProperty("control_type")
    private int controlType;

    @Override
    public String getOpDate() {
        return opDate;
    }

    public static enum ControlTypeEnum {
        CONTROL_SAMPLES(1),
        ARCHIVE_SAMPLES(2),
        CONFIRMATION_SAMPLES(3);

        private final int code;

        ControlTypeEnum(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @JsonCreator
        public static ControlTypeEnum getByCode(int code) {
            for (ControlTypeEnum value : values()) {
                if (value.code == code) {
                    return value;
                }
            }
            throw new IllegalArgumentException(
                    "No enum constant in " + ControlTypeEnum.class.getCanonicalName() + " with code " + code);
        }
    }
}

