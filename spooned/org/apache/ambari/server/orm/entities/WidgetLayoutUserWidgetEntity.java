package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntityPK.class)
@javax.persistence.Entity
@javax.persistence.Table(name = "widget_layout_user_widget")
public class WidgetLayoutUserWidgetEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "widget_layout_id", nullable = false, updatable = false, insertable = false)
    private java.lang.Long widgetLayoutId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "widget_id", nullable = false, updatable = false, insertable = false)
    private java.lang.Long userWidgetId;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "widget_layout_id", referencedColumnName = "id")
    private org.apache.ambari.server.orm.entities.WidgetLayoutEntity widgetLayout;

    @javax.persistence.ManyToOne(cascade = { javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.REFRESH })
    @javax.persistence.JoinColumn(name = "widget_id", referencedColumnName = "id")
    private org.apache.ambari.server.orm.entities.WidgetEntity widget;

    @javax.persistence.Column(name = "widget_order")
    private java.lang.Integer widgetOrder;

    public java.lang.Long getWidgetLayoutId() {
        return widgetLayoutId;
    }

    public void setWidgetLayoutId(java.lang.Long widgetLayoutId) {
        this.widgetLayoutId = widgetLayoutId;
    }

    public java.lang.Long getUserWidgetId() {
        return userWidgetId;
    }

    public void setUserWidgetId(java.lang.Long userWidgetId) {
        this.userWidgetId = userWidgetId;
    }

    public org.apache.ambari.server.orm.entities.WidgetLayoutEntity getWidgetLayout() {
        return widgetLayout;
    }

    public void setWidgetLayout(org.apache.ambari.server.orm.entities.WidgetLayoutEntity widgetLayout) {
        this.widgetLayout = widgetLayout;
    }

    public org.apache.ambari.server.orm.entities.WidgetEntity getWidget() {
        return widget;
    }

    public void setWidget(org.apache.ambari.server.orm.entities.WidgetEntity widget) {
        this.widget = widget;
    }

    public java.lang.Integer getWidgetOrder() {
        return widgetOrder;
    }

    public void setWidgetOrder(java.lang.Integer widgetOrder) {
        this.widgetOrder = widgetOrder;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity that = ((org.apache.ambari.server.orm.entities.WidgetLayoutUserWidgetEntity) (o));
        return widgetLayout.equals(that.widgetLayout) && widget.equals(that.widget);
    }

    @java.lang.Override
    public int hashCode() {
        int result = (null != widgetLayout) ? widgetLayout.hashCode() : 0;
        result = (31 * result) + (widget != null ? widget.hashCode() : 0);
        return result;
    }
}