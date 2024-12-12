package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class SettingRequest {
    private final java.lang.String name;

    private final java.lang.String settingType;

    private final java.lang.String content;

    public SettingRequest(java.lang.String name, java.lang.String settingType, java.lang.String content) {
        this.name = name;
        this.settingType = settingType;
        this.content = content;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.SettingResourceProvider.NAME)
    public java.lang.String getName() {
        return name;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.SettingResourceProvider.SETTING_TYPE)
    public java.lang.String getSettingType() {
        return settingType;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.SettingResourceProvider.CONTENT)
    public java.lang.String getContent() {
        return content;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.SettingRequest other = ((org.apache.ambari.server.controller.SettingRequest) (o));
        return (java.util.Objects.equals(name, other.name) && java.util.Objects.equals(settingType, other.settingType)) && java.util.Objects.equals(content, other.content);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(name, settingType, content);
    }
}