package org.mdlp.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.mdlp.Utils;
import org.mdlp.basestate.data.processor.RestProcessor;
import org.mdlp.data.document.Operation;
import org.mdlp.service.KizEventService;
import org.mdlp.service.KizService;
import org.mdlp.service.MembersService;
import org.mdlp.web.rest.get_events_list.RestKizEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 07.06.2017
 * @package org.mdlp.service.impl
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KizEventServiceImpl implements KizEventService {

    @NotNull
    private final RestProcessor restProcessor;
    @NotNull
    private final MembersService membersService;

    @Override
    public List<RestKizEvent> getKizEvents(KizService.KizEventsFilter filter, boolean async) {

        KizService.KizEvents events = restProcessor.getKizEvents(filter, async);
        if (events == null) {
            return null;
        }

        AtomicInteger index = new AtomicInteger();
        List<RestKizEvent> collect = events.getList().stream()
                .map(item -> {
                    RestKizEvent restItem = new RestKizEvent();
                    restItem.setId(String.valueOf(index.incrementAndGet()));
                    restItem.setEventTypeId(item.getOperation().name());
                    restItem.setDateTime(item.getDateTime());
                    String memberId = item.getMemberId();
                    String receiveId = item.getReceiverMemberId();
                    if (item.getOperation().name().equals(Operation.receive_order_s.name())) {
                        receiveId = item.getMemberId();
                    }
                    fillMemberId(memberId, restItem);
                    fillReceiveId(receiveId, restItem);
                    return restItem;
                })
                .collect(Collectors.toList());

        revertReceiveOrderS(collect);

        List<RestKizEvent> filteredEvents = filter(collect, filter);
        List<RestKizEvent> subList = Utils.page(filteredEvents, filter.getNPage(), filter.getPageSize());
        return subList;
    }

    @Override
    public List<RestKizEvent> getKizEvents(String kizId, boolean async) {
        KizService.KizEvents kizEvents = restProcessor.getKizEvents(kizId, async);
        if (kizEvents == null) {
            return null;
        }

        AtomicInteger index = new AtomicInteger();
        List<RestKizEvent> collect = kizEvents.getList().stream()
                .map(item -> {
                    RestKizEvent restItem = new RestKizEvent();
                    restItem.setId(String.valueOf(index.incrementAndGet()));
                    restItem.setEventTypeId(item.getOperation().name());
                    restItem.setDateTime(item.getDateTime());
                    String memberId = item.getMemberId();
                    String receiveId = item.getReceiverMemberId();
                    restItem.setCost(item.getCost());
                    fillMemberId(memberId, restItem);
                    fillReceiveId(receiveId, restItem);
                    fillCancellationInfo(item, restItem);
                    return restItem;
                })
                .collect(Collectors.toList());

        revertReceiveOrderS(collect);
        fillAddresses(collect);

        return collect;
    }

    private void fillCancellationInfo(KizService.KizEvent item, RestKizEvent restItem) {
        RestKizEvent.CancelInfo cancelInfo = new RestKizEvent.CancelInfo();
        cancelInfo.setCancelled(item.getCancellationInfo().isCancelled());
        if (item.getCancellationInfo().isCancelled()) {
            MembersService.MemberInfo contractor = restProcessor.getContractor(item.getCancellationInfo().getInitiatorId(), false);
            cancelInfo.setInitiatiorName(contractor.getName());
            cancelInfo.setInitiatorInn(contractor.getInn());
            cancelInfo.setInitiatorKpp(contractor.getKpp());
        }
        restItem.setCancellationInfo(cancelInfo);
    }

    private void fillAddresses(List<RestKizEvent> collect) {
        Optional<RestKizEvent> event =
                collect.stream()
                        .filter(info -> info.getEventTypeId().equals(Operation.move_place.name()))
                        .findFirst();

        if (event.isPresent()) {
            for (RestKizEvent restKizEvent : collect) {
                if (restKizEvent.getEventTypeId().equals(Operation.move_place.name())) {
                    // if memberFromId is not found in branches we will try to get it from safe warehouses
                    String address = Optional.ofNullable(membersService.findAddressByBranchId(restKizEvent.getMemberFromId()))
                            .orElseGet(()->membersService.findAddressBySafeWarehousesById(restKizEvent.getMemberFromId()));
                    restKizEvent.setMemberFromAddress(address);
                    // if memberToId is not found in branches we will try to get it from safe warehouses
                    address = Optional.ofNullable(membersService.findAddressByBranchId(restKizEvent.getMemberToId()))
                            .orElseGet(()->membersService.findAddressBySafeWarehousesById(restKizEvent.getMemberToId()));
                    restKizEvent.setMemberToAddress(address);
                }
            }
        }
    }

    private void revertReceiveOrderS(List<RestKizEvent> collect) {
        Optional<RestKizEvent> event =
                collect.stream()
                        .filter(info -> info.getEventTypeId().equals(Operation.receive_order_s.name()))
                        .findFirst();

        if (event.isPresent()) {
            for (RestKizEvent restKizEvent : collect) {
                if (restKizEvent.getEventTypeId().equals(Operation.receive_order_s.name())) {
                    String memberFromInn = event.get().getMemberFromINN();
                    String memberFromKpp = event.get().getMemberToKPP();
                    String memberFromName = event.get().getMemberFromName();
                    restKizEvent.setMemberFromINN(event.get().getMemberToINN());
                    restKizEvent.setMemberFromName(event.get().getMemberToName());
                    restKizEvent.setMemberFromKPP(event.get().getMemberToKPP());
                    restKizEvent.setMemberToINN(memberFromInn);
                    restKizEvent.setMemberToName(memberFromName);
                    restKizEvent.setMemberToKPP(memberFromKpp);
                }
            }
        }

    }

    private void fillReceiveId(String receiveId, RestKizEvent restItem) {
        if (null != receiveId) {
            restItem.setMemberToId(receiveId);
            MembersService.MemberInfo contractor = restProcessor.getContractor(receiveId, false);
            if (contractor != null) {
                restItem.setMemberToINN(contractor.getInn());
                restItem.setMemberToName(contractor.getName());
                restItem.setMemberToKPP(contractor.getKpp());
            }
        }
    }

    private void fillMemberId(String memberId, RestKizEvent restItem) {
        if (null != memberId) {
            restItem.setMemberFromId(memberId);
            MembersService.MemberInfo contractor = restProcessor.getContractor(memberId, false);
            if (contractor != null) {
                restItem.setMemberFromINN(contractor.getInn());
                restItem.setMemberFromName(contractor.getName());
                restItem.setMemberFromKPP(contractor.getKpp());
            }

        }
    }

    private List<RestKizEvent> filter(List<RestKizEvent> events, KizService.KizEventsFilter filter) {
        List<RestKizEvent> allResults = events.stream()
                .filter(info -> Utils.contains(info.getMemberFromINN(), filter.getMemberInnFragment()))
                .filter(info -> Utils.contains(info.getMemberFromName(), filter.getMemberNameFragment()))
                .filter(info -> (Utils.containsInSet(info.getEventTypeId(), filter.getOperations())))
                .collect(Collectors.toList());

        return allResults;
    }

}
