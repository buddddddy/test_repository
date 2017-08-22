package org.mdlp.basestate.data.processor.impl;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.mdlp.Utils;
import org.mdlp.basestate.data.*;
import org.mdlp.basestate.data.address.Address;
import org.mdlp.basestate.data.processor.*;
import org.mdlp.basestate.data.processor.command.RegistrationFilterCommand;
import org.mdlp.basestate.data.statistic.RegistrationEntry;
import org.mdlp.basestate.data.statistic.RegistrationFilter;
import org.mdlp.data.ListItems;
import org.mdlp.service.MembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * Created by SSuvorov on 30.03.2017.
 */
@Component
class ContractorProcessorImpl extends AbstractProcessor implements ContractorProcessor {
    @Value("${app.foreigncontractors.map}")
    private String foreignContractorsNames;

    private Map<String, String> foreignContractorsNamesMap;

    @PostConstruct
    public void init() {
        foreignContractorsNamesMap = new HashMap<>();
        if (foreignContractorsNames != null && !foreignContractorsNames.isEmpty()) {
            String[] split = foreignContractorsNames.split(",");
            for (String field : split) {
                String[] splittedField = field.split(":");
                foreignContractorsNamesMap.put(splittedField[0], splittedField[1]);
            }
        }
    }


    @Value("${basestate.regsRFEndpoint}")
    private String regsRFEndpoint;

    @Value("${basestate.regsRFDetailEndpoint}")
    private String regsRFDetailEndpoint;

    @Value("${basestate.regsRFBranchDetailEndpoint}")
    private String regsRFBranchDetailEndpoint;

    @Value("${basestate.regsForeignEndpoint}")
    private String regsForeignEndpoint;

    @Value("${basestate.regsForeignDetailEndpoint}")
    private String regsForeignDetailEndpoint;

    @Value("${basestate.regsForeignAgentEndpoint}")
    private String regsForeignAgentEndpoint;

    @Value("${basestate.regsForeignAgentDetailEndpoint}")
    private String regsForeignAgentDetailEndpoint;

    @Value("${basestate.regsFilterEndpoint}")
    private String regsFilterEndpoint;

    @Value("${basestate.regsFilterStatusEndpoint}")
    private String regsFilterStatusEndpoint;

    @Value("${basestate.regsFilterResultEndpoint}")
    private String regsFilterResultEndpoint;

    @Value("${basestate.regsFilterErrorEndpoint}")
    private String regsFilterErrorEndpoint;

    @Value("${basestate.regsBranchDetailEndpoint}")
    private String regsBranchDetailEndpoint;

    @Value("${basestate.regsSafeWorehousesDetailEndpoint}")
    private String regsSafeWorehousesDetailEndpoint;

    @Value("${basestate.reestrEgrul}")
    private String reestrEgrulEndpoint;

    @Value("${basestate.reestrEgrip}")
    private String reestrEgripEndpoint;

    @Value("${basestate.reestrRafp}")
    private String reestrRafpEndpoint;

    @Value("${basestate.reestrEsklpDetail}")
    private String reestrEsklpDetail;

    @Autowired
    protected RestUtil restUtil;

