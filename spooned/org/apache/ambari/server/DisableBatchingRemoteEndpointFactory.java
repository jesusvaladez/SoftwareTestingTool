package org.apache.ambari.server;
public class DisableBatchingRemoteEndpointFactory implements org.eclipse.jetty.websocket.common.RemoteEndpointFactory {
    @java.lang.Override
    public org.eclipse.jetty.websocket.api.RemoteEndpoint newRemoteEndpoint(org.eclipse.jetty.websocket.common.LogicalConnection connection, org.eclipse.jetty.websocket.api.extensions.OutgoingFrames outgoingFrames, org.eclipse.jetty.websocket.api.BatchMode batchMode) {
        return new org.eclipse.jetty.websocket.common.WebSocketRemoteEndpoint(connection, outgoingFrames, org.eclipse.jetty.websocket.api.BatchMode.OFF);
    }
}