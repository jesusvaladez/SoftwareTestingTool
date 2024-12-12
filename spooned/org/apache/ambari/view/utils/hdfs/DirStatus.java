package org.apache.ambari.view.utils.hdfs;
import org.apache.hadoop.fs.FileStatus;
public class DirStatus {
    private org.apache.ambari.view.utils.hdfs.DirListInfo dirListInfo;

    private org.apache.hadoop.fs.FileStatus[] fileStatuses;

    public DirStatus(org.apache.hadoop.fs.FileStatus[] fileStatuses, org.apache.ambari.view.utils.hdfs.DirListInfo dirListInfo) {
        this.fileStatuses = fileStatuses;
        this.dirListInfo = dirListInfo;
    }

    public org.apache.ambari.view.utils.hdfs.DirListInfo getDirListInfo() {
        return dirListInfo;
    }

    public void setDirListInfo(org.apache.ambari.view.utils.hdfs.DirListInfo dirListInfo) {
        this.dirListInfo = dirListInfo;
    }

    public org.apache.hadoop.fs.FileStatus[] getFileStatuses() {
        return fileStatuses;
    }

    public void setFileStatuses(org.apache.hadoop.fs.FileStatus[] fileStatuses) {
        this.fileStatuses = fileStatuses;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((("DirStatus{" + "dirListInfo=") + dirListInfo) + ", fileStatuses=") + java.util.Arrays.toString(fileStatuses)) + '}';
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.view.utils.hdfs.DirStatus dirStatus = ((org.apache.ambari.view.utils.hdfs.DirStatus) (o));
        if (dirListInfo != null ? !dirListInfo.equals(dirStatus.dirListInfo) : dirStatus.dirListInfo != null)
            return false;

        return java.util.Arrays.equals(fileStatuses, dirStatus.fileStatuses);
    }

    @java.lang.Override
    public int hashCode() {
        int result = (dirListInfo != null) ? dirListInfo.hashCode() : 0;
        result = (31 * result) + java.util.Arrays.hashCode(fileStatuses);
        return result;
    }
}