package org.apache.ambari.server.orm.entities;
import javax.persistence.Column;
import javax.persistence.Id;
public class WidgetLayoutUserWidgetEntityPK implements java.io.Serializable {
    private java.lang.Long widgetLayoutId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "widget_layout_id", nullable = false, updatable = false)
    public java.lang.Long getWidgetLayoutId() {
        return widgetLayoutId;
    }

    public void setWidgetLayoutId(java.lang.Long widgetLayoutId) {
        this.widgetLayoutId = widgetLayoutId;
    }

    private java.lang.Long userWidgetId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "widget_id", nullable = false, updatable = false)
    public java.lang.Long getUserWidgetId() {
        return userWidgetId;
    }

    public void setUserWidgetId(java.lang.Long userWidgetId) {
        this.userWidgetId = userWidgetId;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntityPK that = ((org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntityPK) (o));
        return java.util.Objects.equals(widgetLayoutId, that.widgetLayoutId) && java.util.Objects.equals(userWidgetId, that.userWidgetId);
    }

    @java.lang.Override
    public int hashCode() {
        int result = (null != widgetLayoutId) ? widgetLayoutId.hashCode() : 0;
        result = (31 * result) + (userWidgetId != null ? userWidgetId.hashCode() : 0);
        return result;
    }
}