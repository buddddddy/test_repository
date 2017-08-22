package org.mdlp.ws;

import org.jetbrains.annotations.NotNull;
import org.mdlp.basestate.data.processor.RestProcessor;
import org.mdlp.utils.AppContext;
import org.mdlp.wsdl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.annotation.security.PermitAll;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import static wsdl.WsdlUtils.marshal;
import static wsdl.WsdlUtils.unmarshal;

@Endpoint
@PermitAll
public class MdlpServiceEndpoint {
    private static final String NAMESPACE_URI = "http://www.mdlp.org/wsdl/MdlpService.wsdl";

    @Value("${xsd.schema.version}")
    private String xsdSchemaVersion;

    @NotNull
    private final RestProcessor restProcessor;

    @Autowired
    public MdlpServiceEndpoint(@NotNull RestProcessor restProcessor) {
        this.restProcessor = restProcessor;
    }

    @PayloadRoot(localPart = "sendTypedDocRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    @NotNull
    public SendTypedDocResponse sendTypedDoc(@NotNull @RequestPayload SendTypedDocRequest request) {
        Documents documents = new Documents().withResult(new Result()
                .withActionId(200)
                .withSessionUi(String.valueOf(AppContext.getInstance().getNextId())));
        Documents coreResult = restProcessor.process(request.getDocuments(), documents);
        return new SendTypedDocResponse().withStatus(new TypedStatus().withResult(coreResult.withVersion(xsdSchemaVersion)));
    }


    @PayloadRoot(localPart = "sendDocRequest", namespace = NAMESPACE_URI)
    @ResponsePayload
    @NotNull
    public SendDocResponse sendDoc(@NotNull @RequestPayload SendDocRequest request) {
        Documents documents = unmarshal(Documents.class, request.getData());
        SendTypedDocRequest typedRequest = new SendTypedDocRequest().withDocuments(documents);
        SendTypedDocResponse typedResponse = sendTypedDoc(typedRequest);
        return new SendDocResponse().withStatus(new Status()
                        .withResult(marshal(typedResponse.getStatus().getResult()))
                        .withData(typedResponse.getStatus().getData())
        );
    }

}
