package org.apache.ambari.server.view;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ViewClassLoaderTest {
    @org.junit.Test
    public void testGetResource() throws java.lang.Exception {
        java.lang.ClassLoader parentClassLoader = EasyMock.createMock(java.lang.ClassLoader.class);
        java.net.URL parentResource = new java.io.File("parent-resource").toURI().toURL();
        org.apache.ambari.server.view.configuration.ViewConfig viewConfig = EasyMock.createNiceMock(org.apache.ambari.server.view.configuration.ViewConfig.class);
        EasyMock.expect(parentClassLoader.getResource("parent-resource")).andReturn(parentResource).once();
        EasyMock.replay(parentClassLoader, viewConfig);
        java.io.File file = new java.io.File("./src/test/resources");
        java.net.URL testURL = file.toURI().toURL();
        java.net.URL[] urls = new java.net.URL[]{ testURL };
        org.apache.ambari.server.view.ViewClassLoader classLoader = new org.apache.ambari.server.view.ViewClassLoader(viewConfig, parentClassLoader, urls);
        java.net.URL url = classLoader.getResource("ambari.properties");
        junit.framework.Assert.assertNotNull(url);
        url = classLoader.getResource("parent-resource");
        junit.framework.Assert.assertNotNull(url);
        junit.framework.Assert.assertSame(parentResource, url);
        EasyMock.verify(parentClassLoader, viewConfig);
    }

    @org.junit.Test
    public void testLoadClass() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewClassLoaderTest.TestClassLoader parentClassLoader = EasyMock.createMock(org.apache.ambari.server.view.ViewClassLoaderTest.TestClassLoader.class);
        java.lang.Class parentClass = java.lang.Object.class;
        org.apache.ambari.server.view.configuration.ViewConfig viewConfig = EasyMock.createNiceMock(org.apache.ambari.server.view.configuration.ViewConfig.class);
        EasyMock.expect(parentClassLoader.getPackage("org.apache.ambari.server.view")).andReturn(null).anyTimes();
        EasyMock.expect(parentClassLoader.loadClass("java.lang.Object")).andReturn(parentClass).anyTimes();
        EasyMock.expect(parentClassLoader.loadClass("ParentClass")).andReturn(parentClass).once();
        EasyMock.expect(parentClassLoader.loadClass("org.apache.ambari.server.controller.spi.ResourceProvider")).andReturn(parentClass).once();
        EasyMock.expect(parentClassLoader.loadClass("org.apache.ambari.view.ViewContext")).andReturn(parentClass).once();
        EasyMock.expect(parentClassLoader.loadClass("javax.xml.parsers.SAXParserFactory")).andReturn(parentClass).once();
        EasyMock.expect(parentClassLoader.loadClass("com.google.inject.AbstractModule")).andReturn(parentClass).once();
        EasyMock.expect(parentClassLoader.loadClass("org.slf4j.LoggerFactory")).andReturn(parentClass).once();
        EasyMock.expect(parentClassLoader.loadClass("com.sun.jersey.api.ConflictException")).andReturn(parentClass).once();
        EasyMock.expect(parentClassLoader.loadClass("org.apache.velocity.VelocityContext")).andReturn(parentClass).once();
        EasyMock.replay(parentClassLoader, viewConfig);
        java.io.File file = new java.io.File("./target/test-classes");
        java.net.URL testURL = file.toURI().toURL();
        java.net.URL[] urls = new java.net.URL[]{ testURL };
        org.apache.ambari.server.view.ViewClassLoader classLoader = new org.apache.ambari.server.view.ViewClassLoader(viewConfig, parentClassLoader, urls);
        java.lang.Class clazz = classLoader.loadClass("ParentClass");
        junit.framework.Assert.assertNotNull(clazz);
        junit.framework.Assert.assertSame(parentClass, clazz);
        clazz = classLoader.loadClass("org.apache.ambari.server.controller.spi.ResourceProvider");
        junit.framework.Assert.assertNotNull(clazz);
        junit.framework.Assert.assertSame(parentClass, clazz);
        clazz = classLoader.loadClass("org.apache.ambari.view.ViewContext");
        junit.framework.Assert.assertNotNull(clazz);
        junit.framework.Assert.assertSame(parentClass, clazz);
        clazz = classLoader.loadClass("javax.xml.parsers.SAXParserFactory");
        junit.framework.Assert.assertNotNull(clazz);
        junit.framework.Assert.assertSame(parentClass, clazz);
        clazz = classLoader.loadClass("com.google.inject.AbstractModule");
        junit.framework.Assert.assertNotNull(clazz);
        junit.framework.Assert.assertSame(parentClass, clazz);
        clazz = classLoader.loadClass("org.slf4j.LoggerFactory");
        junit.framework.Assert.assertNotNull(clazz);
        junit.framework.Assert.assertSame(parentClass, clazz);
        clazz = classLoader.loadClass("com.sun.jersey.api.ConflictException");
        junit.framework.Assert.assertNotNull(clazz);
        junit.framework.Assert.assertSame(parentClass, clazz);
        clazz = classLoader.loadClass("org.apache.velocity.VelocityContext");
        junit.framework.Assert.assertNotNull(clazz);
        junit.framework.Assert.assertSame(parentClass, clazz);
        EasyMock.verify(parentClassLoader, viewConfig);
    }

    public class TestClassLoader extends java.lang.ClassLoader {
        @java.lang.Override
        public java.lang.Package getPackage(java.lang.String s) {
            return super.getPackage(s);
        }
    }
}