package org.apache.ambari.server.controller.metrics.timeline.cache;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;
public class TimelineAppMetricCacheKeySerializer implements org.ehcache.spi.serialization.Serializer<org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey> {
    private final java.lang.ClassLoader classLoader;

    public TimelineAppMetricCacheKeySerializer(java.lang.ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @java.lang.Override
    public java.nio.ByteBuffer serialize(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey key) throws org.ehcache.spi.serialization.SerializerException {
        try {
            java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream objectOutputStream = new java.io.ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(key);
            objectOutputStream.close();
            return java.nio.ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        } catch (java.lang.Exception e) {
            throw new org.ehcache.spi.serialization.SerializerException(e);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey read(java.nio.ByteBuffer binary) throws java.lang.ClassNotFoundException, org.ehcache.spi.serialization.SerializerException {
        try {
            java.io.ByteArrayInputStream byteArrayInputStream = new java.io.ByteArrayInputStream(binary.array());
            java.io.ObjectInputStream objectInputStream = new java.io.ObjectInputStream(byteArrayInputStream);
            return ((org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey) (objectInputStream.readObject()));
        } catch (java.io.IOException | java.lang.ClassNotFoundException e) {
            throw new org.ehcache.spi.serialization.SerializerException("Error during deserialization", e);
        }
    }

    @java.lang.Override
    public boolean equals(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey key, java.nio.ByteBuffer binary) throws java.lang.ClassNotFoundException, org.ehcache.spi.serialization.SerializerException {
        try {
            java.io.ByteArrayInputStream byteArrayInputStream = new java.io.ByteArrayInputStream(binary.array());
            java.io.ObjectInputStream objectInputStream = new java.io.ObjectInputStream(byteArrayInputStream);
            org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey deserializedKey = ((org.apache.ambari.server.controller.metrics.timeline.cache.TimelineAppMetricCacheKey) (objectInputStream.readObject()));
            if (key == deserializedKey)
                return true;

            if ((deserializedKey == null) || (key.getClass() != deserializedKey.getClass()))
                return false;

            if (!key.getMetricNames().equals(deserializedKey.getMetricNames()))
                return false;

            if (!key.getAppId().equals(deserializedKey.getAppId()))
                return false;

            return !(key.getHostNames() != null ? !key.getHostNames().equals(deserializedKey.getHostNames()) : deserializedKey.getHostNames() != null);
        } catch (java.io.IOException e) {
            throw new org.ehcache.spi.serialization.SerializerException("Error during deserialization", e);
        }
    }
}