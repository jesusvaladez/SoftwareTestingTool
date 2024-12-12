package org.apache.ambari.checkstyle;
import io.swagger.annotations.ApiOperation;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
public class InputRestApiOperation {
    @javax.ws.rs.DELETE
    public void undocumentedDELETE() {
    }

    @javax.ws.rs.DELETE
    @io.swagger.annotations.ApiOperation("...")
    public void documentedDELETE() {
    }

    @javax.ws.rs.DELETE
    @org.apache.ambari.annotations.ApiIgnore
    public void ignoredDELETE() {
    }

    @javax.ws.rs.HEAD
    public void undocumentedHEAD() {
    }

    @javax.ws.rs.HEAD
    @io.swagger.annotations.ApiOperation("...")
    public void documentedHEAD() {
    }

    @javax.ws.rs.HEAD
    @org.apache.ambari.annotations.ApiIgnore
    public void ignoredHEAD() {
    }

    @javax.ws.rs.GET
    public void undocumentedGET() {
    }

    @javax.ws.rs.GET
    @io.swagger.annotations.ApiOperation("...")
    public void documentedGET() {
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    public void ignoredGET() {
    }

    @javax.ws.rs.OPTIONS
    public void undocumentedOPTIONS() {
    }

    @javax.ws.rs.OPTIONS
    @io.swagger.annotations.ApiOperation("...")
    public void documentedOPTIONS() {
    }

    @javax.ws.rs.OPTIONS
    @org.apache.ambari.annotations.ApiIgnore
    public void ignoredOPTIONS() {
    }

    @javax.ws.rs.POST
    public void undocumentedPOST() {
    }

    @javax.ws.rs.POST
    @io.swagger.annotations.ApiOperation("...")
    public void documentedPOST() {
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    public void ignoredPOST() {
    }

    @javax.ws.rs.PUT
    public void undocumentedPUT() {
    }

    @javax.ws.rs.PUT
    @io.swagger.annotations.ApiOperation("...")
    public void documentedPUT() {
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    public void ignoredPUT() {
    }
}