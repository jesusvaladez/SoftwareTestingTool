package org.apache.ambari.server.controller.logging;
public class Utils {
    private static int WAIT_COUNT_MAX = 1000;

    private Utils() {
    }

    static void logErrorMessageWithThrowableWithCounter(org.slf4j.Logger logger, java.util.concurrent.atomic.AtomicInteger atomicInteger, java.lang.String errorMessage, java.lang.Throwable throwable) {
        org.apache.ambari.server.controller.logging.Utils.logErrorMessageWithThrowableWithCounter(logger, atomicInteger, errorMessage, throwable, org.apache.ambari.server.controller.logging.Utils.WAIT_COUNT_MAX);
    }

    static void logErrorMessageWithThrowableWithCounter(org.slf4j.Logger logger, java.util.concurrent.atomic.AtomicInteger atomicInteger, java.lang.String errorMessage, java.lang.Throwable throwable, int maxCount) {
        if (atomicInteger.getAndIncrement() == 0) {
            logger.error(errorMessage, throwable);
        } else {
            atomicInteger.compareAndSet(maxCount, 0);
        }
    }

    static void logErrorMessageWithCounter(org.slf4j.Logger logger, java.util.concurrent.atomic.AtomicInteger atomicInteger, java.lang.String errorMessage) {
        org.apache.ambari.server.controller.logging.Utils.logErrorMessageWithCounter(logger, atomicInteger, errorMessage, org.apache.ambari.server.controller.logging.Utils.WAIT_COUNT_MAX);
    }

    static void logErrorMessageWithCounter(org.slf4j.Logger logger, java.util.concurrent.atomic.AtomicInteger atomicInteger, java.lang.String errorMessage, int maxCount) {
        if (atomicInteger.getAndIncrement() == 0) {
            logger.error(errorMessage);
        } else {
            atomicInteger.compareAndSet(maxCount, 0);
        }
    }

    static void logDebugMessageWithCounter(org.slf4j.Logger logger, java.util.concurrent.atomic.AtomicInteger atomicInteger, java.lang.String errorMessage) {
        org.apache.ambari.server.controller.logging.Utils.logDebugMessageWithCounter(logger, atomicInteger, errorMessage, org.apache.ambari.server.controller.logging.Utils.WAIT_COUNT_MAX);
    }

    static void logDebugMessageWithCounter(org.slf4j.Logger logger, java.util.concurrent.atomic.AtomicInteger atomicInteger, java.lang.String debugMessage, int maxCount) {
        if (atomicInteger.getAndIncrement() == 0) {
            logger.debug(debugMessage);
        } else {
            atomicInteger.compareAndSet(maxCount, 0);
        }
    }
}