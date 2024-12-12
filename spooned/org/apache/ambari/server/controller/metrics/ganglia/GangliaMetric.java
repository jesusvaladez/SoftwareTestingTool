package org.apache.ambari.server.controller.metrics.ganglia;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
public class GangliaMetric {
    private java.lang.String ds_name;

    private java.lang.String cluster_name;

    private java.lang.String graph_type;

    private java.lang.String host_name;

    private java.lang.String metric_name;

    private java.lang.Number[][] datapoints;

    private static final java.util.Set<java.lang.String> PERCENTAGE_METRIC;

    static {
        java.util.Set<java.lang.String> temp = new java.util.HashSet<>();
        temp.add("cpu_wio");
        temp.add("cpu_idle");
        temp.add("cpu_nice");
        temp.add("cpu_aidle");
        temp.add("cpu_system");
        temp.add("cpu_user");
        PERCENTAGE_METRIC = java.util.Collections.unmodifiableSet(temp);
    }

    public java.lang.String getDs_name() {
        return ds_name;
    }

    public void setDs_name(java.lang.String ds_name) {
        this.ds_name = ds_name;
    }

    public java.lang.String getCluster_name() {
        return cluster_name;
    }

    public void setCluster_name(java.lang.String cluster_name) {
        this.cluster_name = cluster_name;
    }

    public java.lang.String getGraph_type() {
        return graph_type;
    }

    public void setGraph_type(java.lang.String graph_type) {
        this.graph_type = graph_type;
    }

    public java.lang.String getHost_name() {
        return host_name;
    }

    public void setHost_name(java.lang.String host_name) {
        this.host_name = host_name;
    }

    public java.lang.String getMetric_name() {
        return metric_name;
    }

    public void setMetric_name(java.lang.String metric_name) {
        this.metric_name = metric_name;
    }

    public java.lang.Number[][] getDatapoints() {
        return datapoints;
    }

    public void setDatapoints(java.lang.Number[][] datapoints) {
        this.datapoints = datapoints;
    }

    public void setDatapointsFromList(java.util.List<org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric> listTemporalMetrics) {
        java.lang.Number[][] datapointsArray = new java.lang.Number[listTemporalMetrics.size()][2];
        int cnt = 0;
        if (org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.PERCENTAGE_METRIC.contains(metric_name)) {
            int firstIndex = 0;
            int lastIndex = listTemporalMetrics.size() - 1;
            for (int i = firstIndex; i <= lastIndex; ++i) {
                org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric m = listTemporalMetrics.get(i);
                java.lang.Number val = m.getValue();
                if (100.0 >= val.doubleValue()) {
                    datapointsArray[cnt][0] = val;
                    datapointsArray[cnt][1] = m.getTime();
                    cnt++;
                }
            }
        } else {
            int firstIndex = 0;
            int lastIndex = listTemporalMetrics.size() - 1;
            for (int i = firstIndex; i <= lastIndex; ++i) {
                org.apache.ambari.server.controller.metrics.ganglia.GangliaMetric.TemporalMetric m = listTemporalMetrics.get(i);
                datapointsArray[i][0] = m.getValue();
                datapointsArray[i][1] = m.getTime();
                cnt++;
            }
        }
        this.datapoints = new java.lang.Number[cnt][2];
        for (int i = 0; i < this.datapoints.length; i++) {
            this.datapoints[i][0] = datapointsArray[i][0];
            this.datapoints[i][1] = datapointsArray[i][1];
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
        stringBuilder.append("\n");
        stringBuilder.append("name=");
        stringBuilder.append(ds_name);
        stringBuilder.append("\n");
        stringBuilder.append("cluster name=");
        stringBuilder.append(cluster_name);
        stringBuilder.append("\n");
        stringBuilder.append("graph type=");
        stringBuilder.append(graph_type);
        stringBuilder.append("\n");
        stringBuilder.append("host name=");
        stringBuilder.append(host_name);
        stringBuilder.append("\n");
        stringBuilder.append("api name=");
        stringBuilder.append(metric_name);
        stringBuilder.append("\n");
        stringBuilder.append("datapoints (value/timestamp):");
        stringBuilder.append("\n");
        boolean first = true;
        stringBuilder.append("[");
        for (java.lang.Number[] m : datapoints) {
            if (!first) {
                stringBuilder.append(",");
            }
            stringBuilder.append("[");
            stringBuilder.append(m[0]);
            stringBuilder.append(",");
            stringBuilder.append(m[1].longValue());
            stringBuilder.append("]");
            first = false;
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public static class TemporalMetric {
        private java.lang.Number m_value;

        private java.lang.Number m_time;

        private boolean valid;

        public boolean isValid() {
            return valid;
        }

        public TemporalMetric(java.lang.String value, java.lang.Number time) {
            valid = true;
            try {
                m_value = convertToNumber(value);
            } catch (java.lang.NumberFormatException e) {
                valid = false;
            }
            m_time = time;
        }

        public java.lang.Number getValue() {
            return m_value;
        }

        public java.lang.Number getTime() {
            return m_time;
        }

        private java.lang.Number convertToNumber(java.lang.String s) throws java.lang.NumberFormatException {
            java.lang.Number res;
            if (s.contains(".")) {
                java.lang.Double d = java.lang.Double.parseDouble(s);
                if (d.isNaN() || d.isInfinite()) {
                    throw new java.lang.NumberFormatException(s);
                } else {
                    res = d;
                }
            } else {
                res = java.lang.Long.parseLong(s);
            }
            return res;
        }
    }
}