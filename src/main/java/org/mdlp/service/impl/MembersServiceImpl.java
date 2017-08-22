package org.mdlp.service.impl;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.mdlp.basestate.data.address.Address;
import org.mdlp.basestate.data.address.AddressResponse;
import org.mdlp.basestate.data.processor.AddressProcessor;
import org.mdlp.basestate.data.processor.ContractorProcessor;
import org.mdlp.basestate.data.processor.ContractorType;
import org.mdlp.basestate.data.processor.RestProcessor;
import org.mdlp.basestate.data.statistic.RegistrationFilter;
import org.mdlp.data.ListItems;
import org.mdlp.service.MembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mdlp.Utils.contains;

@Component
public class MembersServiceImpl implements MembersService {

//    private static final String RESOURCE = "members.json";
//
//    private static final TypeReference LIST_TYPE = new TypeReference<List<MemberInfo>>() {
//    };

    @Autowired
    private final RestProcessor restProcessor;

    @Autowired
    private final ContractorProcessor contractorProcessor;

    private final @NotNull AddressProcessor addressProcessor;

    @Value("${basestate.useRegs}")
    private boolean useRegs;


    @Autowired
    @SneakyThrows
    public MembersServiceImpl(@Autowired RestProcessor restProcessor, @Autowired ContractorProcessor contractorProcessor,
                              @Autowired AddressProcessor addressProcessor) {
        this.restProcessor = restProcessor;
        this.contractorProcessor = contractorProcessor;
        this.addressProcessor = addressProcessor;
    }

    @Override
    public ListItems<MemberInfo> findByInnFragment(@NotNull String innFragment) {
        MembersFilter filter = new MembersFilter();
        filter.setInnFragment(innFragment.toLowerCase());
        return action(ContractorType.ALL, !useRegs, filter);
    }

    @Override
    public ListItems<MemberInfo> findByNameFragment(@NotNull String nameFragment) {
        MembersFilter filter = new MembersFilter();
        filter.setNameFragment(nameFragment);
        return action(ContractorType.ALL, !useRegs, filter);
    }

    @Override
    public Optional<MemberInfo> findById(@NotNull String id) {
        return Optional.ofNullable(restProcessor.getContractor(id, !useRegs));
    }

    @Override
    public List<Address> findBranchesByInn(RegistrationFilter filter, boolean async) {
        List<Address> list = contractorProcessor.getContractorBranches(filter, async);

        for (Address address : list) {
            AddressResponse addressResponse = addressProcessor.getPlace(address);

            if (addressResponse.getAddress().equalsIgnoreCase("Адрес не может быть идентифицирован в БД ФИАС")) {
                address.setPlace(addressResponse.getAddress() + " (aoguid:" + address.getAoguid() + " houseguid:" + address.getHouseguid() + ")");
            } else {
                address.setPlace(addressResponse.getAddress());
            }
        }
        return list;
    }

    @Override
    public List<Address> findSafeWarehousesByInn(RegistrationFilter filter, boolean async) {
        List<Address> list = contractorProcessor.getContractorSafeWarehouses(filter, async);

        for (Address address : list) {
            AddressResponse addressResponse = addressProcessor.getPlace(address);

            if (addressResponse.getAddress().equalsIgnoreCase("Адрес не может быть идентифицирован в БД ФИАС")) {
                address.setPlace(addressResponse.getAddress() + " (aoguid:" + address.getAoguid() + " houseguid:" + address.getHouseguid() + ")");
            } else {
                address.setPlace(addressResponse.getAddress());
            }
        }
        return list;
    }

    @Override
    public @NotNull ListItems<MemberInfo> find(@NotNull MembersFilter filter) {
        ListItems<MemberInfo> members = Optional.ofNullable(action(ContractorType.ALL, !useRegs, filter))
                .orElseGet(() -> new ListItems<>(new ArrayList<>(), 0));
        return members;
    }


    @Override
    public String findAddressByBranchId(String branchId) {
        Address address = contractorProcessor.getBranchAddressById(branchId);
        if (address != null) {
            AddressResponse addressResponse = addressProcessor.getPlace(address);
            if (addressResponse.getAddress().equalsIgnoreCase("Адрес не может быть идентифицирован в БД ФИАС")) {
                return addressResponse.getAddress() + " (aoguid:" + address.getAoguid() + " houseguid:" + address.getHouseguid() + ")";
            } else {
                return addressResponse.getAddress();
            }
        }
        return null;
    }

    @Override
    public String findAddressBySafeWarehousesById(String safeWarehouses) {
        Address address = contractorProcessor.getSafeWarehousesAddressById(safeWarehouses);
        AddressResponse addressResponse = addressProcessor.getPlace(address);
        if (addressResponse.getAddress().equalsIgnoreCase("Адрес не может быть идентифицирован в БД ФИАС")) {
            return addressResponse.getAddress() + " (aoguid:" + address.getAoguid() + " houseguid:" + address.getHouseguid() + ")";
        } else {
            return addressResponse.getAddress();
        }
    }

    public ListItems<MemberInfo> action(ContractorType type, boolean async, final MembersFilter filter) {

        Optional<String> federalCode = Optional.ofNullable(filter.getFederalCode());

        ListItems<MemberInfo> contractors;
        if (federalCode.isPresent()) {
            RegistrationFilter regFilter = new RegistrationFilter();
            regFilter.setQueryId(UUID.randomUUID().toString());
            regFilter.setINN(filter.getInnFragment());
            regFilter.setFederalCode(filter.getFederalCode());
            regFilter.setKPP(filter.getKppFragment());
            regFilter.setOGRN(filter.getOgrnFragment());
            regFilter.setOrgName(filter.getNameFragment());
            regFilter.setPaginationLimit(filter.getPageSize());
            regFilter.setPaginationOffset((filter.getNPage() - 1) * filter.getPageSize());
            contractors = restProcessor.getContractors(regFilter, async);
        } else {
            contractors = restProcessor.getContractors(type, async);
            List<MemberInfo> memberInfosNonPaged = contractors.getList().stream()
                    .filter(i -> contains(i.getInn(), filter.getInnFragment()))
                    .filter(i -> contains(i.getOgrn(), filter.getOgrnFragment()))
                    .filter(i -> contains(i.getKpp(), filter.getKppFragment()))
                    .filter(i -> contains(i.getName(), filter.getNameFragment()))
                    .collect(Collectors.toList());

            List<MemberInfo> memberInfos = memberInfosNonPaged.stream()
                    .skip((filter.getNPage() - 1) * filter.getPageSize())
                    .limit(filter.getPageSize())
                    .collect(Collectors.toList());

            contractors = new ListItems<>(memberInfos, memberInfosNonPaged.size());
        }

        return contractors;
    }
}
