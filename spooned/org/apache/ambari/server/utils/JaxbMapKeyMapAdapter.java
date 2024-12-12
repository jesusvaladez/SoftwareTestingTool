package org.apache.ambari.server.utils;
import javax.xml.bind.annotation.adapters.XmlAdapter;
public class JaxbMapKeyMapAdapter extends javax.xml.bind.annotation.adapters.XmlAdapter<org.apache.ambari.server.utils.JaxbMapKeyMap[], java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> {
    private static org.apache.ambari.server.utils.JaxbMapKeyValAdapter mapAdapter = new org.apache.ambari.server.utils.JaxbMapKeyValAdapter();

    @java.lang.Override
    public org.apache.ambari.server.utils.JaxbMapKeyMap[] marshal(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> map) throws java.lang.Exception {
        if (map == null) {
            return null;
        }
        org.apache.ambari.server.utils.JaxbMapKeyMap[] list = new org.apache.ambari.server.utils.JaxbMapKeyMap[map.size()];
        int index = 0;
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stringMapEntry : map.entrySet()) {
            java.util.Map<java.lang.String, java.lang.String> value = stringMapEntry.getValue();
            org.apache.ambari.server.utils.JaxbMapKeyVal[] keyValList = org.apache.ambari.server.utils.JaxbMapKeyMapAdapter.mapAdapter.marshal(value);
            list[index++] = new org.apache.ambari.server.utils.JaxbMapKeyMap(stringMapEntry.getKey(), keyValList);
        }
        return list;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> unmarshal(org.apache.ambari.server.utils.JaxbMapKeyMap[] list) throws java.lang.Exception {
        if (list == null) {
            return null;
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> map = new java.util.TreeMap<>();
        for (org.apache.ambari.server.utils.JaxbMapKeyMap jaxbkeyMap : list) {
            map.put(jaxbkeyMap.key, org.apache.ambari.server.utils.JaxbMapKeyMapAdapter.mapAdapter.unmarshal(jaxbkeyMap.value));
        }
        return map;
    }
}