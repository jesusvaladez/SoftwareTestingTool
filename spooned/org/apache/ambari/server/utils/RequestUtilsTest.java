package org.apache.ambari.server.utils;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
public class RequestUtilsTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String REMOTE_ADDRESS = "12.13.14.15";

    private static final java.lang.String REMOTE_ADDRESS_MULTIPLE = "12.13.14.15,12.13.14.16";

    @org.junit.Test
    public void testGetRemoteAddress() {
        javax.servlet.http.HttpServletRequest mockedRequest = createMock(javax.servlet.http.HttpServletRequest.class);
        EasyMock.expect(mockedRequest.getHeader("X-Forwarded-For")).andReturn(null);
        EasyMock.expect(mockedRequest.getHeader("Proxy-Client-IP")).andReturn("unknown");
        EasyMock.expect(mockedRequest.getHeader("WL-Proxy-Client-IP")).andReturn("");
        EasyMock.expect(mockedRequest.getHeader("HTTP_CLIENT_IP")).andReturn("unknown");
        EasyMock.expect(mockedRequest.getHeader("HTTP_X_FORWARDED_FOR")).andReturn(org.apache.ambari.server.utils.RequestUtilsTest.REMOTE_ADDRESS);
        replayAll();
        java.lang.String remoteAddress = org.apache.ambari.server.utils.RequestUtils.getRemoteAddress(mockedRequest);
        org.junit.Assert.assertEquals(org.apache.ambari.server.utils.RequestUtilsTest.REMOTE_ADDRESS, remoteAddress);
        verifyAll();
    }

    @org.junit.Test
    public void testGetMultipleRemoteAddress() {
        javax.servlet.http.HttpServletRequest mockedRequest = createMock(javax.servlet.http.HttpServletRequest.class);
        EasyMock.expect(mockedRequest.getHeader("X-Forwarded-For")).andReturn(null);
        EasyMock.expect(mockedRequest.getHeader("Proxy-Client-IP")).andReturn("unknown");
        EasyMock.expect(mockedRequest.getHeader("WL-Proxy-Client-IP")).andReturn("");
        EasyMock.expect(mockedRequest.getHeader("HTTP_CLIENT_IP")).andReturn("unknown");
        EasyMock.expect(mockedRequest.getHeader("HTTP_X_FORWARDED_FOR")).andReturn(org.apache.ambari.server.utils.RequestUtilsTest.REMOTE_ADDRESS_MULTIPLE);
        replayAll();
        java.lang.String remoteAddress = org.apache.ambari.server.utils.RequestUtils.getRemoteAddress(mockedRequest);
        org.junit.Assert.assertEquals(org.apache.ambari.server.utils.RequestUtilsTest.REMOTE_ADDRESS, remoteAddress);
        verifyAll();
    }

    @org.junit.Test
    public void testGetRemoteAddressFoundFirstHeader() {
        javax.servlet.http.HttpServletRequest mockedRequest = createMock(javax.servlet.http.HttpServletRequest.class);
        EasyMock.expect(mockedRequest.getHeader("X-Forwarded-For")).andReturn(org.apache.ambari.server.utils.RequestUtilsTest.REMOTE_ADDRESS);
        replayAll();
        java.lang.String remoteAddress = org.apache.ambari.server.utils.RequestUtils.getRemoteAddress(mockedRequest);
        org.junit.Assert.assertEquals(org.apache.ambari.server.utils.RequestUtilsTest.REMOTE_ADDRESS, remoteAddress);
        verifyAll();
    }

    @org.junit.Test
    public void testGetRemoteAddressWhenHeadersAreMissing() {
        javax.servlet.http.HttpServletRequest mockedRequest = createMock(javax.servlet.http.HttpServletRequest.class);
        EasyMock.expect(mockedRequest.getHeader(EasyMock.anyString())).andReturn(null).times(5);
        EasyMock.expect(mockedRequest.getRemoteAddr()).andReturn(org.apache.ambari.server.utils.RequestUtilsTest.REMOTE_ADDRESS);
        replayAll();
        java.lang.String remoteAddress = org.apache.ambari.server.utils.RequestUtils.getRemoteAddress(mockedRequest);
        org.junit.Assert.assertEquals(org.apache.ambari.server.utils.RequestUtilsTest.REMOTE_ADDRESS, remoteAddress);
        verifyAll();
    }

    @org.junit.Test
    public void testGetQueryStringParameters() {
        javax.servlet.http.HttpServletRequest request = createMock(javax.servlet.http.HttpServletRequest.class);
        EasyMock.expect(request.getQueryString()).andReturn(null).once();
        EasyMock.expect(request.getQueryString()).andReturn("").once();
        EasyMock.expect(request.getQueryString()).andReturn("p1=1").once();
        EasyMock.expect(request.getQueryString()).andReturn("p1=1&p2=2").once();
        EasyMock.expect(request.getQueryString()).andReturn("p1=1a&p2=2&p1=1b").once();
        replayAll();
        org.junit.Assert.assertNull(org.apache.ambari.server.utils.RequestUtils.getQueryStringParameters(request));
        org.junit.Assert.assertNull(org.apache.ambari.server.utils.RequestUtils.getQueryStringParameters(request));
        org.springframework.util.MultiValueMap<java.lang.String, java.lang.String> parameters;
        parameters = org.apache.ambari.server.utils.RequestUtils.getQueryStringParameters(request);
        org.junit.Assert.assertNotNull(parameters);
        org.junit.Assert.assertEquals(1, parameters.size());
        org.junit.Assert.assertNotNull(parameters.get("p1"));
        org.junit.Assert.assertEquals(1, parameters.get("p1").size());
        org.junit.Assert.assertEquals("1", parameters.get("p1").get(0));
        parameters = org.apache.ambari.server.utils.RequestUtils.getQueryStringParameters(request);
        org.junit.Assert.assertNotNull(parameters);
        org.junit.Assert.assertEquals(2, parameters.size());
        org.junit.Assert.assertNotNull(parameters.get("p1"));
        org.junit.Assert.assertEquals(1, parameters.get("p1").size());
        org.junit.Assert.assertEquals("1", parameters.get("p1").get(0));
        org.junit.Assert.assertNotNull(parameters.get("p2"));
        org.junit.Assert.assertEquals(1, parameters.get("p2").size());
        org.junit.Assert.assertEquals("2", parameters.get("p2").get(0));
        parameters = org.apache.ambari.server.utils.RequestUtils.getQueryStringParameters(request);
        org.junit.Assert.assertNotNull(parameters);
        org.junit.Assert.assertEquals(2, parameters.size());
        org.junit.Assert.assertNotNull(parameters.get("p1"));
        org.junit.Assert.assertEquals(2, parameters.get("p1").size());
        org.junit.Assert.assertEquals("1a", parameters.get("p1").get(0));
        org.junit.Assert.assertEquals("1b", parameters.get("p1").get(1));
        org.junit.Assert.assertNotNull(parameters.get("p2"));
        org.junit.Assert.assertEquals(1, parameters.get("p2").size());
        org.junit.Assert.assertEquals("2", parameters.get("p2").get(0));
        verifyAll();
    }

    @org.junit.Test
    public void testGetQueryStringParameterValues() {
        javax.servlet.http.HttpServletRequest request = createMock(javax.servlet.http.HttpServletRequest.class);
        EasyMock.expect(request.getQueryString()).andReturn(null).once();
        EasyMock.expect(request.getQueryString()).andReturn("").once();
        EasyMock.expect(request.getQueryString()).andReturn("p1=1").once();
        EasyMock.expect(request.getQueryString()).andReturn("p1=1&p2=2").once();
        EasyMock.expect(request.getQueryString()).andReturn("p1=1a&p2=2&p1=1b").once();
        replayAll();
        org.junit.Assert.assertNull(org.apache.ambari.server.utils.RequestUtils.getQueryStringParameterValues(request, "p1"));
        org.junit.Assert.assertNull(org.apache.ambari.server.utils.RequestUtils.getQueryStringParameterValues(request, "p1"));
        java.util.List<java.lang.String> parameterValues;
        parameterValues = org.apache.ambari.server.utils.RequestUtils.getQueryStringParameterValues(request, "p1");
        org.junit.Assert.assertNotNull(parameterValues);
        org.junit.Assert.assertEquals(1, parameterValues.size());
        org.junit.Assert.assertEquals("1", parameterValues.get(0));
        parameterValues = org.apache.ambari.server.utils.RequestUtils.getQueryStringParameterValues(request, "p2");
        org.junit.Assert.assertNotNull(parameterValues);
        org.junit.Assert.assertEquals(1, parameterValues.size());
        org.junit.Assert.assertEquals("2", parameterValues.get(0));
        parameterValues = org.apache.ambari.server.utils.RequestUtils.getQueryStringParameterValues(request, "p1");
        org.junit.Assert.assertNotNull(parameterValues);
        org.junit.Assert.assertEquals(2, parameterValues.size());
        org.junit.Assert.assertEquals("1a", parameterValues.get(0));
        org.junit.Assert.assertEquals("1b", parameterValues.get(1));
        verifyAll();
    }

    @org.junit.Test
    public void testGetQueryStringParameterValue() {
        javax.servlet.http.HttpServletRequest request = createMock(javax.servlet.http.HttpServletRequest.class);
        EasyMock.expect(request.getQueryString()).andReturn(null).once();
        EasyMock.expect(request.getQueryString()).andReturn("").once();
        EasyMock.expect(request.getQueryString()).andReturn("p1=1").once();
        EasyMock.expect(request.getQueryString()).andReturn("p1=1&p2=2").once();
        EasyMock.expect(request.getQueryString()).andReturn("p1=1a&p2=2&p1=1b").once();
        replayAll();
        org.junit.Assert.assertNull(org.apache.ambari.server.utils.RequestUtils.getQueryStringParameterValue(request, "p1"));
        org.junit.Assert.assertNull(org.apache.ambari.server.utils.RequestUtils.getQueryStringParameterValue(request, "p1"));
        java.lang.String parameterValue;
        parameterValue = org.apache.ambari.server.utils.RequestUtils.getQueryStringParameterValue(request, "p1");
        org.junit.Assert.assertEquals("1", parameterValue);
        parameterValue = org.apache.ambari.server.utils.RequestUtils.getQueryStringParameterValue(request, "p2");
        org.junit.Assert.assertEquals("2", parameterValue);
        parameterValue = org.apache.ambari.server.utils.RequestUtils.getQueryStringParameterValue(request, "p1");
        org.junit.Assert.assertEquals("1a", parameterValue);
        verifyAll();
    }
}