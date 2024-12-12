package org.apache.ambari.server.controller.metrics;
class MetricsAveragePerSecondDownsampling extends org.apache.ambari.server.controller.metrics.MetricsDownsamplingMethod {
    class Accumulo {
        public long ts;

        public java.lang.Double val;

        public Accumulo(long t, java.lang.Double v) {
            this.ts = t;
            this.val = v;
        }
    }

    class OutOfBandAccumuloFilterList<T> extends java.util.ArrayList<org.apache.ambari.server.controller.metrics.MetricsAveragePerSecondDownsampling.Accumulo> {
        org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo;

        OutOfBandAccumuloFilterList(org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
            this.temporalInfo = temporalInfo;
        }

        @java.lang.Override
        public boolean add(org.apache.ambari.server.controller.metrics.MetricsAveragePerSecondDownsampling.Accumulo accumulo) {
            long ts = accumulo.ts;
            if (ts < 9999999999L) {
                ts = ts * 1000;
            }
            if (isWithinTemporalQueryRange(ts, temporalInfo)) {
                return super.add(accumulo);
            }
            return false;
        }
    }

    @java.lang.Override
    public java.lang.Number[][] reportMetricData(org.apache.hadoop.metrics2.sink.timeline.TimelineMetric metricData, org.apache.ambari.server.controller.metrics.MetricsDataTransferMethod dataTransferMethod, org.apache.ambari.server.controller.spi.TemporalInfo temporalInfo) {
        org.apache.ambari.server.controller.metrics.MetricsAveragePerSecondDownsampling.OutOfBandAccumuloFilterList<org.apache.ambari.server.controller.metrics.MetricsAveragePerSecondDownsampling.Accumulo> cache = new org.apache.ambari.server.controller.metrics.MetricsAveragePerSecondDownsampling.OutOfBandAccumuloFilterList<>(temporalInfo);
        final java.util.Iterator<java.util.Map.Entry<java.lang.Long, java.lang.Double>> ci = metricData.getMetricValues().entrySet().iterator();
        java.util.Map.Entry<java.lang.Long, java.lang.Double> e0 = null;
        while (ci.hasNext()) {
            e0 = ci.next();
            if (e0.getValue() == null) {
                cache.add(new org.apache.ambari.server.controller.metrics.MetricsAveragePerSecondDownsampling.Accumulo(e0.getKey() / 1000, null));
            } else {
                break;
            }
        } 
        if (e0 != null) {
            long t0 = e0.getKey() / 1000;
            java.lang.Double s0 = e0.getValue();
            int nSamples = 1;
            boolean lastNonNullEntryAdded = false;
            while (ci.hasNext()) {
                e0 = ci.next();
                if (e0.getValue() == null) {
                    if (!lastNonNullEntryAdded) {
                        cache.add(new org.apache.ambari.server.controller.metrics.MetricsAveragePerSecondDownsampling.Accumulo(t0, dataTransferMethod.getData(s0 / nSamples)));
                        lastNonNullEntryAdded = true;
                    }
                    cache.add(new org.apache.ambari.server.controller.metrics.MetricsAveragePerSecondDownsampling.Accumulo(e0.getKey() / 1000, null));
                    continue;
                }
                long t = e0.getKey() / 1000;
                if (t != t0) {
                    cache.add(new org.apache.ambari.server.controller.metrics.MetricsAveragePerSecondDownsampling.Accumulo(t0, dataTransferMethod.getData(s0 / nSamples)));
                    t0 = t;
                    s0 = e0.getValue();
                    nSamples = 1;
                } else {
                    s0 += e0.getValue();
                    nSamples++;
                }
            } 
            if (!lastNonNullEntryAdded) {
                cache.add(new org.apache.ambari.server.controller.metrics.MetricsAveragePerSecondDownsampling.Accumulo(t0, dataTransferMethod.getData(s0 / nSamples)));
            }
        }
        java.lang.Number[][] datapointsArray = new java.lang.Number[cache.size()][2];
        int cnt = 0;
        for (org.apache.ambari.server.controller.metrics.MetricsAveragePerSecondDownsampling.Accumulo e : cache) {
            datapointsArray[cnt][0] = e.val;
            datapointsArray[cnt][1] = e.ts;
            cnt++;
        }
        return datapointsArray;
    }
}