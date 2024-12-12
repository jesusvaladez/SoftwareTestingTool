package org.apache.ambari.view.pig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.security.UserGroupInformation;
public abstract class HDFSTest extends org.apache.ambari.view.pig.BasePigTest {
    protected static org.apache.hadoop.hdfs.MiniDFSCluster hdfsCluster;

    protected static java.lang.String hdfsURI;

    @org.junit.BeforeClass
    public static void startUp() throws java.lang.Exception {
        org.apache.ambari.view.pig.BasePigTest.startUp();
        java.io.File hdfsDir = new java.io.File("./target/PigTest/hdfs/").getAbsoluteFile();
        org.apache.hadoop.fs.FileUtil.fullyDelete(hdfsDir);
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set(MiniDFSCluster.HDFS_MINIDFS_BASEDIR, hdfsDir.getAbsolutePath());
        conf.set(("hadoop.proxyuser." + java.lang.System.getProperty("user.name")) + ".groups", "*");
        conf.set(("hadoop.proxyuser." + java.lang.System.getProperty("user.name")) + ".hosts", "*");
        org.apache.hadoop.hdfs.MiniDFSCluster.Builder builder = new org.apache.hadoop.hdfs.MiniDFSCluster.Builder(conf);
        org.apache.ambari.view.pig.HDFSTest.hdfsCluster = builder.build();
        org.apache.ambari.view.pig.HDFSTest.hdfsURI = org.apache.ambari.view.pig.HDFSTest.hdfsCluster.getURI().toString();
        org.apache.ambari.view.pig.HDFSTest.hdfsCluster.getFileSystem().mkdir(new org.apache.hadoop.fs.Path("/tmp"), org.apache.hadoop.fs.permission.FsPermission.getDefault());
        org.apache.ambari.view.pig.HDFSTest.hdfsCluster.getFileSystem().setPermission(new org.apache.hadoop.fs.Path("/tmp"), new org.apache.hadoop.fs.permission.FsPermission(org.apache.hadoop.fs.permission.FsAction.ALL, org.apache.hadoop.fs.permission.FsAction.ALL, org.apache.hadoop.fs.permission.FsAction.ALL));
    }

    @org.junit.AfterClass
    public static void shutDown() throws java.lang.Exception {
        org.apache.ambari.view.pig.BasePigTest.shutDown();
        org.apache.ambari.view.pig.HDFSTest.hdfsCluster.shutdown();
        org.apache.ambari.view.pig.HDFSTest.hdfsCluster = null;
    }

    @java.lang.Override
    protected void setupProperties(java.util.Map<java.lang.String, java.lang.String> properties, java.io.File baseDir) throws java.lang.Exception {
        super.setupProperties(properties, baseDir);
        properties.put("webhdfs.url", org.apache.ambari.view.pig.HDFSTest.hdfsURI);
        properties.put("webhdfs.username", java.lang.System.getProperty("user.name"));
    }
}