package org.mdlp.basestate.data;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 23.05.2017
 * @package org.mdlp.basestate.data
 */
public enum DocumentTypeEnum {

    CHEQUE(1),
    BSO(2),
    AGREEMENT(3),
    OTHER(4);

    private final int code;

    DocumentTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static DocumentTypeEnum getByCode(int code) {
        for (DocumentTypeEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException(
                "No enum constant in " + DocumentTypeEnum.class.getCanonicalName() + " with code " + code);
    }
}
