package org.mdlp.basestate.data.processor;

import org.mdlp.basestate.data.*;
import org.mdlp.data.ListItems;

import java.util.List;

/**
 * Created by PVBorisov on 20.06.2017.
 * emailto: Pavel.V.Borisov@firstlinesoftware.com
 */
public interface LicenseProcessor {

    ListItems<License> getLicenseList(LicenseFilter filter, boolean async);

    License getLicense(String licenseNumber, boolean async);

    ListItems<PharmLicense> getPharmLicenseList(PharmLicenseFilter filter, boolean async);

    PharmLicense getPharmLicense(String licenseNumber, boolean async);
}
