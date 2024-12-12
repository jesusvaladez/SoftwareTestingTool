package org.apache.ambari.view.favorite;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class FavoriteService {
    private static final java.lang.String PROPERTY_QUESTION = "what.is.the.question";

    private static final java.lang.String PROPERTY_DONOTKNOW = "i.do.not.know";

    private static final java.lang.String INSTANCE_ITEM = "favorite.item";

    @javax.inject.Inject
    org.apache.ambari.view.ViewContext context;

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/item/{thing}")
    @javax.ws.rs.Consumes({ "text/plain", "application/json" })
    @javax.ws.rs.Produces({ "text/plain", "application/json" })
    public javax.ws.rs.core.Response setFavoriteItem(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("thing")
    java.lang.String thing) {
        context.putInstanceData(org.apache.ambari.view.favorite.FavoriteService.INSTANCE_ITEM, thing);
        java.lang.StringBuffer buf = new java.lang.StringBuffer();
        buf.append("{\"item\" : \"");
        buf.append(thing);
        buf.append("\"}");
        return javax.ws.rs.core.Response.ok(buf.toString()).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/item")
    @javax.ws.rs.Produces({ "text/plain", "application/json" })
    public javax.ws.rs.core.Response getFavoriteItem(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        java.lang.System.out.println(" context = " + context);
        java.lang.String item = context.getInstanceData(org.apache.ambari.view.favorite.FavoriteService.INSTANCE_ITEM);
        if ((item == null) || item.isEmpty())
            item = context.getProperties().get(org.apache.ambari.view.favorite.FavoriteService.PROPERTY_DONOTKNOW);

        java.lang.StringBuffer buf = new java.lang.StringBuffer();
        buf.append("{\"item\" : \"");
        buf.append(item);
        buf.append("\"}");
        return javax.ws.rs.core.Response.ok(buf.toString()).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/question")
    @javax.ws.rs.Produces({ "text/plain", "application/json" })
    public javax.ws.rs.core.Response getQuestion(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        java.lang.String question = context.getProperties().get(org.apache.ambari.view.favorite.FavoriteService.PROPERTY_QUESTION);
        java.lang.String username = context.getUsername();
        java.lang.StringBuffer buf = new java.lang.StringBuffer();
        buf.append("{\"question\" : \"");
        buf.append(question);
        buf.append("\", \"username\" : \"");
        buf.append(username);
        buf.append("\"}");
        return javax.ws.rs.core.Response.ok(buf.toString()).build();
    }
}