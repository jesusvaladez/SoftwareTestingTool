package org.apache.ambari.server.audit.request.eventcreator;
public class RequestAuditEventCreatorHelper {
    public static java.lang.String getNamedProperty(org.apache.ambari.server.api.services.Request request, java.lang.String propertyName) {
        org.apache.ambari.server.api.services.NamedPropertySet first = com.google.common.collect.Iterables.getFirst(request.getBody().getNamedPropertySets(), null);
        if ((first != null) && (first.getProperties().get(propertyName) instanceof java.lang.String)) {
            return java.lang.String.valueOf(first.getProperties().get(propertyName));
        }
        return null;
    }

    public static java.util.List<java.lang.String> getNamedPropertyList(org.apache.ambari.server.api.services.Request request, java.lang.String propertyName) {
        org.apache.ambari.server.api.services.NamedPropertySet first = com.google.common.collect.Iterables.getFirst(request.getBody().getNamedPropertySets(), null);
        if ((first != null) && (first.getProperties().get(propertyName) instanceof java.util.List)) {
            java.util.List<java.lang.String> list = ((java.util.List<java.lang.String>) (first.getProperties().get(propertyName)));
            if (list != null) {
                return list;
            }
        }
        return java.util.Collections.emptyList();
    }

    public static java.lang.String getProperty(org.apache.ambari.server.api.services.Request request, java.lang.String propertyName) {
        java.util.List<java.lang.String> list = org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreatorHelper.getPropertyList(request, propertyName);
        return list.isEmpty() ? null : list.get(0);
    }

    public static java.util.List<java.lang.String> getPropertyList(org.apache.ambari.server.api.services.Request request, java.lang.String propertyName) {
        java.util.List<java.lang.String> list = new java.util.LinkedList<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getBody().getPropertySets()) {
            if (propertyMap.containsKey(propertyName)) {
                list.add(java.lang.String.valueOf(propertyMap.get(propertyName)));
            }
        }
        return list;
    }
}