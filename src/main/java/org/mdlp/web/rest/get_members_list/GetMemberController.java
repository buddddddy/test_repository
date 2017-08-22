package org.mdlp.web.rest.get_members_list;

import org.jetbrains.annotations.NotNull;
import org.mdlp.basestate.data.processor.ContractorType;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.data.ListItems;
import org.mdlp.service.MembersService;
import org.mdlp.service.exception.IllegalMemberResultException;
import org.mdlp.web.rest.ParentRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_member")
public class GetMemberController {


    @NotNull
    private final MembersService membersService;

    @Value("${basestate.useRegs}")
    private boolean useRegs;

    @Autowired
    public GetMemberController(@NotNull MembersService membersService) {
        this.membersService = membersService;
    }

    @NotNull
    @RequestMapping("")
    public RestMember action(@RequestParam String id,
                             @RequestParam(value = "SNILS", required = false) String snilsFragment,
                             @RequestParam(value = "OGRN", required = false) String ogrnFragment,

                             @RequestParam(value = "nPage", defaultValue = "1") int pageNumber,
                             @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                             @RequestParam(defaultValue = "ALL") ContractorType type
    ) {
        if (useRegs) {
            return action(id, false);
        } else {
            action(id, true);
        }

        return null;
    }

    @NotNull
    @RequestMapping("/byInn")
    public RestMember getByInn(@RequestParam(value = "producerInn", required = false) String producerInn) throws IllegalMemberResultException {
        MembersService.MembersFilter filter = new MembersService.MembersFilter();
        filter.setInnFragment(producerInn);
        ListItems<MembersService.MemberInfo> result = membersService.findByInnFragment(producerInn);
        if (result.getTotal() > 1) {
            throw new IllegalMemberResultException("Core is return more then 1 member by exclusive inn");
        }

        return result.getList().isEmpty() ? new RestMember() : getRestMember(result.getList().get(0));
    }

    @NotNull
    private RestMember getRestMember(MembersService.MemberInfo info) {
        RestMember member = new RestMember(info.getId(), info.getName(), info
                .getInn(), info.getOgrn(), info.getEmail(), info.getCertOwnerFullName(), info.getKpp(), info.getRegDate(), info.isResident(), info.getRegistryType());
        return member;
    }

    @NotNull
    @RequestMapping("/core")
    public RestMember action(@RequestParam String id,
                             @RequestParam(defaultValue = "false") boolean async) {

        Optional<MembersService.MemberInfo> info = membersService.findById(id);

        return info.isPresent() ? getRestMember(info.get()) : new RestMember();
    }
}