    @PostConstruct
    private void initialize() {
        regsRFEndpoint = Utils.createEndpointUrl(regsRFEndpoint, baseUrl, coreVersion);
        regsRFDetailEndpoint = Utils.createEndpointUrl(regsRFDetailEndpoint, baseUrl, coreVersion);
        regsRFBranchDetailEndpoint = Utils.createEndpointUrl(regsRFBranchDetailEndpoint, baseUrl, coreVersion);
        regsForeignEndpoint = Utils.createEndpointUrl(regsForeignEndpoint, baseUrl, coreVersion);
        regsForeignDetailEndpoint = Utils.createEndpointUrl(regsForeignDetailEndpoint, baseUrl, coreVersion);
        reestrEgrulEndpoint = Utils.createEndpointUrl(reestrEgrulEndpoint, baseUrl, coreVersion);
        reestrEgripEndpoint = Utils.createEndpointUrl(reestrEgripEndpoint, baseUrl, coreVersion);
        reestrRafpEndpoint = Utils.createEndpointUrl(reestrRafpEndpoint, baseUrl, coreVersion);

        reestrEsklpDetail = Utils.createEndpointUrl(reestrEsklpDetail, baseUrl, coreVersion);
        regsForeignAgentEndpoint = Utils.createEndpointUrl(regsForeignAgentEndpoint, baseUrl, coreVersion);
        regsForeignAgentDetailEndpoint = Utils.createEndpointUrl(regsForeignAgentDetailEndpoint, baseUrl, coreVersion);

        regsFilterEndpoint = Utils.createEndpointUrl(regsFilterEndpoint, baseUrl, coreVersion);
        regsFilterStatusEndpoint = Utils.createEndpointUrl(regsFilterStatusEndpoint, baseUrl, coreVersion);
        regsFilterResultEndpoint = Utils.createEndpointUrl(regsFilterResultEndpoint, baseUrl, coreVersion);
        regsFilterErrorEndpoint = Utils.createEndpointUrl(regsFilterErrorEndpoint, baseUrl, coreVersion);
        regsBranchDetailEndpoint = Utils.createEndpointUrl(regsBranchDetailEndpoint, baseUrl, coreVersion);
        regsSafeWorehousesDetailEndpoint = Utils.createEndpointUrl(regsSafeWorehousesDetailEndpoint, baseUrl, coreVersion);
    }

    @Override
    public ListItems<MembersService.MemberInfo> getContractors(ContractorType type) {
        try {
            switch (type) {
//                case RF: {
//                    return getRFContractors();
//                }
//                case FOREIGN: {
//                    return getForeignContractors();
//                }
                case ALL:
                default: {
                    return getContractors();
                }
            }
        } catch (Exception e) {
            LOG.error("error getting contractors", e);
            return null;
        }
    }

    @Override
    public ListItems<MembersService.MemberInfo> getContractors(RegistrationFilter filter, boolean async) {
        CoreResponse response = restUtil.getTemplate().postForObject(regsFilterEndpoint, filter, CoreResponse.class);
        PagedResult<RegistrationEntry> registrationEntries =
                (PagedResult<RegistrationEntry>) getResult(new RegistrationFilterCommand(response.getQueryId(), regsFilterStatusEndpoint, regsFilterResultEndpoint, regsFilterErrorEndpoint, restUtil));

        List<MembersService.MemberInfo> infos = new ArrayList<>();
        for (RegistrationEntry re : registrationEntries.getFilteredRecords()) {
            String name = StringUtils.stripToEmpty(re.getLastName()) + " " + StringUtils.stripToEmpty(re.getFirstName()) + " " + StringUtils.stripToEmpty(re.getMiddleName());
            MembersService.MemberInfo memberInfo = new MembersService.MemberInfo(re.getId(),
                    re.getINN(), re.getKPP(), (re.getRegDate() != null ? re.getRegDate().getOpDate() : StringUtils.EMPTY),
                    re.getOrgName(), re.getOGRN(), StringUtils.EMPTY, name, true, null);
            infos.add(memberInfo);
        }
        return new ListItems<>(infos, registrationEntries.getFilteredTotalCount() == null ? -1 : registrationEntries.getFilteredTotalCount());
    }

    @Override
    public MembersService.MemberInfos getContractorsByRegion(RegistrationFilter filter) {

        CoreResponse response = restUtil.getTemplate().postForObject(regsFilterEndpoint, filter, CoreResponse.class);
        RegistrationEntry[] registrationEntries = (RegistrationEntry[]) getResult(new RegistrationFilterCommand(response.getQueryId(), regsFilterStatusEndpoint, regsFilterResultEndpoint, regsFilterErrorEndpoint, restUtil));

        List<MembersService.MemberInfo> infos = new ArrayList<>();
        for (RegistrationEntry re : registrationEntries) {
            String name = re.getLastName() + " " + re.getFirstName() + " " + re.getMiddleName();
            MembersService.MemberInfo memberInfo = new MembersService.MemberInfo(re.getId(),
                    re.getINN(), re.getKPP(), (re.getRegDate() != null ? re.getRegDate().getOpDate() : StringUtils.EMPTY),
                    re.getOrgName(), re.getOGRN(), StringUtils.EMPTY, name, true, null);
            infos.add(memberInfo);
        }
        return new MembersService.MemberInfos(infos, infos.size());
    }

