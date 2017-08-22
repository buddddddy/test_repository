package org.mdlp.core.debug;

import com.google.common.io.Files;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.io.ByteStreams.toByteArray;
import static java.util.Collections.list;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.util.StringUtils.isEmpty;
import static org.springframework.web.util.WebUtils.DEFAULT_CHARACTER_ENCODING;

public class DumpingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(DumpingFilter.class);

    private static final AtomicInteger REQUESTS_COUNTER = new AtomicInteger(0);

    private static final String[] CONTENT_TYPE_PREFIXES_TO_DUMP = new String[]{
        "text/",
        "application/json",
        "application/javascript",
    };

    private static boolean isContentTypeMatches(String contentType) {
        if (isEmpty(contentType)) return false;
        contentType = contentType.toLowerCase();
        for (String prefix : CONTENT_TYPE_PREFIXES_TO_DUMP) {
            if (contentType.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private static String getEncoding(String contentType) {
        contentType = contentType.replace("; ", ";").replace(" =", "=");
        String prefix = ";charset=";
        int pos = contentType.indexOf(prefix);
        if (pos < 0) return DEFAULT_CHARACTER_ENCODING;
        return contentType.substring(pos + prefix.length()).trim();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void doFilter(ServletRequest originalRequest, ServletResponse originalResponse, @NotNull FilterChain chain) throws IOException, ServletException {
        if (!(originalRequest instanceof HttpServletRequest) || !(originalResponse instanceof HttpServletResponse)) {
            chain.doFilter(originalRequest, originalResponse);
            return;
        }

        int requestNumber = REQUESTS_COUNTER.incrementAndGet();
        HttpServletRequest request = (HttpServletRequest) originalRequest;

        if (isNotEmpty(Files.getFileExtension(request.getRequestURI()))) {
            chain.doFilter(originalRequest, originalResponse);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\n----------------------------- REQUEST %010d -----------------------------", requestNumber));
        sb.append("\n        URI : ").append(request.getRequestURI());
        sb.append("\n     method : ").append(request.getMethod());
        sb.append("\n    headers : ");
        for (String name : list(request.getHeaderNames())) {
            for (String value : list(request.getHeaders(name))) {
                sb.append("\n              ").append(name).append(" = ").append(value);
            }
        }
        sb.append("\n parameters : ");
        for (String name : list(request.getParameterNames())) {
            for (String value : request.getParameterValues(name)) {
                sb.append("\n              ").append(name).append(" = ").append(value);
            }
        }
        if (isContentTypeMatches(request.getContentType())) {
            request = new ExtendedContentCachingRequestWrapper(request);
            sb.append("\n       body : \n");
            String encoding = getEncoding(request.getContentType());
            Charset charset = Charset.forName(encoding);
            String content = new String(toByteArray(request.getInputStream()), charset);
            request.getInputStream().reset();
            sb.append(content);
        }
        logger.info(sb.toString());

        ContentCachingResponseWrapper response = new ContentCachingResponseWrapper((HttpServletResponse) originalResponse);
        OnBeforeCloseResponseWrapper wrappedResponse = new OnBeforeCloseResponseWrapper(response) {
            @Override
            @SneakyThrows
            protected void onBeforeClose() {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("\n----------------------------- RESPONSE %010d ----------------------------", requestNumber));
                sb.append("\n    headers : ");
                for (String name : response.getHeaderNames()) {
                    for (String value : response.getHeaders(name)) {
                        sb.append("\n              ").append(name).append(" = ").append(value);
                    }
                }
                if (isContentTypeMatches(response.getContentType())) {
                    sb.append("\n       body : \n");
                    String encoding = getEncoding(response.getContentType());
                    Charset charset = Charset.forName(encoding);
                    String content = new String(response.getContentAsByteArray(), charset);
                    sb.append(content);
                }

                logger.info(sb.toString());

                response.copyBodyToResponse();
            }
        };
        chain.doFilter(request, wrappedResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    private static abstract class OnBeforeCloseResponseWrapper extends HttpServletResponseWrapper {
        protected abstract void onBeforeClose();

        private boolean wasClosed;

        private ServletOutputStream outputStream;

        private PrintWriter printWriter;

        public OnBeforeCloseResponseWrapper(@NotNull HttpServletResponse response) {
            super(response);
        }

        private void doBeforeOnClose() {
            if (!wasClosed) {
                wasClosed = true;
                onBeforeClose();
            }
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            super.sendError(sc, msg);
            doBeforeOnClose();
        }

        @Override
        public void sendError(int sc) throws IOException {
            super.sendError(sc);
            doBeforeOnClose();
        }

        @Override
        public void sendRedirect(String location) throws IOException {
            super.sendRedirect(location);
            doBeforeOnClose();
        }

        @Override
        public void flushBuffer() throws IOException {
            super.flushBuffer();
            doBeforeOnClose();
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (null == outputStream) {
                ServletOutputStream delegate = super.getOutputStream();
                outputStream = new ServletOutputStream() {
                    @Override
                    public boolean isReady() {
                        return delegate.isReady();
                    }

                    @Override
                    public void setWriteListener(WriteListener writeListener) {
                        delegate.setWriteListener(writeListener);
                    }

                    @Override
                    public void write(int b) throws IOException {
                        delegate.write(b);
                    }

                    @Override
                    public void close() throws IOException {
                        doBeforeOnClose();
                        super.close();
                    }

                    @Override
                    public void flush() throws IOException {
                        delegate.flush();
                    }
                };
            }
            return outputStream;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (null == printWriter) {
                PrintWriter delegate = super.getWriter();
                printWriter = new PrintWriter(delegate) {
                    @Override
                    public void close() {
                        doBeforeOnClose();
                        super.close();
                    }
                };
            }
            return printWriter;
        }
    }

}
