package org.mdlp.basestate.data;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 25.05.2017
 * @package org.mdlp.basestate.data
 */
public enum SignTypeEnum {

    SGTIN(0),
    SSCC(1);

    private final int code;

    SignTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static SignTypeEnum getByCode(int code) {
        for (SignTypeEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException(
                "No enum constant in " + SignTypeEnum.class.getCanonicalName() + " with code " + code);
    }
}
