package org.apache.ambari.server.customactions;
public class ActionDefinitionManagerTest {
    private static final java.lang.String CUSTOM_ACTION_DEFINITION_ROOT = "./src/test/resources/custom_action_definitions/";

    private static final java.lang.String CUSTOM_ACTION_DEFINITION_INVALID_ROOT = "./src/test/resources/custom_action_definitions_invalid/";

    @org.junit.Test
    public void testReadCustomActionDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.customactions.ActionDefinitionManager manager = new org.apache.ambari.server.customactions.ActionDefinitionManager();
        manager.readCustomActionDefinitions(new java.io.File(org.apache.ambari.server.customactions.ActionDefinitionManagerTest.CUSTOM_ACTION_DEFINITION_ROOT));
        junit.framework.Assert.assertEquals(3, manager.getAllActionDefinition().size());
        org.apache.ambari.server.customactions.ActionDefinition ad = manager.getActionDefinition("customAction1");
        junit.framework.Assert.assertNotNull(ad);
        junit.framework.Assert.assertEquals("customAction1", ad.getActionName());
        junit.framework.Assert.assertEquals("A random test", ad.getDescription());
        junit.framework.Assert.assertEquals("threshold", ad.getInputs());
        junit.framework.Assert.assertEquals("TASKTRACKER", ad.getTargetComponent());
        junit.framework.Assert.assertEquals("MAPREDUCE", ad.getTargetService());
        junit.framework.Assert.assertEquals(60, ((int) (ad.getDefaultTimeout())));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.actionmanager.TargetHostType.ALL, ad.getTargetType());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.actionmanager.ActionType.USER, ad.getActionType());
        junit.framework.Assert.assertEquals(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_COMPONENTS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS), ad.getPermissions());
        ad = manager.getActionDefinition("customAction2");
        junit.framework.Assert.assertNotNull(ad);
        junit.framework.Assert.assertEquals("customAction2", ad.getActionName());
        junit.framework.Assert.assertEquals("A random test", ad.getDescription());
        junit.framework.Assert.assertEquals(null, ad.getInputs());
        junit.framework.Assert.assertEquals("TASKTRACKER", ad.getTargetComponent());
        junit.framework.Assert.assertEquals("MAPREDUCE", ad.getTargetService());
        junit.framework.Assert.assertEquals(60, ((int) (ad.getDefaultTimeout())));
        junit.framework.Assert.assertEquals(null, ad.getTargetType());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.actionmanager.ActionType.USER, ad.getActionType());
        junit.framework.Assert.assertEquals(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_COMPONENTS, org.apache.ambari.server.security.authorization.RoleAuthorization.HOST_ADD_DELETE_HOSTS), ad.getPermissions());
        ad = manager.getActionDefinition("customAction3");
        junit.framework.Assert.assertNotNull(ad);
        junit.framework.Assert.assertEquals("customAction3", ad.getActionName());
        junit.framework.Assert.assertEquals("A random test", ad.getDescription());
        junit.framework.Assert.assertEquals(null, ad.getInputs());
        junit.framework.Assert.assertEquals("TASKTRACKER", ad.getTargetComponent());
        junit.framework.Assert.assertEquals("MAPREDUCE", ad.getTargetService());
        junit.framework.Assert.assertEquals(60, ((int) (ad.getDefaultTimeout())));
        junit.framework.Assert.assertEquals(null, ad.getTargetType());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.actionmanager.ActionType.USER, ad.getActionType());
        junit.framework.Assert.assertNull(ad.getPermissions());
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testReadInvalidCustomActionDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.customactions.ActionDefinitionManager manager = new org.apache.ambari.server.customactions.ActionDefinitionManager();
        manager.readCustomActionDefinitions(new java.io.File(org.apache.ambari.server.customactions.ActionDefinitionManagerTest.CUSTOM_ACTION_DEFINITION_INVALID_ROOT));
    }
}