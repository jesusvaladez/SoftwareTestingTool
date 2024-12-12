package org.apache.ambari.view.utils.hdfs;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.protocol.ErasureCodingPolicy;
import org.apache.hadoop.io.erasurecode.ECSchema;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class HdfsApiTest {
    private org.apache.hadoop.fs.FileSystem fs;

    private org.apache.ambari.view.utils.hdfs.HdfsApi hdfsApi;

    private org.apache.hadoop.conf.Configuration conf;

    private org.apache.hadoop.hdfs.MiniDFSCluster hdfsCluster;

    @org.junit.Before
    public void setup() throws java.io.IOException, org.apache.ambari.view.utils.hdfs.HdfsApiException, java.lang.InterruptedException {
        java.io.File baseDir = new java.io.File("./target/hdfs/" + "HdfsApiTest.filterAndTruncateDirStatus").getAbsoluteFile();
        org.apache.hadoop.fs.FileUtil.fullyDelete(baseDir);
        conf = new org.apache.hadoop.conf.Configuration();
        conf.set(MiniDFSCluster.HDFS_MINIDFS_BASEDIR, baseDir.getAbsolutePath());
        org.apache.hadoop.hdfs.MiniDFSCluster.Builder builder = new org.apache.hadoop.hdfs.MiniDFSCluster.Builder(conf);
        hdfsCluster = builder.build();
        java.lang.String hdfsURI = hdfsCluster.getURI() + "/";
        conf.set("webhdfs.url", hdfsURI);
        conf.set("fs.defaultFS", hdfsURI);
        fs = org.apache.hadoop.fs.FileSystem.get(conf);
        hdfsApi = new org.apache.ambari.view.utils.hdfs.HdfsApi(conf, fs, null);
    }

    @org.junit.After
    public void tearDown() {
        hdfsCluster.shutdown();
    }

    @org.junit.Test
    public void testWith_EC_And_Encryption() {
        org.apache.ambari.view.utils.hdfs.DummyFileStatus fileStatus = Mockito.mock(org.apache.ambari.view.utils.hdfs.DummyFileStatus.class);
        org.apache.hadoop.fs.permission.FsPermission fsPermission = new org.apache.hadoop.fs.permission.FsPermission(((short) (0777)));
        java.lang.String ecPolicyName = "Some-EC-Policy";
        org.apache.hadoop.io.erasurecode.ECSchema ecSchema = new org.apache.hadoop.io.erasurecode.ECSchema("someSchema", 1, 1);
        org.apache.hadoop.hdfs.protocol.ErasureCodingPolicy erasureCodingPolicy = new org.apache.hadoop.hdfs.protocol.ErasureCodingPolicy(ecPolicyName, ecSchema, 1024, ((byte) (0)));
        Mockito.when(fileStatus.getPermission()).thenReturn(fsPermission);
        Mockito.when(fileStatus.getPath()).thenReturn(new org.apache.hadoop.fs.Path("/test/path"));
        Mockito.when(fileStatus.getErasureCodingPolicy()).thenReturn(erasureCodingPolicy);
        Mockito.when(fileStatus.isErasureCoded()).thenReturn(true);
        Mockito.when(fileStatus.isEncrypted()).thenReturn(true);
        java.util.Map<java.lang.String, java.lang.Object> json = hdfsApi.fileStatusToJSON(fileStatus);
        org.junit.Assert.assertEquals(java.lang.Boolean.TRUE, json.get(org.apache.ambari.view.utils.hdfs.HdfsApi.KeyIsErasureCoded));
        org.junit.Assert.assertEquals(java.lang.Boolean.TRUE, json.get(org.apache.ambari.view.utils.hdfs.HdfsApi.KeyIsEncrypted));
        org.junit.Assert.assertEquals(json.get(org.apache.ambari.view.utils.hdfs.HdfsApi.KeyErasureCodingPolicyName), ecPolicyName);
    }

    @org.junit.Test
    public void testWithout_EC_And_Encryption() {
        org.apache.hadoop.fs.permission.FsPermission fsPermission = new org.apache.hadoop.fs.permission.FsPermission(((short) (0777)));
        org.apache.ambari.view.utils.hdfs.DummyFileStatus fileStatus = Mockito.mock(org.apache.ambari.view.utils.hdfs.DummyFileStatus.class);
        Mockito.when(fileStatus.getPermission()).thenReturn(fsPermission);
        Mockito.when(fileStatus.getPath()).thenReturn(new org.apache.hadoop.fs.Path("/test/path"));
        Mockito.when(fileStatus.getErasureCodingPolicy()).thenReturn(null);
        Mockito.when(fileStatus.isErasureCoded()).thenReturn(false);
        Mockito.when(fileStatus.isEncrypted()).thenReturn(false);
        java.util.Map<java.lang.String, java.lang.Object> json = hdfsApi.fileStatusToJSON(fileStatus);
        org.junit.Assert.assertEquals(java.lang.Boolean.FALSE, json.get(org.apache.ambari.view.utils.hdfs.HdfsApi.KeyIsErasureCoded));
        org.junit.Assert.assertEquals(java.lang.Boolean.FALSE, json.get(org.apache.ambari.view.utils.hdfs.HdfsApi.KeyIsEncrypted));
        org.junit.Assert.assertNull(json.get(org.apache.ambari.view.utils.hdfs.HdfsApi.KeyErasureCodingPolicyName));
    }

    @org.junit.Test
    public void testNonHdfsFileStatus() {
        org.apache.ambari.view.utils.hdfs.DummyNonHdfsFileStatus fileStatus = Mockito.mock(org.apache.ambari.view.utils.hdfs.DummyNonHdfsFileStatus.class);
        org.apache.hadoop.fs.permission.FsPermission fsPermission = new org.apache.hadoop.fs.permission.FsPermission(((short) (0777)));
        Mockito.when(fileStatus.getPermission()).thenReturn(fsPermission);
        Mockito.when(fileStatus.getPath()).thenReturn(new org.apache.hadoop.fs.Path("/test/path"));
        Mockito.when(fileStatus.isErasureCoded()).thenReturn(false);
        Mockito.when(fileStatus.isEncrypted()).thenReturn(false);
        java.util.Map<java.lang.String, java.lang.Object> json = hdfsApi.fileStatusToJSON(fileStatus);
        org.junit.Assert.assertEquals(java.lang.Boolean.FALSE, json.get(org.apache.ambari.view.utils.hdfs.HdfsApi.KeyIsErasureCoded));
        org.junit.Assert.assertEquals(java.lang.Boolean.FALSE, json.get(org.apache.ambari.view.utils.hdfs.HdfsApi.KeyIsEncrypted));
        org.junit.Assert.assertNull(json.get(org.apache.ambari.view.utils.hdfs.HdfsApi.KeyErasureCodingPolicyName));
    }

    @org.junit.Test
    public void filterAndTruncateDirStatus() throws java.lang.Exception {
        {
            org.apache.ambari.view.utils.hdfs.DirStatus dirStatus = hdfsApi.filterAndTruncateDirStatus("", 0, null);
            org.junit.Assert.assertEquals(new org.apache.ambari.view.utils.hdfs.DirStatus(null, new org.apache.ambari.view.utils.hdfs.DirListInfo(0, false, 0, "")), dirStatus);
        }
        {
            org.apache.hadoop.fs.FileStatus[] fileStatuses = getFileStatuses(10);
            org.apache.ambari.view.utils.hdfs.DirStatus dirStatus1 = hdfsApi.filterAndTruncateDirStatus("", 0, fileStatuses);
            org.junit.Assert.assertEquals(new org.apache.ambari.view.utils.hdfs.DirStatus(new org.apache.hadoop.fs.FileStatus[0], new org.apache.ambari.view.utils.hdfs.DirListInfo(10, true, 0, "")), dirStatus1);
        }
        {
            int originalSize = 10;
            int maxAllowedSize = 5;
            java.lang.String nameFilter = "";
            org.apache.hadoop.fs.FileStatus[] fileStatuses = getFileStatuses(originalSize);
            org.apache.ambari.view.utils.hdfs.DirStatus dirStatus2 = hdfsApi.filterAndTruncateDirStatus(nameFilter, maxAllowedSize, fileStatuses);
            org.junit.Assert.assertEquals(new org.apache.ambari.view.utils.hdfs.DirStatus(java.util.Arrays.copyOf(fileStatuses, maxAllowedSize), new org.apache.ambari.view.utils.hdfs.DirListInfo(originalSize, true, maxAllowedSize, nameFilter)), dirStatus2);
        }
        {
            int originalSize = 10;
            int maxAllowedSize = 10;
            java.lang.String nameFilter = "";
            org.apache.hadoop.fs.FileStatus[] fileStatuses = getFileStatuses(originalSize);
            org.apache.ambari.view.utils.hdfs.DirStatus dirStatus2 = hdfsApi.filterAndTruncateDirStatus(nameFilter, maxAllowedSize, fileStatuses);
            org.junit.Assert.assertEquals(new org.apache.ambari.view.utils.hdfs.DirStatus(java.util.Arrays.copyOf(fileStatuses, maxAllowedSize), new org.apache.ambari.view.utils.hdfs.DirListInfo(originalSize, false, maxAllowedSize, nameFilter)), dirStatus2);
        }
        {
            int originalSize = 11;
            int maxAllowedSize = 2;
            java.lang.String nameFilter = "1";
            org.apache.hadoop.fs.FileStatus[] fileStatuses = getFileStatuses(originalSize);
            org.apache.ambari.view.utils.hdfs.DirStatus dirStatus = hdfsApi.filterAndTruncateDirStatus(nameFilter, maxAllowedSize, fileStatuses);
            org.junit.Assert.assertEquals(new org.apache.ambari.view.utils.hdfs.DirStatus(new org.apache.hadoop.fs.FileStatus[]{ fileStatuses[1], fileStatuses[10] }, new org.apache.ambari.view.utils.hdfs.DirListInfo(originalSize, false, 2, nameFilter)), dirStatus);
        }
        {
            int originalSize = 20;
            int maxAllowedSize = 3;
            java.lang.String nameFilter = "1";
            org.apache.hadoop.fs.FileStatus[] fileStatuses = getFileStatuses(originalSize);
            org.apache.ambari.view.utils.hdfs.DirStatus dirStatus = hdfsApi.filterAndTruncateDirStatus(nameFilter, maxAllowedSize, fileStatuses);
            org.junit.Assert.assertEquals(new org.apache.ambari.view.utils.hdfs.DirStatus(new org.apache.hadoop.fs.FileStatus[]{ fileStatuses[1], fileStatuses[10], fileStatuses[11] }, new org.apache.ambari.view.utils.hdfs.DirListInfo(originalSize, true, 3, nameFilter)), dirStatus);
        }
        {
            int originalSize = 12;
            int maxAllowedSize = 3;
            java.lang.String nameFilter = "1";
            org.apache.hadoop.fs.FileStatus[] fileStatuses = getFileStatuses(originalSize);
            org.apache.ambari.view.utils.hdfs.DirStatus dirStatus = hdfsApi.filterAndTruncateDirStatus(nameFilter, maxAllowedSize, fileStatuses);
            org.junit.Assert.assertEquals(new org.apache.ambari.view.utils.hdfs.DirStatus(new org.apache.hadoop.fs.FileStatus[]{ fileStatuses[1], fileStatuses[10], fileStatuses[11] }, new org.apache.ambari.view.utils.hdfs.DirListInfo(originalSize, false, 3, nameFilter)), dirStatus);
        }
        {
            int originalSize = 13;
            int maxAllowedSize = 3;
            java.lang.String nameFilter = "1";
            org.apache.hadoop.fs.FileStatus[] fileStatuses = getFileStatuses(originalSize);
            org.apache.ambari.view.utils.hdfs.DirStatus dirStatus = hdfsApi.filterAndTruncateDirStatus(nameFilter, maxAllowedSize, fileStatuses);
            org.junit.Assert.assertEquals(new org.apache.ambari.view.utils.hdfs.DirStatus(new org.apache.hadoop.fs.FileStatus[]{ fileStatuses[1], fileStatuses[10], fileStatuses[11] }, new org.apache.ambari.view.utils.hdfs.DirListInfo(originalSize, true, 3, nameFilter)), dirStatus);
        }
        {
            int originalSize = 0;
            int maxAllowedSize = 3;
            java.lang.String nameFilter = "1";
            org.apache.hadoop.fs.FileStatus[] fileStatuses = getFileStatuses(originalSize);
            org.apache.ambari.view.utils.hdfs.DirStatus dirStatus = hdfsApi.filterAndTruncateDirStatus(nameFilter, maxAllowedSize, fileStatuses);
            org.junit.Assert.assertEquals(new org.apache.ambari.view.utils.hdfs.DirStatus(new org.apache.hadoop.fs.FileStatus[0], new org.apache.ambari.view.utils.hdfs.DirListInfo(originalSize, false, originalSize, nameFilter)), dirStatus);
        }
        {
            int originalSize = 20;
            int maxAllowedSize = 3;
            java.lang.String nameFilter = "";
            org.apache.hadoop.fs.FileStatus[] fileStatuses = getFileStatuses(originalSize);
            org.apache.ambari.view.utils.hdfs.DirStatus dirStatus = hdfsApi.filterAndTruncateDirStatus(nameFilter, maxAllowedSize, fileStatuses);
            org.junit.Assert.assertEquals(new org.apache.ambari.view.utils.hdfs.DirStatus(new org.apache.hadoop.fs.FileStatus[]{ fileStatuses[0], fileStatuses[1], fileStatuses[2] }, new org.apache.ambari.view.utils.hdfs.DirListInfo(originalSize, true, maxAllowedSize, nameFilter)), dirStatus);
        }
        {
            int originalSize = 20;
            int maxAllowedSize = 3;
            java.lang.String nameFilter = null;
            org.apache.hadoop.fs.FileStatus[] fileStatuses = getFileStatuses(originalSize);
            org.apache.ambari.view.utils.hdfs.DirStatus dirStatus = hdfsApi.filterAndTruncateDirStatus(nameFilter, maxAllowedSize, fileStatuses);
            org.junit.Assert.assertEquals(new org.apache.ambari.view.utils.hdfs.DirStatus(new org.apache.hadoop.fs.FileStatus[]{ fileStatuses[0], fileStatuses[1], fileStatuses[2] }, new org.apache.ambari.view.utils.hdfs.DirListInfo(originalSize, true, maxAllowedSize, nameFilter)), dirStatus);
        }
        {
            int originalSize = 3;
            int maxAllowedSize = 3;
            java.lang.String nameFilter = null;
            org.apache.hadoop.fs.FileStatus[] fileStatuses = getFileStatuses(originalSize);
            org.apache.ambari.view.utils.hdfs.DirStatus dirStatus = hdfsApi.filterAndTruncateDirStatus(nameFilter, maxAllowedSize, fileStatuses);
            org.junit.Assert.assertEquals(new org.apache.ambari.view.utils.hdfs.DirStatus(new org.apache.hadoop.fs.FileStatus[]{ fileStatuses[0], fileStatuses[1], fileStatuses[2] }, new org.apache.ambari.view.utils.hdfs.DirListInfo(originalSize, false, maxAllowedSize, nameFilter)), dirStatus);
        }
        {
            int originalSize = 20;
            int maxAllowedSize = 3;
            java.lang.String nameFilter = "a";
            org.apache.hadoop.fs.FileStatus[] fileStatuses = getFileStatuses(originalSize);
            org.apache.ambari.view.utils.hdfs.DirStatus dirStatus = hdfsApi.filterAndTruncateDirStatus(nameFilter, maxAllowedSize, fileStatuses);
            org.junit.Assert.assertEquals(new org.apache.ambari.view.utils.hdfs.DirStatus(new org.apache.hadoop.fs.FileStatus[0], new org.apache.ambari.view.utils.hdfs.DirListInfo(originalSize, false, 0, nameFilter)), dirStatus);
        }
    }

    private org.apache.hadoop.fs.FileStatus[] getFileStatuses(int numberOfFiles) {
        org.apache.hadoop.fs.FileStatus[] fileStatuses = new org.apache.hadoop.fs.FileStatus[numberOfFiles];
        for (int i = 0; i < numberOfFiles; i++) {
            fileStatuses[i] = getFileStatus("/" + i);
        }
        return fileStatuses;
    }

    private org.apache.hadoop.fs.FileStatus getFileStatus(java.lang.String path) {
        return new org.apache.hadoop.fs.FileStatus(10, false, 3, 1000, 10000, new org.apache.hadoop.fs.Path(path));
    }
}