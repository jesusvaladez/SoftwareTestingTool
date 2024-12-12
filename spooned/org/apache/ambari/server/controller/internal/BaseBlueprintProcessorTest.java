package org.apache.ambari.server.controller.internal;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
@java.lang.SuppressWarnings("unchecked")
public class BaseBlueprintProcessorTest {
    @org.junit.Test
    public void testStackRegisterConditionalDependencies() throws java.lang.Exception {
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.AmbariManagementController mockMgmtController = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(mockMgmtController.getStackServices(EasyMock.isA(java.util.Set.class))).andReturn(java.util.Collections.<org.apache.ambari.server.controller.StackServiceResponse>emptySet());
        EasyMock.expect(mockMgmtController.getStackLevelConfigurations(((java.util.Set<org.apache.ambari.server.controller.StackLevelConfigurationRequest>) (EasyMock.anyObject())))).andReturn(java.util.Collections.emptySet()).anyTimes();
        final org.apache.ambari.server.state.DependencyInfo hCatDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("HIVE/HCAT");
        final org.apache.ambari.server.state.DependencyInfo yarnClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("YARN/YARN_CLIENT");
        final org.apache.ambari.server.state.DependencyInfo tezClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("TEZ/TEZ_CLIENT");
        final org.apache.ambari.server.state.DependencyInfo mapReduceTwoClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("YARN/MAPREDUCE2_CLIENT");
        final org.apache.ambari.server.state.DependencyInfo oozieClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("OOZIE/OOZIE_CLIENT");
        mockSupport.replayAll();
        org.apache.ambari.server.controller.internal.Stack testStack = new org.apache.ambari.server.controller.internal.Stack("HDP", "2.1", mockMgmtController) {
            @java.lang.Override
            public java.util.Collection<org.apache.ambari.server.state.DependencyInfo> getDependenciesForComponent(java.lang.String component) {
                if (component.equals("FAKE_MONITORING_SERVER")) {
                    java.util.Set<org.apache.ambari.server.state.DependencyInfo> setOfDependencies = new java.util.HashSet<>();
                    setOfDependencies.add(hCatDependency);
                    setOfDependencies.add(yarnClientDependency);
                    setOfDependencies.add(tezClientDependency);
                    setOfDependencies.add(mapReduceTwoClientDependency);
                    setOfDependencies.add(oozieClientDependency);
                    return setOfDependencies;
                }
                return java.util.Collections.emptySet();
            }

            @java.lang.Override
            void registerConditionalDependencies() {
                super.registerConditionalDependencies();
                java.util.Map<org.apache.ambari.server.state.DependencyInfo, java.lang.String> dependencyConditionalServiceMap = getDependencyConditionalServiceMap();
                java.util.Collection<org.apache.ambari.server.state.DependencyInfo> monitoringDependencies = getDependenciesForComponent("FAKE_MONITORING_SERVER");
                for (org.apache.ambari.server.state.DependencyInfo dependency : monitoringDependencies) {
                    if (dependency.getComponentName().equals("HCAT")) {
                        dependencyConditionalServiceMap.put(dependency, "HIVE");
                    } else if (dependency.getComponentName().equals("OOZIE_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "OOZIE");
                    } else if (dependency.getComponentName().equals("YARN_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "YARN");
                    } else if (dependency.getComponentName().equals("TEZ_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "TEZ");
                    } else if (dependency.getComponentName().equals("MAPREDUCE2_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "MAPREDUCE2");
                    }
                }
            }
        };
        org.junit.Assert.assertEquals("Initial conditional dependency map should be empty", 0, testStack.getDependencyConditionalServiceMap().size());
        testStack.registerConditionalDependencies();
        org.junit.Assert.assertEquals("Set of conditional service mappings is an incorrect size", 5, testStack.getDependencyConditionalServiceMap().size());
        org.junit.Assert.assertEquals("Incorrect service dependency for HCAT", "HIVE", testStack.getDependencyConditionalServiceMap().get(hCatDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for YARN_CLIENT", "YARN", testStack.getDependencyConditionalServiceMap().get(yarnClientDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for TEZ_CLIENT", "TEZ", testStack.getDependencyConditionalServiceMap().get(tezClientDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for MAPREDUCE2_CLIENT", "MAPREDUCE2", testStack.getDependencyConditionalServiceMap().get(mapReduceTwoClientDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for OOZIE_CLIENT", "OOZIE", testStack.getDependencyConditionalServiceMap().get(oozieClientDependency));
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testStackRegisterConditionalDependenciesNoHCAT() throws java.lang.Exception {
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.AmbariManagementController mockMgmtController = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(mockMgmtController.getStackServices(EasyMock.isA(java.util.Set.class))).andReturn(java.util.Collections.<org.apache.ambari.server.controller.StackServiceResponse>emptySet());
        EasyMock.expect(mockMgmtController.getStackLevelConfigurations(((java.util.Set<org.apache.ambari.server.controller.StackLevelConfigurationRequest>) (EasyMock.anyObject())))).andReturn(java.util.Collections.emptySet()).anyTimes();
        final org.apache.ambari.server.state.DependencyInfo yarnClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("YARN/YARN_CLIENT");
        final org.apache.ambari.server.state.DependencyInfo tezClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("TEZ/TEZ_CLIENT");
        final org.apache.ambari.server.state.DependencyInfo mapReduceTwoClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("YARN/MAPREDUCE2_CLIENT");
        final org.apache.ambari.server.state.DependencyInfo oozieClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("OOZIE/OOZIE_CLIENT");
        mockSupport.replayAll();
        org.apache.ambari.server.controller.internal.Stack testStack = new org.apache.ambari.server.controller.internal.Stack("HDP", "2.1", mockMgmtController) {
            @java.lang.Override
            public java.util.Collection<org.apache.ambari.server.state.DependencyInfo> getDependenciesForComponent(java.lang.String component) {
                if (component.equals("FAKE_MONITORING_SERVER")) {
                    java.util.Set<org.apache.ambari.server.state.DependencyInfo> setOfDependencies = new java.util.HashSet<>();
                    setOfDependencies.add(yarnClientDependency);
                    setOfDependencies.add(tezClientDependency);
                    setOfDependencies.add(mapReduceTwoClientDependency);
                    setOfDependencies.add(oozieClientDependency);
                    return setOfDependencies;
                }
                return java.util.Collections.emptySet();
            }

            @java.lang.Override
            void registerConditionalDependencies() {
                super.registerConditionalDependencies();
                java.util.Map<org.apache.ambari.server.state.DependencyInfo, java.lang.String> dependencyConditionalServiceMap = getDependencyConditionalServiceMap();
                java.util.Collection<org.apache.ambari.server.state.DependencyInfo> monitoringDependencies = getDependenciesForComponent("FAKE_MONITORING_SERVER");
                for (org.apache.ambari.server.state.DependencyInfo dependency : monitoringDependencies) {
                    if (dependency.getComponentName().equals("HCAT")) {
                        dependencyConditionalServiceMap.put(dependency, "HIVE");
                    } else if (dependency.getComponentName().equals("OOZIE_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "OOZIE");
                    } else if (dependency.getComponentName().equals("YARN_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "YARN");
                    } else if (dependency.getComponentName().equals("TEZ_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "TEZ");
                    } else if (dependency.getComponentName().equals("MAPREDUCE2_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "MAPREDUCE2");
                    }
                }
            }
        };
        org.junit.Assert.assertEquals("Initial conditional dependency map should be empty", 0, testStack.getDependencyConditionalServiceMap().size());
        testStack.registerConditionalDependencies();
        org.junit.Assert.assertEquals("Set of conditional service mappings is an incorrect size", 4, testStack.getDependencyConditionalServiceMap().size());
        org.junit.Assert.assertEquals("Incorrect service dependency for YARN_CLIENT", "YARN", testStack.getDependencyConditionalServiceMap().get(yarnClientDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for TEZ_CLIENT", "TEZ", testStack.getDependencyConditionalServiceMap().get(tezClientDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for MAPREDUCE2_CLIENT", "MAPREDUCE2", testStack.getDependencyConditionalServiceMap().get(mapReduceTwoClientDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for OOZIE_CLIENT", "OOZIE", testStack.getDependencyConditionalServiceMap().get(oozieClientDependency));
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testStackRegisterConditionalDependenciesNoYarnClient() throws java.lang.Exception {
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.AmbariManagementController mockMgmtController = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(mockMgmtController.getStackServices(EasyMock.isA(java.util.Set.class))).andReturn(java.util.Collections.<org.apache.ambari.server.controller.StackServiceResponse>emptySet());
        EasyMock.expect(mockMgmtController.getStackLevelConfigurations(((java.util.Set<org.apache.ambari.server.controller.StackLevelConfigurationRequest>) (EasyMock.anyObject())))).andReturn(java.util.Collections.emptySet()).anyTimes();
        final org.apache.ambari.server.state.DependencyInfo hCatDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("HIVE/HCAT");
        final org.apache.ambari.server.state.DependencyInfo tezClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("TEZ/TEZ_CLIENT");
        final org.apache.ambari.server.state.DependencyInfo mapReduceTwoClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("YARN/MAPREDUCE2_CLIENT");
        final org.apache.ambari.server.state.DependencyInfo oozieClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("OOZIE/OOZIE_CLIENT");
        mockSupport.replayAll();
        org.apache.ambari.server.controller.internal.Stack testStack = new org.apache.ambari.server.controller.internal.Stack("HDP", "2.1", mockMgmtController) {
            @java.lang.Override
            public java.util.Collection<org.apache.ambari.server.state.DependencyInfo> getDependenciesForComponent(java.lang.String component) {
                if (component.equals("FAKE_MONITORING_SERVER")) {
                    java.util.Set<org.apache.ambari.server.state.DependencyInfo> setOfDependencies = new java.util.HashSet<>();
                    setOfDependencies.add(hCatDependency);
                    setOfDependencies.add(tezClientDependency);
                    setOfDependencies.add(mapReduceTwoClientDependency);
                    setOfDependencies.add(oozieClientDependency);
                    return setOfDependencies;
                }
                return java.util.Collections.emptySet();
            }

            @java.lang.Override
            void registerConditionalDependencies() {
                super.registerConditionalDependencies();
                java.util.Map<org.apache.ambari.server.state.DependencyInfo, java.lang.String> dependencyConditionalServiceMap = getDependencyConditionalServiceMap();
                java.util.Collection<org.apache.ambari.server.state.DependencyInfo> monitoringDependencies = getDependenciesForComponent("FAKE_MONITORING_SERVER");
                for (org.apache.ambari.server.state.DependencyInfo dependency : monitoringDependencies) {
                    if (dependency.getComponentName().equals("HCAT")) {
                        dependencyConditionalServiceMap.put(dependency, "HIVE");
                    } else if (dependency.getComponentName().equals("OOZIE_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "OOZIE");
                    } else if (dependency.getComponentName().equals("YARN_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "YARN");
                    } else if (dependency.getComponentName().equals("TEZ_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "TEZ");
                    } else if (dependency.getComponentName().equals("MAPREDUCE2_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "MAPREDUCE2");
                    }
                }
            }
        };
        org.junit.Assert.assertEquals("Initial conditional dependency map should be empty", 0, testStack.getDependencyConditionalServiceMap().size());
        testStack.registerConditionalDependencies();
        org.junit.Assert.assertEquals("Set of conditional service mappings is an incorrect size", 4, testStack.getDependencyConditionalServiceMap().size());
        org.junit.Assert.assertEquals("Incorrect service dependency for HCAT", "HIVE", testStack.getDependencyConditionalServiceMap().get(hCatDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for TEZ_CLIENT", "TEZ", testStack.getDependencyConditionalServiceMap().get(tezClientDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for MAPREDUCE2_CLIENT", "MAPREDUCE2", testStack.getDependencyConditionalServiceMap().get(mapReduceTwoClientDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for OOZIE_CLIENT", "OOZIE", testStack.getDependencyConditionalServiceMap().get(oozieClientDependency));
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testStackRegisterConditionalDependenciesNoTezClient() throws java.lang.Exception {
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.AmbariManagementController mockMgmtController = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(mockMgmtController.getStackServices(EasyMock.isA(java.util.Set.class))).andReturn(java.util.Collections.<org.apache.ambari.server.controller.StackServiceResponse>emptySet());
        EasyMock.expect(mockMgmtController.getStackLevelConfigurations(((java.util.Set<org.apache.ambari.server.controller.StackLevelConfigurationRequest>) (EasyMock.anyObject())))).andReturn(java.util.Collections.emptySet()).anyTimes();
        final org.apache.ambari.server.state.DependencyInfo hCatDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("HIVE/HCAT");
        final org.apache.ambari.server.state.DependencyInfo yarnClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("YARN/YARN_CLIENT");
        final org.apache.ambari.server.state.DependencyInfo mapReduceTwoClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("YARN/MAPREDUCE2_CLIENT");
        final org.apache.ambari.server.state.DependencyInfo oozieClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("OOZIE/OOZIE_CLIENT");
        mockSupport.replayAll();
        org.apache.ambari.server.controller.internal.Stack testStack = new org.apache.ambari.server.controller.internal.Stack("HDP", "2.1", mockMgmtController) {
            @java.lang.Override
            public java.util.Collection<org.apache.ambari.server.state.DependencyInfo> getDependenciesForComponent(java.lang.String component) {
                if (component.equals("FAKE_MONITORING_SERVER")) {
                    java.util.Set<org.apache.ambari.server.state.DependencyInfo> setOfDependencies = new java.util.HashSet<>();
                    setOfDependencies.add(hCatDependency);
                    setOfDependencies.add(yarnClientDependency);
                    setOfDependencies.add(mapReduceTwoClientDependency);
                    setOfDependencies.add(oozieClientDependency);
                    return setOfDependencies;
                }
                return java.util.Collections.emptySet();
            }

            @java.lang.Override
            void registerConditionalDependencies() {
                super.registerConditionalDependencies();
                java.util.Map<org.apache.ambari.server.state.DependencyInfo, java.lang.String> dependencyConditionalServiceMap = getDependencyConditionalServiceMap();
                java.util.Collection<org.apache.ambari.server.state.DependencyInfo> monitoringDependencies = getDependenciesForComponent("FAKE_MONITORING_SERVER");
                for (org.apache.ambari.server.state.DependencyInfo dependency : monitoringDependencies) {
                    if (dependency.getComponentName().equals("HCAT")) {
                        dependencyConditionalServiceMap.put(dependency, "HIVE");
                    } else if (dependency.getComponentName().equals("OOZIE_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "OOZIE");
                    } else if (dependency.getComponentName().equals("YARN_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "YARN");
                    } else if (dependency.getComponentName().equals("TEZ_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "TEZ");
                    } else if (dependency.getComponentName().equals("MAPREDUCE2_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "MAPREDUCE2");
                    }
                }
            }
        };
        org.junit.Assert.assertEquals("Initial conditional dependency map should be empty", 0, testStack.getDependencyConditionalServiceMap().size());
        testStack.registerConditionalDependencies();
        org.junit.Assert.assertEquals("Set of conditional service mappings is an incorrect size", 4, testStack.getDependencyConditionalServiceMap().size());
        org.junit.Assert.assertEquals("Incorrect service dependency for HCAT", "HIVE", testStack.getDependencyConditionalServiceMap().get(hCatDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for YARN_CLIENT", "YARN", testStack.getDependencyConditionalServiceMap().get(yarnClientDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for MAPREDUCE2_CLIENT", "MAPREDUCE2", testStack.getDependencyConditionalServiceMap().get(mapReduceTwoClientDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for OOZIE_CLIENT", "OOZIE", testStack.getDependencyConditionalServiceMap().get(oozieClientDependency));
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testStackRegisterConditionalDependenciesNoMapReduceClient() throws java.lang.Exception {
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.AmbariManagementController mockMgmtController = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(mockMgmtController.getStackServices(EasyMock.isA(java.util.Set.class))).andReturn(java.util.Collections.<org.apache.ambari.server.controller.StackServiceResponse>emptySet());
        EasyMock.expect(mockMgmtController.getStackLevelConfigurations(((java.util.Set<org.apache.ambari.server.controller.StackLevelConfigurationRequest>) (EasyMock.anyObject())))).andReturn(java.util.Collections.emptySet()).anyTimes();
        final org.apache.ambari.server.state.DependencyInfo hCatDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("HIVE/HCAT");
        final org.apache.ambari.server.state.DependencyInfo yarnClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("YARN/YARN_CLIENT");
        final org.apache.ambari.server.state.DependencyInfo tezClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("TEZ/TEZ_CLIENT");
        final org.apache.ambari.server.state.DependencyInfo oozieClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("OOZIE/OOZIE_CLIENT");
        mockSupport.replayAll();
        org.apache.ambari.server.controller.internal.Stack testStack = new org.apache.ambari.server.controller.internal.Stack("HDP", "2.1", mockMgmtController) {
            @java.lang.Override
            public java.util.Collection<org.apache.ambari.server.state.DependencyInfo> getDependenciesForComponent(java.lang.String component) {
                if (component.equals("FAKE_MONITORING_SERVER")) {
                    java.util.Set<org.apache.ambari.server.state.DependencyInfo> setOfDependencies = new java.util.HashSet<>();
                    setOfDependencies.add(hCatDependency);
                    setOfDependencies.add(yarnClientDependency);
                    setOfDependencies.add(tezClientDependency);
                    setOfDependencies.add(oozieClientDependency);
                    return setOfDependencies;
                }
                return java.util.Collections.emptySet();
            }

            @java.lang.Override
            void registerConditionalDependencies() {
                super.registerConditionalDependencies();
                java.util.Map<org.apache.ambari.server.state.DependencyInfo, java.lang.String> dependencyConditionalServiceMap = getDependencyConditionalServiceMap();
                java.util.Collection<org.apache.ambari.server.state.DependencyInfo> monitoringDependencies = getDependenciesForComponent("FAKE_MONITORING_SERVER");
                for (org.apache.ambari.server.state.DependencyInfo dependency : monitoringDependencies) {
                    if (dependency.getComponentName().equals("HCAT")) {
                        dependencyConditionalServiceMap.put(dependency, "HIVE");
                    } else if (dependency.getComponentName().equals("OOZIE_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "OOZIE");
                    } else if (dependency.getComponentName().equals("YARN_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "YARN");
                    } else if (dependency.getComponentName().equals("TEZ_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "TEZ");
                    } else if (dependency.getComponentName().equals("MAPREDUCE2_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "MAPREDUCE2");
                    }
                }
            }
        };
        org.junit.Assert.assertEquals("Initial conditional dependency map should be empty", 0, testStack.getDependencyConditionalServiceMap().size());
        testStack.registerConditionalDependencies();
        org.junit.Assert.assertEquals("Set of conditional service mappings is an incorrect size", 4, testStack.getDependencyConditionalServiceMap().size());
        org.junit.Assert.assertEquals("Incorrect service dependency for HCAT", "HIVE", testStack.getDependencyConditionalServiceMap().get(hCatDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for YARN_CLIENT", "YARN", testStack.getDependencyConditionalServiceMap().get(yarnClientDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for TEZ_CLIENT", "TEZ", testStack.getDependencyConditionalServiceMap().get(tezClientDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for OOZIE_CLIENT", "OOZIE", testStack.getDependencyConditionalServiceMap().get(oozieClientDependency));
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testStackRegisterConditionalDependenciesNoOozieClient() throws java.lang.Exception {
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.controller.AmbariManagementController mockMgmtController = mockSupport.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        EasyMock.expect(mockMgmtController.getStackServices(EasyMock.isA(java.util.Set.class))).andReturn(java.util.Collections.<org.apache.ambari.server.controller.StackServiceResponse>emptySet());
        EasyMock.expect(mockMgmtController.getStackLevelConfigurations(((java.util.Set<org.apache.ambari.server.controller.StackLevelConfigurationRequest>) (EasyMock.anyObject())))).andReturn(java.util.Collections.emptySet()).anyTimes();
        final org.apache.ambari.server.state.DependencyInfo hCatDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("HIVE/HCAT");
        final org.apache.ambari.server.state.DependencyInfo yarnClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("YARN/YARN_CLIENT");
        final org.apache.ambari.server.state.DependencyInfo tezClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("TEZ/TEZ_CLIENT");
        final org.apache.ambari.server.state.DependencyInfo mapReduceTwoClientDependency = new org.apache.ambari.server.controller.internal.BaseBlueprintProcessorTest.TestDependencyInfo("YARN/MAPREDUCE2_CLIENT");
        mockSupport.replayAll();
        org.apache.ambari.server.controller.internal.Stack testStack = new org.apache.ambari.server.controller.internal.Stack("HDP", "2.1", mockMgmtController) {
            @java.lang.Override
            public java.util.Collection<org.apache.ambari.server.state.DependencyInfo> getDependenciesForComponent(java.lang.String component) {
                if (component.equals("FAKE_MONITORING_SERVER")) {
                    java.util.Set<org.apache.ambari.server.state.DependencyInfo> setOfDependencies = new java.util.HashSet<>();
                    setOfDependencies.add(hCatDependency);
                    setOfDependencies.add(yarnClientDependency);
                    setOfDependencies.add(tezClientDependency);
                    setOfDependencies.add(mapReduceTwoClientDependency);
                    return setOfDependencies;
                }
                return java.util.Collections.emptySet();
            }

            @java.lang.Override
            void registerConditionalDependencies() {
                super.registerConditionalDependencies();
                java.util.Map<org.apache.ambari.server.state.DependencyInfo, java.lang.String> dependencyConditionalServiceMap = getDependencyConditionalServiceMap();
                java.util.Collection<org.apache.ambari.server.state.DependencyInfo> monitoringDependencies = getDependenciesForComponent("FAKE_MONITORING_SERVER");
                for (org.apache.ambari.server.state.DependencyInfo dependency : monitoringDependencies) {
                    if (dependency.getComponentName().equals("HCAT")) {
                        dependencyConditionalServiceMap.put(dependency, "HIVE");
                    } else if (dependency.getComponentName().equals("OOZIE_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "OOZIE");
                    } else if (dependency.getComponentName().equals("YARN_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "YARN");
                    } else if (dependency.getComponentName().equals("TEZ_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "TEZ");
                    } else if (dependency.getComponentName().equals("MAPREDUCE2_CLIENT")) {
                        dependencyConditionalServiceMap.put(dependency, "MAPREDUCE2");
                    }
                }
            }
        };
        org.junit.Assert.assertEquals("Initial conditional dependency map should be empty", 0, testStack.getDependencyConditionalServiceMap().size());
        testStack.registerConditionalDependencies();
        org.junit.Assert.assertEquals("Set of conditional service mappings is an incorrect size", 4, testStack.getDependencyConditionalServiceMap().size());
        org.junit.Assert.assertEquals("Incorrect service dependency for HCAT", "HIVE", testStack.getDependencyConditionalServiceMap().get(hCatDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for YARN_CLIENT", "YARN", testStack.getDependencyConditionalServiceMap().get(yarnClientDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for TEZ_CLIENT", "TEZ", testStack.getDependencyConditionalServiceMap().get(tezClientDependency));
        org.junit.Assert.assertEquals("Incorrect service dependency for MAPREDUCE2_CLIENT", "MAPREDUCE2", testStack.getDependencyConditionalServiceMap().get(mapReduceTwoClientDependency));
        mockSupport.verifyAll();
    }

    private static class TestDependencyInfo extends org.apache.ambari.server.state.DependencyInfo {
        TestDependencyInfo(java.lang.String dependencyName) {
            setName(dependencyName);
        }
    }
}