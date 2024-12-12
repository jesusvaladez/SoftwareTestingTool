package org.apache.ambari.server.ldap;
import com.google.inject.multibindings.Multibinder;
public class LdapModule extends com.google.inject.AbstractModule {
    public static final java.lang.String USER_ATTRIBUTES_DETECTORS = "UserAttributesDetectors";

    public static final java.lang.String GROUP_ATTRIBUTES_DETECTORS = "GroupAttributesDetectors";

    @java.lang.Override
    protected void configure() {
        bind(org.apache.ambari.server.ldap.service.LdapFacade.class).to(org.apache.ambari.server.ldap.service.AmbariLdapFacade.class);
        bind(org.apache.ambari.server.ldap.service.LdapConfigurationService.class).to(org.apache.ambari.server.ldap.service.ads.DefaultLdapConfigurationService.class);
        bind(org.apache.ambari.server.ldap.service.LdapAttributeDetectionService.class).to(org.apache.ambari.server.ldap.service.ads.DefaultLdapAttributeDetectionService.class);
        bind(org.apache.ambari.server.ldap.service.LdapConnectionConfigService.class).to(org.apache.ambari.server.ldap.service.ads.DefaultLdapConnectionConfigService.class);
        bind(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration.class).toProvider(org.apache.ambari.server.ldap.service.AmbariLdapConfigurationProvider.class);
        bind(org.apache.ambari.server.ldap.service.ads.detectors.AttributeDetectorFactory.class);
        com.google.inject.multibindings.Multibinder<org.apache.ambari.server.ldap.service.AttributeDetector> userAttributeDetectorBinder = com.google.inject.multibindings.Multibinder.newSetBinder(binder(), org.apache.ambari.server.ldap.service.AttributeDetector.class, com.google.inject.name.Names.named(org.apache.ambari.server.ldap.LdapModule.USER_ATTRIBUTES_DETECTORS));
        userAttributeDetectorBinder.addBinding().to(org.apache.ambari.server.ldap.service.ads.detectors.UserObjectClassDetector.class);
        userAttributeDetectorBinder.addBinding().to(org.apache.ambari.server.ldap.service.ads.detectors.UserNameAttrDetector.class);
        userAttributeDetectorBinder.addBinding().to(org.apache.ambari.server.ldap.service.ads.detectors.UserGroupMemberAttrDetector.class);
        com.google.inject.multibindings.Multibinder<org.apache.ambari.server.ldap.service.AttributeDetector> groupAttributeDetectorBinder = com.google.inject.multibindings.Multibinder.newSetBinder(binder(), org.apache.ambari.server.ldap.service.AttributeDetector.class, com.google.inject.name.Names.named(org.apache.ambari.server.ldap.LdapModule.GROUP_ATTRIBUTES_DETECTORS));
        groupAttributeDetectorBinder.addBinding().to(org.apache.ambari.server.ldap.service.ads.detectors.GroupObjectClassDetector.class);
        groupAttributeDetectorBinder.addBinding().to(org.apache.ambari.server.ldap.service.ads.detectors.GroupNameAttrDetector.class);
        groupAttributeDetectorBinder.addBinding().to(org.apache.ambari.server.ldap.service.ads.detectors.GroupMemberAttrDetector.class);
    }
}