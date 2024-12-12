package org.apache.ambari.server.serveraction;
import org.apache.commons.lang.time.FastDateFormat;
public class ActionLog {
    private java.lang.StringBuffer stdErr = new java.lang.StringBuffer();

    private java.lang.StringBuffer stdOut = new java.lang.StringBuffer();

    private org.apache.commons.lang.time.FastDateFormat dateFormat = org.apache.commons.lang.time.FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss,SSS");

    public void writeStdErr(java.lang.String message) {
        write(stdErr, message);
    }

    public void writeStdOut(java.lang.String message) {
        write(stdOut, message);
    }

    public java.lang.String getStdErr() {
        return stdErr.toString();
    }

    public java.lang.String getStdOut() {
        return stdOut.toString();
    }

    private void write(java.lang.StringBuffer buffer, java.lang.String message) {
        if (message != null) {
            java.util.Date date = new java.util.Date();
            buffer.append(dateFormat.format(date));
            buffer.append(" - ");
            buffer.append(message);
            buffer.append("\n");
        }
    }
}