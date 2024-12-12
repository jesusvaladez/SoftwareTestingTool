package org.apache.ambari.server.controller.metrics.timeline;
import javax.ws.rs.HttpMethod;
import org.apache.hadoop.metrics2.sink.timeline.Precision;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetric;
import org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
public class MetricsRequestHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.class);

    private static final org.codehaus.jackson.map.ObjectMapper mapper;

    private static final org.codehaus.jackson.map.ObjectReader timelineObjectReader;

    private final org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider;

    static {
        mapper = new org.codehaus.jackson.map.ObjectMapper();
        org.codehaus.jackson.map.AnnotationIntrospector introspector = new org.codehaus.jackson.xc.JaxbAnnotationIntrospector();
        mapper.setAnnotationIntrospector(introspector);
        mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        timelineObjectReader = mapper.reader(org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics.class);
    }

    public MetricsRequestHelper(org.apache.ambari.server.controller.internal.URLStreamProvider streamProvider) {
        this.streamProvider = streamProvider;
    }

    public org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics fetchTimelineMetrics(org.apache.http.client.utils.URIBuilder uriBuilder, java.lang.Long startTime, java.lang.Long endTime) throws java.io.IOException {
        org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.debug("Metrics request url = {}", uriBuilder);
        java.io.BufferedReader reader = null;
        org.apache.hadoop.metrics2.sink.timeline.TimelineMetrics timelineMetrics = null;
        try {
            java.net.HttpURLConnection connection = streamProvider.processURL(uriBuilder.toString(), HttpMethod.GET, ((java.lang.String) (null)), java.util.Collections.emptyMap());
            if (!checkConnectionForPrecisionException(connection)) {
                java.lang.String higherPrecision = getHigherPrecision(uriBuilder, startTime, endTime);
                if (higherPrecision != null) {
                    org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.debug("Requesting metrics with higher precision : {}", higherPrecision);
                    uriBuilder.setParameter("precision", higherPrecision);
                    java.lang.String newSpec = uriBuilder.toString();
                    connection = streamProvider.processURL(newSpec, HttpMethod.GET, ((java.lang.String) (null)), java.util.Collections.<java.lang.String, java.util.List<java.lang.String>>emptyMap());
                    if (!checkConnectionForPrecisionException(connection)) {
                        throw new java.io.IOException("Encountered Precision exception : Higher precision request also failed.");
                    }
                } else {
                    throw new java.io.IOException("Encountered Precision exception : Unable to request higher precision");
                }
            }
            java.io.InputStream inputStream = connection.getInputStream();
            reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream));
            timelineMetrics = org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.timelineObjectReader.readValue(reader);
            if (org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.isTraceEnabled()) {
                for (org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metric : timelineMetrics.getMetrics()) {
                    org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.trace("metric: {}, size = {}, host = {}, app = {}, instance = {}, startTime = {}", metric.getMetricName(), metric.getMetricValues().size(), metric.getHostName(), metric.getAppId(), metric.getInstanceId(), new java.util.Date(metric.getStartTime()));
                }
            }
        } catch (java.io.IOException io) {
            java.lang.String errorMsg = "Error getting timeline metrics : " + io.getMessage();
            org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.error(errorMsg);
            if (org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.debug(errorMsg, io);
            }
            if ((io instanceof java.net.SocketTimeoutException) || (io instanceof java.net.ConnectException)) {
                errorMsg = "Cannot connect to collector: SocketTimeoutException for " + uriBuilder.getHost();
                org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.error(errorMsg);
                throw io;
            }
        } catch (java.net.URISyntaxException e) {
            java.lang.String errorMsg = "Error getting timeline metrics : " + e.getMessage();
            org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.error(errorMsg);
            if (org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.debug(errorMsg, e);
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (java.io.IOException e) {
                    if (org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.isWarnEnabled()) {
                        if (org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.isDebugEnabled()) {
                            org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.warn("Unable to close http input stream : spec=" + uriBuilder, e);
                        } else {
                            org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.warn("Unable to close http input stream : spec=" + uriBuilder);
                        }
                    }
                }
            }
        }
        return timelineMetrics;
    }

    private boolean checkConnectionForPrecisionException(java.net.HttpURLConnection connection) throws java.io.IOException, java.net.URISyntaxException {
        if ((connection != null) && (connection.getResponseCode() == org.apache.http.HttpStatus.SC_BAD_REQUEST)) {
            java.io.InputStream errorStream = connection.getErrorStream();
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(errorStream));
            java.lang.String errorMessage = reader.readLine();
            if ((errorMessage != null) && errorMessage.contains("PrecisionLimitExceededException")) {
                org.apache.ambari.server.controller.metrics.timeline.MetricsRequestHelper.LOG.debug("Encountered Precision exception while requesting metrics : {}", errorMessage);
                return false;
            } else {
                throw new java.io.IOException(errorMessage);
            }
        }
        return true;
    }

    private java.lang.String getHigherPrecision(org.apache.http.client.utils.URIBuilder uriBuilder, java.lang.Long startTime, java.lang.Long endTime) throws java.net.URISyntaxException {
        org.apache.hadoop.metrics2.sink.timeline.Precision currentPrecision = null;
        java.util.List<org.apache.http.NameValuePair> queryParams = uriBuilder.getQueryParams();
        for (java.util.Iterator<org.apache.http.NameValuePair> it = queryParams.iterator(); it.hasNext();) {
            org.apache.http.NameValuePair nvp = it.next();
            if (nvp.getName().equals("precision")) {
                currentPrecision = org.apache.hadoop.metrics2.sink.timeline.Precision.getPrecision(nvp.getValue());
            }
        }
        if (((currentPrecision == null) && (startTime != null)) && (endTime != null)) {
            currentPrecision = org.apache.hadoop.metrics2.sink.timeline.Precision.getPrecision(startTime, endTime);
        }
        org.apache.hadoop.metrics2.sink.timeline.Precision higherPrecision = org.apache.hadoop.metrics2.sink.timeline.Precision.getHigherPrecision(currentPrecision);
        return higherPrecision != null ? higherPrecision.toString() : null;
    }
}