    @Override
    public List<Address> getContractorBranches(RegistrationFilter filter, boolean async) {
        return executeCommand(async, new AsyncCommand<List<Address>>() {
            @Override
            public List<Address> execute() throws IOException {
                CoreResponse response = restUtil.getTemplate().postForObject(regsFilterEndpoint, filter, CoreResponse.class);
                PagedResult<RegistrationEntry> registrationEntries = (PagedResult<RegistrationEntry>) getResult(new RegistrationFilterCommand(
                        response.getQueryId(), regsFilterStatusEndpoint, regsFilterResultEndpoint, regsFilterErrorEndpoint, restUtil));

                if (registrationEntries == null) return new ArrayList<>();

                if (registrationEntries.getFilteredTotalCount() == 0) {
                    throw new IllegalStateException("No registration entry found for INN: " + filter.getINN());
                } else if (registrationEntries.getFilteredTotalCount() > 1) {
                    throw new IllegalStateException("Multiple registration entries found for INN: " + filter.getINN());
                }
                RegistrationEntry entry = registrationEntries.getFilteredRecords()[0];

                List<Address> result = new ArrayList<>();

                for (String branchId : entry.getBranches()) {
                    ResponseEntity<MembersService.MemberBranch> httpResponse = restUtil.getTemplate().exchange(regsBranchDetailEndpoint + branchId,
                            HttpMethod.GET, null, new ParameterizedTypeReference<MembersService.MemberBranch>() {
                            });

                    MembersService.MemberBranch branch = httpResponse.getBody();
                    Address address = new Address();
                    address.setAoguid(branch.getBranchAddress().getAoguid());
                    address.setHouseguid(branch.getBranchAddress().getHouseguid());
                    address.setRoom(branch.getBranchAddress().getRoom());
                    result.add(address);
                }
                return result;
            }
        });
    }
    @Override
    public List<Address> getContractorSafeWarehouses(RegistrationFilter filter, boolean async) {
        return executeCommand(async, new AsyncCommand<List<Address>>() {
            @Override
            public List<Address> execute() throws IOException {
                CoreResponse response = restUtil.getTemplate().postForObject(regsFilterEndpoint, filter, CoreResponse.class);
                PagedResult<RegistrationEntry> registrationEntries = (PagedResult<RegistrationEntry>) getResult(new RegistrationFilterCommand(
                        response.getQueryId(), regsFilterStatusEndpoint, regsFilterResultEndpoint, regsFilterErrorEndpoint, restUtil));

                if (registrationEntries == null) return new ArrayList<>();

                if (registrationEntries.getFilteredTotalCount() == 0) {
                    throw new IllegalStateException("No registration entry found for INN: " + filter.getINN());
                } else if (registrationEntries.getFilteredTotalCount() > 1) {
                    throw new IllegalStateException("Multiple registration entries found for INN: " + filter.getINN());
                }
                RegistrationEntry entry = registrationEntries.getFilteredRecords()[0];

                List<Address> result = new ArrayList<>();

                for (String safeWarehousesId : entry.getSafeWarehouses()) {
                    ResponseEntity<MembersService.SafeWarehouse> httpResponse = restUtil.getTemplate().exchange(regsSafeWorehousesDetailEndpoint + safeWarehousesId,
                            HttpMethod.GET, null, new ParameterizedTypeReference<MembersService.SafeWarehouse>() {
                            });

                    MembersService.SafeWarehouse branch = httpResponse.getBody();
                    Address address = new Address();
                    address.setAoguid(branch.getWarehouseAddress().getAoguid());
                    address.setHouseguid(branch.getWarehouseAddress().getHouseguid());
                    address.setRoom(branch.getWarehouseAddress().getRoom());
                    result.add(address);
                }
                return result;
            }
        });
    }

