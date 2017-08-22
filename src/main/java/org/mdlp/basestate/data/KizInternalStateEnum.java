package org.mdlp.basestate.data;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 24.05.2017
 * @package org.mdlp.basestate.data
 */
public enum KizInternalStateEnum {
    marked(1),
    released(2),
    lp_sampled(3),
    moved_for_disposal(4),
    disposed(5),
    out_of_circulation(6),
    transfered_to_owner(7),
    shipped(8),
    arrived(9),
    declared(10),
    moved_to_warehouse(11),
    in_circulation(12),
    in_realization(13),
    paused_circulation(14),
    in_sale(15),
    in_discount_prescription_sale(16),
    in_medical_use(17),
    emission(18);

    private final int code;

    KizInternalStateEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static KizInternalStateEnum getByCode(int code) {
        for (KizInternalStateEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException(
                "No enum constant in " + KizInternalStateEnum.class.getCanonicalName() + " with code " + code);
    }
}
