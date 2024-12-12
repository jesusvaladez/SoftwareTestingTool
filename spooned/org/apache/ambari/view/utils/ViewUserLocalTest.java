package org.apache.ambari.view.utils;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class ViewUserLocalTest {
    @org.junit.Test
    public void testDifferentUsers() throws java.lang.Exception {
        org.apache.ambari.view.ViewContext viewContext = EasyMock.createNiceMock(org.apache.ambari.view.ViewContext.class);
        EasyMock.expect(viewContext.getInstanceName()).andReturn("INSTANCE1").anyTimes();
        EasyMock.expect(viewContext.getUsername()).andReturn("luke").anyTimes();
        org.apache.ambari.view.ViewContext viewContext2 = EasyMock.createNiceMock(org.apache.ambari.view.ViewContext.class);
        EasyMock.expect(viewContext2.getInstanceName()).andReturn("INSTANCE1").anyTimes();
        EasyMock.expect(viewContext2.getUsername()).andReturn("leia").anyTimes();
        EasyMock.replay(viewContext, viewContext2);
        org.apache.ambari.view.utils.UserLocal<java.lang.Object> test = new org.apache.ambari.view.utils.UserLocal<java.lang.Object>(java.lang.Object.class) {
            @java.lang.Override
            protected synchronized java.lang.Object initialValue(org.apache.ambari.view.ViewContext context) {
                return new java.lang.Object();
            }
        };
        java.lang.Object obj1 = test.get(viewContext);
        java.lang.Object obj2 = test.get(viewContext2);
        org.junit.Assert.assertNotSame(obj1, obj2);
    }

    @org.junit.Test
    public void testDifferentInstances() throws java.lang.Exception {
        org.apache.ambari.view.ViewContext viewContext = EasyMock.createNiceMock(org.apache.ambari.view.ViewContext.class);
        EasyMock.expect(viewContext.getInstanceName()).andReturn("INSTANCE1").anyTimes();
        EasyMock.expect(viewContext.getUsername()).andReturn("luke").anyTimes();
        org.apache.ambari.view.ViewContext viewContext2 = EasyMock.createNiceMock(org.apache.ambari.view.ViewContext.class);
        EasyMock.expect(viewContext2.getInstanceName()).andReturn("INSTANCE2").anyTimes();
        EasyMock.expect(viewContext2.getUsername()).andReturn("luke").anyTimes();
        EasyMock.replay(viewContext, viewContext2);
        org.apache.ambari.view.utils.UserLocal<java.lang.Object> test = new org.apache.ambari.view.utils.UserLocal<java.lang.Object>(java.lang.Object.class) {
            @java.lang.Override
            protected synchronized java.lang.Object initialValue(org.apache.ambari.view.ViewContext context) {
                return new java.lang.Object();
            }
        };
        java.lang.Object obj1 = test.get(viewContext);
        java.lang.Object obj2 = test.get(viewContext2);
        org.junit.Assert.assertNotSame(obj1, obj2);
    }

    @org.junit.Test
    public void testSameUsers() throws java.lang.Exception {
        org.apache.ambari.view.ViewContext viewContext = EasyMock.createNiceMock(org.apache.ambari.view.ViewContext.class);
        EasyMock.expect(viewContext.getInstanceName()).andReturn("INSTANCE1").anyTimes();
        EasyMock.expect(viewContext.getUsername()).andReturn("luke").anyTimes();
        org.apache.ambari.view.ViewContext viewContext2 = EasyMock.createNiceMock(org.apache.ambari.view.ViewContext.class);
        EasyMock.expect(viewContext2.getInstanceName()).andReturn("INSTANCE1").anyTimes();
        EasyMock.expect(viewContext2.getUsername()).andReturn("luke").anyTimes();
        EasyMock.replay(viewContext, viewContext2);
        org.apache.ambari.view.utils.UserLocal<java.lang.Object> test = new org.apache.ambari.view.utils.UserLocal<java.lang.Object>(java.lang.Object.class) {
            @java.lang.Override
            protected synchronized java.lang.Object initialValue(org.apache.ambari.view.ViewContext context) {
                return new java.lang.Object();
            }
        };
        java.lang.Object obj1 = test.get(viewContext);
        java.lang.Object obj2 = test.get(viewContext2);
        org.junit.Assert.assertSame(obj1, obj2);
    }
}