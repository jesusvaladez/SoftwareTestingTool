package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
@javax.ws.rs.Path("/persist/")
public class PersistKeyValueService {
    private static org.apache.ambari.server.api.services.PersistKeyValueImpl persistKeyVal;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.services.PersistKeyValueService.class);

    @com.google.inject.Inject
    public static void init(org.apache.ambari.server.api.services.PersistKeyValueImpl instance) {
        org.apache.ambari.server.api.services.PersistKeyValueService.persistKeyVal = instance;
    }

    @java.lang.SuppressWarnings("unchecked")
    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response update(java.lang.String keyValues) throws javax.ws.rs.WebApplicationException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException, javax.xml.bind.JAXBException, java.io.IOException {
        org.apache.ambari.server.api.services.PersistKeyValueService.LOG.debug("Received message from UI {}", keyValues);
        java.util.Map<java.lang.String, java.lang.String> keyValuesMap = org.apache.ambari.server.utils.StageUtils.fromJson(keyValues, java.util.Map.class);
        for (java.util.Map.Entry<java.lang.String, java.lang.String> keyValue : keyValuesMap.entrySet()) {
            org.apache.ambari.server.api.services.PersistKeyValueService.persistKeyVal.put(keyValue.getKey(), keyValue.getValue());
        }
        return javax.ws.rs.core.Response.status(Response.Status.ACCEPTED).build();
    }

    @java.lang.SuppressWarnings("unchecked")
    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public java.lang.String store(java.lang.String values) throws java.io.IOException, javax.xml.bind.JAXBException {
        org.apache.ambari.server.api.services.PersistKeyValueService.LOG.debug("Received message from UI {}", values);
        java.util.Collection<java.lang.String> valueCollection = org.apache.ambari.server.utils.StageUtils.fromJson(values, java.util.Collection.class);
        java.util.Collection<java.lang.String> keys = new java.util.ArrayList<>(valueCollection.size());
        for (java.lang.String s : valueCollection) {
            keys.add(org.apache.ambari.server.api.services.PersistKeyValueService.persistKeyVal.put(s));
        }
        java.lang.String stringRet = org.apache.ambari.server.utils.StageUtils.jaxbToString(keys);
        org.apache.ambari.server.api.services.PersistKeyValueService.LOG.debug("Returning {}", stringRet);
        return stringRet;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    @javax.ws.rs.Path("{keyName}")
    public java.lang.String getKey(@javax.ws.rs.PathParam("keyName")
    java.lang.String keyName) {
        org.apache.ambari.server.api.services.PersistKeyValueService.LOG.debug("Looking for keyName {}", keyName);
        return org.apache.ambari.server.api.services.PersistKeyValueService.persistKeyVal.getValue(keyName);
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public java.lang.String getAllKeyValues() throws javax.xml.bind.JAXBException, java.io.IOException {
        java.util.Map<java.lang.String, java.lang.String> ret = org.apache.ambari.server.api.services.PersistKeyValueService.persistKeyVal.getAllKeyValues();
        java.lang.String stringRet = org.apache.ambari.server.utils.StageUtils.jaxbToString(ret);
        org.apache.ambari.server.api.services.PersistKeyValueService.LOG.debug("Returning {}", stringRet);
        return stringRet;
    }
}