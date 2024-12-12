package org.apache.ambari.server.controller.metrics.timeline.cache;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;
public class TimelineMetricsCacheValueSerializer implements org.ehcache.spi.serialization.Serializer<org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue> {
    private final java.lang.ClassLoader classLoader;

    public TimelineMetricsCacheValueSerializer(java.lang.ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @java.lang.Override
    public java.nio.ByteBuffer serialize(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue value) throws org.ehcache.spi.serialization.SerializerException {
        try {
            java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream objectOutputStream = new java.io.ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            objectOutputStream.close();
            return java.nio.ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        } catch (java.lang.Exception e) {
            throw new org.ehcache.spi.serialization.SerializerException(e);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue read(java.nio.ByteBuffer binary) throws java.lang.ClassNotFoundException, org.ehcache.spi.serialization.SerializerException {
        try {
            java.io.ByteArrayInputStream byteArrayInputStream = new java.io.ByteArrayInputStream(binary.array());
            java.io.ObjectInputStream objectInputStream = new java.io.ObjectInputStream(byteArrayInputStream);
            return ((org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue) (objectInputStream.readObject()));
        } catch (java.io.IOException | java.lang.ClassNotFoundException e) {
            throw new org.ehcache.spi.serialization.SerializerException("Error during deserialization", e);
        }
    }

    @java.lang.Override
    public boolean equals(org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue value, java.nio.ByteBuffer binary) throws java.lang.ClassNotFoundException, org.ehcache.spi.serialization.SerializerException {
        try {
            java.io.ByteArrayInputStream byteArrayInputStream = new java.io.ByteArrayInputStream(binary.array());
            java.io.ObjectInputStream objectInputStream = new java.io.ObjectInputStream(byteArrayInputStream);
            org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue deserializedValue = ((org.apache.ambari.server.controller.metrics.timeline.cache.TimelineMetricsCacheValue) (objectInputStream.readObject()));
            if (value == deserializedValue)
                return true;

            if ((deserializedValue == null) || (value.getClass() != deserializedValue.getClass()))
                return false;

            if (!value.getStartTime().equals(deserializedValue.getStartTime()))
                return false;

            if (!value.getEndTime().equals(deserializedValue.getEndTime()))
                return false;

            if (!value.getTimelineMetrics().equals(deserializedValue.getTimelineMetrics()))
                return false;

            return value.getPrecision() == deserializedValue.getPrecision();
        } catch (java.io.IOException e) {
            throw new org.ehcache.spi.serialization.SerializerException("Error during deserialization", e);
        }
    }
}