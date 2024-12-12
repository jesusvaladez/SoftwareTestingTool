package org.apache.ambari.server.controller;
public class MpackRequestTest {
    @org.junit.Test
    public void testBasicGetAndSet() {
        org.apache.ambari.server.controller.MpackRequest mpackRequest = new org.apache.ambari.server.controller.MpackRequest(1L);
        org.junit.Assert.assertEquals(((java.lang.Long) (1L)), mpackRequest.getId());
        mpackRequest.setMpackUri("abc.tar.gz");
        mpackRequest.setRegistryId(new java.lang.Long(1));
        mpackRequest.setMpackVersion("3.0");
        mpackRequest.setMpackName("testmpack");
        org.junit.Assert.assertEquals("abc.tar.gz", mpackRequest.getMpackUri());
        org.junit.Assert.assertEquals(new java.lang.Long("1"), mpackRequest.getRegistryId());
        org.junit.Assert.assertEquals("3.0", mpackRequest.getMpackVersion());
        org.junit.Assert.assertEquals("testmpack", mpackRequest.getMpackName());
    }
}