    @Override
    public Address getBranchAddressById(String branchId) {
        ResponseEntity<MembersService.MemberBranch> httpResponse = restUtil.getTemplate().exchange(regsBranchDetailEndpoint + branchId,
                HttpMethod.GET, null, new ParameterizedTypeReference<MembersService.MemberBranch>() {
                });

        MembersService.MemberBranch branch = httpResponse.getBody();
        Address address = null;
        if (branch.getId() != null) {
            address = new Address();
            address.setAoguid(branch.getBranchAddress().getAoguid());
            address.setHouseguid(branch.getBranchAddress().getHouseguid());
            address.setRoom(branch.getBranchAddress().getRoom());
        }

        return address;
    }
    @Override
    public Address getSafeWarehousesAddressById(String safeWarehouses) {
        ResponseEntity<MembersService.SafeWarehouse> httpResponse = restUtil.getTemplate().exchange(regsSafeWorehousesDetailEndpoint + safeWarehouses,
                HttpMethod.GET, null, new ParameterizedTypeReference<MembersService.SafeWarehouse>() {
                });

        MembersService.SafeWarehouse branch = httpResponse.getBody();
        Address address = new Address();
        address.setAoguid(branch.getWarehouseAddress().getAoguid());
        address.setHouseguid(branch.getWarehouseAddress().getHouseguid());
        address.setRoom(branch.getWarehouseAddress().getRoom());
        return address;
    }

    private MembersService.MemberInfos getRFContractors() {
        return getContractors(regsRFDetailEndpoint, regsRFBranchDetailEndpoint, getRFContractorIds());
    }

    private MembersService.MemberInfos getForeignContractors() {
        MembersService.MemberInfos foreignerContractors = getForeignerContractors(regsForeignDetailEndpoint, getForeignContractorIds());
        foreignerContractors.getList().addAll(getForeignerAgentContractors(regsForeignAgentDetailEndpoint, getForeignAgentsContractorIds()).getList());
        return foreignerContractors;
    }

    private MembersService.MemberInfos getForeignerAgentContractors(String regsForeignAgentDetailEndpoint, List<String> ids) {
        List<ForeignAgentContractor> contractors = new ArrayList<>();
        for (String id : ids) {
            ResponseEntity<List<ForeignAgentContractor>> response = restUtil.getTemplate().exchange(regsForeignAgentDetailEndpoint + id,
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<ForeignAgentContractor>>() {
                    });
            contractors.addAll(response.getBody());
        }

        List<MembersService.MemberInfo> infos = new ArrayList<>();
        for (ForeignAgentContractor contractor : contractors) {
            ContractorEGRUL contractorEGRUL = getContractorEGRUL(contractor.getInn());
            String name = contractorEGRUL == null ? StringUtils.EMPTY : contractor.getLastName() + " " + contractor.getFirstName() + " " +
                    contractor.getMiddleName();

            MembersService.MemberInfo memberInfo = new MembersService.MemberInfo(contractor.getId(),
                    contractor.getInn(), contractorEGRUL == null ? StringUtils.EMPTY : contractorEGRUL.getKpp(), contractor.getOpDate(), contractorEGRUL == null ? StringUtils.EMPTY : contractorEGRUL.getOrgName(),
                    contractorEGRUL == null ? StringUtils.EMPTY : contractorEGRUL.getOgrn(), StringUtils.EMPTY, name, true, contractorEGRUL == null ? StringUtils.EMPTY : contractorEGRUL.getRegistryType());
            infos.add(memberInfo);
        }
        return new MembersService.MemberInfos(infos, infos.size());
    }

    private MembersService.MemberInfos getContractors(String mainDetailsUri, String branchDetailsUri, List<String> ids) {
        List<Contractor> contractors = new ArrayList<>();

        for (String id : ids) {
            if (isBranch(id)) {
                id = getHeadIdByBranch(id);
            }

            if (!StringUtils.isEmpty(id)) {
                ResponseEntity<List<Contractor>> response = restUtil.getTemplate().exchange(mainDetailsUri + id,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Contractor>>() {
                        });
                contractors.addAll(response.getBody());
            }
        }
        List<MembersService.MemberInfo> infos = new ArrayList<>();
        for (Contractor contractor : contractors) {
            ContractorEGRUL contractorEGRUL = getContractorEGRUL(contractor.getInn());
            if (contractorEGRUL == null) {
                contractorEGRUL = getContractorEGRIP(contractor.getInn());
            } else if (contractorEGRUL == null) {
                contractorEGRUL = getContractorRAFP(contractor.getInn());
            }
            String name = contractorEGRUL == null ? StringUtils.EMPTY : contractorEGRUL.getLastName() + " " + contractorEGRUL.getFirstName() + " " +
                    contractorEGRUL.getMiddleName();
            MembersService.MemberInfo memberInfo = new MembersService.MemberInfo(contractor.getId(),
                    contractor.getInn(), contractorEGRUL != null ? contractorEGRUL.getKpp() : StringUtils.EMPTY, contractor.getOpDate(), contractorEGRUL == null ? StringUtils.EMPTY : contractorEGRUL.getOrgName(),
                    contractorEGRUL == null ? StringUtils.EMPTY : contractorEGRUL.getOgrn(), StringUtils.EMPTY, name, true, contractorEGRUL != null ? contractorEGRUL.getRegistryType() : null);
            infos.add(memberInfo);
        }
        return new MembersService.MemberInfos(infos, infos.size());
    }

