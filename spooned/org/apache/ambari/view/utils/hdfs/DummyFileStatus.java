package org.apache.ambari.view.utils.hdfs;
import org.apache.hadoop.fs.FileEncryptionInfo;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.protocol.ErasureCodingPolicy;
import org.apache.hadoop.hdfs.protocol.HdfsFileStatus;
public abstract class DummyFileStatus extends org.apache.hadoop.fs.FileStatus implements org.apache.hadoop.hdfs.protocol.HdfsFileStatus {
    @java.lang.Override
    public long getFileId() {
        return 0;
    }

    @java.lang.Override
    public org.apache.hadoop.fs.FileEncryptionInfo getFileEncryptionInfo() {
        return null;
    }

    @java.lang.Override
    public byte[] getLocalNameInBytes() {
        return new byte[0];
    }

    @java.lang.Override
    public byte[] getSymlinkInBytes() {
        return new byte[0];
    }

    @java.lang.Override
    public int getChildrenNum() {
        return 0;
    }

    @java.lang.Override
    public org.apache.hadoop.hdfs.protocol.ErasureCodingPolicy getErasureCodingPolicy() {
        return null;
    }

    @java.lang.Override
    public byte getStoragePolicy() {
        return 0;
    }

    @java.lang.Override
    public void setPermission(org.apache.hadoop.fs.permission.FsPermission fsPermission) {
    }

    @java.lang.Override
    public void setOwner(java.lang.String s) {
    }

    @java.lang.Override
    public void setGroup(java.lang.String s) {
    }
}