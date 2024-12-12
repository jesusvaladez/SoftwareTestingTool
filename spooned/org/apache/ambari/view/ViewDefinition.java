package org.apache.ambari.view;
public interface ViewDefinition {
    public java.lang.String getViewName();

    public java.lang.String getLabel();

    public java.lang.String getDescription();

    public java.lang.String getVersion();

    public java.lang.String getBuild();

    public java.lang.String getMask();

    public org.apache.ambari.view.ViewDefinition.ViewStatus getStatus();

    public java.lang.String getStatusDetail();

    public enum ViewStatus {

        PENDING,
        DEPLOYING,
        DEPLOYED,
        ERROR;}
}