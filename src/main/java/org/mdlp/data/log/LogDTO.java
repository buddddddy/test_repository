package org.mdlp.data.log;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.ToString;
import org.mdlp.web.serializer.LongJsonStringSerializer;

import java.time.LocalDateTime;

@ToString
public class LogDTO {

    private long id;
    private LocalDateTime createdAt = LocalDateTime.now();
    @JsonSerialize(using = LongJsonStringSerializer.class)
    private String outboundMessage;
    @JsonSerialize(using = LongJsonStringSerializer.class)
    private String inboundMessage;
    private int status;
    private String statusDescription;
    private String method;
    private String uri;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getOutboundMessage() {
        return outboundMessage;
    }

    public void setOutboundMessage(String outboundMessage) {
        this.outboundMessage = outboundMessage;
    }

    public String getInboundMessage() {
        return inboundMessage;
    }

    public void setInboundMessage(String inboundMessage) {
        this.inboundMessage = inboundMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatusCode(int status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
