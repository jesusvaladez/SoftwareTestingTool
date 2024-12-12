package org.apache.ambari.server.controller.utilities;
public class ClusterControllerHelper {
    private static java.lang.String PROVIDER_MODULE_CLASS = java.lang.System.getProperty("provider.module.class", "org.apache.ambari.server.controller.internal.DefaultProviderModule");

    private static org.apache.ambari.server.controller.spi.ClusterController controller;

    public static synchronized org.apache.ambari.server.controller.spi.ClusterController getClusterController() {
        if (org.apache.ambari.server.controller.utilities.ClusterControllerHelper.controller == null) {
            try {
                java.lang.Class<?> implClass = java.lang.Class.forName(org.apache.ambari.server.controller.utilities.ClusterControllerHelper.PROVIDER_MODULE_CLASS);
                org.apache.ambari.server.controller.spi.ProviderModule providerModule = org.apache.ambari.server.view.ViewProviderModule.getViewProviderModule(((org.apache.ambari.server.controller.spi.ProviderModule) (implClass.newInstance())));
                org.apache.ambari.server.controller.utilities.ClusterControllerHelper.controller = new org.apache.ambari.server.controller.internal.ClusterControllerImpl(providerModule);
            } catch (java.lang.Exception e) {
                throw new java.lang.IllegalStateException("Can't create provider module " + org.apache.ambari.server.controller.utilities.ClusterControllerHelper.PROVIDER_MODULE_CLASS, e);
            }
        }
        return org.apache.ambari.server.controller.utilities.ClusterControllerHelper.controller;
    }
}