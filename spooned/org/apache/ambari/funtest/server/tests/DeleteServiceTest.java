package org.apache.ambari.funtest.server.tests;
import org.apache.http.HttpStatus;
@org.junit.Ignore
public class DeleteServiceTest extends org.apache.ambari.funtest.server.tests.ServerTestBase {
    private static org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(org.apache.ambari.funtest.server.tests.DeleteServiceTest.class);

    @org.junit.Test
    public void testDeleteService() throws java.lang.Exception {
        java.lang.String clusterName = "c1";
        java.lang.String serviceName = "HDFS";
        org.apache.ambari.funtest.server.ConnectionParams params = new org.apache.ambari.funtest.server.ConnectionParams();
        params.setServerName("localhost");
        params.setServerApiPort(org.apache.ambari.funtest.server.tests.ServerTestBase.serverPort);
        params.setServerAgentPort(org.apache.ambari.funtest.server.tests.ServerTestBase.serverAgentPort);
        params.setUserName("admin");
        params.setPassword("admin");
        org.apache.ambari.funtest.server.utils.ClusterUtils clusterUtils = org.apache.ambari.funtest.server.tests.ServerTestBase.injector.getInstance(org.apache.ambari.funtest.server.utils.ClusterUtils.class);
        clusterUtils.createSampleCluster(params);
        com.google.gson.JsonElement jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.service.GetServiceWebRequest(params, clusterName, serviceName));
        org.junit.Assert.assertTrue(!jsonResponse.isJsonNull());
        com.google.gson.JsonObject jsonServiceInfoObj = jsonResponse.getAsJsonObject().get("ServiceInfo").getAsJsonObject();
        java.lang.String cluster_name = jsonServiceInfoObj.get("cluster_name").getAsString();
        org.junit.Assert.assertEquals(cluster_name, clusterName);
        java.lang.String service_name = jsonServiceInfoObj.get("service_name").getAsString();
        org.junit.Assert.assertEquals(service_name, serviceName);
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.service.StopServiceWebRequest(params, clusterName, serviceName));
        org.apache.ambari.server.orm.dao.ClusterServiceDAO clusterServiceDAO = org.apache.ambari.funtest.server.tests.ServerTestBase.injector.getInstance(org.apache.ambari.server.orm.dao.ClusterServiceDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.ClusterServiceEntity> clusterServiceEntities = clusterServiceDAO.findAll();
        org.junit.Assert.assertEquals(clusterServiceEntities.size(), 1);
        org.junit.Assert.assertEquals(clusterServiceEntities.get(0).getServiceName(), serviceName);
        org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity = clusterServiceEntities.get(0);
        long clusterId = clusterServiceEntity.getClusterId();
        org.apache.ambari.server.orm.dao.ServiceDesiredStateDAO serviceDesiredStateDAO = org.apache.ambari.funtest.server.tests.ServerTestBase.injector.getInstance(org.apache.ambari.server.orm.dao.ServiceDesiredStateDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity> serviceDesiredStateEntities = serviceDesiredStateDAO.findAll();
        org.junit.Assert.assertEquals(serviceDesiredStateEntities.size(), 1);
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity = serviceDesiredStateEntities.get(0);
        org.junit.Assert.assertEquals(serviceDesiredStateEntity.getServiceName(), serviceName);
        org.junit.Assert.assertEquals(serviceDesiredStateEntity.getDesiredState(), org.apache.ambari.server.state.State.INSTALLED);
        org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO = org.apache.ambari.funtest.server.tests.ServerTestBase.injector.getInstance(org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> serviceComponentDesiredStateEntities = serviceComponentDesiredStateDAO.findAll();
        org.junit.Assert.assertEquals(serviceComponentDesiredStateEntities.size(), 3);
        for (org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity : serviceComponentDesiredStateEntities) {
            org.junit.Assert.assertEquals(serviceComponentDesiredStateEntity.getDesiredState(), org.apache.ambari.server.state.State.INSTALLED);
        }
        org.apache.ambari.server.orm.dao.HostComponentStateDAO hostComponentStateDAO = org.apache.ambari.funtest.server.tests.ServerTestBase.injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentStateDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStateEntities = hostComponentStateDAO.findAll();
        org.junit.Assert.assertEquals(hostComponentStateEntities.size(), 3);
        org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO hostComponentDesiredStateDAO = org.apache.ambari.funtest.server.tests.ServerTestBase.injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> hostComponentDesiredStateEntities = hostComponentDesiredStateDAO.findAll();
        org.junit.Assert.assertEquals(hostComponentDesiredStateEntities.size(), 3);
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.service.DeleteServiceWebRequest(params, clusterName, serviceName));
        org.apache.ambari.funtest.server.WebResponse webResponse = new org.apache.ambari.funtest.server.api.service.GetServiceWebRequest(params, clusterName, serviceName).getResponse();
        org.junit.Assert.assertEquals(webResponse.getStatusCode(), HttpStatus.SC_NOT_FOUND);
        clusterServiceEntity = clusterServiceDAO.findByClusterAndServiceNames(clusterName, serviceName);
        org.junit.Assert.assertTrue(clusterServiceEntity == null);
        org.apache.ambari.server.orm.entities.ServiceDesiredStateEntityPK serviceDesiredStateEntityPK = org.apache.ambari.funtest.server.tests.ServerTestBase.injector.getInstance(org.apache.ambari.server.orm.entities.ServiceDesiredStateEntityPK.class);
        serviceDesiredStateEntityPK.setClusterId(clusterId);
        serviceDesiredStateEntityPK.setServiceName(serviceName);
        serviceDesiredStateEntity = serviceDesiredStateDAO.findByPK(serviceDesiredStateEntityPK);
        org.junit.Assert.assertTrue(serviceDesiredStateEntity == null);
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity = serviceComponentDesiredStateDAO.findById(0L);
        org.junit.Assert.assertTrue(serviceComponentDesiredStateEntity == null);
        hostComponentStateEntities = hostComponentStateDAO.findByService(serviceName);
        org.junit.Assert.assertEquals(hostComponentStateEntities.size(), 0);
        hostComponentDesiredStateEntities = hostComponentDesiredStateDAO.findAll();
        org.junit.Assert.assertEquals(hostComponentDesiredStateEntities.size(), 0);
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.cluster.DeleteClusterWebRequest(params, clusterName));
        org.apache.ambari.funtest.server.tests.DeleteServiceTest.LOG.info(jsonResponse);
    }
}