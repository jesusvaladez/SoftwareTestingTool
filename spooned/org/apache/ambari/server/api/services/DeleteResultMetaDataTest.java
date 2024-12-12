package org.apache.ambari.server.api.services;
import org.apache.commons.collections.CollectionUtils;
public class DeleteResultMetaDataTest {
    @org.junit.Test
    public void testDeletedKeys() {
        java.lang.String key1 = "key1";
        java.lang.String key2 = "key2";
        org.apache.ambari.server.api.services.DeleteResultMetadata metadata = new org.apache.ambari.server.api.services.DeleteResultMetadata();
        metadata.addDeletedKey(key1);
        metadata.addDeletedKey(key2);
        org.junit.Assert.assertTrue(org.apache.commons.collections.CollectionUtils.isEqualCollection(com.google.common.collect.Sets.newHashSet(key1, key2), metadata.getDeletedKeys()));
        org.junit.Assert.assertTrue(metadata.getExcptions().isEmpty());
    }

    @org.junit.Test
    public void testException() {
        java.lang.String key1 = "key1";
        java.lang.String key2 = "key2";
        java.lang.String key3 = "key3";
        java.lang.String key4 = "key4";
        java.lang.String key5 = "key5";
        org.apache.ambari.server.api.services.DeleteResultMetadata metadata = new org.apache.ambari.server.api.services.DeleteResultMetadata();
        metadata.addException(key1, new org.apache.ambari.server.security.authorization.AuthorizationException("Exception"));
        metadata.addException(key2, new org.apache.ambari.server.controller.spi.SystemException("Exception"));
        metadata.addException(key3, new org.apache.ambari.server.HostNotFoundException("Exception"));
        metadata.addException(key4, new org.apache.ambari.server.controller.spi.UnsupportedPropertyException(org.apache.ambari.server.controller.spi.Resource.Type.Action, java.util.Collections.emptySet()));
        metadata.addException(key5, new java.lang.NullPointerException());
        org.junit.Assert.assertTrue(metadata.getDeletedKeys().isEmpty());
        java.util.Map<java.lang.String, org.apache.ambari.server.api.services.ResultStatus> resultStatusMap = metadata.getExcptions();
        org.junit.Assert.assertEquals(resultStatusMap.get(key1).getStatus(), org.apache.ambari.server.api.services.ResultStatus.STATUS.FORBIDDEN);
        org.junit.Assert.assertEquals(resultStatusMap.get(key2).getStatus(), org.apache.ambari.server.api.services.ResultStatus.STATUS.SERVER_ERROR);
        org.junit.Assert.assertEquals(resultStatusMap.get(key3).getStatus(), org.apache.ambari.server.api.services.ResultStatus.STATUS.NOT_FOUND);
        org.junit.Assert.assertEquals(resultStatusMap.get(key4).getStatus(), org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST);
        org.junit.Assert.assertEquals(resultStatusMap.get(key5).getStatus(), org.apache.ambari.server.api.services.ResultStatus.STATUS.SERVER_ERROR);
    }
}