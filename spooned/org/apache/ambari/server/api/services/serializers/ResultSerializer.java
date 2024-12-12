package org.apache.ambari.server.api.services.serializers;
public interface ResultSerializer {
    java.lang.Object serialize(org.apache.ambari.server.api.services.Result result);

    java.lang.Object serializeError(org.apache.ambari.server.api.services.ResultStatus error);
}