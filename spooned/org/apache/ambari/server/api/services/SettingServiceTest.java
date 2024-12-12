package org.apache.ambari.server.api.services;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class SettingServiceTest extends org.apache.ambari.server.api.services.BaseServiceTest {
    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> getTestInvocations() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation> listInvocations = new java.util.ArrayList<>();
        org.apache.ambari.server.api.services.SettingService settingService = new org.apache.ambari.server.api.services.SettingServiceTest.TestSettingService("settingName");
        java.lang.reflect.Method m = settingService.getClass().getMethod("getSetting", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        java.lang.Object[] args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo(), "settingName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, settingService, m, args, null));
        settingService = new org.apache.ambari.server.api.services.SettingServiceTest.TestSettingService(null);
        m = settingService.getClass().getMethod("getSettings", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ null, getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.GET, settingService, m, args, null));
        settingService = new org.apache.ambari.server.api.services.SettingServiceTest.TestSettingService(null);
        m = settingService.getClass().getMethod("createSetting", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo() };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.POST, settingService, m, args, "body"));
        settingService = new org.apache.ambari.server.api.services.SettingServiceTest.TestSettingService("settingName");
        m = settingService.getClass().getMethod("updateSetting", java.lang.String.class, javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ "body", getHttpHeaders(), getUriInfo(), "settingName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.PUT, settingService, m, args, "body"));
        settingService = new org.apache.ambari.server.api.services.SettingServiceTest.TestSettingService("settingName");
        m = settingService.getClass().getMethod("deleteSetting", javax.ws.rs.core.HttpHeaders.class, javax.ws.rs.core.UriInfo.class, java.lang.String.class);
        args = new java.lang.Object[]{ getHttpHeaders(), getUriInfo(), "settingName" };
        listInvocations.add(new org.apache.ambari.server.api.services.BaseServiceTest.ServiceTestInvocation(org.apache.ambari.server.api.services.Request.Type.DELETE, settingService, m, args, null));
        return listInvocations;
    }

    private class TestSettingService extends org.apache.ambari.server.api.services.SettingService {
        private java.lang.String settingName;

        private TestSettingService(java.lang.String settingName) {
            this.settingName = settingName;
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.resources.ResourceInstance createSettingResource(java.lang.String settingName) {
            org.junit.Assert.assertEquals(this.settingName, settingName);
            return getTestResource();
        }

        @java.lang.Override
        org.apache.ambari.server.api.services.RequestFactory getRequestFactory() {
            return getTestRequestFactory();
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.services.parsers.RequestBodyParser getBodyParser() {
            return getTestBodyParser();
        }

        @java.lang.Override
        protected org.apache.ambari.server.api.services.serializers.ResultSerializer getResultSerializer() {
            return getTestResultSerializer();
        }
    }
}