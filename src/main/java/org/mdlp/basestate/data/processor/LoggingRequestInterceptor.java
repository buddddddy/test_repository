package org.mdlp.basestate.data.processor;

import org.mdlp.data.log.LogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Created by SSuvorov on 31.03.2017.
 */
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingRequestInterceptor.class);


    public LoggingRequestInterceptor() {
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        LogDTO logDTO = new LogDTO();
        logDTO.setMethod(request.getMethod().toString());
        logDTO.setUri(request.getURI().toString());
        if (body.length > 0 && hasTextBody(request.getHeaders())) {
            logDTO.setOutboundMessage(new String(body, determineCharset(request.getHeaders())));
        }

        try {
            ClientHttpResponse response = execution.execute(request, body);
            log(logDTO, response);
            return response;
        } catch (IOException e) {
            logDTO.setStatusDescription(e.toString());
            throw e;
        } finally {
            logger.info(logDTO.toString());
        }
    }

    private void log(LogDTO logDTO, ClientHttpResponse response) {
        try {
            logDTO.setStatusCode(response.getRawStatusCode());
            logDTO.setStatusDescription(response.getStatusText());
            HttpHeaders responseHeaders = response.getHeaders();
            long contentLength = responseHeaders.getContentLength();
            if (contentLength != 0) {
                if (hasTextBody(responseHeaders)) {
                    String bodyText = StreamUtils.copyToString(response.getBody(), determineCharset(responseHeaders));
                    logDTO.setInboundMessage(bodyText);
                }
            }
        } catch (Exception e) {
            logDTO.setStatusDescription(e.toString());
        }
    }

    protected boolean hasTextBody(HttpHeaders headers) {
        MediaType contentType = headers.getContentType();
        if (contentType != null) {
            String subtype = contentType.getSubtype();
            return "text".equals(contentType.getType()) || "xml".equals(subtype) || "json".equals(subtype);
        }
        return false;
    }

    protected Charset determineCharset(HttpHeaders headers) {
        MediaType contentType = headers.getContentType();
        if (contentType != null) {
            try {
                Charset charSet = contentType.getCharset();
                if (charSet != null) {
                    return charSet;
                }
            } catch (UnsupportedCharsetException e) {
            }
        }
        return StandardCharsets.UTF_8;
    }
}