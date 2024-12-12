package org.apache.ambari.view.utils.hdfs;
public class DirListInfo {
    private int originalSize;

    private boolean truncated;

    private int finalSize;

    private java.lang.String nameFilter;

    public DirListInfo(int originalSize, boolean truncated, int finalSize, java.lang.String nameFilter) {
        this.originalSize = originalSize;
        this.truncated = truncated;
        this.finalSize = finalSize;
        this.nameFilter = nameFilter;
    }

    public int getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(int originalSize) {
        this.originalSize = originalSize;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public int getFinalSize() {
        return finalSize;
    }

    public void setFinalSize(int finalSize) {
        this.finalSize = finalSize;
    }

    public java.lang.String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(java.lang.String nameFilter) {
        this.nameFilter = nameFilter;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((("DirListInfo{" + "originalSize=") + originalSize) + ", truncated=") + truncated) + ", finalSize=") + finalSize) + ", nameFilter='") + nameFilter) + '\'') + '}';
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.view.utils.hdfs.DirListInfo that = ((org.apache.ambari.view.utils.hdfs.DirListInfo) (o));
        if (originalSize != that.originalSize)
            return false;

        if (truncated != that.truncated)
            return false;

        if (finalSize != that.finalSize)
            return false;

        return nameFilter != null ? nameFilter.equals(that.nameFilter) : that.nameFilter == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = originalSize;
        result = (31 * result) + (truncated ? 1 : 0);
        result = (31 * result) + finalSize;
        result = (31 * result) + (nameFilter != null ? nameFilter.hashCode() : 0);
        return result;
    }
}