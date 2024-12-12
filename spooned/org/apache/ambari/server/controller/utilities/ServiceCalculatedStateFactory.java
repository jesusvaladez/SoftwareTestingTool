package org.apache.ambari.server.controller.utilities;
public class ServiceCalculatedStateFactory {
    private static final org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState DEFAULT_SERVICE_CALCULATED_STATE = new org.apache.ambari.server.controller.utilities.state.DefaultServiceCalculatedState();

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.utilities.ServiceCalculatedStateFactory.class);

    private static final java.util.Map<org.apache.ambari.server.state.Service.Type, org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState> serviceStateProviders = new java.util.concurrent.ConcurrentHashMap<>();

    public static org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState getServiceStateProvider(java.lang.String service) {
        org.apache.ambari.server.controller.utilities.state.ServiceCalculatedState suggestedServiceProvider;
        org.apache.ambari.server.state.Service.Type serviceType = null;
        try {
            serviceType = org.apache.ambari.server.state.Service.Type.valueOf(service);
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.utilities.ServiceCalculatedStateFactory.LOG.debug("Could not parse service name \"{}\", will use default state provider", service);
        }
        if (serviceType == null) {
            return org.apache.ambari.server.controller.utilities.ServiceCalculatedStateFactory.DEFAULT_SERVICE_CALCULATED_STATE;
        }
        if (org.apache.ambari.server.controller.utilities.ServiceCalculatedStateFactory.serviceStateProviders.containsKey(serviceType)) {
            suggestedServiceProvider = org.apache.ambari.server.controller.utilities.ServiceCalculatedStateFactory.serviceStateProviders.get(serviceType);
        } else {
            switch (serviceType) {
                case HDFS :
                    suggestedServiceProvider = new org.apache.ambari.server.controller.utilities.state.HDFSServiceCalculatedState();
                    break;
                case FLUME :
                    suggestedServiceProvider = new org.apache.ambari.server.controller.utilities.state.FlumeServiceCalculatedState();
                    break;
                case OOZIE :
                    suggestedServiceProvider = new org.apache.ambari.server.controller.utilities.state.OozieServiceCalculatedState();
                    break;
                case YARN :
                    suggestedServiceProvider = new org.apache.ambari.server.controller.utilities.state.YARNServiceCalculatedState();
                    break;
                case HIVE :
                    suggestedServiceProvider = new org.apache.ambari.server.controller.utilities.state.HiveServiceCalculatedState();
                    break;
                case HBASE :
                    suggestedServiceProvider = new org.apache.ambari.server.controller.utilities.state.HBaseServiceCalculatedState();
                    break;
                default :
                    suggestedServiceProvider = org.apache.ambari.server.controller.utilities.ServiceCalculatedStateFactory.DEFAULT_SERVICE_CALCULATED_STATE;
                    break;
            }
            org.apache.ambari.server.controller.utilities.ServiceCalculatedStateFactory.serviceStateProviders.put(serviceType, suggestedServiceProvider);
        }
        return suggestedServiceProvider;
    }
}