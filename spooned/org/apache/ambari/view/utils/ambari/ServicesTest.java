package org.apache.ambari.view.utils.ambari;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.*;
public class ServicesTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String HTTP_RM_URL1 = "http://c1.ambari.apache.org:8088";

    private static final java.lang.String HTTP_RM_URL2 = "http://c2.ambari.apache.org:8088";

    private static final java.lang.String HTTPS_RM_URL1 = "https://c1.ambari.apache.org:8088";

    private static final java.lang.String HTTPS_RM_URL2 = "https://c2.ambari.apache.org:8088";

    private static final java.lang.String RM_URL1_HOST = "c1.ambari.apache.org";

    private static final java.lang.String RM_URL2_HOST = "c2.ambari.apache.org";

    private static final java.lang.String RM_URL1_HOST_PORT = "c1.ambari.apache.org:8088";

    private static final java.lang.String RM_URL2_HOST_PORT = "c2.ambari.apache.org:8088";

    private static final java.lang.String RM_INFO_API_ENDPOINT = org.apache.ambari.view.utils.ambari.Services.RM_INFO_API_ENDPOINT;

    @org.junit.Test(expected = org.apache.ambari.view.utils.ambari.AmbariApiException.class)
    public void shouldCheckForEmptyATSUrlInCustomConfig() {
        org.apache.ambari.view.ViewContext viewContext = getViewContext(new java.util.HashMap<java.lang.String, java.lang.String>());
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        expect(ambariApi.isClusterAssociated()).andReturn(false);
        replay(viewContext);
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        services.getTimelineServerUrl();
    }

    @org.junit.Test
    public void shouldReturnATSUrlConfiguredInCustomMode() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("yarn.ats.url", org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1);
        org.apache.ambari.view.ViewContext viewContext = getViewContext(map);
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        expect(ambariApi.isClusterAssociated()).andReturn(false);
        replay(viewContext);
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        org.junit.Assert.assertEquals(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1, services.getTimelineServerUrl());
    }

    @org.junit.Test(expected = org.apache.ambari.view.utils.ambari.AmbariApiException.class)
    public void shouldThrowExceptionIfNoProtocolInCustomMode() {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("yarn.ats.url", org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL1_HOST_PORT);
        org.apache.ambari.view.ViewContext viewContext = getViewContext(map);
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        expect(ambariApi.isClusterAssociated()).andReturn(false);
        replay(viewContext);
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        services.getTimelineServerUrl();
    }

    @org.junit.Test
    public void shouldReturnATSUrlFromYarnSiteInClusteredMode() throws java.lang.Exception {
        org.apache.ambari.view.ViewContext viewContext = getViewContext(new java.util.HashMap<java.lang.String, java.lang.String>());
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        org.apache.ambari.view.cluster.Cluster cluster = createNiceMock(org.apache.ambari.view.cluster.Cluster.class);
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        expect(ambariApi.isClusterAssociated()).andReturn(true).anyTimes();
        setClusterExpectation(cluster, "HTTP_ONLY");
        expect(viewContext.getCluster()).andReturn(cluster).anyTimes();
        replayAll();
        org.junit.Assert.assertEquals(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1, services.getTimelineServerUrl());
        reset(cluster);
        setClusterExpectation(cluster, "HTTPS_ONLY");
        replay(cluster);
        org.junit.Assert.assertEquals(org.apache.ambari.view.utils.ambari.ServicesTest.HTTPS_RM_URL2, services.getTimelineServerUrl());
    }

    @org.junit.Test(expected = org.apache.ambari.view.utils.ambari.AmbariApiException.class)
    public void shouldCheckForEmptyYarnRMUrlInCustomConfig() {
        org.apache.ambari.view.ViewContext viewContext = getViewContext(new java.util.HashMap<java.lang.String, java.lang.String>());
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        expect(ambariApi.isClusterAssociated()).andReturn(false);
        replay(viewContext);
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        services.getRMUrl();
    }

    @org.junit.Test(expected = org.apache.ambari.view.utils.ambari.AmbariApiException.class)
    public void shouldCheckIfAllRMUrlsHaveProtocolInCustomConfig() {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("yarn.resourcemanager.url", (org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1 + ",") + org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL2_HOST_PORT);
        org.apache.ambari.view.ViewContext viewContext = getViewContext(map);
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        expect(ambariApi.isClusterAssociated()).andReturn(false);
        replay(viewContext);
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        services.getRMUrl();
    }

    @org.junit.Test
    public void shouldReturnUrlIfSingleIsConfiguredInCustomConfig() {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("yarn.resourcemanager.url", org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1);
        org.apache.ambari.view.ViewContext viewContext = getViewContext(map);
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        expect(ambariApi.isClusterAssociated()).andReturn(false);
        replay(viewContext);
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        org.junit.Assert.assertEquals(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1, services.getRMUrl());
    }

    @org.junit.Test
    public void shouldConnectToFirstUrlWhenMultipleRMUrlIsConfiguredInCustomConfig() throws java.io.IOException {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("yarn.resourcemanager.url", (org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1 + ", ") + org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL2);
        org.apache.ambari.view.ViewContext viewContext = getViewContext(map);
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        org.apache.ambari.view.URLStreamProvider urlStreamProvider = createNiceMock(org.apache.ambari.view.URLStreamProvider.class);
        java.io.InputStream inputStream = org.apache.commons.io.IOUtils.toInputStream("{\"clusterInfo\": {\"haState\": \"ACTIVE\"}}");
        expect(ambariApi.isClusterAssociated()).andReturn(false);
        expect(viewContext.getURLStreamProvider()).andReturn(urlStreamProvider);
        expect(urlStreamProvider.readFrom(eq(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1 + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andReturn(inputStream);
        replayAll();
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        org.junit.Assert.assertEquals(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1, services.getRMUrl());
    }

    @org.junit.Test
    public void shouldConnectToSecondUrlWhenTheFirstURLTimesOut() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("yarn.resourcemanager.url", (org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1 + ", ") + org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL2);
        org.apache.ambari.view.ViewContext viewContext = getViewContext(map);
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        org.apache.ambari.view.URLStreamProvider urlStreamProvider = createNiceMock(org.apache.ambari.view.URLStreamProvider.class);
        java.io.InputStream inputStream = org.apache.commons.io.IOUtils.toInputStream("{\"clusterInfo\": {\"haState\": \"ACTIVE\"}}");
        expect(ambariApi.isClusterAssociated()).andReturn(false);
        expect(viewContext.getURLStreamProvider()).andReturn(urlStreamProvider).anyTimes();
        expect(urlStreamProvider.readFrom(eq(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1 + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andThrow(new java.io.IOException());
        expect(urlStreamProvider.readFrom(eq(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL2 + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andReturn(inputStream);
        replayAll();
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        org.junit.Assert.assertEquals(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL2, services.getRMUrl());
    }

    @org.junit.Test(expected = org.apache.ambari.view.utils.ambari.AmbariApiException.class)
    public void shouldThrowExceptionWhenAllUrlCannotBeReached() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("yarn.resourcemanager.url", (org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1 + ", ") + org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL2);
        org.apache.ambari.view.ViewContext viewContext = getViewContext(map);
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        org.apache.ambari.view.URLStreamProvider urlStreamProvider = createNiceMock(org.apache.ambari.view.URLStreamProvider.class);
        expect(ambariApi.isClusterAssociated()).andReturn(false);
        expect(viewContext.getURLStreamProvider()).andReturn(urlStreamProvider).anyTimes();
        expect(urlStreamProvider.readFrom(eq(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1 + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andThrow(new java.io.IOException());
        expect(urlStreamProvider.readFrom(eq(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL2 + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andThrow(new java.io.IOException());
        replayAll();
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        services.getRMUrl();
    }

    @org.junit.Test
    public void shouldReturnActiveRMUrlWhenConnectingToStandby() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("yarn.resourcemanager.url", (org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1 + ", ") + org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL2);
        org.apache.ambari.view.ViewContext viewContext = getViewContext(map);
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        org.apache.ambari.view.URLStreamProvider urlStreamProvider = createNiceMock(org.apache.ambari.view.URLStreamProvider.class);
        java.io.InputStream inputStream = org.apache.commons.io.IOUtils.toInputStream("{\"clusterInfo\": {\"haState\": \"STANDBY\"}}");
        expect(ambariApi.isClusterAssociated()).andReturn(false);
        expect(viewContext.getURLStreamProvider()).andReturn(urlStreamProvider).anyTimes();
        expect(urlStreamProvider.readFrom(eq(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1 + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andReturn(inputStream);
        java.io.InputStream inputStreamActive = org.apache.commons.io.IOUtils.toInputStream("{\"clusterInfo\": {\"haState\": \"ACTIVE\"}}");
        expect(urlStreamProvider.readFrom(eq(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL2 + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andReturn(inputStreamActive);
        replayAll();
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        org.junit.Assert.assertEquals(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL2, services.getRMUrl());
        verify(urlStreamProvider);
    }

    @org.junit.Test
    public void shouldConnectToRMConfiguredInClusterMode() throws java.lang.Exception {
        org.apache.ambari.view.ViewContext viewContext = getViewContext(new java.util.HashMap<java.lang.String, java.lang.String>());
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        org.apache.ambari.view.cluster.Cluster cluster = createNiceMock(org.apache.ambari.view.cluster.Cluster.class);
        expect(ambariApi.isClusterAssociated()).andReturn(true).anyTimes();
        setClusterExpectation(cluster, "HTTP_ONLY");
        expect(viewContext.getCluster()).andReturn(cluster).anyTimes();
        replayAll();
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        org.junit.Assert.assertEquals(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1, services.getRMUrl());
        reset(cluster);
        setClusterExpectation(cluster, "HTTPS_ONLY");
        replay(cluster);
        org.junit.Assert.assertEquals(org.apache.ambari.view.utils.ambari.ServicesTest.HTTPS_RM_URL2, services.getRMUrl());
        reset(cluster);
        setClusterExpectation(cluster, "HTTPS_ONLY_XYZ");
        replay(cluster);
        org.junit.Assert.assertEquals(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1, services.getRMUrl());
    }

    @org.junit.Test
    public void shouldConnectToDefaultHostPortInClusterModeWhenWebaddressConfigIsEmpty() throws java.lang.Exception {
        org.apache.ambari.view.ViewContext viewContext = getViewContext(new java.util.HashMap<java.lang.String, java.lang.String>());
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        org.apache.ambari.view.cluster.Cluster cluster = createNiceMock(org.apache.ambari.view.cluster.Cluster.class);
        expect(ambariApi.isClusterAssociated()).andReturn(true).anyTimes();
        setClusterExpectationWithEmptyWebappConfig(cluster, "HTTP_ONLY");
        expect(viewContext.getCluster()).andReturn(cluster).anyTimes();
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        replayAll();
        org.junit.Assert.assertEquals(("http://" + org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL1_HOST) + ":8088", services.getRMUrl());
        reset(cluster);
        setClusterExpectationWithEmptyWebappConfig(cluster, "HTTPS_ONLY");
        replay(cluster);
        org.junit.Assert.assertEquals(("https://" + org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL1_HOST) + ":8090", services.getRMUrl());
    }

    @org.junit.Test
    public void shouldConnectToDefaultHostPortInClusterModeWithHAWhenWebaddressConfigIsEmpty() throws java.lang.Exception {
        org.apache.ambari.view.ViewContext viewContext = getViewContext(new java.util.HashMap<java.lang.String, java.lang.String>());
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        org.apache.ambari.view.cluster.Cluster cluster = createNiceMock(org.apache.ambari.view.cluster.Cluster.class);
        org.apache.ambari.view.URLStreamProvider urlStreamProvider = createNiceMock(org.apache.ambari.view.URLStreamProvider.class);
        expect(ambariApi.isClusterAssociated()).andReturn(true).anyTimes();
        setClusterExpectationInHAWithEmptyWebappConfig(cluster, "HTTP_ONLY");
        expect(viewContext.getCluster()).andReturn(cluster).anyTimes();
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        java.io.InputStream inputStream = org.apache.commons.io.IOUtils.toInputStream("{\"clusterInfo\": {\"haState\": \"ACTIVE\"}}");
        expect(viewContext.getURLStreamProvider()).andReturn(urlStreamProvider).anyTimes();
        expect(urlStreamProvider.readFrom(eq((("http://" + org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL1_HOST) + ":8088") + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andReturn(inputStream);
        replayAll();
        org.junit.Assert.assertEquals(("http://" + org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL1_HOST) + ":8088", services.getRMUrl());
        reset(cluster, urlStreamProvider);
        setClusterExpectationInHAWithEmptyWebappConfig(cluster, "HTTPS_ONLY");
        inputStream = org.apache.commons.io.IOUtils.toInputStream("{\"clusterInfo\": {\"haState\": \"ACTIVE\"}}");
        expect(urlStreamProvider.readFrom(eq((("https://" + org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL1_HOST) + ":8090") + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andReturn(inputStream);
        replay(cluster, urlStreamProvider);
        org.junit.Assert.assertEquals(("https://" + org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL1_HOST) + ":8090", services.getRMUrl());
    }

    @org.junit.Test
    public void shouldFetchRMUrlsWhileHAEnabledInClusterMode() throws java.lang.Exception {
        org.apache.ambari.view.ViewContext viewContext = getViewContext(new java.util.HashMap<java.lang.String, java.lang.String>());
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        org.apache.ambari.view.cluster.Cluster cluster = createNiceMock(org.apache.ambari.view.cluster.Cluster.class);
        org.apache.ambari.view.URLStreamProvider urlStreamProvider = createNiceMock(org.apache.ambari.view.URLStreamProvider.class);
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        java.io.InputStream inputStream = org.apache.commons.io.IOUtils.toInputStream("{\"clusterInfo\": {\"haState\": \"ACTIVE\"}}");
        expect(ambariApi.isClusterAssociated()).andReturn(true).anyTimes();
        setClusterExpectationInHA(cluster, "HTTP_ONLY");
        expect(viewContext.getCluster()).andReturn(cluster).anyTimes();
        expect(viewContext.getURLStreamProvider()).andReturn(urlStreamProvider).anyTimes();
        expect(urlStreamProvider.readFrom(eq(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1 + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andReturn(inputStream);
        replayAll();
        org.junit.Assert.assertEquals(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1, services.getRMUrl());
        reset(cluster, urlStreamProvider);
        setClusterExpectationInHA(cluster, "HTTP_ONLY");
        inputStream = org.apache.commons.io.IOUtils.toInputStream("{\"clusterInfo\": {\"haState\": \"ACTIVE\"}}");
        expect(urlStreamProvider.readFrom(eq(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL1 + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andThrow(new java.io.IOException());
        expect(urlStreamProvider.readFrom(eq(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL2 + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andReturn(inputStream);
        replay(cluster, urlStreamProvider);
        org.junit.Assert.assertEquals(org.apache.ambari.view.utils.ambari.ServicesTest.HTTP_RM_URL2, services.getRMUrl());
        reset(cluster, urlStreamProvider);
        setClusterExpectationInHA(cluster, "HTTPS_ONLY");
        inputStream = org.apache.commons.io.IOUtils.toInputStream("{\"clusterInfo\": {\"haState\": \"ACTIVE\"}}");
        expect(urlStreamProvider.readFrom(eq(org.apache.ambari.view.utils.ambari.ServicesTest.HTTPS_RM_URL1 + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andReturn(inputStream);
        replay(cluster, urlStreamProvider);
        org.junit.Assert.assertEquals(org.apache.ambari.view.utils.ambari.ServicesTest.HTTPS_RM_URL1, services.getRMUrl());
        reset(cluster, urlStreamProvider);
        setClusterExpectationInHA(cluster, "HTTPS_ONLY");
        inputStream = org.apache.commons.io.IOUtils.toInputStream("{\"clusterInfo\": {\"haState\": \"ACTIVE\"}}");
        expect(urlStreamProvider.readFrom(eq(org.apache.ambari.view.utils.ambari.ServicesTest.HTTPS_RM_URL1 + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andThrow(new java.io.IOException());
        expect(urlStreamProvider.readFrom(eq(org.apache.ambari.view.utils.ambari.ServicesTest.HTTPS_RM_URL2 + org.apache.ambari.view.utils.ambari.ServicesTest.RM_INFO_API_ENDPOINT), eq("GET"), anyString(), org.easymock.EasyMock.<java.util.Map<java.lang.String, java.lang.String>>anyObject())).andReturn(inputStream);
        replay(cluster, urlStreamProvider);
        org.junit.Assert.assertEquals(org.apache.ambari.view.utils.ambari.ServicesTest.HTTPS_RM_URL2, services.getRMUrl());
    }

    @org.junit.Test
    public void basicGetYARNProtocol() throws java.lang.Exception {
        org.apache.ambari.view.ViewContext viewContext = getViewContext(new java.util.HashMap<java.lang.String, java.lang.String>());
        org.apache.ambari.view.utils.ambari.AmbariApi ambariApi = createNiceMock(org.apache.ambari.view.utils.ambari.AmbariApi.class);
        org.apache.ambari.view.cluster.Cluster cluster = createNiceMock(org.apache.ambari.view.cluster.Cluster.class);
        expect(ambariApi.isClusterAssociated()).andReturn(true).anyTimes();
        setClusterExpectationWithEmptyWebappConfig(cluster, "HTTP_ONLY");
        expect(viewContext.getCluster()).andReturn(cluster).anyTimes();
        org.apache.ambari.view.utils.ambari.Services services = new org.apache.ambari.view.utils.ambari.Services(ambariApi, viewContext);
        replayAll();
        org.junit.Assert.assertEquals("http", services.getYARNProtocol());
    }

    private void setClusterExpectation(org.apache.ambari.view.cluster.Cluster cluster, java.lang.String httpPolicy) {
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.ha.enabled")).andReturn("false");
        expect(cluster.getConfigurationValue("yarn-site", "yarn.http.policy")).andReturn(httpPolicy);
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.webapp.address")).andReturn(org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL1_HOST_PORT);
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.webapp.https.address")).andReturn(org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL2_HOST_PORT);
        expect(cluster.getConfigurationValue("yarn-site", "yarn.timeline-service.webapp.address")).andReturn(org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL1_HOST_PORT);
        expect(cluster.getConfigurationValue("yarn-site", "yarn.timeline-service.webapp.https.address")).andReturn(org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL2_HOST_PORT);
    }

    private void setClusterExpectationInHA(org.apache.ambari.view.cluster.Cluster cluster, java.lang.String httpPolicy) {
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.ha.enabled")).andReturn("true");
        expect(cluster.getConfigurationValue("yarn-site", "yarn.http.policy")).andReturn(httpPolicy);
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.ha.rm-ids")).andReturn("rm1,rm2");
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.webapp.address.rm1")).andReturn(org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL1_HOST_PORT);
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.webapp.address.rm2")).andReturn(org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL2_HOST_PORT);
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.webapp.https.address.rm1")).andReturn(org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL1_HOST_PORT);
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.webapp.https.address.rm2")).andReturn(org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL2_HOST_PORT);
    }

    private void setClusterExpectationInHAWithEmptyWebappConfig(org.apache.ambari.view.cluster.Cluster cluster, java.lang.String httpPolicy) {
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.ha.enabled")).andReturn("true");
        expect(cluster.getConfigurationValue("yarn-site", "yarn.http.policy")).andReturn(httpPolicy);
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.ha.rm-ids")).andReturn("rm1,rm2");
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.hostname.rm1")).andReturn(org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL1_HOST);
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.hostname.rm2")).andReturn(org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL2_HOST);
    }

    private void setClusterExpectationWithEmptyWebappConfig(org.apache.ambari.view.cluster.Cluster cluster, java.lang.String httpPolicy) {
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.ha.enabled")).andReturn("false");
        expect(cluster.getConfigurationValue("yarn-site", "yarn.http.policy")).andReturn(httpPolicy);
        expect(cluster.getConfigurationValue("yarn-site", "yarn.resourcemanager.hostname")).andReturn(org.apache.ambari.view.utils.ambari.ServicesTest.RM_URL1_HOST);
    }

    private org.apache.ambari.view.ViewContext getViewContext(java.util.Map<java.lang.String, java.lang.String> map) {
        org.apache.ambari.view.ViewContext viewContextMock = createNiceMock(org.apache.ambari.view.ViewContext.class);
        expect(viewContextMock.getProperties()).andReturn(map);
        return viewContextMock;
    }
}