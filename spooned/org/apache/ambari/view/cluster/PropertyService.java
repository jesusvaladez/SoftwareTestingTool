package org.apache.ambari.view.cluster;
public abstract class PropertyService {
    @javax.inject.Inject
    protected org.apache.ambari.view.ViewContext context;

    protected java.lang.String getResponse(java.lang.String... propertyNames) {
        java.util.Map<java.lang.String, java.lang.String> properties = context.getProperties();
        java.lang.StringBuffer buffer = new java.lang.StringBuffer();
        int count = 0;
        buffer.append("[");
        for (java.lang.String propertyName : propertyNames) {
            if ((count++) > 0) {
                buffer.append(",\n");
            }
            buffer.append(getPropertyResponse(properties, propertyName));
        }
        buffer.append("]");
        return buffer.toString();
    }

    private java.lang.String getPropertyResponse(java.util.Map<java.lang.String, java.lang.String> properties, java.lang.String key) {
        java.lang.StringBuffer buffer = new java.lang.StringBuffer();
        java.lang.String value = properties.get(key);
        buffer.append("{\"");
        buffer.append(key);
        buffer.append("\" : \"");
        buffer.append(value);
        buffer.append("\"}");
        return buffer.toString();
    }
}