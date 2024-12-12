package org.apache.ambari.server.view;
import javax.annotation.Nullable;
@com.google.inject.Singleton
public class ViewDirectoryWatcher implements org.apache.ambari.server.view.DirectoryWatcher {
    public static final int FIXED_FILE_COUNTER = 30;

    public static final int FILE_CHECK_INTERVAL_MILLIS = 200;

    @com.google.inject.Inject
    org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    org.apache.ambari.server.view.ViewRegistry viewRegistry;

    private java.nio.file.WatchService watchService;

    private java.util.concurrent.ExecutorService executorService = java.util.concurrent.Executors.newSingleThreadExecutor();

    private java.util.concurrent.Future<?> watchTask;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.view.ViewDirectoryWatcher.class);

    private java.util.List<com.google.common.base.Function<java.nio.file.Path, java.lang.Boolean>> hooks = com.google.common.collect.Lists.newArrayList(java.util.Collections.singleton(loggingHook()));

    public void addHook(com.google.common.base.Function<java.nio.file.Path, java.lang.Boolean> hook) {
        hooks.add(hook);
    }

    private com.google.common.base.Function<java.nio.file.Path, java.lang.Boolean> loggingHook() {
        return new com.google.common.base.Function<java.nio.file.Path, java.lang.Boolean>() {
            @javax.annotation.Nullable
            @java.lang.Override
            public java.lang.Boolean apply(@javax.annotation.Nullable
            java.nio.file.Path path) {
                org.apache.ambari.server.view.ViewDirectoryWatcher.LOG.info("Finished processing the view definition for" + path);
                return true;
            }
        };
    }

    @java.lang.Override
    public void start() {
        try {
            java.nio.file.Path path = buildWatchService();
            java.lang.Runnable task = startWatching(path);
            watchTask = executorService.submit(task);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.view.ViewDirectoryWatcher.LOG.error("There were errors in starting the view directory watcher. This task will not run", e);
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    private static <T> java.nio.file.WatchEvent<T> cast(java.nio.file.WatchEvent<?> event) {
        return ((java.nio.file.WatchEvent<T>) (event));
    }

    private java.lang.Runnable startWatching(final java.nio.file.Path path) {
        return new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                try {
                    while (true) {
                        java.nio.file.WatchKey key = watchService.take();
                        org.apache.ambari.server.view.ViewDirectoryWatcher.LOG.info("Watcher Key was signalled");
                        for (java.nio.file.WatchEvent<?> event : key.pollEvents()) {
                            org.apache.ambari.server.view.ViewDirectoryWatcher.LOG.info("Watcher recieved poll event");
                            java.nio.file.WatchEvent<java.nio.file.Path> ev = org.apache.ambari.server.view.ViewDirectoryWatcher.cast(event);
                            java.nio.file.Path resolvedPath = path.resolve(ev.context());
                            org.apache.ambari.server.view.ViewDirectoryWatcher.LOG.info(java.lang.String.format("Event %s: %s\n", ev.kind(), resolvedPath));
                            if (!canBlockTillFileAvailable(resolvedPath)) {
                                org.apache.ambari.server.view.ViewDirectoryWatcher.LOG.info("Watcher detected that the file was either empty or corrupt");
                                continue;
                            }
                            if (!verify(resolvedPath)) {
                                org.apache.ambari.server.view.ViewDirectoryWatcher.LOG.info("The uploaded file was 1> Empty 2> Not a regular file or 3> Not a valid Jar archive file");
                                continue;
                            }
                            try {
                                org.apache.ambari.server.view.ViewDirectoryWatcher.LOG.info("Starting view extraction");
                                viewRegistry.readViewArchive(resolvedPath);
                                for (com.google.common.base.Function<java.nio.file.Path, java.lang.Boolean> hook : hooks) {
                                    hook.apply(resolvedPath);
                                }
                            } catch (java.lang.Exception e) {
                                org.apache.ambari.server.view.ViewDirectoryWatcher.LOG.error("Cannot read the view archive, offending file: " + resolvedPath, e);
                            }
                        }
                        if (!key.reset()) {
                            org.apache.ambari.server.view.ViewDirectoryWatcher.LOG.error("The watch key could not be reset, Directory watcher will not run anymore");
                            break;
                        }
                    } 
                } catch (java.lang.InterruptedException x) {
                    org.apache.ambari.server.view.ViewDirectoryWatcher.LOG.info("Cancelling the directory watcher", x);
                    return;
                }
            }
        };
    }

    private boolean canBlockTillFileAvailable(java.nio.file.Path resolvedPath) throws java.lang.InterruptedException {
        long oldLength;
        long newSize;
        long emptyCheck = 0;
        int fixed = 0;
        java.io.File file = resolvedPath.toAbsolutePath().toFile();
        while ((file.length() == 0) && (emptyCheck < 5)) {
            java.lang.Thread.sleep(org.apache.ambari.server.view.ViewDirectoryWatcher.FILE_CHECK_INTERVAL_MILLIS);
            emptyCheck++;
        } 
        if (emptyCheck == 5)
            return false;

        oldLength = file.length();
        while (true) {
            org.apache.ambari.server.view.ViewDirectoryWatcher.LOG.info("Waiting for file to be completely copied");
            java.lang.Thread.sleep(org.apache.ambari.server.view.ViewDirectoryWatcher.FILE_CHECK_INTERVAL_MILLIS);
            newSize = file.length();
            if (newSize > oldLength) {
                oldLength = newSize;
                continue;
            } else if (oldLength == newSize) {
                fixed++;
            } else {
                return false;
            }
            if (fixed > org.apache.ambari.server.view.ViewDirectoryWatcher.FIXED_FILE_COUNTER) {
                org.apache.ambari.server.view.ViewDirectoryWatcher.LOG.info(("File " + resolvedPath) + " has finished copying");
                return true;
            }
        } 
    }

    private boolean verify(java.nio.file.Path resolvedPath) {
        java.util.zip.ZipFile zipFile = null;
        try {
            java.io.File file = resolvedPath.toAbsolutePath().toFile();
            com.google.common.base.Preconditions.checkArgument(!file.isDirectory());
            com.google.common.base.Preconditions.checkArgument(file.length() > 0);
            zipFile = new java.util.zip.ZipFile(file);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.view.ViewDirectoryWatcher.LOG.info("Verification failed ", e);
            return false;
        } finally {
            org.apache.ambari.server.utils.Closeables.closeSilently(zipFile);
        }
        return true;
    }

    private java.nio.file.Path buildWatchService() throws java.io.IOException {
        java.io.File viewsDir = configuration.getViewsDir();
        java.nio.file.Path path = java.nio.file.Paths.get(viewsDir.getAbsolutePath());
        watchService = path.getFileSystem().newWatchService();
        path.register(watchService, java.nio.file.StandardWatchEventKinds.ENTRY_CREATE);
        return path;
    }

    @java.lang.Override
    public boolean isRunning() {
        if (watchTask != null)
            return !watchTask.isDone();

        return false;
    }

    @java.lang.Override
    public void stop() {
        watchTask.cancel(true);
    }
}