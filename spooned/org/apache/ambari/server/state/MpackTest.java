package org.apache.ambari.server.state;
public class MpackTest {
    @org.junit.Test
    public void testMpacks() {
        org.apache.ambari.server.state.Mpack mpack = new org.apache.ambari.server.state.Mpack();
        mpack.setName("name");
        mpack.setResourceId(((long) (100)));
        mpack.setDescription("desc");
        mpack.setVersion("3.0");
        mpack.setMpackUri("abc.tar.gz");
        mpack.setRegistryId(new java.lang.Long(100));
        org.junit.Assert.assertEquals("name", mpack.getName());
        org.junit.Assert.assertEquals(new java.lang.Long(100), mpack.getResourceId());
        org.junit.Assert.assertEquals("desc", mpack.getDescription());
        org.junit.Assert.assertEquals("abc.tar.gz", mpack.getMpackUri());
        org.junit.Assert.assertEquals(new java.lang.Long(100), mpack.getRegistryId());
    }

    @org.junit.Test
    public void testMpacksUsingGson() {
        java.lang.String mpackJsonContents = "{\n" + (((((((((((((((((((((((((((((((((((("  \"definition\": \"hdpcore-1.0.0-b16-definition.tar.gz\",\n" + "  \"description\": \"Hortonworks Data Platform Core\",\n") + "  \"id\": \"hdpcore\",\n") + "  \"modules\": [\n") + "    {\n") + "      \"category\": \"SERVER\",\n") + "      \"components\": [\n") + "        {\n") + "          \"id\": \"zookeeper_server\",\n") + "          \"version\": \"3.4.0.0-b17\",\n") + "          \"name\": \"ZOOKEEPER_SERVER\",\n") + "          \"category\": \"MASTER\",\n") + "          \"isExternal\": \"False\"\n") + "        }\n") + "      ],\n") + "      \"definition\": \"zookeeper-3.4.0.0-b17-definition.tar.gz\",\n") + "      \"dependencies\": [\n") + "        {\n") + "          \"id\": \"zookeeper_clients\",\n") + "          \"name\": \"ZOOKEEPER_CLIENTS\",\n") + "          \"dependencyType\": \"INSTALL\"\n") + "        }\n") + "      ],\n") + "      \"description\": \"Centralized service which provides highly reliable distributed coordination\",\n") + "      \"displayName\": \"ZooKeeper\",\n") + "      \"id\": \"zookeeper\",\n") + "      \"name\": \"ZOOKEEPER\",\n") + "      \"version\": \"3.4.0.0-b17\"\n") + "    }\n") + "  ],\n") + "  \"name\": \"HDPCORE\",\n") + "  \"prerequisites\": {\n") + "    \"max-ambari-version\": \"3.1.0.0\",\n") + "    \"min-ambari-version\": \"3.0.0.0\"\n") + "  },\n") + "  \"version\": \"1.0.0-b16\"\n") + "}");
        java.util.HashMap<java.lang.String, java.lang.String> expectedPrereq = new java.util.HashMap<>();
        expectedPrereq.put("min-ambari-version", "3.0.0.0");
        expectedPrereq.put("max-ambari-version", "3.1.0.0");
        java.util.ArrayList<org.apache.ambari.server.state.Module> expectedModules = new java.util.ArrayList<>();
        org.apache.ambari.server.state.Module zkfc = new org.apache.ambari.server.state.Module();
        zkfc.setVersion("3.4.0.0-b17");
        zkfc.setDefinition("zookeeper-3.4.0.0-b17-definition.tar.gz");
        zkfc.setName("ZOOKEEPER");
        zkfc.setId("zookeeper");
        zkfc.setDisplayName("ZooKeeper");
        zkfc.setDescription("Centralized service which provides highly reliable distributed coordination");
        zkfc.setCategory(org.apache.ambari.server.state.Module.Category.SERVER);
        org.apache.ambari.server.state.ModuleDependency moduleDependency = new org.apache.ambari.server.state.ModuleDependency();
        moduleDependency.setId("zookeeper_clients");
        moduleDependency.setName("ZOOKEEPER_CLIENTS");
        moduleDependency.setDependencyType(org.apache.ambari.server.state.ModuleDependency.DependencyType.INSTALL);
        java.util.ArrayList moduleDepList = new java.util.ArrayList();
        moduleDepList.add(moduleDependency);
        zkfc.setDependencies(moduleDepList);
        java.util.ArrayList compList = new java.util.ArrayList();
        org.apache.ambari.server.state.ModuleComponent zk_server = new org.apache.ambari.server.state.ModuleComponent();
        zk_server.setId("zookeeper_server");
        zk_server.setName("ZOOKEEPER_SERVER");
        zk_server.setCategory("MASTER");
        zk_server.setIsExternal(java.lang.Boolean.FALSE);
        zk_server.setVersion("3.4.0.0-b17");
        compList.add(zk_server);
        zkfc.setComponents(compList);
        expectedModules.add(zkfc);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        org.apache.ambari.server.state.Mpack mpack = gson.fromJson(mpackJsonContents, org.apache.ambari.server.state.Mpack.class);
        org.junit.Assert.assertEquals("HDPCORE", mpack.getName());
        org.junit.Assert.assertEquals("1.0.0-b16", mpack.getVersion());
        org.junit.Assert.assertEquals("Hortonworks Data Platform Core", mpack.getDescription());
        org.junit.Assert.assertEquals(expectedPrereq, mpack.getPrerequisites());
        org.junit.Assert.assertEquals(expectedModules.toString(), mpack.getModules().toString());
    }
}