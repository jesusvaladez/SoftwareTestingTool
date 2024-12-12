package org.apache.ambari.server.actionmanager;
import javax.xml.bind.JAXBException;
public class ServiceComponentHostEventWrapper {
    private org.apache.ambari.server.state.ServiceComponentHostEvent event = null;

    private java.lang.String eventJson = null;

    public ServiceComponentHostEventWrapper(org.apache.ambari.server.state.ServiceComponentHostEvent event) {
        this.event = event;
    }

    public ServiceComponentHostEventWrapper(java.lang.String eventJson) {
        this.eventJson = eventJson;
    }

    public org.apache.ambari.server.state.ServiceComponentHostEvent getEvent() {
        if (event != null) {
            return event;
        } else if (eventJson != null) {
            try {
                event = org.apache.ambari.server.utils.StageUtils.fromJson(eventJson, org.apache.ambari.server.state.ServiceComponentHostEvent.class);
                return event;
            } catch (java.io.IOException e) {
                throw new java.lang.RuntimeException("Illegal Json for event", e);
            }
        }
        return null;
    }

    public java.lang.String getEventJson() {
        if (eventJson != null) {
            return eventJson;
        } else if (event != null) {
            try {
                eventJson = org.apache.ambari.server.utils.StageUtils.jaxbToString(event);
                return eventJson;
            } catch (javax.xml.bind.JAXBException | java.io.IOException e) {
                throw new java.lang.RuntimeException("Couldn't get json", e);
            }
        } else {
            return null;
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        if (event != null) {
            return event.toString();
        } else if (eventJson != null) {
            return eventJson;
        }
        return "null";
    }
}