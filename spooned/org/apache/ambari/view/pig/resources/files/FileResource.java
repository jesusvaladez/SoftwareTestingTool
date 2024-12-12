package org.apache.ambari.view.pig.resources.files;
public class FileResource {
    private java.lang.String filePath;

    private java.lang.String fileContent;

    private boolean hasNext;

    private long page;

    private long pageCount;

    public java.lang.String getFilePath() {
        return filePath;
    }

    public void setFilePath(java.lang.String filePath) {
        this.filePath = filePath;
    }

    public java.lang.String getFileContent() {
        return fileContent;
    }

    public void setFileContent(java.lang.String fileContent) {
        this.fileContent = fileContent;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }
}