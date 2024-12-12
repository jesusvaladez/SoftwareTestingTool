package org.apache.ambari.server.utils;
import javax.xml.bind.annotation.adapters.XmlAdapter;
public class JaxbMapKeyListAdapter extends javax.xml.bind.annotation.adapters.XmlAdapter<org.apache.ambari.server.utils.JaxbMapKeyList[], java.util.Map<java.lang.String, java.util.List<java.lang.String>>> {
    @java.lang.Override
    public org.apache.ambari.server.utils.JaxbMapKeyList[] marshal(java.util.Map<java.lang.String, java.util.List<java.lang.String>> map) throws java.lang.Exception {
        if (map == null) {
            return null;
        }
        org.apache.ambari.server.utils.JaxbMapKeyList[] list = new org.apache.ambari.server.utils.JaxbMapKeyList[map.size()];
        int index = 0;
        for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> stringListEntry : map.entrySet()) {
            org.apache.ambari.server.utils.JaxbMapKeyList jaxbMap = new org.apache.ambari.server.utils.JaxbMapKeyList(stringListEntry.getKey(), stringListEntry.getValue());
            list[index++] = jaxbMap;
        }
        return list;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.util.List<java.lang.String>> unmarshal(org.apache.ambari.server.utils.JaxbMapKeyList[] list) throws java.lang.Exception {
        if (list == null) {
            return null;
        }
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> m = new java.util.TreeMap<>();
        for (org.apache.ambari.server.utils.JaxbMapKeyList jaxbMap : list) {
            m.put(jaxbMap.key, jaxbMap.value);
        }
        return m;
    }
}