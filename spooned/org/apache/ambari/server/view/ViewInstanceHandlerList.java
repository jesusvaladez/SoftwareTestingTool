package org.apache.ambari.server.view;
public interface ViewInstanceHandlerList {
    void addViewInstance(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition) throws org.apache.ambari.view.SystemException;

    void shareSessionCacheToViews(org.eclipse.jetty.server.session.SessionCache serverSessionCache);

    void removeViewInstance(org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition);
}