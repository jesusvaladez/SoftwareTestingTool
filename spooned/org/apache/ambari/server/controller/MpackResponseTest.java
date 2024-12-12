package org.apache.ambari.server.controller;
public class MpackResponseTest {
    @org.junit.Test
    public void testBasicGetAndSet() {
        org.apache.ambari.server.controller.MpackResponse mpackResponse = new org.apache.ambari.server.controller.MpackResponse(setupMpack());
        org.junit.Assert.assertEquals(((java.lang.Long) (100L)), mpackResponse.getId());
        org.junit.Assert.assertEquals(((java.lang.Long) (100L)), mpackResponse.getRegistryId());
        org.junit.Assert.assertEquals("3.0", mpackResponse.getMpackVersion());
        org.junit.Assert.assertEquals("abc.tar.gz", mpackResponse.getMpackUri());
        org.junit.Assert.assertEquals("testMpack", mpackResponse.getMpackName());
    }

    public org.apache.ambari.server.state.Mpack setupMpack() {
        org.apache.ambari.server.state.Mpack mpack = new org.apache.ambari.server.state.Mpack();
        mpack.setResourceId(100L);
        mpack.setModules(new java.util.ArrayList<org.apache.ambari.server.state.Module>());
        mpack.setPrerequisites(new java.util.HashMap<java.lang.String, java.lang.String>());
        mpack.setRegistryId(100L);
        mpack.setVersion("3.0");
        mpack.setMpackUri("abc.tar.gz");
        mpack.setDescription("Test mpack");
        mpack.setName("testMpack");
        return mpack;
    }
}