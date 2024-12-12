package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBException;
@javax.ws.rs.Path("/keys/")
public class KeyService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.services.KeyService.class);

    private static org.apache.ambari.server.api.services.PersistKeyValueImpl persistKeyVal;

    @com.google.inject.Inject
    public static void init(org.apache.ambari.server.api.services.PersistKeyValueImpl instance) {
        org.apache.ambari.server.api.services.KeyService.persistKeyVal = instance;
    }

    @javax.ws.rs.Path("{number}")
    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public java.lang.String getKeys(@javax.ws.rs.PathParam("number")
    int number) throws java.io.IOException, javax.xml.bind.JAXBException {
        java.util.Collection<java.lang.String> keys = org.apache.ambari.server.api.services.KeyService.persistKeyVal.generateKeys(number);
        java.lang.String result = org.apache.ambari.server.utils.StageUtils.jaxbToString(keys);
        org.apache.ambari.server.api.services.KeyService.log.info("Returning keys {}", result);
        return result;
    }
}