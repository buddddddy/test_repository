package org.mdlp.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.mdlp.basestate.data.address.Address;
import org.mdlp.basestate.data.statistic.RegistrationFilter;
import org.mdlp.data.ListItems;

import java.util.List;
import java.util.Optional;

public interface MembersService {

    ListItems<MemberInfo> findByInnFragment(@NotNull String innFragment);

    ListItems<MemberInfo> findByNameFragment(@NotNull String nameFragment);

    Optional<MemberInfo> findById(@NotNull String id);

    List<Address> findBranchesByInn(RegistrationFilter filter, boolean async);

    List<Address> findSafeWarehousesByInn(RegistrationFilter filter, boolean async);

    String findAddressBySafeWarehousesById(String safeWarehousesId);

    String findAddressByBranchId(String branchId);

    default @NotNull ListItems<MemberInfo> find(@NotNull MembersFilter filter) {
        return find(filter);
    }

    @Data
    class MemberInfo {

        @NotNull
        private final String id;

        @NotNull
        private final String inn;

        @NotNull
        private final String kpp;

        @NotNull
        private final String regDate;

        @NotNull
        private final String name;

        @NotNull
        private final String ogrn;

        @NotNull
        private final String email;

        @NotNull
        private final String certOwnerFullName;

        private final boolean isResident;

        private final String registryType;

    }

    @Data
    @AllArgsConstructor
    class MemberBranch {

        @NotNull
        @JsonProperty("id")
        private String id;

        @NotNull
        @JsonProperty("system_subj_id")
        private String systemSubjId;

        @NotNull
        @JsonProperty("branch_id")
        private String branchId;

        @NotNull
        @JsonProperty("branch_address")
        private Address branchAddress;
    }

    @Data
    @AllArgsConstructor
    class SafeWarehouse {

        @NotNull
        @JsonProperty("id")
        private String id;

        @NotNull
        @JsonProperty("system_subj_id")
        private String systemSubjId;

        @NotNull
        @JsonProperty("warehouse_id")
        private String branchId;

        @NotNull
        @JsonProperty("warehouse_address")
        private Address warehouseAddress;
    }

    @Data
    class MemberInfos {

        @NotNull
        private final List<MemberInfo> list;

        private final long total;

    }

    @Data
    class MembersFilter {

        private String federalCode;

        private String innFragment;

        private String snilsFragment;

        private String ogrnFragment;

        private String kppFragment;

        private String nameFragment;

        private Integer entityRegType;

        private int nPage;

        private int pageSize;
    }

}
