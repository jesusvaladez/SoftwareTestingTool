package org.apache.ambari.server.audit.request.creator;
import org.easymock.Capture;
import org.easymock.EasyMock;
public class AuditEventCreatorTestHelper {
    public static org.apache.ambari.server.audit.event.AuditEvent getEvent(org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator eventCreator, org.apache.ambari.server.api.services.Request request, org.apache.ambari.server.api.services.Result result) {
        java.util.Set<org.apache.ambari.server.audit.request.eventcreator.RequestAuditEventCreator> creatorSet = new java.util.HashSet<>();
        creatorSet.add(eventCreator);
        org.apache.ambari.server.audit.AuditLogger auditLogger = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.audit.AuditLogger.class);
        org.easymock.EasyMock.expect(auditLogger.isEnabled()).andReturn(true).anyTimes();
        org.easymock.Capture<org.apache.ambari.server.audit.event.AuditEvent> capture = org.easymock.EasyMock.newCapture();
        auditLogger.log(org.easymock.EasyMock.capture(capture));
        org.easymock.EasyMock.expectLastCall();
        org.easymock.EasyMock.replay(auditLogger);
        org.apache.ambari.server.audit.request.RequestAuditLogger requestAuditLogger = new org.apache.ambari.server.audit.request.RequestAuditLoggerImpl(auditLogger, creatorSet);
        requestAuditLogger.log(request, result);
        return capture.getValue();
    }

    public static org.apache.ambari.server.api.services.Request createRequest(final org.apache.ambari.server.api.services.Request.Type requestType, final org.apache.ambari.server.controller.spi.Resource.Type resourceType, final java.util.Map<java.lang.String, java.lang.Object> properties, final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> resource) {
        return org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(requestType, resourceType, properties, resource, "");
    }

    public static org.apache.ambari.server.api.services.Request createRequest(final org.apache.ambari.server.api.services.Request.Type requestType, final org.apache.ambari.server.controller.spi.Resource.Type resourceType, final java.util.Map<java.lang.String, java.lang.Object> properties, final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> resource, final java.lang.String queryString) {
        return new org.apache.ambari.server.api.services.Request() {
            org.apache.ambari.server.api.services.RequestBody body = new org.apache.ambari.server.api.services.RequestBody();

            @java.lang.Override
            public org.apache.ambari.server.api.services.Result process() {
                return null;
            }

            @java.lang.Override
            public org.apache.ambari.server.api.resources.ResourceInstance getResource() {
                return new org.apache.ambari.server.api.resources.ResourceInstance() {
                    @java.lang.Override
                    public void setKeyValueMap(java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyValueMap) {
                    }

                    @java.lang.Override
                    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyValueMap() {
                        return resource;
                    }

                    @java.lang.Override
                    public org.apache.ambari.server.api.query.Query getQuery() {
                        return null;
                    }

                    @java.lang.Override
                    public org.apache.ambari.server.api.resources.ResourceDefinition getResourceDefinition() {
                        return new org.apache.ambari.server.api.resources.ResourceDefinition() {
                            @java.lang.Override
                            public java.lang.String getPluralName() {
                                return null;
                            }

                            @java.lang.Override
                            public java.lang.String getSingularName() {
                                return null;
                            }

                            @java.lang.Override
                            public org.apache.ambari.server.controller.spi.Resource.Type getType() {
                                return resourceType;
                            }

                            @java.lang.Override
                            public java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> getSubResourceDefinitions() {
                                return null;
                            }

                            @java.lang.Override
                            public java.util.List<org.apache.ambari.server.api.resources.ResourceDefinition.PostProcessor> getPostProcessors() {
                                return null;
                            }

                            @java.lang.Override
                            public org.apache.ambari.server.api.query.render.Renderer getRenderer(java.lang.String name) throws java.lang.IllegalArgumentException {
                                return null;
                            }

                            @java.lang.Override
                            public java.util.Collection<java.lang.String> getCreateDirectives() {
                                return null;
                            }

                            @java.lang.Override
                            public java.util.Collection<java.lang.String> getReadDirectives() {
                                return null;
                            }

                            @java.lang.Override
                            public boolean isCreatable() {
                                return false;
                            }

                            @java.lang.Override
                            public java.util.Collection<java.lang.String> getUpdateDirectives() {
                                return null;
                            }

                            @java.lang.Override
                            public java.util.Collection<java.lang.String> getDeleteDirectives() {
                                return null;
                            }
                        };
                    }

                    @java.lang.Override
                    public java.util.Map<java.lang.String, org.apache.ambari.server.api.resources.ResourceInstance> getSubResources() {
                        return null;
                    }

                    @java.lang.Override
                    public boolean isCollectionResource() {
                        return false;
                    }
                };
            }

            @java.lang.Override
            public java.lang.String getURI() {
                return "http://example.com:8080/api/v1/test" + queryString;
            }

            @java.lang.Override
            public org.apache.ambari.server.api.services.Request.Type getRequestType() {
                return requestType;
            }

            @java.lang.Override
            public int getAPIVersion() {
                return 0;
            }

            @java.lang.Override
            public org.apache.ambari.server.controller.spi.Predicate getQueryPredicate() {
                return null;
            }

            @java.lang.Override
            public java.util.Map<java.lang.String, org.apache.ambari.server.controller.spi.TemporalInfo> getFields() {
                return null;
            }

            @java.lang.Override
            public org.apache.ambari.server.api.services.RequestBody getBody() {
                if (properties != null) {
                    org.apache.ambari.server.api.services.NamedPropertySet nps = new org.apache.ambari.server.api.services.NamedPropertySet("", properties);
                    body.addPropertySet(nps);
                }
                return body;
            }

            @java.lang.Override
            public java.util.Map<java.lang.String, java.util.List<java.lang.String>> getHttpHeaders() {
                return null;
            }

            @java.lang.Override
            public org.apache.ambari.server.controller.spi.PageRequest getPageRequest() {
                return null;
            }

            @java.lang.Override
            public org.apache.ambari.server.controller.spi.SortRequest getSortRequest() {
                return null;
            }

            @java.lang.Override
            public org.apache.ambari.server.api.query.render.Renderer getRenderer() {
                return null;
            }

            @java.lang.Override
            public java.lang.String getRemoteAddress() {
                return "1.2.3.4";
            }
        };
    }

    public static org.apache.ambari.server.api.services.Result createResult(final org.apache.ambari.server.api.services.ResultStatus status) {
        return org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(status, null);
    }

    public static org.apache.ambari.server.api.services.Result createResult(final org.apache.ambari.server.api.services.ResultStatus status, final org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree) {
        return new org.apache.ambari.server.api.services.Result() {
            @java.lang.Override
            public org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> getResultTree() {
                return resultTree;
            }

            @java.lang.Override
            public boolean isSynchronous() {
                return false;
            }

            @java.lang.Override
            public org.apache.ambari.server.api.services.ResultStatus getStatus() {
                return status;
            }

            @java.lang.Override
            public void setResultStatus(org.apache.ambari.server.api.services.ResultStatus status) {
            }

            @java.lang.Override
            public void setResultMetadata(org.apache.ambari.server.api.services.ResultMetadata resultMetadata) {
            }

            @java.lang.Override
            public org.apache.ambari.server.api.services.ResultMetadata getResultMetadata() {
                return null;
            }
        };
    }
}