package org.apache.ambari.view.utils;
public class UserLocal<T> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.utils.UserLocal.class);

    private static final java.util.Map<java.lang.Class, java.util.Map<java.lang.String, java.lang.Object>> viewSingletonObjects = new java.util.concurrent.ConcurrentHashMap<>();

    private final java.lang.Class<? extends T> tClass;

    private static final java.util.Map<java.lang.String, java.util.concurrent.locks.ReentrantLock> locks = new java.util.HashMap<>();

    public UserLocal(java.lang.Class<? extends T> tClass) {
        this.tClass = tClass;
    }

    protected synchronized T initialValue(org.apache.ambari.view.ViewContext context) {
        return null;
    }

    private static java.util.concurrent.locks.ReentrantLock getLockFor(java.lang.String key) {
        org.apache.ambari.view.utils.UserLocal.LOG.info("Finding lock for : {}", key);
        if (null == org.apache.ambari.view.utils.UserLocal.locks.get(key)) {
            org.apache.ambari.view.utils.UserLocal.LOG.info("Lock not found for {} ", key);
            synchronized(org.apache.ambari.view.utils.UserLocal.locks) {
                if (null == org.apache.ambari.view.utils.UserLocal.locks.get(key)) {
                    org.apache.ambari.view.utils.UserLocal.LOG.info("Creating lock for {} ", key);
                    org.apache.ambari.view.utils.UserLocal.locks.put(key, new java.util.concurrent.locks.ReentrantLock());
                }
            }
        }
        return org.apache.ambari.view.utils.UserLocal.locks.get(key);
    }

    public T get(org.apache.ambari.view.ViewContext context) {
        if (!org.apache.ambari.view.utils.UserLocal.viewSingletonObjects.containsKey(tClass)) {
            synchronized(org.apache.ambari.view.utils.UserLocal.viewSingletonObjects) {
                if (!org.apache.ambari.view.utils.UserLocal.viewSingletonObjects.containsKey(tClass)) {
                    org.apache.ambari.view.utils.UserLocal.viewSingletonObjects.put(tClass, new java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object>());
                }
            }
        }
        java.util.Map<java.lang.String, java.lang.Object> instances = org.apache.ambari.view.utils.UserLocal.viewSingletonObjects.get(tClass);
        java.lang.String key = getTagName(context);
        org.apache.ambari.view.utils.UserLocal.LOG.debug("looking for key : {}", key);
        if (!instances.containsKey(key)) {
            java.lang.String lockKey = (tClass.getName() + "_") + key;
            org.apache.ambari.view.utils.UserLocal.LOG.info("key {} not found. getting lock for {}", key, lockKey);
            java.util.concurrent.locks.ReentrantLock lock = org.apache.ambari.view.utils.UserLocal.getLockFor(lockKey);
            boolean gotLock = lock.tryLock();
            if (!gotLock) {
                org.apache.ambari.view.utils.UserLocal.LOG.error("Lock could not be obtained for {}. Throwing exception.", lockKey);
                throw new java.lang.RuntimeException(java.lang.String.format("Failed to initialize %s for %s. Try Again.", tClass.getName(), key));
            } else {
                try {
                    T initValue = initialValue(context);
                    org.apache.ambari.view.utils.UserLocal.LOG.info("Obtained initial value : {} for key : {}", initValue, key);
                    instances.put(key, initValue);
                } finally {
                    lock.unlock();
                }
            }
        }
        return ((T) (instances.get(key)));
    }

    public void set(T obj, org.apache.ambari.view.ViewContext context) {
        if (!org.apache.ambari.view.utils.UserLocal.viewSingletonObjects.containsKey(tClass)) {
            synchronized(org.apache.ambari.view.utils.UserLocal.viewSingletonObjects) {
                if (!org.apache.ambari.view.utils.UserLocal.viewSingletonObjects.containsKey(tClass)) {
                    org.apache.ambari.view.utils.UserLocal.viewSingletonObjects.put(tClass, new java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object>());
                }
            }
        }
        java.lang.String key = getTagName(context);
        org.apache.ambari.view.utils.UserLocal.LOG.info("setting key : value {} : {}", key, obj);
        java.util.Map<java.lang.String, java.lang.Object> instances = org.apache.ambari.view.utils.UserLocal.viewSingletonObjects.get(tClass);
        instances.put(key, obj);
    }

    public void remove(org.apache.ambari.view.ViewContext context) {
        java.lang.String key = getTagName(context);
        org.apache.ambari.view.utils.UserLocal.LOG.info("removing key : {}", key);
        java.util.Map<java.lang.String, java.lang.Object> instances = org.apache.ambari.view.utils.UserLocal.viewSingletonObjects.get(tClass);
        if (null != instances) {
            instances.remove(key);
        }
    }

    private java.lang.String getTagName(org.apache.ambari.view.ViewContext context) {
        if (context == null) {
            return "<null>";
        }
        return java.lang.String.format("%s:%s", context.getInstanceName(), context.getUsername());
    }

    public static void dropAllConnections(java.lang.Class tClass) {
        org.apache.ambari.view.utils.UserLocal.LOG.info("removing all {} ", tClass.getName());
        java.util.Map<java.lang.String, java.lang.Object> instances = org.apache.ambari.view.utils.UserLocal.viewSingletonObjects.get(tClass);
        if (instances != null) {
            instances.clear();
        }
    }

    public static void dropAllConnections() {
        org.apache.ambari.view.utils.UserLocal.LOG.info("clearing all viewSingletonObjects.");
        org.apache.ambari.view.utils.UserLocal.viewSingletonObjects.clear();
    }

    public static void dropInstanceCache(java.lang.String instanceName) {
        org.apache.ambari.view.utils.UserLocal.LOG.info("removing all the keys for instanceName : {}", instanceName);
        for (java.util.Map<java.lang.String, java.lang.Object> cache : org.apache.ambari.view.utils.UserLocal.viewSingletonObjects.values()) {
            for (java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.Object>> it = cache.entrySet().iterator(); it.hasNext();) {
                java.util.Map.Entry<java.lang.String, java.lang.Object> entry = it.next();
                if (entry.getKey().startsWith(instanceName + ":")) {
                    org.apache.ambari.view.utils.UserLocal.LOG.debug("removing key : {} ", entry.getKey());
                    it.remove();
                }
            }
        }
    }
}