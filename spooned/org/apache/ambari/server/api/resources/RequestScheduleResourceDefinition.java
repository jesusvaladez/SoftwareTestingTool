package org.apache.ambari.server.api.resources;
public class RequestScheduleResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public RequestScheduleResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.RequestSchedule);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "request_schedules";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "request_schedule";
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors() {
        return java.util.Collections.singletonList(new org.apache.ambari.server.api.resources.RequestScheduleResourceDefinition.RequestScheduleHrefPostProcessor());
    }

    private class RequestScheduleHrefPostProcessor implements org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            java.lang.String[] tokens = href.split("/");
            for (int i = 0; i < tokens.length; ++i) {
                java.lang.String s = tokens[i];
                sb.append(s).append('/');
                if ("clusters".equals(s)) {
                    sb.append(tokens[i + 1]).append('/');
                    break;
                }
            }
            java.lang.Object scheduleId = resultNode.getObject().getPropertyValue(getClusterController().getSchema(org.apache.ambari.server.controller.spi.Resource.Type.RequestSchedule).getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.RequestSchedule));
            sb.append("request_schedules/").append(scheduleId);
            resultNode.setProperty("href", sb.toString());
        }
    }
}