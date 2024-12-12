package org.apache.ambari.server.state.repository;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.expect;
public class VersionDefinitionTest extends org.easymock.EasyMockSupport {
    private static java.io.File file = new java.io.File("src/test/resources/version_definition_test.xml");

    @org.junit.Test
    public void testLoadingString() throws java.lang.Exception {
        java.lang.String xmlString = org.apache.commons.io.FileUtils.readFileToString(org.apache.ambari.server.state.repository.VersionDefinitionTest.file, java.nio.charset.Charset.defaultCharset());
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(xmlString);
        validateXml(xml);
    }

    @org.junit.Test
    public void testLoadingUrl() throws java.lang.Exception {
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(org.apache.ambari.server.state.repository.VersionDefinitionTest.file.toURI().toURL());
        validateXml(xml);
    }

    private void validateXml(org.apache.ambari.server.state.repository.VersionDefinitionXml xml) throws java.lang.Exception {
        org.junit.Assert.assertNotNull(xml.release);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.PATCH, xml.release.repositoryType);
        org.junit.Assert.assertEquals("HDP-2.3", xml.release.stackId);
        org.junit.Assert.assertEquals("2.3.4.1", xml.release.version);
        org.junit.Assert.assertEquals("2.3.4.[1-9]", xml.release.compatibleWith);
        org.junit.Assert.assertEquals("http://docs.hortonworks.com/HDPDocuments/HDP2/HDP-2.3.4/", xml.release.releaseNotes);
        org.junit.Assert.assertEquals(4, xml.manifestServices.size());
        org.junit.Assert.assertEquals("HDFS-271", xml.manifestServices.get(0).serviceId);
        org.junit.Assert.assertEquals("HDFS", xml.manifestServices.get(0).serviceName);
        org.junit.Assert.assertEquals("2.7.1", xml.manifestServices.get(0).version);
        org.junit.Assert.assertEquals("10", xml.manifestServices.get(0).versionId);
        org.junit.Assert.assertEquals(3, xml.availableServices.size());
        org.junit.Assert.assertEquals("HDFS-271", xml.availableServices.get(0).serviceIdReference);
        org.junit.Assert.assertEquals(0, xml.availableServices.get(0).components.size());
        org.junit.Assert.assertEquals("HIVE-110", xml.availableServices.get(2).serviceIdReference);
        org.junit.Assert.assertEquals(1, xml.availableServices.get(2).components.size());
        org.junit.Assert.assertNotNull(xml.repositoryInfo);
        org.junit.Assert.assertEquals(2, xml.repositoryInfo.getOses().size());
        org.junit.Assert.assertEquals("redhat6", xml.repositoryInfo.getOses().get(0).getFamily());
        org.junit.Assert.assertEquals(2, xml.repositoryInfo.getOses().get(0).getRepos().size());
        org.junit.Assert.assertEquals("http://public-repo-1.hortonworks.com/HDP/centos6/2.x/updates/2.3.0.0", xml.repositoryInfo.getOses().get(0).getRepos().get(0).getBaseUrl());
        org.junit.Assert.assertEquals("HDP-2.3", xml.repositoryInfo.getOses().get(0).getRepos().get(0).getRepoId());
        org.junit.Assert.assertEquals("HDP", xml.repositoryInfo.getOses().get(0).getRepos().get(0).getRepoName());
        org.junit.Assert.assertNull(xml.repositoryInfo.getOses().get(0).getPackageVersion());
    }

    @org.junit.Test
    public void testAllServices() throws java.lang.Exception {
        java.io.File f = new java.io.File("src/test/resources/version_definition_test_all_services.xml");
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        org.apache.ambari.server.state.StackInfo stack = new org.apache.ambari.server.state.StackInfo() {
            @java.lang.Override
            public org.apache.ambari.server.state.ServiceInfo getService(java.lang.String name) {
                return null;
            }
        };
        org.junit.Assert.assertEquals(4, xml.manifestServices.size());
        org.junit.Assert.assertEquals(3, xml.getAvailableServices(stack).size());
    }

    @org.junit.Test
    public void testStackManifest() throws java.lang.Exception {
        java.io.File f = new java.io.File("src/test/resources/version_definition_test_all_services.xml");
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        org.apache.ambari.server.state.StackInfo stack = new org.apache.ambari.server.state.StackInfo() {
            private java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> m_services = new java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceInfo>() {
                {
                    put("HDFS", org.apache.ambari.server.state.repository.VersionDefinitionTest.makeService("HDFS"));
                    put("HBASE", org.apache.ambari.server.state.repository.VersionDefinitionTest.makeService("HBASE"));
                    put("HIVE", org.apache.ambari.server.state.repository.VersionDefinitionTest.makeService("HIVE"));
                    put("YARN", org.apache.ambari.server.state.repository.VersionDefinitionTest.makeService("YARN"));
                }
            };

            @java.lang.Override
            public org.apache.ambari.server.state.ServiceInfo getService(java.lang.String name) {
                return m_services.get(name);
            }

            @java.lang.Override
            public synchronized java.util.Collection<org.apache.ambari.server.state.ServiceInfo> getServices() {
                return m_services.values();
            }
        };
        java.util.List<org.apache.ambari.server.state.repository.ManifestServiceInfo> stackServices = xml.getStackServices(stack);
        org.junit.Assert.assertEquals(4, xml.manifestServices.size());
        org.junit.Assert.assertEquals(3, xml.getAvailableServices(stack).size());
        org.junit.Assert.assertEquals(4, stackServices.size());
        boolean foundHdfs = false;
        boolean foundYarn = false;
        boolean foundHive = false;
        for (org.apache.ambari.server.state.repository.ManifestServiceInfo msi : stackServices) {
            if ("HDFS".equals(msi.m_name)) {
                foundHdfs = true;
                org.junit.Assert.assertEquals("HDFS Display", msi.m_display);
                org.junit.Assert.assertEquals("HDFS Comment", msi.m_comment);
                org.junit.Assert.assertEquals(1, msi.m_versions.size());
                org.junit.Assert.assertEquals("2.7.1", msi.m_versions.iterator().next());
            } else if ("YARN".equals(msi.m_name)) {
                foundYarn = true;
                org.junit.Assert.assertEquals(1, msi.m_versions.size());
                org.junit.Assert.assertEquals("1.1.1", msi.m_versions.iterator().next());
            } else if ("HIVE".equals(msi.m_name)) {
                foundHive = true;
                org.junit.Assert.assertEquals(2, msi.m_versions.size());
                org.junit.Assert.assertTrue(msi.m_versions.contains("1.1.0"));
                org.junit.Assert.assertTrue(msi.m_versions.contains("2.0.0"));
            }
        }
        org.junit.Assert.assertTrue(foundHdfs);
        org.junit.Assert.assertTrue(foundYarn);
        org.junit.Assert.assertTrue(foundHive);
    }

    @org.junit.Test
    public void testSerialization() throws java.lang.Exception {
        java.io.File f = new java.io.File("src/test/resources/version_definition_test_all_services.xml");
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        java.lang.String xmlString = xml.toXml();
        xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(xmlString);
        org.junit.Assert.assertNotNull(xml.release.build);
        org.junit.Assert.assertEquals("1234", xml.release.build);
        f = new java.io.File("src/test/resources/version_definition_with_tags.xml");
        xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        xmlString = xml.toXml();
        xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(xmlString);
        org.junit.Assert.assertEquals(2, xml.repositoryInfo.getOses().size());
        java.util.List<org.apache.ambari.server.state.stack.RepositoryXml.Repo> repos = null;
        for (org.apache.ambari.server.state.stack.RepositoryXml.Os os : xml.repositoryInfo.getOses()) {
            if (os.getFamily().equals("redhat6")) {
                repos = os.getRepos();
            }
        }
        org.junit.Assert.assertNotNull(repos);
        org.junit.Assert.assertEquals(3, repos.size());
        org.apache.ambari.server.state.stack.RepositoryXml.Repo found = null;
        for (org.apache.ambari.server.state.stack.RepositoryXml.Repo repo : repos) {
            if (repo.getRepoName().equals("HDP-GPL")) {
                found = repo;
                break;
            }
        }
        org.junit.Assert.assertNotNull(found);
        org.junit.Assert.assertNotNull(found.getTags());
        org.junit.Assert.assertEquals(1, found.getTags().size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.state.stack.RepoTag.GPL, found.getTags().iterator().next());
    }

    @org.junit.Test
    public void testMerger() throws java.lang.Exception {
        java.io.File f = new java.io.File("src/test/resources/version_definition_test_all_services.xml");
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml1 = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml2 = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        org.junit.Assert.assertEquals(2, xml1.repositoryInfo.getOses().size());
        org.junit.Assert.assertEquals(2, xml2.repositoryInfo.getOses().size());
        org.apache.ambari.server.state.stack.RepositoryXml.Os target = null;
        for (org.apache.ambari.server.state.stack.RepositoryXml.Os os : xml1.repositoryInfo.getOses()) {
            if (os.getFamily().equals("redhat7")) {
                target = os;
            }
        }
        org.junit.Assert.assertNotNull(target);
        xml1.repositoryInfo.getOses().remove(target);
        target = null;
        for (org.apache.ambari.server.state.stack.RepositoryXml.Os os : xml2.repositoryInfo.getOses()) {
            if (os.getFamily().equals("redhat6")) {
                target = os;
            } else {
                java.lang.reflect.Field field = org.apache.ambari.server.state.stack.RepositoryXml.Os.class.getDeclaredField("packageVersion");
                field.setAccessible(true);
                field.set(os, "2_3_4_2");
            }
        }
        org.junit.Assert.assertNotNull(target);
        xml2.repositoryInfo.getOses().remove(target);
        xml2.release.version = "2.3.4.2";
        xml2.release.build = "2468";
        org.junit.Assert.assertEquals(1, xml1.repositoryInfo.getOses().size());
        org.junit.Assert.assertEquals(1, xml2.repositoryInfo.getOses().size());
        org.apache.ambari.server.state.repository.VersionDefinitionXml.Merger builder = new org.apache.ambari.server.state.repository.VersionDefinitionXml.Merger();
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml3 = builder.merge();
        org.junit.Assert.assertNull(xml3);
        builder.add(xml1.release.version, xml1);
        builder.add("", xml2);
        xml3 = builder.merge();
        org.junit.Assert.assertNotNull(xml3);
        org.junit.Assert.assertNull("Merged definition cannot have a build", xml3.release.build);
        org.junit.Assert.assertEquals(xml3.release.version, "2.3.4.1");
        org.apache.ambari.server.state.stack.RepositoryXml.Os redhat6 = null;
        org.apache.ambari.server.state.stack.RepositoryXml.Os redhat7 = null;
        org.junit.Assert.assertEquals(2, xml3.repositoryInfo.getOses().size());
        for (org.apache.ambari.server.state.stack.RepositoryXml.Os os : xml3.repositoryInfo.getOses()) {
            if (os.getFamily().equals("redhat6")) {
                redhat6 = os;
            } else if (os.getFamily().equals("redhat7")) {
                redhat7 = os;
            }
        }
        org.junit.Assert.assertNotNull(redhat6);
        org.junit.Assert.assertNotNull(redhat7);
        org.junit.Assert.assertNull(redhat6.getPackageVersion());
        org.junit.Assert.assertEquals("2_3_4_2", redhat7.getPackageVersion());
        xml3.toXml();
    }

    @org.junit.Test
    public void testLoadingBadNewLine() throws java.lang.Exception {
        java.util.List<?> lines = org.apache.commons.io.FileUtils.readLines(org.apache.ambari.server.state.repository.VersionDefinitionTest.file);
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        for (java.lang.Object line : lines) {
            java.lang.String lineString = line.toString().trim();
            if (lineString.startsWith("<baseurl>")) {
                lineString = lineString.replace("<baseurl>", "");
                lineString = lineString.replace("</baseurl>", "");
                builder.append("<baseurl>\n");
                builder.append(lineString).append('\n');
                builder.append("</baseurl>\n");
            } else if (lineString.startsWith("<version>")) {
                lineString = lineString.replace("<version>", "");
                lineString = lineString.replace("</version>", "");
                builder.append("<version>\n");
                builder.append(lineString).append('\n');
                builder.append("</version>\n");
            } else {
                builder.append(line.toString().trim()).append('\n');
            }
        }
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(builder.toString());
        validateXml(xml);
    }

    @org.junit.Test
    public void testPackageVersion() throws java.lang.Exception {
        java.io.File f = new java.io.File("src/test/resources/hbase_version_test.xml");
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        java.lang.String xmlString = xml.toXml();
        xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(xmlString);
        org.junit.Assert.assertNotNull(xml.release.build);
        org.junit.Assert.assertEquals("3396", xml.release.build);
        org.junit.Assert.assertEquals("redhat6", xml.repositoryInfo.getOses().get(0).getFamily());
        org.junit.Assert.assertEquals("2_3_4_0_3396", xml.repositoryInfo.getOses().get(0).getPackageVersion());
        org.junit.Assert.assertNotNull(xml.getPackageVersion("redhat6"));
        org.junit.Assert.assertEquals("2_3_4_0_3396", xml.getPackageVersion("redhat6"));
        org.junit.Assert.assertNull(xml.getPackageVersion("suse11"));
    }

    @org.junit.Test
    public void testMaintVersion() throws java.lang.Exception {
        java.io.File f = new java.io.File("src/test/resources/version_definition_test_maint.xml");
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        java.lang.String xmlString = xml.toXml();
        xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(xmlString);
        org.junit.Assert.assertEquals(org.apache.ambari.spi.RepositoryType.MAINT, xml.release.repositoryType);
        org.junit.Assert.assertEquals("2.3.4.1", xml.release.version);
        org.junit.Assert.assertEquals("1234", xml.release.build);
        org.junit.Assert.assertEquals("redhat6", xml.repositoryInfo.getOses().get(0).getFamily());
        java.util.List<org.apache.ambari.server.state.repository.AvailableServiceReference> availableServices = xml.availableServices;
        org.junit.Assert.assertEquals(3, availableServices.size());
        java.util.List<org.apache.ambari.server.state.repository.ManifestService> manifestServices = xml.manifestServices;
        org.junit.Assert.assertEquals(4, manifestServices.size());
        org.apache.ambari.server.state.repository.ManifestService hdfs = null;
        org.apache.ambari.server.state.repository.ManifestService hive = null;
        for (org.apache.ambari.server.state.repository.ManifestService as : manifestServices) {
            if (as.serviceId.equals("HDFS-271")) {
                hdfs = as;
            } else if (as.serviceId.equals("HIVE-200")) {
                hive = as;
            }
        }
        org.junit.Assert.assertNotNull(hdfs);
        org.junit.Assert.assertNotNull(hive);
        org.junit.Assert.assertEquals("2.3.4.0", hdfs.releaseVersion);
        org.junit.Assert.assertNull(hive.releaseVersion);
        org.apache.ambari.server.state.StackInfo stack = new org.apache.ambari.server.state.StackInfo() {
            @java.lang.Override
            public org.apache.ambari.server.state.ServiceInfo getService(java.lang.String name) {
                return org.apache.ambari.server.state.repository.VersionDefinitionTest.makeService("HIVE", "HIVE_METASTORE");
            }
        };
        java.util.Collection<org.apache.ambari.server.state.repository.AvailableService> availables = xml.getAvailableServices(stack);
        org.junit.Assert.assertEquals(2, availables.size());
        boolean found = false;
        for (org.apache.ambari.server.state.repository.AvailableService available : availables) {
            if (available.getName().equals("HIVE")) {
                found = true;
                org.junit.Assert.assertEquals(2, available.getVersions().size());
                for (org.apache.ambari.server.state.repository.AvailableVersion version : available.getVersions()) {
                    if (version.getVersion().equals("1.1.0")) {
                        org.junit.Assert.assertEquals("1.0.9", version.getReleaseVersion());
                    } else {
                        org.junit.Assert.assertNull(version.getReleaseVersion());
                    }
                }
            }
        }
        org.junit.Assert.assertTrue("Found available version for HIVE", found);
    }

    @org.junit.Test
    public void testAvailableFull() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repositoryVersion.getVersion()).andReturn("2.3.4.0").atLeastOnce();
        org.apache.ambari.server.state.Service serviceHdfs = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(serviceHdfs.getName()).andReturn("HDFS").atLeastOnce();
        EasyMock.expect(serviceHdfs.getDisplayName()).andReturn("HDFS").atLeastOnce();
        EasyMock.expect(serviceHdfs.getDesiredRepositoryVersion()).andReturn(repositoryVersion).atLeastOnce();
        org.apache.ambari.server.state.Service serviceHBase = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(serviceHBase.getName()).andReturn("HBASE").atLeastOnce();
        EasyMock.expect(serviceHBase.getDisplayName()).andReturn("HBase").atLeastOnce();
        EasyMock.expect(serviceHBase.getDesiredRepositoryVersion()).andReturn(repositoryVersion).atLeastOnce();
        org.apache.ambari.server.state.StackInfo stackInfo = createNiceMock(org.apache.ambari.server.state.StackInfo.class);
        EasyMock.expect(stackInfo.getReleaseVersion()).andReturn(new org.apache.ambari.server.state.repository.DefaultStackVersion()).atLeastOnce();
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        EasyMock.expect(ami.getStack(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class))).andReturn(stackInfo).atLeastOnce();
        org.apache.ambari.server.state.Service serviceAMS = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(cluster.getServices()).andReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HDFS", serviceHdfs).put("HBASE", serviceHBase).put("AMBARI_METRICS", serviceAMS).build()).atLeastOnce();
        replayAll();
        java.io.File f = new java.io.File("src/test/resources/version_definition_test_all_services.xml");
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        org.apache.ambari.server.state.repository.ClusterVersionSummary summary = xml.getClusterSummary(cluster, ami);
        org.junit.Assert.assertEquals(2, summary.getAvailableServiceNames().size());
        f = new java.io.File("src/test/resources/version_definition_test_maint.xml");
        xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        summary = xml.getClusterSummary(cluster, ami);
        org.junit.Assert.assertEquals(0, summary.getAvailableServiceNames().size());
        f = new java.io.File("src/test/resources/version_definition_test_maint.xml");
        xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        xml.release.repositoryType = org.apache.ambari.spi.RepositoryType.STANDARD;
        xml.availableServices = java.util.Collections.emptyList();
        summary = xml.getClusterSummary(cluster, ami);
        org.junit.Assert.assertEquals(2, summary.getAvailableServiceNames().size());
        f = new java.io.File("src/test/resources/version_definition_test_maint_partial.xml");
        xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        summary = xml.getClusterSummary(cluster, ami);
        org.junit.Assert.assertEquals(1, summary.getAvailableServiceNames().size());
    }

    @org.junit.Test
    public void testAvailableBuildVersion() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);
        EasyMock.expect(repositoryVersion.getVersion()).andReturn("2.3.4.1-1").atLeastOnce();
        org.apache.ambari.server.state.Service serviceHdfs = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(serviceHdfs.getName()).andReturn("HDFS").atLeastOnce();
        EasyMock.expect(serviceHdfs.getDisplayName()).andReturn("HDFS").atLeastOnce();
        EasyMock.expect(serviceHdfs.getDesiredRepositoryVersion()).andReturn(repositoryVersion).atLeastOnce();
        org.apache.ambari.server.state.Service serviceHBase = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(serviceHBase.getName()).andReturn("HBASE").atLeastOnce();
        EasyMock.expect(serviceHBase.getDisplayName()).andReturn("HBase").atLeastOnce();
        EasyMock.expect(serviceHBase.getDesiredRepositoryVersion()).andReturn(repositoryVersion).atLeastOnce();
        org.apache.ambari.server.state.StackInfo stackInfo = createNiceMock(org.apache.ambari.server.state.StackInfo.class);
        EasyMock.expect(stackInfo.getReleaseVersion()).andReturn(new org.apache.ambari.server.state.repository.DefaultStackVersion()).atLeastOnce();
        org.apache.ambari.server.api.services.AmbariMetaInfo ami = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        EasyMock.expect(ami.getStack(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class))).andReturn(stackInfo).atLeastOnce();
        org.apache.ambari.server.state.Service serviceAMS = createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(cluster.getServices()).andReturn(com.google.common.collect.ImmutableMap.<java.lang.String, org.apache.ambari.server.state.Service>builder().put("HDFS", serviceHdfs).put("HBASE", serviceHBase).put("AMBARI_METRICS", serviceAMS).build()).atLeastOnce();
        replayAll();
        java.io.File f = new java.io.File("src/test/resources/version_definition_test_maint_partial.xml");
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        xml.release.version = "2.3.4.1";
        xml.release.build = "2";
        org.apache.ambari.server.state.repository.ClusterVersionSummary summary = xml.getClusterSummary(cluster, ami);
        org.junit.Assert.assertEquals(1, summary.getAvailableServiceNames().size());
    }

    @org.junit.Test
    public void testRecursiveDependencyDetection() throws java.lang.Exception {
        java.io.File f = new java.io.File("src/test/resources/version_definition_test_all_services.xml");
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> dependencies = new java.util.HashMap<>();
        dependencies.put("A", com.google.common.collect.Lists.newArrayList("B", "X"));
        dependencies.put("B", com.google.common.collect.Lists.newArrayList("C", "D", "E"));
        dependencies.put("E", com.google.common.collect.Lists.newArrayList("A", "F"));
        dependencies.put("F", com.google.common.collect.Lists.newArrayList("B", "E"));
        dependencies.put("X", com.google.common.collect.Lists.newArrayList("Y", "Z", "A"));
        dependencies.put("Z", com.google.common.collect.Lists.newArrayList("B"));
        java.util.Set<java.lang.String> installedServices = com.google.common.collect.Sets.newHashSet("A", "B", "C", "D", "E", "F", "G", "H");
        java.util.Set<java.lang.String> servicesInUpgrade = com.google.common.collect.Sets.newHashSet("A");
        java.util.Set<java.lang.String> results = xml.getRecursiveDependencies(com.google.common.collect.Sets.newHashSet("B"), dependencies, servicesInUpgrade, installedServices);
        org.junit.Assert.assertEquals(5, results.size());
        org.junit.Assert.assertTrue(results.contains("B"));
        org.junit.Assert.assertTrue(results.contains("C"));
        org.junit.Assert.assertTrue(results.contains("D"));
        org.junit.Assert.assertTrue(results.contains("E"));
        org.junit.Assert.assertTrue(results.contains("F"));
        servicesInUpgrade = com.google.common.collect.Sets.newHashSet("A", "B", "C", "E", "F");
        results = xml.getRecursiveDependencies(com.google.common.collect.Sets.newHashSet("D"), dependencies, servicesInUpgrade, installedServices);
        org.junit.Assert.assertEquals(1, results.size());
        org.junit.Assert.assertTrue(results.contains("D"));
        servicesInUpgrade = com.google.common.collect.Sets.newHashSet("A", "F");
        results = xml.getRecursiveDependencies(com.google.common.collect.Sets.newHashSet("B", "E"), dependencies, servicesInUpgrade, installedServices);
        org.junit.Assert.assertEquals(4, results.size());
        org.junit.Assert.assertTrue(results.contains("B"));
        org.junit.Assert.assertTrue(results.contains("C"));
        org.junit.Assert.assertTrue(results.contains("D"));
        org.junit.Assert.assertTrue(results.contains("E"));
    }

    @org.junit.Test
    public void testBuild() throws java.lang.Exception {
        org.apache.ambari.server.state.ServiceInfo serviceWithVersionAdvertised = createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        org.apache.ambari.server.state.ServiceInfo serviceWithoutVersionAdvertised = createNiceMock(org.apache.ambari.server.state.ServiceInfo.class);
        java.util.List<org.apache.ambari.server.state.ServiceInfo> stackServices = com.google.common.collect.Lists.newArrayList(serviceWithVersionAdvertised, serviceWithoutVersionAdvertised);
        EasyMock.expect(serviceWithVersionAdvertised.isVersionAdvertised()).andReturn(true).atLeastOnce();
        EasyMock.expect(serviceWithVersionAdvertised.getName()).andReturn("BAR").atLeastOnce();
        EasyMock.expect(serviceWithVersionAdvertised.getVersion()).andReturn("1.5.0").atLeastOnce();
        EasyMock.expect(serviceWithoutVersionAdvertised.isVersionAdvertised()).andReturn(false).atLeastOnce();
        EasyMock.expect(serviceWithoutVersionAdvertised.getName()).andReturn("BAZ").atLeastOnce();
        EasyMock.expect(serviceWithoutVersionAdvertised.getVersion()).andReturn("2.0.0").atLeastOnce();
        java.io.File f = new java.io.File("src/test/resources/version_definition_test_all_services.xml");
        org.apache.ambari.server.state.repository.VersionDefinitionXml xml1 = org.apache.ambari.server.state.repository.VersionDefinitionXml.load(f.toURI().toURL());
        org.apache.ambari.server.state.stack.RepositoryXml repositoryXml = createNiceMock(org.apache.ambari.server.state.stack.RepositoryXml.class);
        EasyMock.expect(repositoryXml.getOses()).andReturn(xml1.repositoryInfo.getOses()).atLeastOnce();
        org.apache.ambari.server.state.StackInfo stackInfo = createNiceMock(org.apache.ambari.server.state.StackInfo.class);
        EasyMock.expect(stackInfo.getName()).andReturn("FOO").anyTimes();
        EasyMock.expect(stackInfo.getVersion()).andReturn("1.0.0").anyTimes();
        EasyMock.expect(stackInfo.getServices()).andReturn(stackServices).atLeastOnce();
        EasyMock.expect(stackInfo.getRepositoryXml()).andReturn(repositoryXml).atLeastOnce();
        replayAll();
        org.apache.ambari.server.state.repository.VersionDefinitionXml vdf = org.apache.ambari.server.state.repository.VersionDefinitionXml.build(stackInfo);
        org.junit.Assert.assertEquals(1, vdf.manifestServices.size());
        java.util.List<org.apache.ambari.server.state.repository.ManifestServiceInfo> manifestServices = vdf.getStackServices(stackInfo);
        org.junit.Assert.assertEquals(2, manifestServices.size());
        verifyAll();
    }

    private static org.apache.ambari.server.state.ServiceInfo makeService(final java.lang.String name) {
        return new org.apache.ambari.server.state.ServiceInfo() {
            @java.lang.Override
            public java.lang.String getName() {
                return name;
            }

            @java.lang.Override
            public java.lang.String getDisplayName() {
                return name + " Display";
            }

            @java.lang.Override
            public java.lang.String getVersion() {
                return "1.1.1";
            }

            @java.lang.Override
            public java.lang.String getComment() {
                return name + " Comment";
            }
        };
    }

    private static org.apache.ambari.server.state.ServiceInfo makeService(final java.lang.String name, final java.lang.String component) {
        return new org.apache.ambari.server.state.ServiceInfo() {
            @java.lang.Override
            public java.lang.String getName() {
                return name;
            }

            @java.lang.Override
            public java.lang.String getDisplayName() {
                return name + " Display";
            }

            @java.lang.Override
            public java.lang.String getVersion() {
                return "1.1.1";
            }

            @java.lang.Override
            public java.lang.String getComment() {
                return name + " Comment";
            }

            @java.lang.Override
            public org.apache.ambari.server.state.ComponentInfo getComponentByName(java.lang.String name) {
                return null;
            }
        };
    }
}