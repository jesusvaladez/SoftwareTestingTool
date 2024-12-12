package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.TPROXY_CONFIGURATION;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_HOST;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_ENABLED_SERVICES;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_MANAGE_SERVICES;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_AUTHENTICATION_ENABLED;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
@org.junit.runner.RunWith(org.easymock.EasyMockRunner.class)
public class AmbariServerConfigurationHandlerTest extends org.easymock.EasyMockSupport {
    @org.junit.Test
    public void getComponentConfigurations() {
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> ssoEntities = new java.util.ArrayList<>();
        ssoEntities.add(createEntity(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_MANAGE_SERVICES.key(), "true"));
        ssoEntities.add(createEntity(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_ENABLED_SERVICES.key(), "AMBARI,SERVICE1"));
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> ldapEntities = new java.util.ArrayList<>();
        ldapEntities.add(createEntity(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED.key(), "true"));
        ldapEntities.add(createEntity(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_HOST.key(), "host1"));
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> tproxyEntities = new java.util.ArrayList<>();
        tproxyEntities.add(createEntity(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.TPROXY_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.TPROXY_AUTHENTICATION_ENABLED.key(), "true"));
        tproxyEntities.add(createEntity(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.TPROXY_CONFIGURATION.getCategoryName(), "ambari.tproxy.proxyuser.knox.hosts", "host1"));
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> allEntities = new java.util.ArrayList<>();
        allEntities.addAll(ssoEntities);
        allEntities.addAll(ldapEntities);
        allEntities.addAll(tproxyEntities);
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAO ambariConfigurationDAO = createMock(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class);
        EasyMock.expect(ambariConfigurationDAO.findAll()).andReturn(allEntities).once();
        EasyMock.expect(ambariConfigurationDAO.findByCategory(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName())).andReturn(ssoEntities).once();
        EasyMock.expect(ambariConfigurationDAO.findByCategory(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName())).andReturn(ldapEntities).once();
        EasyMock.expect(ambariConfigurationDAO.findByCategory(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.TPROXY_CONFIGURATION.getCategoryName())).andReturn(tproxyEntities).once();
        EasyMock.expect(ambariConfigurationDAO.findByCategory("invalid category")).andReturn(null).once();
        org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher = createMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler handler = new org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler(ambariConfigurationDAO, publisher);
        replayAll();
        java.util.Map<java.lang.String, org.apache.ambari.server.api.services.RootServiceComponentConfiguration> allConfigurations = handler.getComponentConfigurations(null);
        junit.framework.Assert.assertEquals(3, allConfigurations.size());
        junit.framework.Assert.assertTrue(allConfigurations.containsKey(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName()));
        junit.framework.Assert.assertTrue(allConfigurations.containsKey(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName()));
        junit.framework.Assert.assertTrue(allConfigurations.containsKey(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.TPROXY_CONFIGURATION.getCategoryName()));
        java.util.Map<java.lang.String, org.apache.ambari.server.api.services.RootServiceComponentConfiguration> ssoConfigurations = handler.getComponentConfigurations(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName());
        junit.framework.Assert.assertEquals(1, ssoConfigurations.size());
        junit.framework.Assert.assertTrue(ssoConfigurations.containsKey(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName()));
        java.util.Map<java.lang.String, org.apache.ambari.server.api.services.RootServiceComponentConfiguration> ldapConfigurations = handler.getComponentConfigurations(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName());
        junit.framework.Assert.assertEquals(1, ldapConfigurations.size());
        junit.framework.Assert.assertTrue(ldapConfigurations.containsKey(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName()));
        java.util.Map<java.lang.String, org.apache.ambari.server.api.services.RootServiceComponentConfiguration> tproxyConfigurations = handler.getComponentConfigurations(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.TPROXY_CONFIGURATION.getCategoryName());
        junit.framework.Assert.assertEquals(1, tproxyConfigurations.size());
        junit.framework.Assert.assertTrue(tproxyConfigurations.containsKey(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.TPROXY_CONFIGURATION.getCategoryName()));
        java.util.Map<java.lang.String, org.apache.ambari.server.api.services.RootServiceComponentConfiguration> invalidConfigurations = handler.getComponentConfigurations("invalid category");
        junit.framework.Assert.assertNull(invalidConfigurations);
        verifyAll();
    }

    @org.junit.Test
    public void removeComponentConfiguration() {
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAO ambariConfigurationDAO = createMock(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class);
        EasyMock.expect(ambariConfigurationDAO.removeByCategory(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName())).andReturn(1).once();
        EasyMock.expect(ambariConfigurationDAO.removeByCategory("invalid category")).andReturn(0).once();
        org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher = createMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        publisher.publish(EasyMock.anyObject(org.apache.ambari.server.events.AmbariConfigurationChangedEvent.class));
        EasyMock.expectLastCall().once();
        org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler handler = new org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler(ambariConfigurationDAO, publisher);
        replayAll();
        handler.removeComponentConfiguration(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName());
        handler.removeComponentConfiguration("invalid category");
        verifyAll();
    }

