package org.apache.ambari.view.pig.utils;
import org.apache.hadoop.fs.FSDataInputStream;
public class FilePaginator {
    private static int PAGE_SIZE = (1 * 1024) * 1024;

    private java.lang.String filePath;

    private org.apache.ambari.view.ViewContext context;

    public FilePaginator(java.lang.String filePath, org.apache.ambari.view.ViewContext context) {
        this.filePath = filePath;
        this.context = context;
    }

    public static void setPageSize(int PAGE_SIZE) {
        org.apache.ambari.view.pig.utils.FilePaginator.PAGE_SIZE = PAGE_SIZE;
    }

    public long pageCount() throws java.io.IOException, java.lang.InterruptedException {
        return ((long) (java.lang.Math.ceil(org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context).getFileStatus(filePath).getLen() / ((double) (org.apache.ambari.view.pig.utils.FilePaginator.PAGE_SIZE)))));
    }

    public java.lang.String readPage(long page) throws java.io.IOException, java.lang.InterruptedException {
        org.apache.hadoop.fs.FSDataInputStream stream = org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context).open(filePath);
        try {
            stream.seek(page * org.apache.ambari.view.pig.utils.FilePaginator.PAGE_SIZE);
        } catch (java.io.IOException e) {
            throw new java.lang.IllegalArgumentException(("Page " + page) + " does not exists");
        }
        byte[] buffer = new byte[org.apache.ambari.view.pig.utils.FilePaginator.PAGE_SIZE];
        int readCount = 0;
        int read = 0;
        while (read < org.apache.ambari.view.pig.utils.FilePaginator.PAGE_SIZE) {
            try {
                readCount = stream.read(buffer, read, org.apache.ambari.view.pig.utils.FilePaginator.PAGE_SIZE - read);
            } catch (java.io.IOException e) {
                stream.close();
                throw e;
            }
            if (readCount == (-1))
                break;

            read += readCount;
        } 
        if (read != 0) {
            byte[] readData = java.util.Arrays.copyOfRange(buffer, 0, read);
            return new java.lang.String(readData, java.nio.charset.Charset.forName("UTF-8"));
        } else {
            if (page == 0) {
                return "";
            }
            throw new java.lang.IllegalArgumentException(("Page " + page) + " does not exists");
        }
    }
}