package org.apache.ambari.server.controller.utilities;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.reset;
public class KerberosIdentityCleanerTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    private static final java.lang.String HOST = "c6401";

    private static final java.lang.String HOST2 = "c6402";

    private static final java.lang.String OOZIE = "OOZIE";

    private static final java.lang.String OOZIE_SERVER = "OOZIE_SERVER";

    private static final java.lang.String OOZIE_2 = "OOZIE2";

    private static final java.lang.String OOZIE_SERVER_2 = "OOZIE_SERVER2";

    private static final java.lang.String YARN_2 = "YARN2";

    private static final java.lang.String RESOURCE_MANAGER_2 = "RESOURCE_MANAGER2";

    private static final java.lang.String YARN = "YARN";

    private static final java.lang.String RESOURCE_MANAGER = "RESOURCE_MANAGER";

    private static final java.lang.String HDFS = "HDFS";

    private static final java.lang.String NAMENODE = "NAMENODE";

    private static final java.lang.String DATANODE = "DATANODE";

    private static final long CLUSTER_ID = 1;

    @org.easymock.Mock
    private org.apache.ambari.server.controller.KerberosHelper kerberosHelper;

    @org.easymock.Mock
    private org.apache.ambari.server.state.Clusters clusters;

    @org.easymock.Mock
    private org.apache.ambari.server.state.Cluster cluster;

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> installedServices = new java.util.HashMap<>();

    private org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory kerberosDescriptorFactory = new org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory();

    private org.apache.ambari.server.controller.utilities.KerberosIdentityCleaner kerberosIdentityCleaner;

    private org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor;

    @org.junit.Test
    public void removesAllKerberosIdentitesOfComponentAfterComponentWasUninstalled() throws java.lang.Exception {
        installComponent(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE_SERVER, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST);
        kerberosHelper.deleteIdentities(cluster, java.util.Collections.singletonList(new org.apache.ambari.server.serveraction.kerberos.Component(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE_SERVER, -1L)), null);
        EasyMock.expectLastCall().once();
        replayAll();
        uninstallComponent(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE_SERVER, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST);
        verifyAll();
    }

    @org.junit.Test
    public void skipsRemovingIdentityThatIsSharedByPrincipalName() throws java.lang.Exception {
        installComponent(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE_SERVER, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST);
        installComponent(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE_2, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE_SERVER_2, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST);
        kerberosHelper.deleteIdentities(cluster, java.util.Collections.singletonList(new org.apache.ambari.server.serveraction.kerberos.Component(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE_SERVER, -1L)), null);
        EasyMock.expectLastCall().once();
        replayAll();
        uninstallComponent(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE_SERVER, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST);
        verifyAll();
    }

    @org.junit.Test
    public void skipsRemovingIdentityThatIsSharedByKeyTabFilePath() throws java.lang.Exception {
        installComponent(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.YARN, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.RESOURCE_MANAGER, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST);
        installComponent(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.YARN_2, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.RESOURCE_MANAGER_2, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST);
        kerberosHelper.deleteIdentities(cluster, java.util.Collections.singletonList(new org.apache.ambari.server.serveraction.kerberos.Component(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.YARN, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.RESOURCE_MANAGER, -1L)), null);
        EasyMock.expectLastCall().once();
        replayAll();
        uninstallComponent(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.YARN, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.RESOURCE_MANAGER, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST);
        verifyAll();
    }

    @org.junit.Test
    public void skipsRemovingIdentityWhenClusterIsNotKerberized() throws java.lang.Exception {
        EasyMock.reset(cluster);
        EasyMock.expect(cluster.getSecurityType()).andReturn(org.apache.ambari.server.state.SecurityType.NONE).anyTimes();
        replayAll();
        uninstallComponent(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE_SERVER, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST);
        verifyAll();
    }

    @org.junit.Test
    public void removesServiceIdentitiesSkipComponentIdentitiesAfterServiceWasUninstalled() throws java.lang.Exception {
        installComponent(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE_SERVER, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST);
        kerberosHelper.deleteIdentities(cluster, hdfsComponents(), null);
        EasyMock.expectLastCall().once();
        replayAll();
        uninstallService(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HDFS, hdfsComponents());
        verifyAll();
    }

    @org.junit.Test
    public void skipsRemovingIdentityWhenClusterIsUpgrading() throws java.lang.Exception {
        installComponent(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE_SERVER, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST);
        EasyMock.reset(cluster);
        EasyMock.expect(cluster.getSecurityType()).andReturn(org.apache.ambari.server.state.SecurityType.KERBEROS).once();
        EasyMock.expect(cluster.getUpgradeInProgress()).andReturn(createNiceMock(org.apache.ambari.server.orm.entities.UpgradeEntity.class)).once();
        replayAll();
        uninstallComponent(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.OOZIE_SERVER, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST);
        verifyAll();
    }

    private java.util.ArrayList<org.apache.ambari.server.serveraction.kerberos.Component> hdfsComponents() {
        return com.google.common.collect.Lists.newArrayList(new org.apache.ambari.server.serveraction.kerberos.Component(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HDFS, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.NAMENODE, 0L), new org.apache.ambari.server.serveraction.kerberos.Component(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HOST, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.HDFS, org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.DATANODE, 0L));
    }

    private void installComponent(java.lang.String serviceName, java.lang.String componentName, java.lang.String... hostNames) {
        org.apache.ambari.server.state.Service service = createMock((serviceName + "_") + componentName, org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent component = createMock(componentName, org.apache.ambari.server.state.ServiceComponent.class);
        EasyMock.expect(component.getName()).andReturn(componentName).anyTimes();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        EasyMock.expect(component.getServiceComponentHosts()).andReturn(hosts).anyTimes();
        for (java.lang.String hostName : hostNames) {
            org.apache.ambari.server.state.ServiceComponentHost host = createMock(hostName, org.apache.ambari.server.state.ServiceComponentHost.class);
            EasyMock.expect(host.getHostName()).andReturn(hostName).anyTimes();
            hosts.put(hostName, host);
        }
        installedServices.put(serviceName, service);
        EasyMock.expect(service.getName()).andReturn(serviceName).anyTimes();
        EasyMock.expect(service.getServiceComponents()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponent>() {
            {
                put(componentName, component);
            }
        }).anyTimes();
    }

    private void uninstallComponent(java.lang.String service, java.lang.String component, java.lang.String host) throws org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException {
        kerberosIdentityCleaner.componentRemoved(new org.apache.ambari.server.events.ServiceComponentUninstalledEvent(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.CLUSTER_ID, "any", "any", service, component, host, false, false, -1L));
    }

    private void uninstallService(java.lang.String service, java.util.List<org.apache.ambari.server.serveraction.kerberos.Component> components) throws org.apache.ambari.server.serveraction.kerberos.KerberosMissingAdminCredentialsException {
        kerberosIdentityCleaner.serviceRemoved(new org.apache.ambari.server.events.ServiceRemovedEvent(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.CLUSTER_ID, "any", "any", service, components));
    }

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        kerberosIdentityCleaner = new org.apache.ambari.server.controller.utilities.KerberosIdentityCleaner(new org.apache.ambari.server.events.publishers.AmbariEventPublisher(), kerberosHelper, clusters);
        kerberosDescriptor = kerberosDescriptorFactory.createInstance("{" + ((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((("  'services': [" + "    {") + "      'name': 'OOZIE',") + "      'components': [") + "        {") + "          'name': 'OOZIE_SERVER',") + "          'identities': [") + "            {") + "              'name': '/HDFS/NAMENODE/hdfs'") + "            },") + "            {") + "              'name': 'oozie_server1',") + "              'principal': { 'value': 'oozie1/_HOST@EXAMPLE.COM' }") + "            },") + "") + "            {") + "              'name': 'oozie_server2',") + "              'principal': { 'value': 'oozie/_HOST@EXAMPLE.COM' }") + "            }") + "          ]") + "        }") + "      ]") + "    },") + "    {") + "      'name': 'OOZIE2',") + "      'components': [") + "        {") + "          'name': 'OOZIE_SERVER2',") + "          'identities': [") + "            {") + "              'name': 'oozie_server3',") + "              'principal': { 'value': 'oozie/_HOST@EXAMPLE.COM' }") + "            }") + "") + "          ]") + "        }") + "      ]") + "    },") + "    {") + "      'name': 'YARN',") + "      'components': [") + "        {") + "          'name': 'RESOURCE_MANAGER',") + "          'identities': [") + "            {") + "              'name': 'rm_unique'") + "            },") + "            {") + "              'name': 'rm1-shared',") + "              'keytab' : { 'file' : 'shared' }") + "            }") + "          ]") + "        }") + "      ]") + "    },") + "    {") + "      'name': 'YARN2',") + "      'components': [") + "        {") + "          'name': 'RESOURCE_MANAGER2',") + "          'identities': [") + "            {") + "              'name': 'rm2-shared',") + "              'keytab' : { 'file' : 'shared' }") + "            }") + "          ]") + "        }") + "      ]") + "    },") + "    {") + "      'name': 'HDFS',") + "      'identities': [") + "            {") + "              'name': 'hdfs-service'") + "            },") + "            {") + "              'name': 'shared',") + "              'principal': { 'value': 'oozie/_HOST@EXAMPLE.COM' }") + "            },") + "            {") + "              'name': '/YARN/RESOURCE_MANAGER/rm'") + "            },") + "          ],") + "      'components': [") + "        {") + "          'name': 'NAMENODE',") + "          'identities': [") + "            {") + "              'name': 'namenode'") + "            }") + "          ]") + "        },") + "        {") + "          'name': 'DATANODE',") + "          'identities': [") + "            {") + "              'name': 'datanode'") + "            }") + "          ]") + "        }") + "      ]") + "    }") + "  ]") + "}"));
        EasyMock.expect(clusters.getCluster(org.apache.ambari.server.controller.utilities.KerberosIdentityCleanerTest.CLUSTER_ID)).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getSecurityType()).andReturn(org.apache.ambari.server.state.SecurityType.KERBEROS).anyTimes();
        EasyMock.expect(kerberosHelper.getKerberosDescriptor(cluster, false)).andReturn(kerberosDescriptor).anyTimes();
        EasyMock.expect(cluster.getServices()).andReturn(installedServices).anyTimes();
        EasyMock.expect(cluster.getUpgradeInProgress()).andReturn(null).once();
    }
}