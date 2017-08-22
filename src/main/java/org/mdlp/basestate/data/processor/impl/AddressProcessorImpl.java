package org.mdlp.basestate.data.processor.impl;

import org.mdlp.Utils;
import org.mdlp.basestate.data.DrugInfoEsklp;
import org.mdlp.basestate.data.address.Address;
import org.mdlp.basestate.data.address.AddressResponse;
import org.mdlp.basestate.data.processor.AbstractProcessor;
import org.mdlp.basestate.data.processor.AddressProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 01.07.2017
 * @package org.mdlp.basestate.data.processor.impl
 */
@Component
public class AddressProcessorImpl extends AbstractProcessor implements AddressProcessor {

    @Value("${basestate.fiasAddressResolveEndpoint}")
    private String fiasAddressResolveEndpoint;

    @PostConstruct
    private void initialize() {
        fiasAddressResolveEndpoint = Utils.createEndpointUrl(fiasAddressResolveEndpoint, baseUrl, coreVersion);
    }

    @Override
    public AddressResponse getPlace(Address address) {
        AddressResponse addressResponse;
        try {
            addressResponse = restUtil.getTemplate().postForObject(fiasAddressResolveEndpoint, address, AddressResponse.class);
        } catch (Exception e) {
            addressResponse = new AddressResponse();
            addressResponse.setAddress("Адрес не может быть идентифицирован в БД ФИАС");
        }
        return addressResponse;
    }
}
