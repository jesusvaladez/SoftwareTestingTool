package org.apache.ambari.server.controller.internal;
import static org.apache.commons.collections.CollectionUtils.isEqualCollection;
public class DeleteStatusMetaDataTest {
    private java.lang.String key1 = "key1";

    private java.lang.String key2 = "key2";

    @org.junit.Test
    public void testDeletedKeys() {
        org.apache.ambari.server.controller.internal.DeleteStatusMetaData metaData = new org.apache.ambari.server.controller.internal.DeleteStatusMetaData();
        metaData.addDeletedKey(key1);
        metaData.addDeletedKey(key2);
        org.junit.Assert.assertTrue(org.apache.commons.collections.CollectionUtils.isEqualCollection(com.google.common.collect.Sets.newHashSet(key1, key2), metaData.getDeletedKeys()));
        org.junit.Assert.assertTrue(metaData.getExceptionForKeys().isEmpty());
    }

    @org.junit.Test
    public void testExceptions() {
        org.apache.ambari.server.controller.internal.DeleteStatusMetaData metaData = new org.apache.ambari.server.controller.internal.DeleteStatusMetaData();
        metaData.addException(key1, new org.apache.ambari.server.controller.spi.SystemException("test"));
        metaData.addException(key2, new org.apache.ambari.server.ObjectNotFoundException("test"));
        org.junit.Assert.assertTrue(metaData.getExceptionForKeys().get(key1) instanceof org.apache.ambari.server.controller.spi.SystemException);
        org.junit.Assert.assertTrue(metaData.getExceptionForKeys().get(key2) instanceof org.apache.ambari.server.ObjectNotFoundException);
    }
}