package org.mdlp.basestate.data.introduction;

import lombok.Data;

import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 25.05.2017
 * @package org.mdlp.basestate.data.introduction
 */
@Data
public class OrderDetails {

    private List<OrderDetailsInner> orderDetails;

    @Data
    public static class OrderDetailsInner {
        private String sgtin;
        private String sscc;

    }
}
