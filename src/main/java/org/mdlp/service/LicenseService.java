package org.mdlp.service;

import org.mdlp.basestate.data.*;
import org.mdlp.data.ListItems;
import org.mdlp.web.rest.RestItemList;

import java.util.List;

/**
 * Created by PVBorisov on 20.06.2017.
 * emailto: Pavel.V.Borisov@firstlinesoftware.com
 */
public interface LicenseService {

    ListItems<License> getLicenseList(LicenseFilter filter, boolean async);

    License getLicense(String licenseNumber, boolean async);

    ListItems<PharmLicense> getPharmLicenseList(PharmLicenseFilter filter, boolean async);

    PharmLicense getPharmLicense(String licenseNumber, boolean async);
}
