package org.mdlp.web.rest.get_reestr_esklp;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 01.07.2017
 * @package org.mdlp.web.rest.get_reestr_pharm_licenses
 */
public enum EsklpStatusEnum {
    SGTIN("0", "Действующий"),
    SSCC("1", "Недействующий");

    private final String code;
    private final String value;

    EsklpStatusEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static EsklpStatusEnum getByCode(String code) {
        for (EsklpStatusEnum value : values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException(
                "No enum constant in " + EsklpStatusEnum.class.getCanonicalName() + " with code " + code);
    }
}
