package org.apache.ambari.view.phonelist;
public class PhoneUser {
    private java.lang.String name;

    private java.lang.String phone;

    public PhoneUser() {
    }

    public PhoneUser(java.lang.String name, java.lang.String phone) {
        this.name = name;
        this.phone = phone;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getPhone() {
        return phone;
    }

    public void setPhone(java.lang.String phone) {
        this.phone = phone;
    }
}