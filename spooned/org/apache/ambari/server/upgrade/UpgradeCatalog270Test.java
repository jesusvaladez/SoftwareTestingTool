package org.apache.ambari.server.upgrade;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
import org.apache.commons.lang.StringUtils;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PERMISSION_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRINCIPAL_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRIVILEGE_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_RESOURCE_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_CATEGORY_NAME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_PROPERTY_NAME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_SEQUENCE_NAME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_SEQUENCE_VALUE_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_DESIRED_STATE_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_NAME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_STATE_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_HOSTCOMPONENTDESIREDSTATE_COMPONENT_NAME;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_HOSTCOMPONENTSTATE_COMPONENT_NAME;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_KKP_HOST_ID;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_KKP_KEYTAB_PATH;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_KKP_PRINCIPAL_NAME;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_KKP_SERVICE_PRINCIPAL;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_SERVICECOMPONENTDESIREDSTATE_SERVICE_NAME;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.HOSTS_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.HOST_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_PRINCIPAL_HOST_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_PRINCIPAL_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.KEYTAB_PATH_FIELD;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_MAPPING_SERVICE_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_GROUP_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_MEMBER_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_USER_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.PK_KERBEROS_KEYTAB;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.PK_KKP;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.PK_KKP_MAPPING_SERVICE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.PRINCIPAL_NAME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_APPLICABLE_SERVICES_FOREIGN_KEY;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_APPLICABLE_SERVICES_REPO_DEFINITION_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_APPLICABLE_SERVICES_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_BASE_URL_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_COMPONENTS_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_DISTRIBUTION_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_FOREIGN_KEY;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_MIRRORS_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_PRIMARY_KEY;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_REPO_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_REPO_NAME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_REPO_OS_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_UNIQUE_REPO_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_AMBARI_MANAGED_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_FAMILY_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_FOREIGN_KEY;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_PRIMARY_KEY;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_REPO_VERSION_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_FOREIGN_KEY;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_REPO_DEFINITION_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_TAG_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_REPOSITORIES_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_REPO_VERSION_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REQUEST_DISPLAY_STATUS_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REQUEST_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.REQUEST_USER_NAME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.SECURITY_STATE_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.SERVICE_COMPONENT_DESIRED_STATE_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.SERVICE_DESIRED_STATE_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.SERVICE_NAME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.STAGE_DISPLAY_STATUS_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.STAGE_STATUS_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.STAGE_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.UNIQUE_USERS_0_INDEX;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.UNI_KKP;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CONSECUTIVE_FAILURES_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_DISPLAY_NAME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_LDAP_USER_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_LOCAL_USERNAME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_PASSWORD_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_TYPE_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_VERSION_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_AUTHENTICATION_KEY_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_AUTHENTICATION_TYPE_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_CREATE_TIME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_PRIMARY_KEY;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_TABLE;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_UPDATE_TIME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_AUTHENTICATION_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_AUTHENTICATION_USERS_FOREIGN_KEY;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_ID_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.WIDGET_TABLE;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.niceMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.startsWith;
import static org.easymock.EasyMock.verify;
import static org.mockito.Matchers.anyInt;
@org.junit.runner.RunWith(org.easymock.EasyMockRunner.class)
public class UpgradeCatalog270Test {
    public static final com.google.gson.Gson GSON = new com.google.gson.Gson();

    @org.junit.Rule
    public org.junit.rules.ExpectedException expectedException = org.junit.rules.ExpectedException.none();

    @org.easymock.Mock(type = org.easymock.MockType.STRICT)
    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProvider;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private com.google.inject.Injector injector;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private javax.persistence.EntityManager entityManager;

    @org.easymock.Mock(type = org.easymock.MockType.DEFAULT)
    private org.apache.ambari.server.orm.DBAccessor dbAccessor;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.stack.OsFamily osFamily;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Config config;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.actionmanager.ActionManager actionManager;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Clusters clusters;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.Cluster cluster;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    org.apache.ambari.server.orm.dao.AmbariConfigurationDAO ambariConfigurationDao;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    org.apache.ambari.server.orm.dao.ArtifactDAO artifactDAO;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.controller.AmbariManagementController ambariManagementController;

