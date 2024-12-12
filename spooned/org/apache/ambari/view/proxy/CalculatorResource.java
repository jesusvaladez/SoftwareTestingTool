package org.apache.ambari.view.proxy;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class CalculatorResource {
    @javax.inject.Inject
    org.apache.ambari.view.ViewContext context;

    @javax.ws.rs.GET
    @javax.ws.rs.Produces({ "text/html" })
    public javax.ws.rs.core.Response getUsage(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) throws java.io.IOException {
        java.lang.String entity = "<h2>Usage of calculator</h2><br>" + ((((("<ul>" + "<li><b>calculator/add/{a}/{b}</b> - add {a} to {b}</li>") + "<li><b>calculator/subtract/{a}/{b}</b> - subtract {a} from {b}</li>") + "<li><b>calculator/multiply/{a}/{b}</b> - multiply {a} with {b}</li>") + "<li><b>calculator/divide/{a}/{b}</b> - divide {a} by {b}</li>") + "</ul>");
        return javax.ws.rs.core.Response.ok(entity).type("text/html").build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/add/{a}/{b}")
    @javax.ws.rs.Produces({ "text/html" })
    public javax.ws.rs.core.Response add(@javax.ws.rs.PathParam("a")
    double a, @javax.ws.rs.PathParam("b")
    double b) {
        java.lang.String result = (((a + " + ") + b) + " = ") + (a + b);
        return javax.ws.rs.core.Response.ok(("<b>" + result) + "</b>").type("text/html").build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/subtract/{a}/{b}")
    @javax.ws.rs.Produces({ "text/html" })
    public javax.ws.rs.core.Response subtract(@javax.ws.rs.PathParam("a")
    double a, @javax.ws.rs.PathParam("b")
    double b) {
        java.lang.String result = (((a + " - ") + b) + " = ") + (a - b);
        return javax.ws.rs.core.Response.ok(("<b>" + result) + "</b>").type("text/html").build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/multiply/{a}/{b}")
    @javax.ws.rs.Produces({ "text/html" })
    public javax.ws.rs.core.Response multiply(@javax.ws.rs.PathParam("a")
    double a, @javax.ws.rs.PathParam("b")
    double b) {
        java.lang.String result = (((a + " * ") + b) + " = ") + (a * b);
        return javax.ws.rs.core.Response.ok(("<b>" + result) + "</b>").type("text/html").build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/divide/{a}/{b}")
    @javax.ws.rs.Produces({ "text/html" })
    public javax.ws.rs.core.Response divide(@javax.ws.rs.PathParam("a")
    double a, @javax.ws.rs.PathParam("b")
    double b) {
        java.lang.String result = (((a + " / ") + b) + " = ") + (a / b);
        return javax.ws.rs.core.Response.ok(("<b>" + result) + "</b>").type("text/html").build();
    }
}