    private MembersService.MemberInfos getForeignerContractors(String mainDetailsUri, List<String> ids) {
        List<ForeignContractor> contractors = new ArrayList<>();

        for (String id : ids) {

            if (!StringUtils.isEmpty(id)) {
                ResponseEntity<List<ForeignContractor>> response = restUtil.getTemplate().exchange(mainDetailsUri + id,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<ForeignContractor>>() {
                        });
                contractors.addAll(response.getBody());
            }
        }
        List<MembersService.MemberInfo> infos = new ArrayList<>();
        for (ForeignContractor contractor : contractors) {
//            ResponseEntity<DrugInfoEsklp> response = restUtil.getTemplate().exchange(
//                    Utils.createURI(reestrEsklpDetail, contractor.getRegNum()),
//                    HttpMethod.GET, null, DrugInfoEsklp.class);
//            DrugInfoEsklp esklp = response.getBody();
            ContractorEGRUL contractorEGRUL = getContractorRAFP(contractor.getItin());
            MembersService.MemberInfo memberInfo = new MembersService.MemberInfo(contractor.getId(),
                    contractor.getItin(), StringUtils.EMPTY, contractor.getRegDate(), contractorEGRUL == null ? foreignContractorsNamesMap.get(contractor.getItin()) : contractorEGRUL.getOrgName(),
                    StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, false, contractorEGRUL == null ? null : contractorEGRUL.getRegistryType());
            infos.add(memberInfo);
        }
        return new MembersService.MemberInfos(infos, infos.size());
    }

    private String getHeadIdByBranch(String id) {
        ResponseEntity<List<ContractorBranch>> response = restUtil.getTemplate().exchange(regsRFBranchDetailEndpoint + id,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<ContractorBranch>>() {
                });
        return response.getBody().get(0).getSystemSubjId();
    }

    private ContractorEGRUL getContractorEGRUL(String inn) {
        ContractorEGRUL contractorEGRUL = null;
        try {
            ResponseEntity<ContractorEGRUL> response =
                    restUtil.getTemplate().exchange(reestrEgrulEndpoint + inn, HttpMethod.GET, null, ContractorEGRUL.class);
            contractorEGRUL = response.getBody();
            if (contractorEGRUL.getInn() == null) {
                contractorEGRUL = null;
                throw new Exception();
            }
            contractorEGRUL.setRegistryType("ЕГРЮЛ");
        } catch (Exception e) {
            LOG.error("Can't identify member {} by EGRUL registry!", inn);
        }
        return contractorEGRUL;
    }

    private ContractorEGRUL getContractorEGRIP(String inn) {
        ContractorEGRUL contractorEGRUL = null;
        try {
            ResponseEntity<ContractorEGRUL> response =
                    restUtil.getTemplate().exchange(reestrEgripEndpoint + inn, HttpMethod.GET, null, ContractorEGRUL.class);
            contractorEGRUL = response.getBody();
            if (contractorEGRUL.getInn() == null) {
                contractorEGRUL = null;
                throw new Exception();
            }
            contractorEGRUL.setRegistryType("ЕГРИП");
        } catch (Exception e) {
            LOG.error("Can't identify member {} by EGRIP registry!", inn);
        }
        return contractorEGRUL;
    }

