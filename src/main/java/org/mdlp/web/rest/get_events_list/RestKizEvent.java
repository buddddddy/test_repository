package org.mdlp.web.rest.get_events_list;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RestKizEvent {

    @JsonProperty("id")
    private String id;

    @JsonProperty("eventTypeId")
    private String eventTypeId;

    @JsonProperty("memberFromName")
    private String memberFromName;

    @JsonProperty("memberFromId")
    private String memberFromId;

    @JsonProperty("memberToId")
    private String memberToId;

    @JsonProperty("memberFromINN")
    private String memberFromINN;

    @JsonProperty("memberFromKPP")
    private String memberFromKPP;

    @JsonProperty("memberToName")
    private String memberToName;

    @JsonProperty("memberToINN")
    private String memberToINN;

    @JsonProperty("memberToKPP")
    private String memberToKPP;

    @JsonProperty("memberFromAddress")
    private String memberFromAddress;

    @JsonProperty("memberToAddress")
    private String memberToAddress;

    @JsonProperty("dateTime")
    private LocalDateTime dateTime;

    @JsonProperty("inside")
    private String inside;

    @JsonProperty("cost")
    private Long cost;

    @JsonIgnore
    private String kizId;

    @JsonProperty("cancellationInfo")
    private CancelInfo cancellationInfo;

    @Data
    public static class CancelInfo {
        @JsonProperty("cancelled")
        private boolean cancelled = false;

        @JsonProperty("inn")
        private String initiatorInn;

        @JsonProperty("title")
        private String initiatiorName;

        @JsonProperty("kpp")
        private String initiatorKpp;

    }
}
