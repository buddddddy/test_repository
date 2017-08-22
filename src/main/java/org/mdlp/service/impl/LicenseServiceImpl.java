package org.mdlp.service.impl;

import org.jetbrains.annotations.NotNull;
import org.mdlp.basestate.data.*;
import org.mdlp.basestate.data.address.Address;
import org.mdlp.basestate.data.address.AddressResponse;
import org.mdlp.basestate.data.processor.AddressProcessor;
import org.mdlp.basestate.data.processor.LicenseProcessor;
import org.mdlp.data.ListItems;
import org.mdlp.service.LicenseService;
import org.mdlp.web.rest.RestItemList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by PVBorisov on 20.06.2017.
 * emailto: Pavel.V.Borisov@firstlinesoftware.com
 */
@Component
public class LicenseServiceImpl implements LicenseService {

    @NotNull
    private final LicenseProcessor licenseProcessor;
    @NotNull
    private final AddressProcessor addressProcessor;

    @Autowired
    public LicenseServiceImpl(@NotNull LicenseProcessor licenseProcessor, @NotNull AddressProcessor addressProcessor) {
        this.licenseProcessor = licenseProcessor;
        this.addressProcessor = addressProcessor;
    }

    @Override
    public ListItems<License> getLicenseList(LicenseFilter filter, boolean async) {
        ListItems<License> licencesInfos = Optional.ofNullable(licenseProcessor.getLicenseList(filter, async))
                .orElseGet(() -> new ListItems<>(new ArrayList<>(), 0));

        for (License license : licencesInfos.getList()) {
            AddressResponse addressResponse = addressProcessor.getPlace(new Address(license.getAddress().getAoguid(), license.getAddress().getHouseGuid(), null, null));
            if (addressResponse.getAddress().equalsIgnoreCase("Адрес не может быть идентифицирован в БД ФИАС")) {
                license.getAddress().setPlace(addressResponse.getAddress() + " (aoguid:" + license.getAddress().getAoguid() + " houseguid:" + license.getAddress().getHouseGuid() + ")");
            } else {
                license.getAddress().setPlace(addressResponse.getAddress());
            }
        }
        return licencesInfos;
    }

    @Override
    public License getLicense(String licenseNumber, boolean async) {
        License license = licenseProcessor.getLicense(licenseNumber, async);
        if (license == null) {
            throw new IllegalStateException("No unique result for license number: " + licenseNumber);
        }
        AddressResponse addressResponse = addressProcessor.getPlace(new Address(license.getAddress().getAoguid(), license.getAddress().getHouseGuid(), null, null));
        if (addressResponse.getAddress().equalsIgnoreCase("Адрес не может быть идентифицирован в БД ФИАС")) {
            license.getAddress().setPlace(addressResponse.getAddress() + " (aoguid:" + license.getAddress().getAoguid() + " houseguid:" + license.getAddress().getHouseGuid() + ")");
        } else {
            license.getAddress().setPlace(addressResponse.getAddress());
        }
        return license;
    }

    @Override
    public ListItems<PharmLicense> getPharmLicenseList(PharmLicenseFilter filter, boolean async) {
        ListItems<PharmLicense> licencesInfos = Optional.ofNullable(licenseProcessor.getPharmLicenseList(filter, async))
                .orElseGet(() -> new ListItems<>(new ArrayList<>(), 0));


        for (PharmLicense pharmLicense : licencesInfos.getList()) {
            AddressResponse addressResponse = addressProcessor.getPlace(new Address(pharmLicense.getAddress().getAoguid(), pharmLicense.getAddress().getHouseGuid(), null, null));
            if (addressResponse.getAddress().equalsIgnoreCase("Адрес не может быть идентифицирован в БД ФИАС")) {
                pharmLicense.getAddress().setPlace(addressResponse.getAddress() + " (aoguid:" + pharmLicense.getAddress().getAoguid() + " houseguid:" + pharmLicense.getAddress().getHouseGuid() + ")");
            } else {
                pharmLicense.getAddress().setPlace(addressResponse.getAddress());
            }
        }
        return licencesInfos;
    }

    @Override
    public PharmLicense getPharmLicense(String licenseNumber, boolean async) {
        PharmLicense license = licenseProcessor.getPharmLicense(licenseNumber, async);
        if (license == null) {
            throw new IllegalStateException("No unique result for pharm license number: " + licenseNumber);
        }
        AddressResponse addressResponse = addressProcessor.getPlace(new Address(license.getAddress().getAoguid(), license.getAddress().getHouseGuid(), null, null));
        if (addressResponse.getAddress().equalsIgnoreCase("Адрес не может быть идентифицирован в БД ФИАС")) {
            license.getAddress().setPlace(addressResponse.getAddress() + " (aoguid:" + license.getAddress().getAoguid() + " houseguid:" + license.getAddress().getHouseGuid() + ")");
        } else {
            license.getAddress().setPlace(addressResponse.getAddress());
        }
        return license;
    }
}
