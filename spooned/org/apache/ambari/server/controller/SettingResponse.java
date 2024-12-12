package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class SettingResponse {
    private final java.lang.String name;

    private final java.lang.String settingType;

    private final java.lang.String content;

    private final java.lang.String updatedBy;

    private final long updateTimestamp;

    public SettingResponse(java.lang.String name, java.lang.String settingType, java.lang.String content, java.lang.String updatedBy, long updateTimestamp) {
        this.name = name;
        this.settingType = settingType;
        this.content = content;
        this.updatedBy = updatedBy;
        this.updateTimestamp = updateTimestamp;
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

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.SettingResourceProvider.UPDATED_BY)
    public java.lang.String getUpdatedBy() {
        return updatedBy;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.SettingResourceProvider.UPDATE_TIMESTAMP)
    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.SettingResponse other = ((org.apache.ambari.server.controller.SettingResponse) (o));
        return (((java.util.Objects.equals(name, other.name) && java.util.Objects.equals(settingType, other.settingType)) && java.util.Objects.equals(content, other.content)) && java.util.Objects.equals(updatedBy, other.updatedBy)) && (updateTimestamp == other.updateTimestamp);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(name, settingType, content, updatedBy, updateTimestamp);
    }

    public interface SettingResponseWrapper extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.SettingResourceProvider.RESPONSE_KEY)
        @java.lang.SuppressWarnings("unused")
        org.apache.ambari.server.controller.SettingResponse getSettingResponse();
    }
}