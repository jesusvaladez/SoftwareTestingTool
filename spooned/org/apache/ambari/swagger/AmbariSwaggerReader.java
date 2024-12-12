package org.apache.ambari.swagger;
public class AmbariSwaggerReader extends com.github.kongchen.swagger.docgen.reader.JaxrsReader {
    protected static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.swagger.AmbariSwaggerReader.class);

    public AmbariSwaggerReader(io.swagger.models.Swagger swagger, org.apache.maven.plugin.logging.Log LOG) {
        super(swagger, LOG);
    }

    private final java.util.Map<java.lang.Class<?>, org.apache.ambari.swagger.NestedApiRecord> nestedAPIs = com.google.common.collect.Maps.newHashMap();

    @java.lang.Override
    public io.swagger.models.Swagger getSwagger() {
        if (null == this.swagger) {
            this.swagger = new io.swagger.models.Swagger();
        }
        return this.swagger;
    }

    @java.lang.Override
    public io.swagger.models.Swagger read(java.util.Set<java.lang.Class<?>> classes) {
        org.apache.ambari.swagger.AmbariSwaggerReader.logger.debug("Looking for nested API's");
        for (java.lang.Class<?> cls : classes) {
            org.apache.ambari.swagger.AmbariSwaggerReader.logger.debug("Examining API {}", cls.getSimpleName());
            for (java.lang.reflect.Method method : cls.getMethods()) {
                javax.ws.rs.Path methodPath = org.springframework.core.annotation.AnnotationUtils.findAnnotation(method, javax.ws.rs.Path.class);
                if (null != methodPath) {
                    java.lang.Class<?> returnType = method.getReturnType();
                    io.swagger.annotations.Api nestedApi = org.springframework.core.annotation.AnnotationUtils.findAnnotation(returnType, io.swagger.annotations.Api.class);
                    javax.ws.rs.Path nestedApiPath = org.springframework.core.annotation.AnnotationUtils.findAnnotation(returnType, javax.ws.rs.Path.class);
                    org.apache.ambari.swagger.AmbariSwaggerReader.logger.debug("Examining API method {}#{}, path={}, returnType={}", cls.getSimpleName(), method.getName(), nestedApiPath != null ? nestedApiPath.value() : null, returnType.getSimpleName());
                    if (null != nestedApi) {
                        if (null != nestedApiPath) {
                            org.apache.ambari.swagger.AmbariSwaggerReader.logger.info("This class exists both as top level and nested API: {}, treating it as top level API", returnType.getName());
                        } else {
                            boolean skipAdd = false;
                            java.lang.Class<?> preferredParentClass = cls;
                            java.lang.String parentApiPath;
                            java.lang.String methodPathAsString = methodPath.value();
                            if (nestedAPIs.containsKey(returnType)) {
                                org.apache.ambari.annotations.SwaggerPreferredParent preferredParentAnnotation = org.springframework.core.annotation.AnnotationUtils.findAnnotation(returnType, org.apache.ambari.annotations.SwaggerPreferredParent.class);
                                if (null != preferredParentAnnotation) {
                                    preferredParentClass = preferredParentAnnotation.preferredParent();
                                    if (nestedAPIs.get(returnType).parentApi.getName().equals(preferredParentClass.getName())) {
                                        skipAdd = true;
                                    } else {
                                        org.apache.ambari.swagger.AmbariSwaggerReader.logger.info("Setting top level API of {} to {} based on @SwaggerPreferredParent " + "annotation", returnType, preferredParentClass.getSimpleName());
                                        try {
                                            method = preferredParentClass.getMethod(method.getName(), method.getParameterTypes());
                                        } catch (java.lang.NoSuchMethodException exc) {
                                            skipAdd = true;
                                            org.apache.ambari.swagger.AmbariSwaggerReader.logger.error("{} class defined as parent API is invalid due to method mismatch! Ignoring " + "API {}", preferredParentClass, returnType);
                                        }
                                    }
                                } else {
                                    org.apache.ambari.swagger.AmbariSwaggerReader.logger.warn("{} is a nested API of multiple top level API's. Ignoring top level API {}", returnType, cls);
                                    skipAdd = true;
                                }
                            }
                            if (skipAdd) {
                                continue;
                            } else {
                                nestedAPIs.remove(returnType);
                            }
                            org.apache.ambari.annotations.SwaggerOverwriteNestedAPI swaggerOverwriteNestedAPI = org.springframework.core.annotation.AnnotationUtils.findAnnotation(returnType, org.apache.ambari.annotations.SwaggerOverwriteNestedAPI.class);
                            if (null != swaggerOverwriteNestedAPI) {
                                preferredParentClass = swaggerOverwriteNestedAPI.parentApi();
                                parentApiPath = swaggerOverwriteNestedAPI.parentApiPath();
                                methodPathAsString = swaggerOverwriteNestedAPI.parentMethodPath();
                            } else {
                                parentApiPath = validateParentApiPath(preferredParentClass);
                            }
                            org.apache.ambari.swagger.AmbariSwaggerReader.logger.info("Registering nested API: {}", returnType);
                            org.apache.ambari.swagger.NestedApiRecord nar = new org.apache.ambari.swagger.NestedApiRecord(returnType, preferredParentClass, parentApiPath, method, methodPathAsString);
                            nestedAPIs.put(returnType, nar);
                        }
                    }
                }
            }
        }
        org.apache.ambari.swagger.AmbariSwaggerReader.logger.info("Found {} nested API's", nestedAPIs.size());
        return super.read(classes);
    }

    private java.lang.String validateParentApiPath(java.lang.Class<?> cls) {
        javax.ws.rs.Path apiPath = org.springframework.core.annotation.AnnotationUtils.findAnnotation(cls, javax.ws.rs.Path.class);
        if (null == apiPath) {
            org.apache.ambari.swagger.AmbariSwaggerReader.logger.warn("Parent api {} also seems to be a nested API. The current version does not support " + "multi-level nesting.", cls.getSimpleName());
            return "";
        } else {
            return apiPath.value();
        }
    }

    @java.lang.Override
    protected io.swagger.models.Swagger read(java.lang.Class<?> cls, java.lang.String parentPath, java.lang.String parentMethod, boolean readHidden, java.lang.String[] parentConsumes, java.lang.String[] parentProduces, java.util.Map<java.lang.String, io.swagger.models.Tag> parentTags, java.util.List<io.swagger.models.parameters.Parameter> parentParameters) {
        org.apache.ambari.swagger.NestedApiRecord nestedApiRecord = nestedAPIs.get(cls);
        if (null != nestedApiRecord) {
            org.apache.ambari.swagger.AmbariSwaggerReader.logger.info("Processing nested API: {}", nestedApiRecord);
            java.util.List<io.swagger.models.parameters.Parameter> pathParameters = new java.util.ArrayList<>();
            org.apache.ambari.annotations.SwaggerOverwriteNestedAPI swaggerOverwriteNestedAPI = org.springframework.core.annotation.AnnotationUtils.findAnnotation(nestedApiRecord.nestedApi, org.apache.ambari.annotations.SwaggerOverwriteNestedAPI.class);
            if (null != swaggerOverwriteNestedAPI) {
                org.apache.ambari.swagger.AmbariSwaggerReader.logger.info("Will use path params from @SwaggerOverwriteNestedAPI: {}", ((java.lang.Object[]) (swaggerOverwriteNestedAPI.pathParameters())));
                for (java.lang.String param : swaggerOverwriteNestedAPI.pathParameters()) {
                    io.swagger.models.parameters.PathParameter pathParam = new io.swagger.models.parameters.PathParameter();
                    pathParam.setName(param);
                    pathParam.setType(StringProperty.TYPE);
                    pathParameters.add(pathParam);
                }
            } else {
                io.swagger.models.Operation operation = parseMethod(nestedApiRecord.parentMethod);
                pathParameters = com.google.common.collect.ImmutableList.copyOf(com.google.common.collect.Collections2.filter(operation.getParameters(), com.google.common.base.Predicates.instanceOf(io.swagger.models.parameters.PathParameter.class)));
                org.apache.ambari.swagger.AmbariSwaggerReader.logger.info("Will copy path params from parent method: {}", com.google.common.collect.Lists.transform(pathParameters, new org.apache.ambari.swagger.ParameterToName()));
            }
            return super.read(cls, org.apache.ambari.swagger.AmbariSwaggerReader.joinPaths(nestedApiRecord.parentApiPath, nestedApiRecord.parentMethodPath, parentPath), parentMethod, readHidden, parentConsumes, parentProduces, parentTags, pathParameters);
        } else {
            org.apache.ambari.swagger.AmbariSwaggerReader.logger.info("Processing top level API: {}", cls.getSimpleName());
            return super.read(cls, parentPath, parentMethod, readHidden, parentConsumes, parentProduces, parentTags, parentParameters);
        }
    }

    static java.lang.String joinPaths(java.lang.String firstPath, java.lang.String... paths) {
        java.lang.StringBuilder joined = new java.lang.StringBuilder(firstPath);
        for (java.lang.String path : paths) {
            if (path.isEmpty()) {
            } else if (joined.length() == 0) {
                joined.append(path);
            } else if (joined.charAt(joined.length() - 1) == '/') {
                if (path.startsWith("/")) {
                    joined.append(path.substring(1, path.length()));
                } else {
                    joined.append(path);
                }
            } else if (path.startsWith("/")) {
                joined.append(path);
            } else {
                joined.append('/').append(path);
            }
        }
        return joined.toString();
    }
}