    private ContractorEGRUL getContractorRAFP(String inn) {
        ContractorEGRUL contractorEGRUL = null;
        try {
            ResponseEntity<ContractorEGRUL> response =
                    restUtil.getTemplate().getForEntity(reestrRafpEndpoint + inn, ContractorEGRUL.class);
            contractorEGRUL = response.getBody();
            if (contractorEGRUL.getInn() == null) {
                contractorEGRUL = null;
                throw new Exception();
            }
            contractorEGRUL.setRegistryType("РАФП");
        } catch (Exception e) {
            LOG.error("Can't identify member {} by RAFP registry!", inn);
        }
        return contractorEGRUL;
    }

    private ListItems<MembersService.MemberInfo> getContractors() {
        List<MembersService.MemberInfo> contractors = new ArrayList<>();
        contractors.addAll(getRFContractors().getList());
        contractors.addAll(getForeignContractors().getList());
        return new ListItems<>(contractors, contractors.size());
    }

    @Override
    public List<String> getContractorIds(ContractorType type) {
        try {
            switch (type) {
                case RF: {
                    return getRFContractorIds();
                }
                case FOREIGN: {
                    return getForeignContractorIds();
                }
                case ALL:
                default: {
                    return getContractorIds();
                }
            }
        } catch (Exception e) {
            LOG.error("error getting contractors ids", e);
            return Collections.emptyList();
        }
    }

    @Override
    public MembersService.MemberInfo getContractor(String id) {
        List<String> ids = Lists.newArrayList(id);
        MembersService.MemberInfos infos = getContractors(regsRFDetailEndpoint, regsRFBranchDetailEndpoint, ids);
        //TODO need refactoring
        if (infos == null || infos.getList().isEmpty() || null == infos.getList().get(0) || infos.getList().get(0).getId() == null) {
            infos = getForeignerContractors(regsForeignDetailEndpoint, ids);
        }
        if (infos == null || infos.getList().isEmpty()) {
            infos = getForeignerContractors(regsForeignAgentEndpoint, ids);
        }
        if (infos != null && !infos.getList().isEmpty()) {
            return infos.getList().get(0);
        }
        return null;
    }

    @Override
    public MembersService.MemberInfo getContractorByInn(String inn) {
        RegistrationFilter filter = new RegistrationFilter();
        filter.setINN(inn);
        filter.setQueryId(UUID.randomUUID().toString());
        CoreResponse response = restUtil.getTemplate().postForObject(regsFilterEndpoint, filter, CoreResponse.class);
        PagedResult<RegistrationEntry> registrationEntries = (PagedResult<RegistrationEntry>) getResult(new RegistrationFilterCommand(response.getQueryId(), regsFilterStatusEndpoint, regsFilterResultEndpoint, regsFilterErrorEndpoint, restUtil));

        MembersService.MemberInfo memberInfo = null;
        for (RegistrationEntry re : registrationEntries.getFilteredRecords()) {
            String name = re.getLastName() + " " + re.getFirstName() + " " +
                    re.getMiddleName();
            memberInfo = new MembersService.MemberInfo(re.getId(),
                    re.getINN(), re.getKPP(), (re.getRegDate() != null ? re.getRegDate().getOpDate() : StringUtils.EMPTY),
                    re.getOrgName(), re.getOGRN(), StringUtils.EMPTY, name, true, null);
            break;
        }
        return memberInfo;
    }

    private List<String> getRFContractorIds() {
        return getContractorIds(regsRFEndpoint);
    }

    private List<String> getForeignContractorIds() {
        return getContractorIds(regsForeignEndpoint);
    }

    public List<String> getForeignAgentsContractorIds() {
        return getContractorIds(regsForeignAgentEndpoint);
    }

    private List<String> getContractorIds(String uri) {
        ResponseEntity<List<JsonId>> response = restUtil.getTemplate().exchange(uri,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<JsonId>>() {
                });
        List<JsonId> responseIds = response.getBody();
        List<String> ids = new ArrayList<>();
        for (JsonId jsonId : responseIds) {
            ids.add(jsonId.getId());
        }
        return ids;
    }

    private List<String> getContractorIds() {
        List<String> ids = new ArrayList<>();
        ids.addAll(getRFContractorIds());
        ids.addAll(getForeignContractorIds());

        return ids;
    }

    private boolean isBranch(String id) {
        return id.length() == 14;
    }
}