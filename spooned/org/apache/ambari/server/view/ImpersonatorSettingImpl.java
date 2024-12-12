package org.apache.ambari.server.view;
public class ImpersonatorSettingImpl implements org.apache.ambari.view.ImpersonatorSetting {
    private java.lang.String doAsParamName;

    private java.lang.String username;

    public static final java.lang.String DEFAULT_DO_AS_PARAM = "doAs";

    public ImpersonatorSettingImpl(org.apache.ambari.view.ViewContext context) {
        this.username = context.getUsername();
        this.doAsParamName = org.apache.ambari.server.view.ImpersonatorSettingImpl.DEFAULT_DO_AS_PARAM;
    }

    public ImpersonatorSettingImpl(org.apache.ambari.view.ViewContext context, java.lang.String doAsParamName) {
        this.username = context.getUsername();
        this.doAsParamName = doAsParamName;
    }

    public ImpersonatorSettingImpl(java.lang.String username, java.lang.String doAsParamName) {
        this.username = username;
        this.doAsParamName = doAsParamName;
    }

    @java.lang.Override
    public java.lang.String getDoAsParamName() {
        return this.doAsParamName;
    }

    @java.lang.Override
    public java.lang.String getUsername() {
        return this.username;
    }
}