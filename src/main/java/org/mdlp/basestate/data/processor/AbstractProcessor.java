package org.mdlp.basestate.data.processor;

import org.mdlp.basestate.data.AsyncResult;
import org.mdlp.basestate.data.processor.command.AsyncRestCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by SSuvorov on 30.03.2017.
 */
public abstract class AbstractProcessor {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractProcessor.class);

    public final static int REQUEST_PROCESSING = 1;
    public final static int REQUEST_SUCCESS = 2;
    public final static int REQUEST_ERROR = 3;

    @Value("${basestate.responseWaitSeconds}")
    private int responseWaitSeconds;

    @Value("${basestate.responseWaitOperationsSeconds}")
    private int responseWaitOperationsSeconds;

    @Value("${basestate.responseWaitTotalSeconds}")
    private int responseWaitTotalSeconds;

    @Value("${basestate.responseWaitOperationsTotalSeconds}")
    private int responseWaitOperationsTotalSeconds;

    @Value("${basestate.url}")
    protected String baseUrl;

    @Value("${basestate.core.version}")
    protected String coreVersion;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    protected RestUtil restUtil;

    protected <T extends AsyncRestCommand> Object getResult(T command) {
        long start = System.currentTimeMillis();
        AsyncResult result;
        while (true) {
            result = command.execute();
            if (result.isOperationResult()) {
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(responseWaitSeconds);
            } catch (InterruptedException e) {
                // skip
            }
            long end = System.currentTimeMillis();
            long duration = TimeUnit.MILLISECONDS.toSeconds(end - start);
            if (duration > responseWaitTotalSeconds) {
                break;
            }
        }
        return result.getResult();
    }

    protected <T extends AsyncRestCommand> Object getOperationsResult(T command) {
        long start = System.currentTimeMillis();
        AsyncResult result;
        while (true) {
            result = command.execute();
            if (result.isOperationResult()) {
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(responseWaitOperationsSeconds);
            } catch (InterruptedException e) {
                // skip
            }
            long end = System.currentTimeMillis();
            long duration = TimeUnit.MILLISECONDS.toSeconds(end - start);
            if (duration > responseWaitOperationsTotalSeconds) {
                break;
            }
        }
        return result.getResult();
    }

    protected <T> T executeCommand(boolean async, AsyncCommand<T> command) {
        if (async) {
            taskExecutor.execute(() -> {
                try {
                    command.execute();
                } catch (Exception e) {
                    LOG.error("failed to execute async task", e);
                }
            });
            return null;
        } else {
            try {
                return command.execute();
            } catch (Exception e) {
                LOG.error("failed to execute command", e);
            }
            return null;
        }
    }

    protected abstract class AsyncCommand<T> {
        public abstract T execute() throws IOException;
    }
}