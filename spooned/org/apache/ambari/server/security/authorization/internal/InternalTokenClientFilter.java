package org.apache.ambari.server.security.authorization.internal;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
public class InternalTokenClientFilter extends com.sun.jersey.api.client.filter.ClientFilter {
    public static final java.lang.String INTERNAL_TOKEN_HEADER = "X-Internal-Token";

    private final org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorage;

    @com.google.inject.Inject
    public InternalTokenClientFilter(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorage) {
        this.tokenStorage = tokenStorage;
    }

    @java.lang.Override
    public com.sun.jersey.api.client.ClientResponse handle(com.sun.jersey.api.client.ClientRequest cr) throws com.sun.jersey.api.client.ClientHandlerException {
        cr.getHeaders().add(org.apache.ambari.server.security.authorization.internal.InternalTokenClientFilter.INTERNAL_TOKEN_HEADER, tokenStorage.getInternalToken());
        return getNext().handle(cr);
    }
}