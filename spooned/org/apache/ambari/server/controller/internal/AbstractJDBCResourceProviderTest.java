package org.apache.ambari.server.controller.internal;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class AbstractJDBCResourceProviderTest {
    private static final java.lang.String property1 = "property1";

    private static final java.lang.String property2 = "property2";

    @org.junit.Test
    public void test() throws java.sql.SQLException {
        java.util.Set<java.lang.String> requestedIds = new java.util.TreeSet<>();
        requestedIds.add(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.property1);
        requestedIds.add("none1");
        requestedIds.add(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.property2);
        org.apache.ambari.server.controller.internal.AbstractJDBCResourceProvider<org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestFields> provider = new org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestAbstractJDBCResourceProviderImpl(requestedIds, null);
        org.junit.Assert.assertEquals((org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestFields.field1 + ",") + org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestFields.field2, provider.getDBFieldString(requestedIds));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestFields.field1.toString(), provider.getDBFieldString(java.util.Collections.singleton(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.property1)));
        org.junit.Assert.assertEquals("", provider.getDBFieldString(java.util.Collections.singleton("none1")));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestFields.field1, provider.getDBField(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.property1));
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestFields.field2, provider.getDBField(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.property2));
        java.sql.ResultSet rs = EasyMock.createMock(java.sql.ResultSet.class);
        EasyMock.expect(rs.getString(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestFields.field1.toString())).andReturn("1").once();
        EasyMock.expect(rs.getLong(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestFields.field2.toString())).andReturn(2L).once();
        EasyMock.expect(rs.getInt(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestFields.field1.toString())).andReturn(3).once();
        EasyMock.replay(rs);
        org.apache.ambari.server.controller.spi.Resource r = new org.apache.ambari.server.controller.internal.ResourceImpl(((org.apache.ambari.server.controller.spi.Resource.Type) (null)));
        provider.setString(r, org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.property1, rs, requestedIds);
        provider.setString(r, "none2", rs, requestedIds);
        org.junit.Assert.assertEquals("1", r.getPropertyValue(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.property1));
        r = new org.apache.ambari.server.controller.internal.ResourceImpl(((org.apache.ambari.server.controller.spi.Resource.Type) (null)));
        provider.setLong(r, org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.property2, rs, requestedIds);
        provider.setLong(r, "none2", rs, requestedIds);
        org.junit.Assert.assertEquals(2L, r.getPropertyValue(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.property2));
        r = new org.apache.ambari.server.controller.internal.ResourceImpl(((org.apache.ambari.server.controller.spi.Resource.Type) (null)));
        provider.setInt(r, org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.property1, rs, requestedIds);
        provider.setInt(r, "none2", rs, requestedIds);
        org.junit.Assert.assertEquals(3, r.getPropertyValue(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.property1));
        EasyMock.verify(rs);
    }

    private enum TestFields {

        field1,
        field2;}

    private static class TestAbstractJDBCResourceProviderImpl extends org.apache.ambari.server.controller.internal.AbstractJDBCResourceProvider<org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestFields> {
        protected TestAbstractJDBCResourceProviderImpl(java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds) {
            super(propertyIds, keyPropertyIds);
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return null;
        }

        @java.lang.Override
        public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return null;
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return null;
        }

        @java.lang.Override
        public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
            return null;
        }

        @java.lang.Override
        protected java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestFields> getDBFieldMap() {
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestFields> fields = new java.util.HashMap<>();
            fields.put(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.property1, org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestFields.field1);
            fields.put(org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.property2, org.apache.ambari.server.controller.internal.AbstractJDBCResourceProviderTest.TestFields.field2);
            return fields;
        }

        @java.lang.Override
        protected java.util.Set<java.lang.String> getPKPropertyIds() {
            return null;
        }
    }
}