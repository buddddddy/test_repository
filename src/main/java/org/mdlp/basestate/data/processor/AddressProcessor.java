package org.mdlp.basestate.data.processor;

import org.mdlp.basestate.data.address.Address;
import org.mdlp.basestate.data.address.AddressResponse;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 01.07.2017
 * @package org.mdlp.basestate.data.processor
 */
public interface AddressProcessor {

    AddressResponse getPlace(Address address);
}
