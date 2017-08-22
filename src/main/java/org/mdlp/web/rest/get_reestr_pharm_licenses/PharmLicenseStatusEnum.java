package org.mdlp.web.rest.get_reestr_pharm_licenses;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 01.07.2017
 * @package org.mdlp.web.rest.get_reestr_pharm_licenses
 */
public enum PharmLicenseStatusEnum {
    SGTIN("0", "Действует"),
    SSCC("1", "Не действует");

    private final String code;
    private final String value;

    PharmLicenseStatusEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static PharmLicenseStatusEnum getByCode(String code) {
        for (PharmLicenseStatusEnum value : values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException(
                "No enum constant in " + PharmLicenseStatusEnum.class.getCanonicalName() + " with code " + code);
    }
}
