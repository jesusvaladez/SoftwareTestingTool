package org.apache.oozie.ambari.view.model;
public class APIResult {
    private org.apache.oozie.ambari.view.model.APIResult.Status status;

    private java.lang.Object data;

    private org.apache.oozie.ambari.view.model.Paging paging = new org.apache.oozie.ambari.view.model.Paging();

    public org.apache.oozie.ambari.view.model.APIResult.Status getStatus() {
        return status;
    }

    public void setStatus(org.apache.oozie.ambari.view.model.APIResult.Status status) {
        this.status = status;
    }

    public java.lang.Object getData() {
        return data;
    }

    public void setData(java.lang.Object data) {
        this.data = data;
    }

    public org.apache.oozie.ambari.view.model.Paging getPaging() {
        return paging;
    }

    public void setPaging(org.apache.oozie.ambari.view.model.Paging paging) {
        this.paging = paging;
    }

    public static enum Status {

        SUCCESS,
        ERROR;}

    public static void main(java.lang.String[] args) {
        java.lang.System.out.println("hello");
    }
}