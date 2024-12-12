package org.apache.ambari.server.api.services.stackadvisor;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createNiceMock;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.support.membermodification.MemberModifier.stub;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class)
public class StackAdvisorRunnerTest {
    private org.junit.rules.TemporaryFolder temp = new org.junit.rules.TemporaryFolder();

    @org.junit.Before
    public void setUp() throws java.io.IOException {
        temp.create();
    }

    @org.junit.After
    public void tearDown() throws java.io.IOException {
        temp.delete();
    }

    @org.junit.Test(expected = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException.class)
    public void testRunScript_processStartThrowsException_returnFalse() throws java.lang.Exception {
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType saCommandType = org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType.RECOMMEND_COMPONENT_LAYOUT;
        java.io.File actionDirectory = temp.newFolder("actionDir");
        java.lang.ProcessBuilder processBuilder = createNiceMock(java.lang.ProcessBuilder.class);
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner = new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner();
        org.apache.ambari.server.configuration.Configuration configMock = createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        saRunner.setConfigs(configMock);
        stub(org.powermock.api.easymock.PowerMock.method(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class, "prepareShellCommand")).toReturn(processBuilder);
        EasyMock.expect(processBuilder.environment()).andReturn(new java.util.HashMap<>()).times(3);
        EasyMock.expect(processBuilder.start()).andThrow(new java.io.IOException());
        replay(processBuilder, configMock);
        saRunner.runScript(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, saCommandType, actionDirectory);
    }

    @org.junit.Test(expected = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequestException.class)
    public void testRunScript_processExitCode1_returnFalse() throws java.lang.Exception {
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType saCommandType = org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType.RECOMMEND_COMPONENT_LAYOUT;
        java.io.File actionDirectory = temp.newFolder("actionDir");
        java.lang.ProcessBuilder processBuilder = createNiceMock(java.lang.ProcessBuilder.class);
        java.lang.Process process = createNiceMock(java.lang.Process.class);
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner = new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner();
        org.apache.ambari.server.configuration.Configuration configMock = createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        saRunner.setConfigs(configMock);
        stub(org.powermock.api.easymock.PowerMock.method(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class, "prepareShellCommand")).toReturn(processBuilder);
        EasyMock.expect(processBuilder.environment()).andReturn(new java.util.HashMap<>()).times(3);
        EasyMock.expect(processBuilder.start()).andReturn(process);
        EasyMock.expect(process.waitFor()).andReturn(1);
        replay(processBuilder, process, configMock);
        saRunner.runScript(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, saCommandType, actionDirectory);
    }

    @org.junit.Test(expected = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException.class)
    public void testRunScript_processExitCode2_returnFalse() throws java.lang.Exception {
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType saCommandType = org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType.RECOMMEND_COMPONENT_LAYOUT;
        java.io.File actionDirectory = temp.newFolder("actionDir");
        java.lang.ProcessBuilder processBuilder = createNiceMock(java.lang.ProcessBuilder.class);
        java.lang.Process process = createNiceMock(java.lang.Process.class);
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner = new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner();
        org.apache.ambari.server.configuration.Configuration configMock = createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        saRunner.setConfigs(configMock);
        stub(org.powermock.api.easymock.PowerMock.method(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class, "prepareShellCommand")).toReturn(processBuilder);
        EasyMock.expect(processBuilder.environment()).andReturn(new java.util.HashMap<>()).times(3);
        EasyMock.expect(processBuilder.start()).andReturn(process);
        EasyMock.expect(process.waitFor()).andReturn(2);
        replay(processBuilder, process, configMock);
        saRunner.runScript(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, saCommandType, actionDirectory);
    }

    @org.junit.Test
    public void testRunScript_processExitCodeZero_returnTrue() throws java.lang.Exception {
        org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType saCommandType = org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType.RECOMMEND_COMPONENT_LAYOUT;
        java.io.File actionDirectory = temp.newFolder("actionDir");
        java.lang.ProcessBuilder processBuilder = createNiceMock(java.lang.ProcessBuilder.class);
        java.lang.Process process = createNiceMock(java.lang.Process.class);
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner = new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner();
        org.apache.ambari.server.configuration.Configuration configMock = createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        saRunner.setConfigs(configMock);
        stub(org.powermock.api.easymock.PowerMock.method(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner.class, "prepareShellCommand")).toReturn(processBuilder);
        EasyMock.expect(processBuilder.environment()).andReturn(new java.util.HashMap<>()).times(3);
        EasyMock.expect(processBuilder.start()).andReturn(process);
        EasyMock.expect(process.waitFor()).andReturn(0);
        replay(processBuilder, process, configMock);
        try {
            saRunner.runScript(org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType.PYTHON, saCommandType, actionDirectory);
        } catch (org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException ex) {
            org.junit.Assert.fail("Should not fail with StackAdvisorException");
        }
    }
}