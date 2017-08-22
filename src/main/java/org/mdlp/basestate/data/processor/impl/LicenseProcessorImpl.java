package org.mdlp.basestate.data.processor.impl;

import org.mdlp.Utils;
import org.mdlp.basestate.data.*;
import org.mdlp.basestate.data.processor.AbstractProcessor;
import org.mdlp.basestate.data.processor.CoreResponse;
import org.mdlp.basestate.data.processor.LicenseProcessor;
import org.mdlp.basestate.data.processor.RestUtil;
import org.mdlp.basestate.data.processor.command.LicenseCommand;
import org.mdlp.basestate.data.processor.command.PharmLicenseCommand;
import org.mdlp.data.ListItems;
import org.mdlp.service.DrugsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by PVBorisov on 20.06.2017.
 * emailto: Pavel.V.Borisov@firstlinesoftware.com
 */
@Component
public class LicenseProcessorImpl extends AbstractProcessor implements LicenseProcessor {

    protected static final Logger LOG = LoggerFactory.getLogger(LicenseProcessorImpl.class);

    @Value("${basestate.reestrLicenseFilterEndpoint}")
    private String reestrLicense;

    @Value("${basestate.reestrPharmLicenseFilterEndpoint}")
    private String pharmLicenseFilterEndpoint;

    @Value("${basestate.reestrLicenseDetail}")
    private String reestrLicenseDetail;

    @Value("${basestate.reestrLicenseDetailByNumber}")
    private String reestrLicenseDetailByNumber;

    @Value("${basestate.reestrLicenseStatusEndpoint}")
    private String reestrLicenseStatusEndpoint;

    @Value("${basestate.reestrLicenseResultEndpoint}")
    private String reestrLicenseResultEndpoint;

    @Value("${basestate.reestrLicenseErrorEndpoint}")
    private String reestrLicenseErrorEndpoint;

    @Autowired
    protected RestUtil restUtil;

    @PostConstruct
    private void initialize() {
        reestrLicense = Utils.createEndpointUrl(reestrLicense, baseUrl, coreVersion);
        pharmLicenseFilterEndpoint = Utils.createEndpointUrl(pharmLicenseFilterEndpoint, baseUrl, coreVersion);
        reestrLicenseDetail = Utils.createEndpointUrl(reestrLicenseDetail, baseUrl, coreVersion);
        reestrLicenseDetailByNumber = Utils.createEndpointUrl(reestrLicenseDetailByNumber, baseUrl, coreVersion);
        reestrLicenseStatusEndpoint = Utils.createEndpointUrl(reestrLicenseStatusEndpoint, baseUrl, coreVersion);
        reestrLicenseResultEndpoint = Utils.createEndpointUrl(reestrLicenseResultEndpoint, baseUrl, coreVersion);
        reestrLicenseErrorEndpoint = Utils.createEndpointUrl(reestrLicenseErrorEndpoint, baseUrl, coreVersion);
    }

    @Override
    public ListItems<License> getLicenseList(LicenseFilter filter, boolean async) {
        return executeCommand(async, new AsyncCommand<ListItems<License>>() {
            @Override
            public ListItems<License> execute() throws IOException {
                filter.setQueryId(UUID.randomUUID().toString());
                CoreResponse response = restUtil.getTemplate().postForObject(reestrLicense, filter, CoreResponse.class);
                PagedResult<License> licenses = (PagedResult<License>) getResult(new LicenseCommand(response.getQueryId(),
                        reestrLicenseStatusEndpoint, reestrLicenseResultEndpoint, reestrLicenseErrorEndpoint, restUtil));

                List<License> list = Arrays.asList(licenses.getFilteredRecords());
                return new ListItems<>(list, licenses.getFilteredTotalCount() == null ? -1 : licenses.getFilteredTotalCount());
            }
        });
    }

    @Override
    public License getLicense(String licenseNumber, boolean async) {
        return executeCommand(async, new AsyncCommand<License>() {
            @Override
            public License execute() throws IOException {
                LicenseFilter filter = new LicenseFilter();
                filter.setLicenseNumber(licenseNumber);
                filter.setQueryId(UUID.randomUUID().toString());
                CoreResponse response = restUtil.getTemplate().postForObject(reestrLicense, filter, CoreResponse.class);
                PagedResult<License> licenses = (PagedResult<License>) getResult(new LicenseCommand(response.getQueryId(),
                        reestrLicenseStatusEndpoint, reestrLicenseResultEndpoint, reestrLicenseErrorEndpoint, restUtil));
                return licenses.getFilteredRecords()[0];
            }
        });
    }

    @Override
    public ListItems<PharmLicense> getPharmLicenseList(PharmLicenseFilter filter, boolean async) {
        return executeCommand(async, new AsyncCommand<ListItems<PharmLicense>>() {
            @Override
            public ListItems<PharmLicense> execute() throws IOException {
                filter.setQueryId(UUID.randomUUID().toString());
                CoreResponse response = restUtil.getTemplate().postForObject(pharmLicenseFilterEndpoint, filter, CoreResponse.class);
                PagedResult<PharmLicense> licenses = (PagedResult<PharmLicense>) getResult(new PharmLicenseCommand(response.getQueryId(),
                        reestrLicenseStatusEndpoint, reestrLicenseResultEndpoint, reestrLicenseErrorEndpoint, restUtil));
                List<PharmLicense> list = Arrays.asList(licenses.getFilteredRecords());
                return new ListItems<>(list, licenses.getFilteredTotalCount() == null ? -1 : licenses.getFilteredTotalCount());
            }
        });
    }

    @Override
    public PharmLicense getPharmLicense(String licenseNumber, boolean async) {
        return executeCommand(async, new AsyncCommand<PharmLicense>() {
            @Override
            public PharmLicense execute() throws IOException {
                LicenseFilter filter = new LicenseFilter();
                filter.setLicenseNumber(licenseNumber);
                filter.setQueryId(UUID.randomUUID().toString());
                CoreResponse response = restUtil.getTemplate().postForObject(pharmLicenseFilterEndpoint, filter, CoreResponse.class);
                PagedResult<PharmLicense> licenses = (PagedResult<PharmLicense>) getResult(new PharmLicenseCommand(response.getQueryId(),
                        reestrLicenseStatusEndpoint, reestrLicenseResultEndpoint, reestrLicenseErrorEndpoint, restUtil));
                return licenses.getFilteredRecords()[0];
            }
        });
    }
}
