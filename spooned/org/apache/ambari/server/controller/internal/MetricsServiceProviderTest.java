package org.apache.ambari.server.controller.internal;
import static org.mockito.Mockito.mock;
public class MetricsServiceProviderTest {
    private static class TestMetricProviderModule extends org.apache.ambari.server.controller.internal.AbstractProviderModule {
        org.apache.ambari.server.controller.spi.ResourceProvider clusterResourceProvider = Mockito.mock(org.apache.ambari.server.controller.internal.ClusterResourceProvider.class);

        org.apache.ambari.server.controller.spi.ResourceProvider hostCompResourceProvider = Mockito.mock(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.class);

        @java.lang.Override
        protected org.apache.ambari.server.controller.spi.ResourceProvider createResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type) {
            if (type == org.apache.ambari.server.controller.spi.Resource.Type.Cluster)
                return clusterResourceProvider;
            else if (type == org.apache.ambari.server.controller.spi.Resource.Type.HostComponent)
                return hostCompResourceProvider;

            return null;
        }
    }
}