package org.mdlp.web.rest.get_events_list;

import org.jetbrains.annotations.NotNull;
import org.mdlp.Utils;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.service.KizEventService;
import org.mdlp.service.KizService.KizEventsFilter;
import org.mdlp.web.rest.ParentRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_events_list")
public class GetEventsListController {

    @NotNull
    private final KizEventService kizEventService;

    @Value("${basestate.useKizHistory}")
    private boolean useKizHistory;

    @Autowired
    public GetEventsListController(@NotNull KizEventService kizEventService) {
        this.kizEventService = kizEventService;
    }

    @NotNull
    @RequestMapping("")
    public RestKizEventsList action(
            @RequestParam(value = "types", required = false) Set<String> operations,
            @RequestParam(value = "kizId", required = false) String kizId,
            @RequestParam(value = "memberINN", required = false) String memberInnFragment,
            @RequestParam(value = "memberName", required = false) String memberNameFragment,
            @RequestParam(value = "dateFrom", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate dateTo,

            @RequestParam(value = "nPage", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize
    ) {
        if (useKizHistory && kizId != null) {
            return action(kizId, false);
        } else if (kizId != null) {
            action(kizId, true);
        }
        KizEventsFilter filter = new KizEventsFilter();
        filter.setOperations(operations);
        filter.setKizId(kizId);
        filter.setMemberInnFragment(memberInnFragment);
        filter.setMemberNameFragment(memberNameFragment);
        filter.setDateFrom(Utils.convertLocalDateToLocalDateTimeMin(dateFrom));
        filter.setDateTo(Utils.convertLocalDateToLocalDateTimeMin(dateTo));
        filter.setNPage(pageNumber);
        filter.setPageSize(pageSize);

        List<RestKizEvent> kizEvents = kizEventService.getKizEvents(filter, false);

        return new RestKizEventsList(
                kizEvents,
                kizEvents.size()
        );
    }

    @NotNull
    @RequestMapping("/core")
    public RestKizEventsList action(@RequestParam String kizId, @RequestParam(defaultValue = "false") boolean async) {

        List<RestKizEvent> kizEvents = kizEventService.getKizEvents(kizId, async);

        if (kizEvents == null) {
            return new RestKizEventsList();
        }

        return new RestKizEventsList(
                kizEvents,
                kizEvents.size()
        );
    }
}
