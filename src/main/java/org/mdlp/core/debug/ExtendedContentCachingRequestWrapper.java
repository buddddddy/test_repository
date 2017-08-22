package org.mdlp.core.debug;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.google.common.io.ByteStreams.toByteArray;

public class ExtendedContentCachingRequestWrapper extends ContentCachingRequestWrapper {

    private volatile ServletInputStream inputStream;

    public ExtendedContentCachingRequestWrapper(@NotNull HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (null == inputStream) inputStream = new ServletInputStreamImpl(toByteArray(super.getInputStream()));
        return inputStream;
    }

    private static class ServletInputStreamImpl extends ServletInputStream {

        @NotNull
        private final ByteArrayInputStream inputStream;

        public ServletInputStreamImpl(@NotNull byte[] content) {
            inputStream = new ByteArrayInputStream(content);
        }

        @Override
        public synchronized void reset() throws IOException {
            inputStream.reset();
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public boolean isFinished() {
            return 0 == inputStream.available();
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }

        @Override
        public void close() throws IOException {
            inputStream.close();
            super.close();
        }

    }

}