    @org.junit.Before
    public void init() {
        EasyMock.reset(entityManagerProvider, injector);
        EasyMock.expect(entityManagerProvider.get()).andReturn(entityManager).anyTimes();
        EasyMock.expect(injector.getInstance(com.google.gson.Gson.class)).andReturn(null).anyTimes();
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.controller.MaintenanceStateHelper.class)).andReturn(null).anyTimes();
        EasyMock.replay(entityManagerProvider, injector);
    }

    @org.junit.After
    public void tearDown() {
    }

    @org.junit.Test
    public void testExecuteDMLUpdates() throws java.lang.Exception {
        java.lang.reflect.Method addNewConfigurationsFromXml = org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.class.getDeclaredMethod("addNewConfigurationsFromXml");
        java.lang.reflect.Method showHcatDeletedUserMessage = org.apache.ambari.server.upgrade.UpgradeCatalog270.class.getDeclaredMethod("showHcatDeletedUserMessage");
        java.lang.reflect.Method setStatusOfStagesAndRequests = org.apache.ambari.server.upgrade.UpgradeCatalog270.class.getDeclaredMethod("setStatusOfStagesAndRequests");
        java.lang.reflect.Method updateLogSearchConfigs = org.apache.ambari.server.upgrade.UpgradeCatalog270.class.getDeclaredMethod("updateLogSearchConfigs");
        java.lang.reflect.Method updateKerberosConfigurations = org.apache.ambari.server.upgrade.UpgradeCatalog270.class.getDeclaredMethod("updateKerberosConfigurations");
        java.lang.reflect.Method upgradeLdapConfiguration = org.apache.ambari.server.upgrade.UpgradeCatalog270.class.getDeclaredMethod("moveAmbariPropertiesToAmbariConfiguration");
        java.lang.reflect.Method createRoleAuthorizations = org.apache.ambari.server.upgrade.UpgradeCatalog270.class.getDeclaredMethod("createRoleAuthorizations");
        java.lang.reflect.Method addUserAuthenticationSequence = org.apache.ambari.server.upgrade.UpgradeCatalog270.class.getDeclaredMethod("addUserAuthenticationSequence");
        java.lang.reflect.Method renameAmbariInfra = org.apache.ambari.server.upgrade.UpgradeCatalog270.class.getDeclaredMethod("renameAmbariInfra");
        java.lang.reflect.Method updateKerberosDescriptorArtifacts = org.apache.ambari.server.upgrade.UpgradeCatalog270.class.getSuperclass().getDeclaredMethod("updateKerberosDescriptorArtifacts");
        java.lang.reflect.Method updateSolrConfigurations = org.apache.ambari.server.upgrade.UpgradeCatalog270.class.getDeclaredMethod("updateSolrConfigurations");
        java.lang.reflect.Method updateAmsConfigurations = org.apache.ambari.server.upgrade.UpgradeCatalog270.class.getDeclaredMethod("updateAmsConfigs");
        java.lang.reflect.Method updateStormConfigurations = org.apache.ambari.server.upgrade.UpgradeCatalog270.class.getDeclaredMethod("updateStormConfigs");
        java.lang.reflect.Method clearHadoopMetrics2Content = org.apache.ambari.server.upgrade.UpgradeCatalog270.class.getDeclaredMethod("clearHadoopMetrics2Content");
        org.apache.ambari.server.upgrade.UpgradeCatalog270 upgradeCatalog270 = EasyMock.createMockBuilder(org.apache.ambari.server.upgrade.UpgradeCatalog270.class).addMockedMethod(showHcatDeletedUserMessage).addMockedMethod(addNewConfigurationsFromXml).addMockedMethod(setStatusOfStagesAndRequests).addMockedMethod(updateLogSearchConfigs).addMockedMethod(updateKerberosConfigurations).addMockedMethod(upgradeLdapConfiguration).addMockedMethod(createRoleAuthorizations).addMockedMethod(addUserAuthenticationSequence).addMockedMethod(renameAmbariInfra).addMockedMethod(updateKerberosDescriptorArtifacts).addMockedMethod(updateSolrConfigurations).addMockedMethod(updateAmsConfigurations).addMockedMethod(updateStormConfigurations).addMockedMethod(clearHadoopMetrics2Content).createMock();
        upgradeCatalog270.addNewConfigurationsFromXml();
        EasyMock.expectLastCall().once();
        upgradeCatalog270.showHcatDeletedUserMessage();
        EasyMock.expectLastCall().once();
        upgradeCatalog270.createRoleAuthorizations();
        EasyMock.expectLastCall().once();
        upgradeCatalog270.setStatusOfStagesAndRequests();
        EasyMock.expectLastCall().once();
        upgradeCatalog270.updateLogSearchConfigs();
        upgradeCatalog270.updateKerberosConfigurations();
        EasyMock.expectLastCall().once();
        upgradeCatalog270.moveAmbariPropertiesToAmbariConfiguration();
        EasyMock.expectLastCall().once();
        upgradeCatalog270.addUserAuthenticationSequence();
        EasyMock.expectLastCall().once();
        upgradeCatalog270.renameAmbariInfra();
        EasyMock.expectLastCall().once();
        upgradeCatalog270.updateKerberosDescriptorArtifacts();
        EasyMock.expectLastCall().once();
        upgradeCatalog270.updateSolrConfigurations();
        EasyMock.expectLastCall().once();
        upgradeCatalog270.updateAmsConfigs();
        EasyMock.expectLastCall().once();
        upgradeCatalog270.updateStormConfigs();
        EasyMock.expectLastCall().once();
        upgradeCatalog270.clearHadoopMetrics2Content();
        EasyMock.expectLastCall().once();
        EasyMock.replay(upgradeCatalog270);
        upgradeCatalog270.executeDMLUpdates();
        EasyMock.verify(upgradeCatalog270);
    }

    @org.junit.Test
    public void testExecuteDDLUpdates() throws java.lang.Exception {
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_HOSTCOMPONENTDESIREDSTATE_COMPONENT_NAME);
        EasyMock.expectLastCall().once();
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_HOSTCOMPONENTSTATE_COMPONENT_NAME);
        EasyMock.expectLastCall().once();
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.SERVICE_COMPONENT_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_SERVICECOMPONENTDESIREDSTATE_SERVICE_NAME);
        EasyMock.expectLastCall().once();
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> updateStageTableCaptures = EasyMock.newCapture(CaptureType.ALL);
        dbAccessor.addColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.STAGE_TABLE), EasyMock.capture(updateStageTableCaptures));
        EasyMock.expectLastCall().once();
        dbAccessor.addColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.STAGE_TABLE), EasyMock.capture(updateStageTableCaptures));
        EasyMock.expectLastCall().once();
        dbAccessor.addColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.REQUEST_TABLE), EasyMock.capture(updateStageTableCaptures));
        EasyMock.expectLastCall().once();
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> updateRequestTableCapture = EasyMock.newCapture(CaptureType.ALL);
        dbAccessor.addColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.REQUEST_TABLE), EasyMock.capture(updateRequestTableCapture));
        EasyMock.expectLastCall().once();
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> updateWidgetTableCapture = EasyMock.newCapture(CaptureType.ALL);
        dbAccessor.addColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.WIDGET_TABLE), EasyMock.capture(updateWidgetTableCapture));
        EasyMock.expectLastCall().once();
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> hrcOpsDisplayNameColumn = EasyMock.newCapture();
        dbAccessor.addColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.HOST_ROLE_COMMAND_TABLE), EasyMock.capture(hrcOpsDisplayNameColumn));
        EasyMock.expectLastCall().once();
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> lastValidColumn = EasyMock.newCapture();
        dbAccessor.addColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_STATE_TABLE), EasyMock.capture(lastValidColumn));
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.SECURITY_STATE_COLUMN);
        EasyMock.expectLastCall().once();
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.SECURITY_STATE_COLUMN);
        EasyMock.expectLastCall().once();
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.SERVICE_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.SECURITY_STATE_COLUMN);
        EasyMock.expectLastCall().once();
        org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> ambariConfigurationTableColumns = EasyMock.newCapture();
        dbAccessor.createTable(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_TABLE), EasyMock.capture(ambariConfigurationTableColumns));
        EasyMock.expectLastCall().once();
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_TABLE, "PK_ambari_configuration", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_CATEGORY_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_PROPERTY_NAME_COLUMN);
        EasyMock.expectLastCall().once();
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> temporaryColumnCreationCapture = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> temporaryColumnRenameCapture = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> createUserAuthenticationTableCaptures = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> createMembersTableCaptures = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> createAdminPrincipalTableCaptures = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> updateUserTableCaptures = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> alterUserTableCaptures = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> addRepoOsTableCapturedColumns = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> addRepoDefinitionTableCapturedColumns = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> addRepoTagsTableCapturedColumns = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> addRepoApplicableServicesTableCapturedColumns = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<java.lang.String[]> insertRepoOsTableRowColumns = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<java.lang.String[]> insertRepoOsTableRowValues = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<java.lang.String[]> insertAmbariSequencesRowColumns = EasyMock.newCapture(CaptureType.ALL);
        org.easymock.Capture<java.lang.String[]> insertAmbariSequencesRowValues = EasyMock.newCapture(CaptureType.ALL);
        EasyMock.expect(dbAccessor.getColumnType(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_TYPE_COLUMN)).andReturn(0).anyTimes();
        prepareConvertingUsersCreationTime(dbAccessor, temporaryColumnCreationCapture, temporaryColumnRenameCapture);
        prepareCreateUserAuthenticationTable(dbAccessor, createUserAuthenticationTableCaptures);
        prepareUpdateGroupMembershipRecords(dbAccessor, createMembersTableCaptures);
        prepareUpdateAdminPrivilegeRecords(dbAccessor, createAdminPrincipalTableCaptures);
        prepareUpdateUsersTable(dbAccessor, updateUserTableCaptures, alterUserTableCaptures);
        prepareUpdateRepoTables(dbAccessor, addRepoOsTableCapturedColumns, addRepoDefinitionTableCapturedColumns, addRepoTagsTableCapturedColumns, addRepoApplicableServicesTableCapturedColumns, insertRepoOsTableRowColumns, insertRepoOsTableRowValues, insertAmbariSequencesRowColumns, insertAmbariSequencesRowValues);
        org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> kerberosKeytabColumnsCapture = EasyMock.newCapture();
        dbAccessor.createTable(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_TABLE), EasyMock.capture(kerberosKeytabColumnsCapture));
        EasyMock.expectLastCall().once();
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.PK_KERBEROS_KEYTAB, org.apache.ambari.server.upgrade.UpgradeCatalog270.KEYTAB_PATH_FIELD);
        EasyMock.expectLastCall().once();
        org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> kerberosKeytabPrincipalColumnsCapture = EasyMock.newCapture();
        dbAccessor.createTable(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE), EasyMock.capture(kerberosKeytabPrincipalColumnsCapture));
        EasyMock.expectLastCall().once();
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.PK_KKP, org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_ID_COLUMN);
        EasyMock.expectLastCall().once();
        dbAccessor.addUniqueConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.UNI_KKP, org.apache.ambari.server.upgrade.UpgradeCatalog270.KEYTAB_PATH_FIELD, org.apache.ambari.server.upgrade.UpgradeCatalog270.PRINCIPAL_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.HOST_ID_COLUMN);
        EasyMock.expectLastCall().once();
        org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> mappingColumnsCapture = EasyMock.newCapture();
        dbAccessor.createTable(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_MAPPING_SERVICE_TABLE), EasyMock.capture(mappingColumnsCapture));
        EasyMock.expectLastCall().once();
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_MAPPING_SERVICE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.PK_KKP_MAPPING_SERVICE, org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.SERVICE_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_NAME_COLUMN);
        EasyMock.expectLastCall().once();
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_KKP_KEYTAB_PATH, org.apache.ambari.server.upgrade.UpgradeCatalog270.KEYTAB_PATH_FIELD, org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.KEYTAB_PATH_FIELD, false);
        EasyMock.expectLastCall().once();
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_KKP_HOST_ID, org.apache.ambari.server.upgrade.UpgradeCatalog270.HOST_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.HOSTS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.HOST_ID_COLUMN, false);
        EasyMock.expectLastCall().once();
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_KKP_PRINCIPAL_NAME, org.apache.ambari.server.upgrade.UpgradeCatalog270.PRINCIPAL_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_PRINCIPAL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.PRINCIPAL_NAME_COLUMN, false);
        EasyMock.expectLastCall().once();
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_MAPPING_SERVICE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_KKP_SERVICE_PRINCIPAL, org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_ID_COLUMN, false);
        EasyMock.expectLastCall().once();
        java.sql.Connection c = EasyMock.niceMock(java.sql.Connection.class);
        java.sql.Statement s = EasyMock.niceMock(java.sql.Statement.class);
        EasyMock.expect(s.executeQuery(EasyMock.anyString())).andReturn(null).once();
        EasyMock.expect(c.createStatement()).andReturn(s).once();
        EasyMock.expect(dbAccessor.getConnection()).andReturn(c).once();
        dbAccessor.dropTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_PRINCIPAL_HOST_TABLE);
        EasyMock.replay(dbAccessor);
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(getTestGuiceModule());
        org.apache.ambari.server.upgrade.UpgradeCatalog270 upgradeCatalog270 = injector.getInstance(org.apache.ambari.server.upgrade.UpgradeCatalog270.class);
        upgradeCatalog270.executeDDLUpdates();
        org.junit.Assert.assertTrue(updateStageTableCaptures.hasCaptured());
        validateColumns(updateStageTableCaptures.getValues(), java.util.Arrays.asList(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.STAGE_STATUS_COLUMN, java.lang.String.class, 255, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.STAGE_DISPLAY_STATUS_COLUMN, java.lang.String.class, 255, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REQUEST_DISPLAY_STATUS_COLUMN, java.lang.String.class, 255, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, false)));
        org.junit.Assert.assertTrue(updateRequestTableCapture.hasCaptured());
        validateColumns(updateRequestTableCapture.getValues(), java.util.Arrays.asList(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REQUEST_USER_NAME_COLUMN, java.lang.String.class, 255)));
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo capturedOpsDisplayNameColumn = hrcOpsDisplayNameColumn.getValue();
        org.junit.Assert.assertEquals(org.apache.ambari.server.upgrade.UpgradeCatalog270.HRC_OPS_DISPLAY_NAME_COLUMN, capturedOpsDisplayNameColumn.getName());
        org.junit.Assert.assertEquals(null, capturedOpsDisplayNameColumn.getDefaultValue());
        org.junit.Assert.assertEquals(java.lang.String.class, capturedOpsDisplayNameColumn.getType());
        org.junit.Assert.assertTrue(ambariConfigurationTableColumns.hasCaptured());
        validateColumns(ambariConfigurationTableColumns.getValue(), java.util.Arrays.asList(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_CATEGORY_NAME_COLUMN, java.lang.String.class, 100, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_PROPERTY_NAME_COLUMN, java.lang.String.class, 100, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN, java.lang.String.class, 2048, null, true)));
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = ambariConfigurationTableColumns.getValue();
        org.junit.Assert.assertEquals(3, columns.size());
        for (org.apache.ambari.server.orm.DBAccessor.DBColumnInfo column : columns) {
            java.lang.String columnName = column.getName();
            if (org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_CATEGORY_NAME_COLUMN.equals(columnName)) {
                org.junit.Assert.assertEquals(java.lang.String.class, column.getType());
                org.junit.Assert.assertEquals(java.lang.Integer.valueOf(100), column.getLength());
                org.junit.Assert.assertEquals(null, column.getDefaultValue());
                org.junit.Assert.assertFalse(column.isNullable());
            } else if (org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_PROPERTY_NAME_COLUMN.equals(columnName)) {
                org.junit.Assert.assertEquals(java.lang.String.class, column.getType());
                org.junit.Assert.assertEquals(java.lang.Integer.valueOf(100), column.getLength());
                org.junit.Assert.assertEquals(null, column.getDefaultValue());
                org.junit.Assert.assertFalse(column.isNullable());
            } else if (org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN.equals(columnName)) {
                org.junit.Assert.assertEquals(java.lang.String.class, column.getType());
                org.junit.Assert.assertEquals(java.lang.Integer.valueOf(2048), column.getLength());
                org.junit.Assert.assertEquals(null, column.getDefaultValue());
                org.junit.Assert.assertTrue(column.isNullable());
            } else {
                org.junit.Assert.fail("Unexpected column name: " + columnName);
            }
        }
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo capturedLastValidColumn = lastValidColumn.getValue();
        org.junit.Assert.assertEquals(upgradeCatalog270.COMPONENT_LAST_STATE_COLUMN, capturedLastValidColumn.getName());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.State.UNKNOWN, capturedLastValidColumn.getDefaultValue());
        org.junit.Assert.assertEquals(java.lang.String.class, capturedLastValidColumn.getType());
        validateConvertingUserCreationTime(temporaryColumnCreationCapture, temporaryColumnRenameCapture);
        validateCreateUserAuthenticationTable(createUserAuthenticationTableCaptures);
        validateUpdateGroupMembershipRecords(createMembersTableCaptures);
        validateUpdateAdminPrivilegeRecords(createAdminPrincipalTableCaptures);
        validateUpdateUsersTable(updateUserTableCaptures, alterUserTableCaptures);
        validateCreateRepoOsTable(addRepoOsTableCapturedColumns, addRepoDefinitionTableCapturedColumns, addRepoTagsTableCapturedColumns, insertRepoOsTableRowColumns, insertRepoOsTableRowValues, insertAmbariSequencesRowColumns, insertAmbariSequencesRowValues);
        EasyMock.verify(dbAccessor);
    }

    private com.google.inject.Module getTestGuiceModule() {
        com.google.inject.Module module = new com.google.inject.AbstractModule() {
            @java.lang.Override
            public void configure() {
                org.apache.ambari.server.testutils.PartialNiceMockBinder.newBuilder().addConfigsBindings().addPasswordEncryptorBindings().addLdapBindings().addFactoriesInstallBinding().build().configure(binder());
                bind(org.apache.ambari.server.orm.DBAccessor.class).toInstance(dbAccessor);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(osFamily);
                bind(javax.persistence.EntityManager.class).toInstance(entityManager);
                bind(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class).toInstance(ambariConfigurationDao);
                bind(org.apache.ambari.server.topology.PersistedState.class).toInstance(EasyMock.mock(org.apache.ambari.server.topology.PersistedStateImpl.class));
                bind(org.apache.ambari.server.state.Clusters.class).toInstance(EasyMock.mock(org.apache.ambari.server.state.cluster.ClustersImpl.class));
                bind(org.apache.ambari.server.security.SecurityHelper.class).toInstance(EasyMock.mock(org.apache.ambari.server.security.SecurityHelper.class));
                bind(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class).to(org.apache.ambari.server.actionmanager.HostRoleCommandFactoryImpl.class);
                bind(org.apache.ambari.server.actionmanager.ActionDBAccessor.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class));
                bind(com.google.inject.persist.UnitOfWork.class).toInstance(EasyMock.createNiceMock(com.google.inject.persist.UnitOfWork.class));
                bind(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class).to(org.apache.ambari.server.metadata.CachedRoleCommandOrderProvider.class);
                bind(org.apache.ambari.server.actionmanager.StageFactory.class).to(org.apache.ambari.server.actionmanager.StageFactoryImpl.class);
                bind(org.apache.ambari.server.audit.AuditLogger.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.audit.AuditLoggerDefaultImpl.class));
                bind(org.springframework.security.crypto.password.PasswordEncoder.class).toInstance(new org.springframework.security.crypto.password.StandardPasswordEncoder());
                bind(org.apache.ambari.server.hooks.HookService.class).to(org.apache.ambari.server.hooks.users.UserHookService.class);
                bind(org.apache.ambari.server.state.ServiceComponentHostFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHostFactory.class));
                bind(org.apache.ambari.server.controller.AbstractRootServiceResponseFactory.class).to(org.apache.ambari.server.controller.RootServiceResponseFactory.class);
                bind(org.apache.ambari.server.security.encryption.CredentialStoreService.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class));
                bind(org.apache.ambari.server.controller.AmbariManagementController.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class));
                bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class));
                bind(org.apache.ambari.server.api.services.AmbariMetaInfo.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class));
                bind(org.apache.ambari.server.controller.KerberosHelper.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.controller.KerberosHelperImpl.class));
                bind(org.apache.ambari.server.mpack.MpackManagerFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.mpack.MpackManagerFactory.class));
                bind(org.apache.ambari.server.stack.StackManagerFactory.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.stack.StackManagerFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Host.class, org.apache.ambari.server.state.host.HostImpl.class).build(org.apache.ambari.server.state.host.HostFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Cluster.class, org.apache.ambari.server.state.cluster.ClusterImpl.class).build(org.apache.ambari.server.state.cluster.ClusterFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().build(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContextFactory.class));
                install(new com.google.inject.assistedinject.FactoryModuleBuilder().implement(org.apache.ambari.server.state.Service.class, org.apache.ambari.server.state.ServiceImpl.class).build(org.apache.ambari.server.state.ServiceFactory.class));
            }
        };
        return module;
    }

    private void prepareConvertingUsersCreationTime(org.apache.ambari.server.orm.DBAccessor dbAccessor, org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> temporaryColumnCreationCapture, org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> temporaryColumnRenameCapture) throws java.sql.SQLException {
        EasyMock.expect(dbAccessor.getColumnType(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN)).andReturn(java.sql.Types.TIMESTAMP).once();
        final java.lang.String temporaryColumnName = org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN + "_numeric";
        EasyMock.expect(dbAccessor.tableHasColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, temporaryColumnName)).andReturn(java.lang.Boolean.FALSE);
        dbAccessor.addColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE), EasyMock.capture(temporaryColumnCreationCapture));
        EasyMock.expect(dbAccessor.tableHasColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN)).andReturn(java.lang.Boolean.TRUE);
        final java.sql.Connection connectionMock = EasyMock.niceMock(java.sql.Connection.class);
        EasyMock.expect(dbAccessor.getConnection()).andReturn(connectionMock).once();
        final java.sql.PreparedStatement preparedStatementMock = EasyMock.niceMock(java.sql.PreparedStatement.class);
        EasyMock.expect(connectionMock.prepareStatement(EasyMock.anyString())).andReturn(preparedStatementMock);
        final java.sql.ResultSet resultSetMock = EasyMock.niceMock(java.sql.ResultSet.class);
        EasyMock.expect(preparedStatementMock.executeQuery()).andReturn(resultSetMock);
        EasyMock.expect(resultSetMock.next()).andReturn(java.lang.Boolean.TRUE).once();
        EasyMock.expect(resultSetMock.getInt(1)).andReturn(1).anyTimes();
        EasyMock.expect(resultSetMock.getTimestamp(2)).andReturn(new java.sql.Timestamp(1L)).anyTimes();
        EasyMock.replay(connectionMock, preparedStatementMock, resultSetMock);
        EasyMock.expect(dbAccessor.updateTable(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE), EasyMock.eq(temporaryColumnName), EasyMock.eq(1L), EasyMock.anyString())).andReturn(Matchers.anyInt());
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN);
        EasyMock.expectLastCall().once();
        dbAccessor.renameColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE), EasyMock.eq(temporaryColumnName), EasyMock.capture(temporaryColumnRenameCapture));
    }

    private void prepareCreateUserAuthenticationTable(org.apache.ambari.server.orm.DBAccessor dbAccessor, org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> capturedData) throws java.sql.SQLException {
        java.lang.String temporaryTableName = org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_TABLE + "_tmp";
        dbAccessor.dropTable(EasyMock.eq(temporaryTableName));
        EasyMock.expectLastCall().times(2);
        dbAccessor.createTable(EasyMock.eq(temporaryTableName), EasyMock.capture(capturedData));
        EasyMock.expectLastCall().once();
        EasyMock.expect(dbAccessor.executeUpdate(EasyMock.startsWith("insert into " + temporaryTableName))).andReturn(1).once();
        EasyMock.expect(dbAccessor.executeUpdate(EasyMock.startsWith("update " + temporaryTableName))).andReturn(1).once();
        dbAccessor.createTable(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_TABLE), EasyMock.capture(capturedData));
        EasyMock.expectLastCall().once();
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_PRIMARY_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_AUTHENTICATION_ID_COLUMN);
        EasyMock.expectLastCall().once();
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_AUTHENTICATION_USERS_FOREIGN_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN, false);
        EasyMock.expectLastCall().once();
        EasyMock.expect(dbAccessor.executeUpdate(EasyMock.startsWith("insert into " + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_TABLE))).andReturn(1).once();
    }

    private void validateConvertingUserCreationTime(org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> temporaryColumnCreationCapture, org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> temporaryColumnRenameCapture) {
        org.junit.Assert.assertTrue(temporaryColumnCreationCapture.hasCaptured());
        org.junit.Assert.assertTrue(temporaryColumnRenameCapture.hasCaptured());
        org.junit.Assert.assertEquals(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN, java.lang.Long.class, null, null, false), temporaryColumnRenameCapture.getValue());
    }

    private void validateCreateUserAuthenticationTable(org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> capturedData) {
        org.junit.Assert.assertTrue(capturedData.hasCaptured());
        java.util.List<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> capturedValues = capturedData.getValues();
        org.junit.Assert.assertEquals(2, capturedValues.size());
        for (java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> capturedValue : capturedValues) {
            validateColumns(capturedValue, java.util.Arrays.asList(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_AUTHENTICATION_ID_COLUMN, java.lang.Long.class, null, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_ID_COLUMN, java.lang.Integer.class, null, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_AUTHENTICATION_TYPE_COLUMN, java.lang.String.class, 50, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_AUTHENTICATION_KEY_COLUMN, java.lang.String.class, 2048, null, true), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_CREATE_TIME_COLUMN, java.lang.Long.class, null, null, true), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_UPDATE_TIME_COLUMN, java.lang.Long.class, null, null, true)));
        }
    }

    private void prepareUpdateGroupMembershipRecords(org.apache.ambari.server.orm.DBAccessor dbAccessor, org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> capturedData) throws java.sql.SQLException {
        java.lang.String temporaryTableName = org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_TABLE + "_tmp";
        dbAccessor.dropTable(EasyMock.eq(temporaryTableName));
        EasyMock.expectLastCall().times(2);
        dbAccessor.createTable(EasyMock.eq(temporaryTableName), EasyMock.capture(capturedData));
        EasyMock.expectLastCall().once();
        EasyMock.expect(dbAccessor.executeUpdate(EasyMock.startsWith("insert into " + temporaryTableName))).andReturn(1).once();
        dbAccessor.truncateTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_TABLE);
        EasyMock.expectLastCall().once();
        EasyMock.expect(dbAccessor.executeUpdate(EasyMock.startsWith("insert into " + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_TABLE))).andReturn(1).once();
    }

    private void validateUpdateGroupMembershipRecords(org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> capturedData) {
        org.junit.Assert.assertTrue(capturedData.hasCaptured());
        java.util.List<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> capturedValues = capturedData.getValues();
        org.junit.Assert.assertEquals(1, capturedValues.size());
        for (java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> capturedValue : capturedValues) {
            validateColumns(capturedValue, java.util.Arrays.asList(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_MEMBER_ID_COLUMN, java.lang.Long.class, null, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_USER_ID_COLUMN, java.lang.Long.class, null, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_GROUP_ID_COLUMN, java.lang.Long.class, null, null, false)));
        }
    }

    private void prepareUpdateAdminPrivilegeRecords(org.apache.ambari.server.orm.DBAccessor dbAccessor, org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> capturedData) throws java.sql.SQLException {
        java.lang.String temporaryTableName = org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_TABLE + "_tmp";
        dbAccessor.dropTable(EasyMock.eq(temporaryTableName));
        EasyMock.expectLastCall().times(2);
        dbAccessor.createTable(EasyMock.eq(temporaryTableName), EasyMock.capture(capturedData));
        EasyMock.expectLastCall().once();
        EasyMock.expect(dbAccessor.executeUpdate(EasyMock.startsWith("insert into " + temporaryTableName))).andReturn(1).once();
        dbAccessor.truncateTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_TABLE);
        EasyMock.expectLastCall().once();
        EasyMock.expect(dbAccessor.executeUpdate(EasyMock.startsWith("insert into " + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_TABLE))).andReturn(1).once();
    }

    private void validateUpdateAdminPrivilegeRecords(org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> capturedData) {
        org.junit.Assert.assertTrue(capturedData.hasCaptured());
        java.util.List<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> capturedValues = capturedData.getValues();
        org.junit.Assert.assertEquals(1, capturedValues.size());
        for (java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> capturedValue : capturedValues) {
            validateColumns(capturedValue, java.util.Arrays.asList(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRIVILEGE_ID_COLUMN, java.lang.Long.class, null, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PERMISSION_ID_COLUMN, java.lang.Long.class, null, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_RESOURCE_ID_COLUMN, java.lang.Long.class, null, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRINCIPAL_ID_COLUMN, java.lang.Long.class, null, null, false)));
        }
    }

    private void prepareUpdateUsersTable(org.apache.ambari.server.orm.DBAccessor dbAccessor, org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> updateUserTableCaptures, org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> alterUserTableCaptures) throws java.sql.SQLException {
        EasyMock.expect(dbAccessor.executeUpdate(EasyMock.startsWith("delete from " + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE))).andReturn(1).once();
        dbAccessor.dropUniqueConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.UNIQUE_USERS_0_INDEX);
        EasyMock.expectLastCall().once();
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_TYPE_COLUMN);
        EasyMock.expectLastCall().once();
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_LDAP_USER_COLUMN);
        EasyMock.expectLastCall().once();
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_PASSWORD_COLUMN);
        EasyMock.expectLastCall().once();
        dbAccessor.addColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE), EasyMock.capture(updateUserTableCaptures));
        EasyMock.expectLastCall().atLeastOnce();
        EasyMock.expect(dbAccessor.executeUpdate(EasyMock.startsWith("update " + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE))).andReturn(1).once();
        dbAccessor.alterColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE), EasyMock.capture(alterUserTableCaptures));
        EasyMock.expectLastCall().atLeastOnce();
        dbAccessor.addUniqueConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.UNIQUE_USERS_0_INDEX, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN);
        EasyMock.expectLastCall().once();
    }

    private void prepareUpdateRepoTables(org.apache.ambari.server.orm.DBAccessor dbAccessor, org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> addRepoOsTableCapturedColumns, org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> addRepoDefinitionTableCapturedColumns, org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> addRepoTagsTableCapturedColumns, org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> addRepoApplicableServicesTableCapturedColumns, org.easymock.Capture<java.lang.String[]> insertRepoOsTableRowColumns, org.easymock.Capture<java.lang.String[]> insertRepoOsTableRowValues, org.easymock.Capture<java.lang.String[]> insertAmbariSequencesRowColumns, org.easymock.Capture<java.lang.String[]> insertAmbariSequencesRowValues) throws java.sql.SQLException {
        dbAccessor.createTable(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_TABLE), EasyMock.capture(addRepoOsTableCapturedColumns));
        EasyMock.expectLastCall().once();
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_PRIMARY_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_ID_COLUMN);
        EasyMock.expectLastCall().once();
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_FOREIGN_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_REPO_VERSION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_REPO_VERSION_ID_COLUMN, false);
        EasyMock.expectLastCall().once();
        dbAccessor.createTable(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_TABLE), EasyMock.capture(addRepoDefinitionTableCapturedColumns));
        EasyMock.expectLastCall().once();
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_PRIMARY_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_ID_COLUMN);
        EasyMock.expectLastCall().once();
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_FOREIGN_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_REPO_OS_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_ID_COLUMN, false);
        EasyMock.expectLastCall().once();
        dbAccessor.createTable(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_TABLE), EasyMock.capture(addRepoTagsTableCapturedColumns));
        EasyMock.expectLastCall().once();
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_FOREIGN_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_REPO_DEFINITION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_ID_COLUMN, false);
        EasyMock.expectLastCall().once();
        dbAccessor.createTable(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_APPLICABLE_SERVICES_TABLE), EasyMock.capture(addRepoApplicableServicesTableCapturedColumns));
        EasyMock.expectLastCall().once();
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_APPLICABLE_SERVICES_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_APPLICABLE_SERVICES_FOREIGN_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_APPLICABLE_SERVICES_REPO_DEFINITION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_ID_COLUMN, false);
        EasyMock.expectLastCall().once();
        EasyMock.expect(dbAccessor.tableHasColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_TABLE), EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_REPOSITORIES_COLUMN))).andReturn(true).once();
        final java.util.Map<java.lang.Long, java.lang.String> repoMap = new java.util.HashMap<>();
        repoMap.put(1L, getSampleRepositoryData());
        EasyMock.expect(dbAccessor.getKeyToStringColumnMap(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_TABLE), EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_REPO_VERSION_ID_COLUMN), EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_REPOSITORIES_COLUMN), EasyMock.eq(null), EasyMock.eq(null), EasyMock.eq(true))).andReturn(repoMap).once();
        EasyMock.expect(dbAccessor.insertRowIfMissing(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_TABLE), EasyMock.capture(insertRepoOsTableRowColumns), EasyMock.capture(insertRepoOsTableRowValues), EasyMock.eq(false))).andReturn(true).once();
        EasyMock.expect(dbAccessor.insertRowIfMissing(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_TABLE), EasyMock.capture(insertAmbariSequencesRowColumns), EasyMock.capture(insertAmbariSequencesRowValues), EasyMock.eq(false))).andReturn(true).once();
        EasyMock.expect(dbAccessor.insertRowIfMissing(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_TABLE), EasyMock.capture(insertAmbariSequencesRowColumns), EasyMock.capture(insertAmbariSequencesRowValues), EasyMock.eq(false))).andReturn(true).once();
        dbAccessor.dropColumn(EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_TABLE), EasyMock.eq(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_REPOSITORIES_COLUMN));
        EasyMock.expectLastCall().once();
    }

    private java.lang.String getSampleRepositoryData() {
        return "[{\"repositories\":[],\"OperatingSystems/os_type\":\"redhat7\"}]";
    }

    private void validateUpdateUsersTable(org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> updateUserTableCaptures, org.easymock.Capture<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> alterUserTableCaptures) {
        org.junit.Assert.assertTrue(updateUserTableCaptures.hasCaptured());
        validateColumns(updateUserTableCaptures.getValues(), java.util.Arrays.asList(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CONSECUTIVE_FAILURES_COLUMN, java.lang.Integer.class, null, 0, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_DISPLAY_NAME_COLUMN, java.lang.String.class, 255, null, true), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_LOCAL_USERNAME_COLUMN, java.lang.String.class, 255, null, true), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_VERSION_COLUMN, java.lang.Long.class, null, 0, false)));
        org.junit.Assert.assertTrue(alterUserTableCaptures.hasCaptured());
        validateColumns(alterUserTableCaptures.getValues(), java.util.Arrays.asList(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_DISPLAY_NAME_COLUMN, java.lang.String.class, 255, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_LOCAL_USERNAME_COLUMN, java.lang.String.class, 255, null, false)));
    }

    private void validateCreateRepoOsTable(org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> addRepoOsTableCapturedColumns, org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> addRepoDefinitionTableCapturedColumns, org.easymock.Capture<java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo>> addRepoTagsTableCapturedColumns, org.easymock.Capture<java.lang.String[]> insertRepoOsTableRowColumns, org.easymock.Capture<java.lang.String[]> insertRepoOsTableRowValues, org.easymock.Capture<java.lang.String[]> insertAmbariSequencesRowColumns, org.easymock.Capture<java.lang.String[]> insertAmbariSequencesRowValues) {
        org.junit.Assert.assertTrue(addRepoOsTableCapturedColumns.hasCaptured());
        validateColumns(addRepoOsTableCapturedColumns.getValue(), java.util.Arrays.asList(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_ID_COLUMN, java.lang.Long.class, null, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_REPO_VERSION_ID_COLUMN, java.lang.Long.class, null, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_FAMILY_COLUMN, java.lang.String.class, 255, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_AMBARI_MANAGED_COLUMN, java.lang.Integer.class, null, 1, true)));
        org.junit.Assert.assertTrue(addRepoDefinitionTableCapturedColumns.hasCaptured());
        validateColumns(addRepoDefinitionTableCapturedColumns.getValue(), java.util.Arrays.asList(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_ID_COLUMN, java.lang.Long.class, null, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_REPO_OS_ID_COLUMN, java.lang.Long.class, null, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_REPO_NAME_COLUMN, java.lang.String.class, 255, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_REPO_ID_COLUMN, java.lang.String.class, 255, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_BASE_URL_COLUMN, java.lang.String.class, 2048, null, true), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_DISTRIBUTION_COLUMN, java.lang.String.class, 2048, null, true), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_COMPONENTS_COLUMN, java.lang.String.class, 2048, null, true), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_UNIQUE_REPO_COLUMN, java.lang.Integer.class, 1, 1, true), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_MIRRORS_COLUMN, java.lang.String.class, 2048, null, true)));
        org.junit.Assert.assertTrue(addRepoTagsTableCapturedColumns.hasCaptured());
        validateColumns(addRepoTagsTableCapturedColumns.getValue(), java.util.Arrays.asList(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_REPO_DEFINITION_ID_COLUMN, java.lang.Long.class, null, null, false), new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_TAG_COLUMN, java.lang.String.class, 255, null, false)));
        java.util.List<java.lang.String[]> values;
        org.junit.Assert.assertTrue(insertRepoOsTableRowColumns.hasCaptured());
        values = insertRepoOsTableRowColumns.getValues();
        org.junit.Assert.assertEquals(1, values.size());
        org.junit.Assert.assertArrayEquals(new java.lang.String[]{ org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_REPO_VERSION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_AMBARI_MANAGED_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_FAMILY_COLUMN }, values.get(0));
        org.junit.Assert.assertTrue(insertRepoOsTableRowValues.hasCaptured());
        values = insertRepoOsTableRowValues.getValues();
        org.junit.Assert.assertEquals(1, values.size());
        org.junit.Assert.assertArrayEquals(new java.lang.String[]{ "1", "1", "1", "'redhat7'" }, values.get(0));
        org.junit.Assert.assertTrue(insertAmbariSequencesRowColumns.hasCaptured());
        values = insertAmbariSequencesRowColumns.getValues();
        org.junit.Assert.assertEquals(2, values.size());
        org.junit.Assert.assertArrayEquals(new java.lang.String[]{ org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_SEQUENCE_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_SEQUENCE_VALUE_COLUMN }, values.get(0));
        org.junit.Assert.assertArrayEquals(new java.lang.String[]{ org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_SEQUENCE_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_SEQUENCE_VALUE_COLUMN }, values.get(1));
        org.junit.Assert.assertTrue(insertAmbariSequencesRowValues.hasCaptured());
        values = insertAmbariSequencesRowValues.getValues();
        org.junit.Assert.assertEquals(2, values.size());
        org.junit.Assert.assertArrayEquals(new java.lang.String[]{ "'repo_os_id_seq'", "2" }, values.get(0));
        org.junit.Assert.assertArrayEquals(new java.lang.String[]{ "'repo_definition_id_seq'", "1" }, values.get(1));
    }

    private void validateColumns(java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> capturedColumns, java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> expectedColumns) {
        org.junit.Assert.assertEquals(expectedColumns.size(), capturedColumns.size());
        expectedColumns = new java.util.ArrayList<>(expectedColumns);
        capturedColumns = new java.util.ArrayList<>(capturedColumns);
        java.util.Iterator<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> capturedColumnIterator = capturedColumns.iterator();
        while (capturedColumnIterator.hasNext()) {
            org.apache.ambari.server.orm.DBAccessor.DBColumnInfo capturedColumnInfo = capturedColumnIterator.next();
            java.util.Iterator<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> expectedColumnIterator = expectedColumns.iterator();
            while (expectedColumnIterator.hasNext()) {
                org.apache.ambari.server.orm.DBAccessor.DBColumnInfo expectedColumnInfo = expectedColumnIterator.next();
                if (expectedColumnInfo.equals(capturedColumnInfo)) {
                    expectedColumnIterator.remove();
                    capturedColumnIterator.remove();
                    break;
                }
            } 
        } 
        org.junit.Assert.assertTrue("Not all captured columns were expected", capturedColumns.isEmpty());
        org.junit.Assert.assertTrue("Not all expected columns were captured", expectedColumns.isEmpty());
    }

    @org.junit.Test
    public void testLogSearchUpdateConfigs() throws java.lang.Exception {
        EasyMock.reset(clusters, cluster);
        EasyMock.expect(clusters.getClusters()).andReturn(com.google.common.collect.ImmutableMap.of("normal", cluster)).once();
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        com.google.inject.Injector injector2 = easyMockSupport.createNiceMock(com.google.inject.Injector.class);
        org.apache.ambari.server.controller.AmbariManagementControllerImpl controller = EasyMock.createMockBuilder(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class).addMockedMethod("createConfiguration", org.apache.ambari.server.controller.ConfigurationRequest.class).addMockedMethod("getClusters", new java.lang.Class[]{  }).addMockedMethod("createConfig", org.apache.ambari.server.state.Cluster.class, org.apache.ambari.server.state.StackId.class, java.lang.String.class, java.util.Map.class, java.lang.String.class, java.util.Map.class).createNiceMock();
        org.apache.ambari.server.state.ConfigHelper configHelper = EasyMock.createMockBuilder(org.apache.ambari.server.state.ConfigHelper.class).addMockedMethod("createConfigType", org.apache.ambari.server.state.Cluster.class, org.apache.ambari.server.state.StackId.class, org.apache.ambari.server.controller.AmbariManagementController.class, java.lang.String.class, java.util.Map.class, java.lang.String.class, java.lang.String.class).createMock();
        EasyMock.expect(injector2.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class)).andReturn(controller).anyTimes();
        EasyMock.expect(injector2.getInstance(org.apache.ambari.server.state.ConfigHelper.class)).andReturn(configHelper).anyTimes();
        EasyMock.expect(injector2.getInstance(org.apache.ambari.server.orm.DBAccessor.class)).andReturn(dbAccessor).anyTimes();
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        configHelper.createConfigType(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.eq(controller), EasyMock.eq("logsearch-common-properties"), EasyMock.eq(java.util.Collections.emptyMap()), EasyMock.eq("ambari-upgrade"), EasyMock.eq("Updated logsearch-common-properties during Ambari Upgrade from 2.6.0 to 3.0.0"));
        EasyMock.expectLastCall().once();
        java.util.Map<java.lang.String, java.lang.String> oldLogSearchProperties = com.google.common.collect.ImmutableMap.of("logsearch.logfeeder.include.default.level", "FATAL,ERROR,WARN");
        java.util.Map<java.lang.String, java.lang.String> expectedLogFeederProperties = com.google.common.collect.ImmutableMap.of("logfeeder.include.default.level", "FATAL,ERROR,WARN");
        org.apache.ambari.server.state.Config logFeederPropertiesConf = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(cluster.getDesiredConfigByType("logfeeder-properties")).andReturn(logFeederPropertiesConf).times(2);
        EasyMock.expect(logFeederPropertiesConf.getProperties()).andReturn(java.util.Collections.emptyMap()).once();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> logFeederPropertiesCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(controller.createConfig(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.eq("logfeeder-properties"), EasyMock.capture(logFeederPropertiesCapture), EasyMock.anyString(), org.easymock.EasyMock.anyObject())).andReturn(config).once();
        org.apache.ambari.server.state.Config logSearchPropertiesConf = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(cluster.getDesiredConfigByType("logsearch-properties")).andReturn(logSearchPropertiesConf).times(2);
        EasyMock.expect(logSearchPropertiesConf.getProperties()).andReturn(oldLogSearchProperties).times(2);
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> logSearchPropertiesCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(controller.createConfig(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.eq("logsearch-properties"), EasyMock.capture(logSearchPropertiesCapture), EasyMock.anyString(), org.easymock.EasyMock.anyObject())).andReturn(config).once();
        java.util.Map<java.lang.String, java.lang.String> oldLogFeederLog4j = com.google.common.collect.ImmutableMap.of("content", "<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">");
        java.util.Map<java.lang.String, java.lang.String> expectedLogFeederLog4j = com.google.common.collect.ImmutableMap.of("content", "<!DOCTYPE log4j:configuration SYSTEM \"http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/xml/doc-files/log4j.dtd\">");
        org.apache.ambari.server.state.Config logFeederLog4jConf = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(cluster.getDesiredConfigByType("logfeeder-log4j")).andReturn(logFeederLog4jConf).atLeastOnce();
        EasyMock.expect(logFeederLog4jConf.getProperties()).andReturn(oldLogFeederLog4j).anyTimes();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> logFeederLog4jCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(controller.createConfig(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyString(), EasyMock.capture(logFeederLog4jCapture), EasyMock.anyString(), org.easymock.EasyMock.anyObject())).andReturn(config).once();
        java.util.Map<java.lang.String, java.lang.String> oldLogSearchLog4j = com.google.common.collect.ImmutableMap.of("content", "<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">");
        java.util.Map<java.lang.String, java.lang.String> expectedLogSearchLog4j = com.google.common.collect.ImmutableMap.of("content", "<!DOCTYPE log4j:configuration SYSTEM \"http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/xml/doc-files/log4j.dtd\">");
        org.apache.ambari.server.state.Config logSearchLog4jConf = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(cluster.getDesiredConfigByType("logsearch-log4j")).andReturn(logSearchLog4jConf).atLeastOnce();
        EasyMock.expect(logSearchLog4jConf.getProperties()).andReturn(oldLogSearchLog4j).anyTimes();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> logSearchLog4jCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(controller.createConfig(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyString(), EasyMock.capture(logSearchLog4jCapture), EasyMock.anyString(), org.easymock.EasyMock.anyObject())).andReturn(config).once();
        java.util.Map<java.lang.String, java.lang.String> oldLogSearchServiceLogsConf = com.google.common.collect.ImmutableMap.of("content", "<before/><requestHandler name=\"/admin/\"   class=\"solr.admin.AdminHandlers\" /><after/>");
        java.util.Map<java.lang.String, java.lang.String> expectedLogSearchServiceLogsConf = com.google.common.collect.ImmutableMap.of("content", "<before/><after/>");
        org.apache.ambari.server.state.Config logSearchServiceLogsConf = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(cluster.getDesiredConfigByType("logsearch-service_logs-solrconfig")).andReturn(logSearchServiceLogsConf).atLeastOnce();
        EasyMock.expect(logSearchServiceLogsConf.getProperties()).andReturn(oldLogSearchServiceLogsConf).anyTimes();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> logSearchServiceLogsConfCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(controller.createConfig(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyString(), EasyMock.capture(logSearchServiceLogsConfCapture), EasyMock.anyString(), org.easymock.EasyMock.anyObject())).andReturn(config).once();
        java.util.Map<java.lang.String, java.lang.String> oldLogSearchAuditLogsConf = com.google.common.collect.ImmutableMap.of("content", "<before/><requestHandler name=\"/admin/\"   class=\"solr.admin.AdminHandlers\" /><after/>");
        java.util.Map<java.lang.String, java.lang.String> expectedLogSearchAuditLogsConf = com.google.common.collect.ImmutableMap.of("content", "<before/><after/>");
        org.apache.ambari.server.state.Config logSearchAuditLogsConf = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(cluster.getDesiredConfigByType("logsearch-audit_logs-solrconfig")).andReturn(logSearchAuditLogsConf).atLeastOnce();
        EasyMock.expect(logSearchAuditLogsConf.getProperties()).andReturn(oldLogSearchAuditLogsConf).anyTimes();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> logSearchAuditLogsConfCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(controller.createConfig(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyString(), EasyMock.capture(logSearchAuditLogsConfCapture), EasyMock.anyString(), org.easymock.EasyMock.anyObject())).andReturn(config).once();
        java.util.Map<java.lang.String, java.lang.String> oldLogFeederOutputConf = com.google.common.collect.ImmutableMap.of("content", "      \"zk_connect_string\":\"{{logsearch_solr_zk_quorum}}{{logsearch_solr_zk_znode}}\",\n" + ((((((("      \"collection\":\"{{logsearch_solr_collection_service_logs}}\",\n" + "      \"number_of_shards\": \"{{logsearch_collection_service_logs_numshards}}\",\n") + "      \"splits_interval_mins\": \"{{logsearch_service_logs_split_interval_mins}}\",\n") + "\n") + "      \"zk_connect_string\":\"{{logsearch_solr_zk_quorum}}{{logsearch_solr_zk_znode}}\",\n") + "      \"collection\":\"{{logsearch_solr_collection_audit_logs}}\",\n") + "      \"number_of_shards\": \"{{logsearch_collection_audit_logs_numshards}}\",\n") + "      \"splits_interval_mins\": \"{{logsearch_audit_logs_split_interval_mins}}\",\n"));
        java.util.Map<java.lang.String, java.lang.String> expectedLogFeederOutputConf = com.google.common.collect.ImmutableMap.of("content", "      \"zk_connect_string\":\"{{logsearch_solr_zk_quorum}}{{logsearch_solr_zk_znode}}\",\n" + ((("      \"type\": \"service\",\n" + "\n") + "      \"zk_connect_string\":\"{{logsearch_solr_zk_quorum}}{{logsearch_solr_zk_znode}}\",\n") + "      \"type\": \"audit\",\n"));
        org.apache.ambari.server.state.Config logFeederOutputConf = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(cluster.getDesiredConfigByType("logfeeder-output-config")).andReturn(logFeederOutputConf).atLeastOnce();
        EasyMock.expect(logFeederOutputConf.getProperties()).andReturn(oldLogFeederOutputConf).anyTimes();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> logFeederOutputConfCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(controller.createConfig(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyString(), EasyMock.capture(logFeederOutputConfCapture), EasyMock.anyString(), org.easymock.EasyMock.anyObject())).andReturn(config).once();
        java.lang.String serviceConfigMapping = "serviceconfigmapping";
        java.lang.String clusterConfig = "clusterconfig";
        dbAccessor.executeQuery(EasyMock.startsWith("DELETE FROM " + serviceConfigMapping));
        EasyMock.expectLastCall().once();
        dbAccessor.executeQuery(EasyMock.startsWith("DELETE FROM " + clusterConfig));
        EasyMock.expectLastCall().once();
        EasyMock.replay(clusters, cluster, dbAccessor);
        EasyMock.replay(controller, injector2);
        EasyMock.replay(logSearchPropertiesConf, logFeederPropertiesConf);
        EasyMock.replay(logFeederLog4jConf, logSearchLog4jConf);
        EasyMock.replay(logSearchServiceLogsConf, logSearchAuditLogsConf);
        EasyMock.replay(logFeederOutputConf);
        new org.apache.ambari.server.upgrade.UpgradeCatalog270(injector2).updateLogSearchConfigs();
        easyMockSupport.verifyAll();
        java.util.Map<java.lang.String, java.lang.String> newLogFeederProperties = logFeederPropertiesCapture.getValue();
        org.junit.Assert.assertTrue(com.google.common.collect.Maps.difference(expectedLogFeederProperties, newLogFeederProperties).areEqual());
        java.util.Map<java.lang.String, java.lang.String> newLogSearchProperties = logSearchPropertiesCapture.getValue();
        org.junit.Assert.assertTrue(com.google.common.collect.Maps.difference(java.util.Collections.emptyMap(), newLogSearchProperties).areEqual());
        java.util.Map<java.lang.String, java.lang.String> updatedLogFeederLog4j = logFeederLog4jCapture.getValue();
        org.junit.Assert.assertTrue(com.google.common.collect.Maps.difference(expectedLogFeederLog4j, updatedLogFeederLog4j).areEqual());
        java.util.Map<java.lang.String, java.lang.String> updatedLogSearchLog4j = logSearchLog4jCapture.getValue();
        org.junit.Assert.assertTrue(com.google.common.collect.Maps.difference(expectedLogSearchLog4j, updatedLogSearchLog4j).areEqual());
        java.util.Map<java.lang.String, java.lang.String> updatedServiceLogsConf = logSearchServiceLogsConfCapture.getValue();
        org.junit.Assert.assertTrue(com.google.common.collect.Maps.difference(expectedLogSearchServiceLogsConf, updatedServiceLogsConf).areEqual());
        java.util.Map<java.lang.String, java.lang.String> updatedAuditLogsConf = logSearchAuditLogsConfCapture.getValue();
        org.junit.Assert.assertTrue(com.google.common.collect.Maps.difference(expectedLogSearchAuditLogsConf, updatedAuditLogsConf).areEqual());
        java.util.Map<java.lang.String, java.lang.String> updatedLogFeederOutputConf = logFeederOutputConfCapture.getValue();
        org.junit.Assert.assertTrue(com.google.common.collect.Maps.difference(expectedLogFeederOutputConf, updatedLogFeederOutputConf).areEqual());
    }

    @org.junit.Test
    public void testUpdateKerberosConfigurations() throws org.apache.ambari.server.AmbariException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP", "2.6.0.0");
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> propertiesWithGroup = new java.util.HashMap<>();
        propertiesWithGroup.put("group", "ambari_managed_identities");
        propertiesWithGroup.put("kdc_host", "host1.example.com");
        propertiesWithGroup.put("realm", "example.com");
        org.apache.ambari.server.state.Config newConfig = EasyMock.createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(newConfig.getTag()).andReturn("version2").atLeastOnce();
        EasyMock.expect(newConfig.getType()).andReturn("kerberos-env").atLeastOnce();
        org.apache.ambari.server.controller.ServiceConfigVersionResponse response = EasyMock.createMock(org.apache.ambari.server.controller.ServiceConfigVersionResponse.class);
        org.apache.ambari.server.state.Config configWithGroup = EasyMock.createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(configWithGroup.getProperties()).andReturn(propertiesWithGroup).atLeastOnce();
        EasyMock.expect(configWithGroup.getPropertiesAttributes()).andReturn(java.util.Collections.emptyMap()).atLeastOnce();
        EasyMock.expect(configWithGroup.getTag()).andReturn("version1").atLeastOnce();
        org.apache.ambari.server.state.Cluster cluster1 = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster1.getDesiredConfigByType("kerberos-env")).andReturn(configWithGroup).atLeastOnce();
        EasyMock.expect(cluster1.getConfigsByType("kerberos-env")).andReturn(java.util.Collections.singletonMap("v1", configWithGroup)).atLeastOnce();
        EasyMock.expect(cluster1.getServiceByConfigType("kerberos-env")).andReturn("KERBEROS").atLeastOnce();
        EasyMock.expect(cluster1.getClusterName()).andReturn("c1").atLeastOnce();
        EasyMock.expect(cluster1.getDesiredStackVersion()).andReturn(stackId).atLeastOnce();
        EasyMock.expect(cluster1.getConfig(EasyMock.eq("kerberos-env"), EasyMock.anyString())).andReturn(newConfig).atLeastOnce();
        EasyMock.expect(cluster1.addDesiredConfig("ambari-upgrade", java.util.Collections.singleton(newConfig), "Updated kerberos-env during Ambari Upgrade from 2.6.2 to 2.7.0.")).andReturn(response).once();
        java.util.Map<java.lang.String, java.lang.String> propertiesWithoutGroup = new java.util.HashMap<>();
        propertiesWithoutGroup.put("kdc_host", "host2.example.com");
        propertiesWithoutGroup.put("realm", "example.com");
        org.apache.ambari.server.state.Config configWithoutGroup = EasyMock.createMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(configWithoutGroup.getProperties()).andReturn(propertiesWithoutGroup).atLeastOnce();
        org.apache.ambari.server.state.Cluster cluster2 = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster2.getDesiredConfigByType("kerberos-env")).andReturn(configWithoutGroup).atLeastOnce();
        org.apache.ambari.server.state.Cluster cluster3 = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(cluster3.getDesiredConfigByType("kerberos-env")).andReturn(null).atLeastOnce();
        clusterMap.put("c1", cluster1);
        clusterMap.put("c2", cluster2);
        clusterMap.put("c3", cluster3);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        EasyMock.expect(clusters.getClusters()).andReturn(clusterMap).anyTimes();
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> capturedProperties = EasyMock.newCapture();
        org.apache.ambari.server.controller.AmbariManagementControllerImpl controller = EasyMock.createMockBuilder(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class).addMockedMethod("createConfiguration", org.apache.ambari.server.controller.ConfigurationRequest.class).addMockedMethod("getClusters", new java.lang.Class[]{  }).addMockedMethod("createConfig", org.apache.ambari.server.state.Cluster.class, org.apache.ambari.server.state.StackId.class, java.lang.String.class, java.util.Map.class, java.lang.String.class, java.util.Map.class).addMockedMethod("getClusterMetadataOnConfigsUpdate", org.apache.ambari.server.state.Cluster.class).createMock();
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(controller.createConfig(EasyMock.eq(cluster1), EasyMock.eq(stackId), EasyMock.eq("kerberos-env"), EasyMock.capture(capturedProperties), EasyMock.anyString(), EasyMock.anyObject(java.util.Map.class))).andReturn(newConfig).once();
        com.google.inject.Injector injector = EasyMock.createNiceMock(com.google.inject.Injector.class);
        org.apache.ambari.server.state.ConfigHelper configHelper = EasyMock.createStrictMock(org.apache.ambari.server.state.ConfigHelper.class);
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class)).andReturn(controller).anyTimes();
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.agent.stomp.MetadataHolder.class)).andReturn(EasyMock.createNiceMock(org.apache.ambari.server.agent.stomp.MetadataHolder.class)).anyTimes();
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class)).andReturn(EasyMock.createNiceMock(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class)).anyTimes();
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.controller.AmbariServer.class)).andReturn(EasyMock.createNiceMock(org.apache.ambari.server.controller.AmbariServer.class)).anyTimes();
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class)).andReturn(configHelper).anyTimes();
        org.apache.ambari.server.controller.KerberosHelper kerberosHelperMock = EasyMock.createNiceMock(org.apache.ambari.server.controller.KerberosHelper.class);
        EasyMock.expect(kerberosHelperMock.createTemporaryDirectory()).andReturn(new java.io.File("/invalid/file/path")).times(2);
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class)).andReturn(kerberosHelperMock).anyTimes();
        configHelper.updateAgentConfigs(EasyMock.anyObject(java.util.Set.class));
        EasyMock.expectLastCall();
        EasyMock.replay(controller, clusters, cluster1, cluster2, configWithGroup, configWithoutGroup, newConfig, response, injector, kerberosHelperMock, configHelper);
        java.lang.reflect.Field field = org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.class.getDeclaredField("configuration");
        org.apache.ambari.server.upgrade.UpgradeCatalog270 upgradeCatalog270 = EasyMock.createMockBuilder(org.apache.ambari.server.upgrade.UpgradeCatalog270.class).addMockedMethod("getPrepareIdentityServerAction").addMockedMethod("executeInTransaction").createMock();
        org.apache.ambari.server.serveraction.kerberos.PrepareKerberosIdentitiesServerAction mockAction = EasyMock.createNiceMock(org.apache.ambari.server.serveraction.kerberos.PrepareKerberosIdentitiesServerAction.class);
        EasyMock.expect(upgradeCatalog270.getPrepareIdentityServerAction()).andReturn(mockAction).times(2);
        upgradeCatalog270.executeInTransaction(EasyMock.anyObject());
        EasyMock.expectLastCall().times(2);
        upgradeCatalog270.injector = injector;
        EasyMock.replay(upgradeCatalog270);
        field.set(upgradeCatalog270, EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class));
        upgradeCatalog270.updateKerberosConfigurations();
        EasyMock.verify(controller, clusters, cluster1, cluster2, configWithGroup, configWithoutGroup, newConfig, response, injector, upgradeCatalog270, configHelper);
        org.junit.Assert.assertEquals(1, capturedProperties.getValues().size());
        java.util.Map<java.lang.String, java.lang.String> properties = capturedProperties.getValue();
        org.junit.Assert.assertEquals(3, properties.size());
        org.junit.Assert.assertEquals("ambari_managed_identities", properties.get("ipa_user_group"));
        org.junit.Assert.assertEquals("host1.example.com", properties.get("kdc_host"));
        org.junit.Assert.assertEquals("example.com", properties.get("realm"));
        org.junit.Assert.assertEquals(3, propertiesWithGroup.size());
        org.junit.Assert.assertEquals("ambari_managed_identities", propertiesWithGroup.get("group"));
        org.junit.Assert.assertEquals("host1.example.com", propertiesWithGroup.get("kdc_host"));
        org.junit.Assert.assertEquals("example.com", propertiesWithGroup.get("realm"));
    }

    @org.junit.Test
    public void shouldSaveLdapConfigurationIfPropertyIsSetInAmbariProperties() throws java.lang.Exception {
        final com.google.inject.Module module = getTestGuiceModule();
        EasyMock.expect(entityManager.find(EasyMock.anyObject(), EasyMock.anyObject())).andReturn(null).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED.key(), "true");
        properties.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.AMBARI_MANAGES_LDAP_CONFIGURATION.key(), "true");
        properties.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED_SERVICES.key(), "AMBARI");
        EasyMock.expect(ambariConfigurationDao.reconcileCategory(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName(), properties, false)).andReturn(true).once();
        EasyMock.replay(entityManager, ambariConfigurationDao);
        final com.google.inject.Injector injector = com.google.inject.Guice.createInjector(module);
        injector.getInstance(org.apache.ambari.server.configuration.Configuration.class).setProperty("ambari.ldap.isConfigured", "true");
        final org.apache.ambari.server.upgrade.UpgradeCatalog270 upgradeCatalog270 = new org.apache.ambari.server.upgrade.UpgradeCatalog270(injector);
        upgradeCatalog270.moveAmbariPropertiesToAmbariConfiguration();
        EasyMock.verify(entityManager, ambariConfigurationDao);
    }

    @org.junit.Test
    public void shouldNotSaveLdapConfigurationIfPropertyIsNotSetInAmbariProperties() throws java.lang.Exception {
        final com.google.inject.Module module = getTestGuiceModule();
        EasyMock.expect(entityManager.find(EasyMock.anyObject(), EasyMock.anyObject())).andReturn(null).anyTimes();
        final java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED.key(), "true");
        EasyMock.expect(ambariConfigurationDao.reconcileCategory(org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName(), properties, false)).andReturn(true).once();
        EasyMock.replay(entityManager, ambariConfigurationDao);
        final com.google.inject.Injector injector = com.google.inject.Guice.createInjector(module);
        final org.apache.ambari.server.upgrade.UpgradeCatalog270 upgradeCatalog270 = new org.apache.ambari.server.upgrade.UpgradeCatalog270(injector);
        upgradeCatalog270.moveAmbariPropertiesToAmbariConfiguration();
        expectedException.expect(java.lang.AssertionError.class);
        expectedException.expectMessage("Expectation failure on verify");
        EasyMock.verify(entityManager, ambariConfigurationDao);
    }

    @org.junit.Test
    public void testupdateKerberosDescriptorArtifact() throws java.lang.Exception {
        java.lang.String kerberosDescriptorJson = org.apache.commons.io.IOUtils.toString(getClass().getClassLoader().getResourceAsStream("org/apache/ambari/server/upgrade/kerberos_descriptor.json"), "UTF-8");
        org.junit.Assert.assertTrue(kerberosDescriptorJson.contains("${clusterHostInfo/webhcat_server_host|append(core-site/hadoop.proxyuser.HTTP.hosts, \\\\\\\\,, true)}"));
        org.junit.Assert.assertTrue(kerberosDescriptorJson.contains("${clusterHostInfo/rm_host}"));
        org.apache.ambari.server.orm.entities.ArtifactEntity artifactEntity = new org.apache.ambari.server.orm.entities.ArtifactEntity();
        artifactEntity.setArtifactName("kerberos_descriptor");
        artifactEntity.setArtifactData(org.apache.ambari.server.upgrade.UpgradeCatalog270Test.GSON.<java.util.Map<java.lang.String, java.lang.Object>>fromJson(kerberosDescriptorJson, java.util.Map.class));
        org.apache.ambari.server.upgrade.UpgradeCatalog270 upgradeCatalog270 = EasyMock.createMockBuilder(org.apache.ambari.server.upgrade.UpgradeCatalog270.class).createMock();
        EasyMock.expect(artifactDAO.merge(artifactEntity)).andReturn(artifactEntity);
        EasyMock.replay(upgradeCatalog270);
        upgradeCatalog270.updateKerberosDescriptorArtifact(artifactDAO, artifactEntity);
        final java.lang.String newKerberosDescriptorJson = org.apache.ambari.server.upgrade.UpgradeCatalog270Test.GSON.toJson(artifactEntity.getArtifactData());
        int oldCount = substringCount(kerberosDescriptorJson, org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME);
        int newCount = substringCount(newKerberosDescriptorJson, org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
        org.junit.Assert.assertThat(newCount, org.hamcrest.core.Is.is(oldCount));
        org.junit.Assert.assertTrue(newKerberosDescriptorJson.contains("${clusterHostInfo/webhcat_server_hosts|append(core-site/hadoop.proxyuser.HTTP.hosts, \\\\,, true)}"));
        org.junit.Assert.assertTrue(newKerberosDescriptorJson.contains("${clusterHostInfo/resourcemanager_hosts}"));
        EasyMock.verify(upgradeCatalog270);
    }

    private int substringCount(java.lang.String source, java.lang.String substring) {
        int count = 0;
        int i = -1;
        while ((i = source.indexOf(substring, i + 1)) != (-1)) {
            ++count;
        } 
        return count;
    }

    @org.junit.Test
    public void testupdateLuceneMatchVersion() throws java.lang.Exception {
        java.lang.String solrConfigXml = org.apache.commons.io.IOUtils.toString(getClass().getClassLoader().getResourceAsStream("org/apache/ambari/server/upgrade/solrconfig-v500.xml.j2"), "UTF-8");
        org.apache.ambari.server.upgrade.UpgradeCatalog270 upgradeCatalog270 = EasyMock.createMockBuilder(org.apache.ambari.server.upgrade.UpgradeCatalog270.class).createMock();
        EasyMock.replay(upgradeCatalog270);
        java.lang.String updated = upgradeCatalog270.updateLuceneMatchVersion(solrConfigXml, "7.3.1");
        org.junit.Assert.assertThat(updated.contains("<luceneMatchVersion>7.3.1</luceneMatchVersion>"), org.hamcrest.core.Is.is(true));
        org.junit.Assert.assertThat(updated.contains("<luceneMatchVersion>5.0.0</luceneMatchVersion>"), org.hamcrest.core.Is.is(false));
        EasyMock.verify(upgradeCatalog270);
    }

    @org.junit.Test
    public void testupdateMergeFactor() throws java.lang.Exception {
        java.lang.String solrConfigXml = org.apache.commons.io.IOUtils.toString(getClass().getClassLoader().getResourceAsStream("org/apache/ambari/server/upgrade/solrconfig-v500.xml.j2"), "UTF-8");
        org.apache.ambari.server.upgrade.UpgradeCatalog270 upgradeCatalog270 = EasyMock.createMockBuilder(org.apache.ambari.server.upgrade.UpgradeCatalog270.class).createMock();
        EasyMock.replay(upgradeCatalog270);
        java.lang.String updated = upgradeCatalog270.updateMergeFactor(solrConfigXml, "logsearch_service_logs_merge_factor");
        org.junit.Assert.assertThat(updated.contains("<int name=\"maxMergeAtOnce\">{{logsearch_service_logs_merge_factor}}</int>"), org.hamcrest.core.Is.is(true));
        org.junit.Assert.assertThat(updated.contains("<int name=\"segmentsPerTier\">{{logsearch_service_logs_merge_factor}}</int>"), org.hamcrest.core.Is.is(true));
        org.junit.Assert.assertThat(updated.contains("<mergeFactor>{{logsearch_service_logs_merge_factor}}</mergeFactor>"), org.hamcrest.core.Is.is(false));
        EasyMock.verify(upgradeCatalog270);
    }

    @org.junit.Test
    public void testupdateInfraSolrEnv() {
        java.lang.String solrConfigXml = "#SOLR_HOST=\"192.168.1.1\"\n" + (("SOLR_HOST=\"192.168.1.1\"\n" + "SOLR_KERB_NAME_RULES=\"{{infra_solr_kerberos_name_rules}}\"\n") + "SOLR_AUTHENTICATION_CLIENT_CONFIGURER=\"org.apache.solr.client.solrj.impl.Krb5HttpClientConfigurer\"");
        org.apache.ambari.server.upgrade.UpgradeCatalog270 upgradeCatalog270 = EasyMock.createMockBuilder(org.apache.ambari.server.upgrade.UpgradeCatalog270.class).createMock();
        EasyMock.replay(upgradeCatalog270);
        java.lang.String updated = upgradeCatalog270.updateInfraSolrEnv(solrConfigXml);
        org.junit.Assert.assertThat(updated, org.hamcrest.core.Is.is("SOLR_HOST=`hostname -f`\nSOLR_HOST=`hostname -f`\n\nSOLR_AUTH_TYPE=\"kerberos\""));
        EasyMock.verify(upgradeCatalog270);
    }

    @org.junit.Test
    public void testRemoveAdminHandlers() {
        org.apache.ambari.server.upgrade.UpgradeCatalog270 upgradeCatalog270 = EasyMock.createMockBuilder(org.apache.ambari.server.upgrade.UpgradeCatalog270.class).createMock();
        EasyMock.replay(upgradeCatalog270);
        java.lang.String updated = upgradeCatalog270.removeAdminHandlers("<requestHandler name=\"/admin/\"\n" + "                  class=\"solr.admin.AdminHandlers\"/>");
        org.junit.Assert.assertThat(updated, org.hamcrest.core.Is.is(""));
        EasyMock.verify(upgradeCatalog270);
    }

    @org.junit.Test
    public void testUpdateAmsConfigs() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> oldProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("timeline.metrics.service.default.result.limit", "15840");
                put("timeline.container-metrics.ttl", "2592000");
                put("timeline.metrics.cluster.aggregate.splitpoints", "cpu_user,mem_free");
                put("timeline.metrics.host.aggregate.splitpoints", "kafka.metric,nimbus.metric");
                put("timeline.metrics.downsampler.topn.metric.patterns", "dfs.NNTopUserOpCounts.windowMs=60000.op=__%.user=%," + "dfs.NNTopUserOpCounts.windowMs=300000.op=__%.user=%,dfs.NNTopUserOpCounts.windowMs=1500000.op=__%.user=%");
            }
        };
        java.util.Map<java.lang.String, java.lang.String> newProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("timeline.metrics.service.default.result.limit", "5760");
                put("timeline.container-metrics.ttl", "1209600");
                put("timeline.metrics.downsampler.topn.metric.patterns", StringUtils.EMPTY);
            }
        };
        java.util.Map<java.lang.String, java.lang.String> oldAmsHBaseSiteProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("hbase.snapshot.enabled", "false");
            }
        };
        java.util.Map<java.lang.String, java.lang.String> newAmsHBaseSiteProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("hbase.snapshot.enabled", "true");
            }
        };
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.state.Clusters clusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.Cluster cluster = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config mockAmsSite = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(clusters.getClusters()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Cluster>() {
            {
                put("normal", cluster);
            }
        }).once();
        EasyMock.expect(cluster.getDesiredConfigByType("ams-site")).andReturn(mockAmsSite).atLeastOnce();
        EasyMock.expect(mockAmsSite.getProperties()).andReturn(oldProperties).anyTimes();
        org.apache.ambari.server.state.Config mockAmsHbaseSite = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(cluster.getDesiredConfigByType("ams-hbase-site")).andReturn(mockAmsHbaseSite).atLeastOnce();
        EasyMock.expect(mockAmsHbaseSite.getProperties()).andReturn(oldAmsHBaseSiteProperties).anyTimes();
        com.google.inject.Injector injector = easyMockSupport.createNiceMock(com.google.inject.Injector.class);
        EasyMock.expect(injector.getInstance(com.google.gson.Gson.class)).andReturn(null).anyTimes();
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.controller.MaintenanceStateHelper.class)).andReturn(null).anyTimes();
        EasyMock.replay(injector, clusters, mockAmsSite, mockAmsHbaseSite, cluster);
        org.apache.ambari.server.controller.AmbariManagementControllerImpl controller = EasyMock.createMockBuilder(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class).addMockedMethod("createConfiguration", org.apache.ambari.server.controller.ConfigurationRequest.class).addMockedMethod("getClusters", new java.lang.Class[]{  }).addMockedMethod("createConfig", org.apache.ambari.server.state.Cluster.class, org.apache.ambari.server.state.StackId.class, java.lang.String.class, java.util.Map.class, java.lang.String.class, java.util.Map.class).withConstructor(EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class), clusters, injector).createNiceMock();
        com.google.inject.Injector injector2 = easyMockSupport.createNiceMock(com.google.inject.Injector.class);
        org.easymock.Capture<java.util.Map> propertiesCapture = org.easymock.EasyMock.newCapture(CaptureType.ALL);
        EasyMock.expect(injector2.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class)).andReturn(controller).anyTimes();
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(controller.createConfig(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyString(), EasyMock.capture(propertiesCapture), EasyMock.anyString(), EasyMock.anyObject(java.util.Map.class))).andReturn(EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class)).times(2);
        EasyMock.replay(controller, injector2);
        new org.apache.ambari.server.upgrade.UpgradeCatalog270(injector2).updateAmsConfigs();
        easyMockSupport.verifyAll();
        org.junit.Assert.assertEquals(propertiesCapture.getValues().size(), 2);
        java.util.Map<java.lang.String, java.lang.String> updatedProperties = propertiesCapture.getValues().get(0);
        org.junit.Assert.assertTrue(com.google.common.collect.Maps.difference(newProperties, updatedProperties).areEqual());
        updatedProperties = propertiesCapture.getValues().get(1);
        org.junit.Assert.assertTrue(com.google.common.collect.Maps.difference(newAmsHBaseSiteProperties, updatedProperties).areEqual());
    }

    @org.junit.Test
    public void testUpdateAmsConfigsWithNoContainerMetrics() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> oldProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("timeline.metrics.service.default.result.limit", "15840");
                put("timeline.metrics.host.aggregate.splitpoints", "kafka.metric,nimbus.metric");
            }
        };
        java.util.Map<java.lang.String, java.lang.String> newProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("timeline.metrics.service.default.result.limit", "5760");
            }
        };
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.state.Clusters clusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.Cluster cluster = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config mockAmsSite = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(clusters.getClusters()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Cluster>() {
            {
                put("normal", cluster);
            }
        }).once();
        EasyMock.expect(cluster.getDesiredConfigByType("ams-site")).andReturn(mockAmsSite).atLeastOnce();
        EasyMock.expect(mockAmsSite.getProperties()).andReturn(oldProperties).anyTimes();
        com.google.inject.Injector injector = easyMockSupport.createNiceMock(com.google.inject.Injector.class);
        EasyMock.expect(injector.getInstance(com.google.gson.Gson.class)).andReturn(null).anyTimes();
        EasyMock.expect(injector.getInstance(org.apache.ambari.server.controller.MaintenanceStateHelper.class)).andReturn(null).anyTimes();
        EasyMock.replay(injector, clusters, mockAmsSite, cluster);
        org.apache.ambari.server.controller.AmbariManagementControllerImpl controller = EasyMock.createMockBuilder(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class).addMockedMethod("createConfiguration", org.apache.ambari.server.controller.ConfigurationRequest.class).addMockedMethod("getClusters", new java.lang.Class[]{  }).addMockedMethod("createConfig", org.apache.ambari.server.state.Cluster.class, org.apache.ambari.server.state.StackId.class, java.lang.String.class, java.util.Map.class, java.lang.String.class, java.util.Map.class).withConstructor(EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class), clusters, injector).createNiceMock();
        com.google.inject.Injector injector2 = easyMockSupport.createNiceMock(com.google.inject.Injector.class);
        org.easymock.Capture<java.util.Map> propertiesCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(injector2.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class)).andReturn(controller).anyTimes();
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(controller.createConfig(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyString(), EasyMock.capture(propertiesCapture), EasyMock.anyString(), EasyMock.anyObject(java.util.Map.class))).andReturn(EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class)).once();
        EasyMock.replay(controller, injector2);
        new org.apache.ambari.server.upgrade.UpgradeCatalog270(injector2).updateAmsConfigs();
        easyMockSupport.verifyAll();
        java.util.Map<java.lang.String, java.lang.String> updatedProperties = propertiesCapture.getValue();
        org.junit.Assert.assertTrue(com.google.common.collect.Maps.difference(newProperties, updatedProperties).areEqual());
    }

    @org.junit.Test
    public void testStormConfigs() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> stormProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("_storm.thrift.nonsecure.transport", "org.apache.storm.security.auth.SimpleTransportPlugin");
                put("_storm.thrift.secure.transport", "org.apache.storm.security.auth.kerberos.KerberosSaslTransportPlugin");
                put("storm.thrift.transport", "{{storm_thrift_transport}}");
                put("storm.zookeeper.port", "2181");
            }
        };
        java.util.Map<java.lang.String, java.lang.String> newStormProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("storm.thrift.transport", "org.apache.storm.security.auth.SimpleTransportPlugin");
                put("storm.zookeeper.port", "2181");
            }
        };
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.state.Clusters clusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.Cluster cluster = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config mockStormSite = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(clusters.getClusters()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Cluster>() {
            {
                put("normal", cluster);
            }
        }).once();
        EasyMock.expect(cluster.getDesiredConfigByType("storm-site")).andReturn(mockStormSite).atLeastOnce();
        EasyMock.expect(cluster.getSecurityType()).andReturn(org.apache.ambari.server.state.SecurityType.NONE).anyTimes();
        EasyMock.expect(mockStormSite.getProperties()).andReturn(stormProperties).anyTimes();
        com.google.inject.Injector injector = easyMockSupport.createNiceMock(com.google.inject.Injector.class);
        EasyMock.replay(injector, clusters, mockStormSite, cluster);
        org.apache.ambari.server.controller.AmbariManagementControllerImpl controller = EasyMock.createMockBuilder(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class).addMockedMethod("getClusters", new java.lang.Class[]{  }).addMockedMethod("createConfig", org.apache.ambari.server.state.Cluster.class, org.apache.ambari.server.state.StackId.class, java.lang.String.class, java.util.Map.class, java.lang.String.class, java.util.Map.class).withConstructor(EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class), clusters, injector).createNiceMock();
        com.google.inject.Injector injector2 = easyMockSupport.createNiceMock(com.google.inject.Injector.class);
        org.easymock.Capture<java.util.Map> propertiesCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(injector2.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class)).andReturn(controller).anyTimes();
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(controller.createConfig(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyString(), EasyMock.capture(propertiesCapture), EasyMock.anyString(), EasyMock.anyObject(java.util.Map.class))).andReturn(EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class)).once();
        EasyMock.replay(controller, injector2);
        new org.apache.ambari.server.upgrade.UpgradeCatalog270(injector2).updateStormConfigs();
        easyMockSupport.verifyAll();
        java.util.Map<java.lang.String, java.lang.String> updatedProperties = propertiesCapture.getValue();
        org.junit.Assert.assertTrue(com.google.common.collect.Maps.difference(newStormProperties, updatedProperties).areEqual());
    }

    @org.junit.Test
    public void testStormConfigsWithKerberos() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> stormProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("_storm.thrift.nonsecure.transport", "org.apache.storm.security.auth.SimpleTransportPlugin");
                put("_storm.thrift.secure.transport", "org.apache.storm.security.auth.kerberos.KerberosSaslTransportPlugin");
                put("storm.thrift.transport", "{{storm_thrift_transport}}");
                put("storm.zookeeper.port", "2181");
            }
        };
        java.util.Map<java.lang.String, java.lang.String> newStormProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("storm.thrift.transport", "org.apache.storm.security.auth.kerberos.KerberosSaslTransportPlugin");
                put("storm.zookeeper.port", "2181");
            }
        };
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.state.Clusters clusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.Cluster cluster = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config mockStormSite = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(clusters.getClusters()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Cluster>() {
            {
                put("normal", cluster);
            }
        }).once();
        EasyMock.expect(cluster.getDesiredConfigByType("storm-site")).andReturn(mockStormSite).atLeastOnce();
        EasyMock.expect(cluster.getSecurityType()).andReturn(org.apache.ambari.server.state.SecurityType.KERBEROS).anyTimes();
        EasyMock.expect(mockStormSite.getProperties()).andReturn(stormProperties).anyTimes();
        com.google.inject.Injector injector = easyMockSupport.createNiceMock(com.google.inject.Injector.class);
        EasyMock.replay(injector, clusters, mockStormSite, cluster);
        org.apache.ambari.server.controller.AmbariManagementControllerImpl controller = EasyMock.createMockBuilder(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class).addMockedMethod("getClusters", new java.lang.Class[]{  }).addMockedMethod("createConfig", org.apache.ambari.server.state.Cluster.class, org.apache.ambari.server.state.StackId.class, java.lang.String.class, java.util.Map.class, java.lang.String.class, java.util.Map.class).withConstructor(EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class), clusters, injector).createNiceMock();
        com.google.inject.Injector injector2 = easyMockSupport.createNiceMock(com.google.inject.Injector.class);
        org.easymock.Capture<java.util.Map> propertiesCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(injector2.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class)).andReturn(controller).anyTimes();
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(controller.createConfig(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyString(), EasyMock.capture(propertiesCapture), EasyMock.anyString(), EasyMock.anyObject(java.util.Map.class))).andReturn(EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class)).once();
        EasyMock.replay(controller, injector2);
        new org.apache.ambari.server.upgrade.UpgradeCatalog270(injector2).updateStormConfigs();
        easyMockSupport.verifyAll();
        java.util.Map<java.lang.String, java.lang.String> updatedProperties = propertiesCapture.getValue();
        org.junit.Assert.assertTrue(com.google.common.collect.Maps.difference(newStormProperties, updatedProperties).areEqual());
    }

    @org.junit.Test
    public void testClearHadoopMetrics2Content() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> oldContentProperty = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("content", "# Licensed to the Apache Software Foundation (ASF) under one or more...");
            }
        };
        java.util.Map<java.lang.String, java.lang.String> newContentProperty = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("content", "");
            }
        };
        org.easymock.EasyMockSupport easyMockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.state.Clusters clusters = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        final org.apache.ambari.server.state.Cluster cluster = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Config mockHadoopMetrics2Properties = easyMockSupport.createNiceMock(org.apache.ambari.server.state.Config.class);
        EasyMock.expect(clusters.getClusters()).andReturn(new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.Cluster>() {
            {
                put("normal", cluster);
            }
        }).once();
        EasyMock.expect(cluster.getDesiredConfigByType("hadoop-metrics2.properties")).andReturn(mockHadoopMetrics2Properties).atLeastOnce();
        EasyMock.expect(mockHadoopMetrics2Properties.getProperties()).andReturn(oldContentProperty).anyTimes();
        com.google.inject.Injector injector = easyMockSupport.createNiceMock(com.google.inject.Injector.class);
        EasyMock.replay(injector, clusters, mockHadoopMetrics2Properties, cluster);
        org.apache.ambari.server.controller.AmbariManagementControllerImpl controller = EasyMock.createMockBuilder(org.apache.ambari.server.controller.AmbariManagementControllerImpl.class).addMockedMethod("getClusters", new java.lang.Class[]{  }).addMockedMethod("createConfig", org.apache.ambari.server.state.Cluster.class, org.apache.ambari.server.state.StackId.class, java.lang.String.class, java.util.Map.class, java.lang.String.class, java.util.Map.class).withConstructor(EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.ActionManager.class), clusters, injector).createNiceMock();
        com.google.inject.Injector injector2 = easyMockSupport.createNiceMock(com.google.inject.Injector.class);
        org.easymock.Capture<java.util.Map> propertiesCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(injector2.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class)).andReturn(controller).anyTimes();
        EasyMock.expect(controller.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(controller.createConfig(EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), EasyMock.anyString(), EasyMock.capture(propertiesCapture), EasyMock.anyString(), EasyMock.anyObject(java.util.Map.class))).andReturn(EasyMock.createNiceMock(org.apache.ambari.server.state.Config.class)).once();
        EasyMock.replay(controller, injector2);
        new org.apache.ambari.server.upgrade.UpgradeCatalog270(injector2).clearHadoopMetrics2Content();
        easyMockSupport.verifyAll();
        java.util.Map<java.lang.String, java.lang.String> updatedProperties = propertiesCapture.getValue();
        org.junit.Assert.assertTrue(com.google.common.collect.Maps.difference(newContentProperty, updatedProperties).areEqual());
    }
}