package org.apache.ambari.server.api.resources;
public class RequestResourceDefinition extends org.apache.ambari.server.api.resources.BaseResourceDefinition {
    public RequestResourceDefinition() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Request, org.apache.ambari.server.controller.spi.Resource.Type.Stage, org.apache.ambari.server.controller.spi.Resource.Type.Task);
    }

    @java.lang.Override
    public java.lang.String getPluralName() {
        return "requests";
    }

    @java.lang.Override
    public java.lang.String getSingularName() {
        return "request";
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors() {
        return java.util.Arrays.asList(new org.apache.ambari.server.api.resources.RequestResourceDefinition.RequestHrefPostProcessor(), new org.apache.ambari.server.api.resources.RequestResourceDefinition.RequestSourceScheduleHrefPostProcessor());
    }

    private class RequestHrefPostProcessor implements org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            java.lang.Object requestId = resultNode.getObject().getPropertyValue(getClusterController().getSchema(org.apache.ambari.server.controller.spi.Resource.Type.Request).getKeyPropertyId(org.apache.ambari.server.controller.spi.Resource.Type.Request));
            if (href.endsWith("/")) {
                href = href.substring(0, href.length() - 1);
            }
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            if (href.endsWith("/requests")) {
                sb.append(href);
                sb.append('/').append(requestId);
            } else {
                java.lang.String[] tokens = href.split("/");
                java.lang.Integer tokenCount = tokens.length;
                for (int i = 0; i < tokenCount; ++i) {
                    java.lang.String fragment = tokens[i];
                    sb.append(fragment);
                    if (i < (tokenCount - 1)) {
                        sb.append('/');
                    }
                    if ("clusters".equals(fragment) && ((i + 1) < tokenCount)) {
                        java.lang.String clusterName = getClusterName(tokens[i + 1]);
                        sb.append(clusterName).append("/");
                        sb.append("requests/").append(requestId);
                        break;
                    }
                }
            }
            resultNode.setProperty("href", sb.toString());
        }

        private java.lang.String getClusterName(java.lang.String token) {
            int pos = token.indexOf('?');
            if (pos > 0) {
                return token.substring(0, pos);
            } else {
                return token;
            }
        }
    }

    private class RequestSourceScheduleHrefPostProcessor implements org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor {
        @java.lang.Override
        public void process(org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultNode, java.lang.String href) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            java.lang.String[] toks = href.split("/");
            for (int i = 0; i < toks.length; ++i) {
                java.lang.String s = toks[i];
                sb.append(s).append('/');
                if ("clusters".equals(s)) {
                    sb.append(toks[i + 1]).append('/');
                    break;
                }
            }
            java.lang.Object scheduleId = resultNode.getObject().getPropertyValue(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_SOURCE_SCHEDULE_ID);
            if (scheduleId != null) {
                sb.append("request_schedules/").append(scheduleId);
                resultNode.getObject().setProperty(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_SOURCE_SCHEDULE_HREF, sb.toString());
            }
        }
    }
}