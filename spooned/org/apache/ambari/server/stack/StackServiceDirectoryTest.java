package org.apache.ambari.server.stack;
public class StackServiceDirectoryTest {
    private org.apache.ambari.server.stack.StackServiceDirectoryTest.MockStackServiceDirectory createStackServiceDirectory(java.lang.String servicePath) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.StackServiceDirectoryTest.MockStackServiceDirectory ssd = new org.apache.ambari.server.stack.StackServiceDirectoryTest.MockStackServiceDirectory(servicePath);
        return ssd;
    }

    @org.junit.Test
    public void testValidServiceAdvisorClassName() throws java.lang.Exception {
        java.lang.String pathWithInvalidChars = "/Fake-Stack.Name/1.0/services/FAKESERVICE/";
        java.lang.String serviceNameValidChars = "FakeService";
        java.lang.String pathWithValidChars = "/FakeStackName/1.0/services/FAKESERVICE/";
        java.lang.String serviceNameInvalidChars = "Fake-Serv.ice";
        java.lang.String desiredServiceAdvisorName = "FakeStackName10FakeServiceServiceAdvisor";
        org.apache.ambari.server.stack.StackServiceDirectoryTest.MockStackServiceDirectory ssd1 = createStackServiceDirectory(pathWithInvalidChars);
        org.junit.Assert.assertEquals(desiredServiceAdvisorName, ssd1.getAdvisorName(serviceNameValidChars));
        org.apache.ambari.server.stack.StackServiceDirectoryTest.MockStackServiceDirectory ssd2 = createStackServiceDirectory(pathWithValidChars);
        org.junit.Assert.assertEquals(desiredServiceAdvisorName, ssd2.getAdvisorName(serviceNameInvalidChars));
        org.apache.ambari.server.stack.StackServiceDirectoryTest.MockStackServiceDirectory ssd3 = createStackServiceDirectory(pathWithInvalidChars);
        org.junit.Assert.assertEquals(desiredServiceAdvisorName, ssd3.getAdvisorName(serviceNameInvalidChars));
        org.apache.ambari.server.stack.StackServiceDirectoryTest.MockStackServiceDirectory ssd4 = createStackServiceDirectory(pathWithValidChars);
        org.junit.Assert.assertEquals(desiredServiceAdvisorName, ssd4.getAdvisorName(serviceNameValidChars));
    }

    private class MockStackServiceDirectory extends org.apache.ambari.server.stack.StackServiceDirectory {
        java.io.File advisor = null;

        MockStackServiceDirectory(java.lang.String servicePath) throws org.apache.ambari.server.AmbariException {
            super(servicePath);
            advisor = new java.io.File(servicePath, org.apache.ambari.server.stack.StackDirectory.SERVICE_ADVISOR_FILE_NAME);
        }

        protected void parsePath() {
        }

        public java.io.File getAdvisorFile() {
            return advisor;
        }
    }
}