    @org.junit.Test
    public void updateComponentCategory() throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_ENABLED_SERVICES.key(), "SERVICE1");
        properties.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_MANAGE_SERVICES.key(), "true");
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAO ambariConfigurationDAO = createMock(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class);
        EasyMock.expect(ambariConfigurationDAO.reconcileCategory(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName(), properties, true)).andReturn(true).once();
        EasyMock.expect(ambariConfigurationDAO.reconcileCategory(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName(), properties, false)).andReturn(true).once();
        org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher = createMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        publisher.publish(EasyMock.anyObject(org.apache.ambari.server.events.AmbariConfigurationChangedEvent.class));
        EasyMock.expectLastCall().times(2);
        org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler handler = new org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler(ambariConfigurationDAO, publisher);
        replayAll();
        handler.updateComponentCategory(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName(), properties, false);
        handler.updateComponentCategory(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName(), properties, true);
        try {
            handler.updateComponentCategory("invalid category", properties, true);
            junit.framework.Assert.fail("Expecting IllegalArgumentException to be thrown");
        } catch (java.lang.IllegalArgumentException e) {
        }
        verifyAll();
    }

    @org.junit.Test
    public void getConfigurations() {
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> ssoEntities = new java.util.ArrayList<>();
        ssoEntities.add(createEntity(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_MANAGE_SERVICES.key(), "true"));
        ssoEntities.add(createEntity(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_ENABLED_SERVICES.key(), "AMBARI,SERVICE1"));
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> allEntities = new java.util.ArrayList<>(ssoEntities);
        allEntities.add(createEntity(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED.key(), "true"));
        allEntities.add(createEntity(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_HOST.key(), "host1"));
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAO ambariConfigurationDAO = createMock(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class);
        EasyMock.expect(ambariConfigurationDAO.findAll()).andReturn(allEntities).once();
        org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher = createMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler handler = new org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler(ambariConfigurationDAO, publisher);
        replayAll();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> allConfigurations = handler.getConfigurations();
        junit.framework.Assert.assertEquals(2, allConfigurations.size());
        junit.framework.Assert.assertTrue(allConfigurations.containsKey(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName()));
        junit.framework.Assert.assertTrue(allConfigurations.containsKey(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName()));
        verifyAll();
    }

    @org.junit.Test
    public void getConfigurationProperties() {
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> ssoEntities = new java.util.ArrayList<>();
        ssoEntities.add(createEntity(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_MANAGE_SERVICES.key(), "true"));
        ssoEntities.add(createEntity(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_ENABLED_SERVICES.key(), "AMBARI,SERVICE1"));
        java.util.List<org.apache.ambari.server.orm.entities.AmbariConfigurationEntity> allEntities = new java.util.ArrayList<>(ssoEntities);
        allEntities.add(createEntity(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED.key(), "true"));
        allEntities.add(createEntity(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName(), org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_HOST.key(), "host1"));
        org.apache.ambari.server.orm.dao.AmbariConfigurationDAO ambariConfigurationDAO = createMock(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class);
        EasyMock.expect(ambariConfigurationDAO.findByCategory(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName())).andReturn(ssoEntities).once();
        EasyMock.expect(ambariConfigurationDAO.findByCategory("invalid category")).andReturn(null).once();
        org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher = createMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler handler = new org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler(ambariConfigurationDAO, publisher);
        replayAll();
        java.util.Map<java.lang.String, java.lang.String> ssoConfigurations = handler.getConfigurationProperties(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.SSO_CONFIGURATION.getCategoryName());
        junit.framework.Assert.assertEquals(2, ssoConfigurations.size());
        junit.framework.Assert.assertTrue(ssoConfigurations.containsKey(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_ENABLED_SERVICES.key()));
        junit.framework.Assert.assertTrue(ssoConfigurations.containsKey(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_MANAGE_SERVICES.key()));
        java.util.Map<java.lang.String, java.lang.String> invalidConfigurations = handler.getConfigurationProperties("invalid category");
        junit.framework.Assert.assertNull(invalidConfigurations);
        verifyAll();
    }

    private org.apache.ambari.server.orm.entities.AmbariConfigurationEntity createEntity(java.lang.String categoryName, java.lang.String key, java.lang.String value) {
        org.apache.ambari.server.orm.entities.AmbariConfigurationEntity entity = new org.apache.ambari.server.orm.entities.AmbariConfigurationEntity();
        entity.setCategoryName(categoryName);
        entity.setPropertyName(key);
        entity.setPropertyValue(value);
        return entity;
    }
}