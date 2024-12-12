package org.apache.ambari.server.utils;
import javax.xml.bind.annotation.adapters.XmlAdapter;
public class JaxbMapKeyValAdapter extends javax.xml.bind.annotation.adapters.XmlAdapter<org.apache.ambari.server.utils.JaxbMapKeyVal[], java.util.Map<java.lang.String, java.lang.String>> {
    @java.lang.Override
    public org.apache.ambari.server.utils.JaxbMapKeyVal[] marshal(java.util.Map<java.lang.String, java.lang.String> m) throws java.lang.Exception {
        if (m == null) {
            return null;
        }
        org.apache.ambari.server.utils.JaxbMapKeyVal[] list = new org.apache.ambari.server.utils.JaxbMapKeyVal[m.size()];
        int index = 0;
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : m.entrySet()) {
            org.apache.ambari.server.utils.JaxbMapKeyVal jaxbMap = new org.apache.ambari.server.utils.JaxbMapKeyVal(entry.getKey(), entry.getValue());
            list[index++] = jaxbMap;
        }
        return list;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> unmarshal(org.apache.ambari.server.utils.JaxbMapKeyVal[] jm) throws java.lang.Exception {
        if (jm == null) {
            return null;
        }
        java.util.Map<java.lang.String, java.lang.String> m = new java.util.TreeMap<>();
        for (org.apache.ambari.server.utils.JaxbMapKeyVal jaxbMap : jm) {
            m.put(jaxbMap.key, jaxbMap.value);
        }
        return m;
    }
}