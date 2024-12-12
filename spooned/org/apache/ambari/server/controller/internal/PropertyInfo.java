package org.apache.ambari.server.controller.internal;
public class PropertyInfo {
    private final java.lang.String propertyId;

    private final boolean temporal;

    private final boolean pointInTime;

    private java.lang.String amsId;

    private boolean amsHostMetric;

    private java.lang.String unit;

    public PropertyInfo(java.lang.String propertyId, boolean temporal, boolean pointInTime) {
        this.propertyId = propertyId;
        this.temporal = temporal;
        this.pointInTime = pointInTime;
    }

    public java.lang.String getPropertyId() {
        return propertyId;
    }

    public boolean isTemporal() {
        return temporal;
    }

    public boolean isPointInTime() {
        return pointInTime;
    }

    public java.lang.String getAmsId() {
        return amsId;
    }

    public void setAmsId(java.lang.String amsId) {
        this.amsId = amsId;
    }

    public boolean isAmsHostMetric() {
        return amsHostMetric;
    }

    public void setAmsHostMetric(boolean amsHostMetric) {
        this.amsHostMetric = amsHostMetric;
    }

    public java.lang.String getUnit() {
        return unit;
    }

    public void setUnit(java.lang.String unit) {
        this.unit = unit;
    }
}