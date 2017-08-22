package org.mdlp.web.rest.get_members_list;

import org.jetbrains.annotations.NotNull;
import org.mdlp.Utils;
import org.mdlp.basestate.data.processor.ContractorType;
import org.mdlp.basestate.data.processor.RestProcessor;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.data.ListItems;
import org.mdlp.service.MembersService;
import org.mdlp.service.MembersService.MembersFilter;
import org.mdlp.web.rest.ParentRestController;
import org.mdlp.web.rest.RestItemList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_members_list")
public class GetMembersListController {

    @NotNull
    private final MembersService membersService;

    @NotNull
    private final RestProcessor restProcessor;

    @Value("${basestate.useRegs}")
    private boolean useRegs;

    @Autowired
    public GetMembersListController(@NotNull MembersService membersService,
                                    @NotNull RestProcessor restProcessor) {
        this.membersService = membersService;
        this.restProcessor = restProcessor;
    }

    @NotNull
    @RequestMapping("")
    public RestItemList action(
            @RequestParam(value = "INN", required = false) String innFragment,
            @RequestParam(value = "SNILS", required = false) String snilsFragment,
            @RequestParam(value = "KPP", required = false) String kppFragment,
            @RequestParam(value = "OGRN", required = false) String ogrnFragment,
            @RequestParam(value = "federal_code", required = false) String federalCode,
            @RequestParam(value = "nameEGR", required = false) String nameFragment,

            @RequestParam(value = "nPage", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "ALL") ContractorType type
    ) {
        MembersFilter filter = new MembersFilter();
        filter.setInnFragment(innFragment);
        filter.setSnilsFragment(snilsFragment);
        filter.setOgrnFragment(ogrnFragment);
        filter.setKppFragment(kppFragment);
        filter.setFederalCode(federalCode);
        filter.setNameFragment(nameFragment);
        filter.setNPage(pageNumber);
        filter.setPageSize(pageSize);

        ListItems<MembersService.MemberInfo> infos = membersService.find(filter);
        RestItemList<RestMember> restMembers = getRestMembers(infos);

        return Utils.setTotalAndLazyMode(restMembers, pageNumber, pageSize);
    }


    @NotNull
    private RestItemList<RestMember> getRestMembers(ListItems<MembersService.MemberInfo> infos) {
        RestItemList<RestMember> restMembersList = new RestItemList<>();
        List<RestMember> restMembers = infos.getList().stream()
                .map(item -> {
                    RestMember restItem = new RestMember();
                    restItem.setId(item.getId());
                    restItem.setName(item.getName());
                    restItem.setINN(item.getInn());
                    restItem.setOGRN(item.getOgrn());
                    restItem.setEmail(item.getEmail());
                    restItem.setKpp(item.getKpp());
                    restItem.setRegDate(item.getRegDate());
                    restItem.setCertOwnerFIO(item.getCertOwnerFullName());
                    return restItem;
                })
                .collect(Collectors.toList());

        restMembersList.setList(restMembers);
        restMembersList.setTotal(infos.getTotal());
        return restMembersList;
    }

    @NotNull
    @RequestMapping("/core")
    public RestItemList action(
            @RequestParam(defaultValue = "ALL") ContractorType type,
            @RequestParam(defaultValue = "false") boolean async) {
        ListItems memberInfos = membersService.find(new MembersFilter());


        return getRestMembers(memberInfos);
    }
}
