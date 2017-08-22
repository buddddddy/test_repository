package org.mdlp.basestate.data.processor;

import org.mdlp.basestate.data.address.Address;
import org.mdlp.basestate.data.statistic.RegistrationFilter;
import org.mdlp.data.ListItems;
import org.mdlp.service.MembersService;

import java.util.List;

/**
 * Created by SSuvorov on 11.04.2017.
 */
public interface ContractorProcessor {
    ListItems<MembersService.MemberInfo> getContractors(ContractorType type);

    ListItems<MembersService.MemberInfo> getContractors(RegistrationFilter filter, boolean async);

    MembersService.MemberInfos getContractorsByRegion(RegistrationFilter filter);

    List<Address> getContractorBranches(RegistrationFilter filter, boolean async);

    List<Address> getContractorSafeWarehouses(RegistrationFilter filter, boolean async);

    Address getBranchAddressById(String branchId);

    Address getSafeWarehousesAddressById(String branchId);

    List<String> getContractorIds(ContractorType type);

    MembersService.MemberInfo getContractor(String id);

    MembersService.MemberInfo getContractorByInn(String inn);
}
