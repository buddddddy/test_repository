package org.mdlp.web.rest.get_members_list;

import org.jetbrains.annotations.NotNull;
import org.mdlp.Utils;
import org.mdlp.basestate.data.address.Address;
import org.mdlp.basestate.data.statistic.RegistrationFilter;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.service.MembersService;
import org.mdlp.web.rest.ParentRestController;
import org.mdlp.web.rest.RestItemList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author PVBorisov (Pavel.V.Borisov@firstlinesoftware.com)
 * @version 04.07.2017
 */
@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_member_filials_list")
public class GetMemberBranchesController {

    private final @NotNull MembersService membersService;

    @Autowired
    public GetMemberBranchesController(@NotNull MembersService membersService) {
        this.membersService = membersService;
    }

    @NotNull
    @RequestMapping("")
    public RestItemList<Address> action(
        @RequestParam(value = "producerInn", required = true) String producerInn,
        @RequestParam(value = "filterType", required = false) String filterType,
        @RequestParam(value = "nPage", defaultValue = "1") int pageNumber,
        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {

        RegistrationFilter filter = new RegistrationFilter();
        filter.setQueryId(UUID.randomUUID().toString());
        filter.setINN(producerInn);

        int offset = (pageNumber - 1) * pageSize;
        filter.setPaginationOffset(offset);
        filter.setPaginationLimit(pageSize);

        List<Address> addresses = membersService.findBranchesByInn(filter, false);

        if (addresses == null) {
            return new RestItemList<>();
        }

        RestItemList restItemList = new RestItemList();
        restItemList.setList(addresses);
        restItemList.setTotal(addresses.size());
        return Utils.setTotalAndLazyMode(restItemList, pageNumber, pageSize);
    }
}
