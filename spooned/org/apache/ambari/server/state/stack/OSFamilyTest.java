package org.apache.ambari.server.state.stack;
@org.junit.experimental.categories.Category({ category.StackUpgradeTest.class })
public class OSFamilyTest {
    org.apache.ambari.server.state.stack.OsFamily os_family = null;

    private com.google.inject.Injector injector;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        os_family = injector.getInstance(org.apache.ambari.server.state.stack.OsFamily.class);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testOSListing() throws java.lang.Exception {
        java.util.Set<java.lang.String> actual_oslist = os_family.os_list();
        java.util.Set<java.lang.String> expected_oslist = new java.util.HashSet<>(java.util.Arrays.asList("redhat6", "oraclelinux5", "suse11", "fedora6", "opensuse11", "centos6", "fedora5", "centos5", "ubuntu12", "redhat5", "sles11", "oraclelinux6", "debian12", "sled11", "win2012server6", "win2012serverr26", "win2008serverr26", "win2008server6"));
        junit.framework.Assert.assertNotNull(actual_oslist);
        junit.framework.Assert.assertEquals(expected_oslist, actual_oslist);
    }

    @org.junit.Test
    public void testParsingOS() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> expected_map = new java.util.HashMap<>();
        expected_map.put("distro", "ubuntu");
        expected_map.put("versions", "12");
        java.lang.String test_value = "ubuntu12";
        java.lang.Class[] parse_os_args = new java.lang.Class[]{ java.lang.String.class };
        java.lang.reflect.Method parse_os = os_family.getClass().getDeclaredMethod("parse_os", parse_os_args);
        parse_os.setAccessible(true);
        java.lang.Object test_map = parse_os.invoke(os_family, test_value);
        parse_os.setAccessible(false);
        junit.framework.Assert.assertNotNull(test_map);
        junit.framework.Assert.assertEquals(expected_map.getClass().getName(), test_map.getClass().getName());
        junit.framework.Assert.assertEquals(expected_map, test_map);
    }

    @org.junit.Test
    public void testFindTypes() throws java.lang.Exception {
        java.util.Set<java.lang.String> expected_set = new java.util.HashSet<>(java.util.Arrays.asList("ubuntu12", "debian12"));
        java.util.Set<java.lang.String> actual_set = os_family.findTypes("ubuntu12");
        junit.framework.Assert.assertNotNull(actual_set);
        junit.framework.Assert.assertEquals(expected_set, actual_set);
    }

    @org.junit.Test
    public void testFind() throws java.lang.Exception {
        java.lang.String expected_result = "ubuntu12";
        java.lang.String actual_result = os_family.find("debian12");
        junit.framework.Assert.assertNotNull(actual_result);
        junit.framework.Assert.assertEquals(expected_result, actual_result);
        expected_result = "winsrv6";
        actual_result = os_family.find("win2012server6");
        junit.framework.Assert.assertNotNull(actual_result);
        junit.framework.Assert.assertEquals(expected_result, actual_result);
    }
}