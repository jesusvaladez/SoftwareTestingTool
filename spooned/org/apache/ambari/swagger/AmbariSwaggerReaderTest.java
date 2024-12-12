package org.apache.ambari.swagger;
public class AmbariSwaggerReaderTest {
    @org.junit.Test
    public void testJoinPaths() {
        org.junit.Assert.assertEquals("/toplevel/nested/{param}/list", org.apache.ambari.swagger.AmbariSwaggerReader.joinPaths("", "/", "/", "", "toplevel", "/nested/", "/{param}", "list"));
        org.junit.Assert.assertEquals("/toplevel/nested/{param}/list", org.apache.ambari.swagger.AmbariSwaggerReader.joinPaths("/", "toplevel", "", "/nested/", "/", "/{param}", "list", ""));
    }

    @org.junit.Test
    public void swaggerBasicCase() {
        org.apache.ambari.swagger.AmbariSwaggerReader asr = new org.apache.ambari.swagger.AmbariSwaggerReader(null, EasyMock.createMock(org.apache.maven.plugin.logging.Log.class));
        io.swagger.models.Swagger swagger = asr.read(com.google.common.collect.ImmutableSet.of(org.apache.ambari.swagger.TopLevelAPI.class, org.apache.ambari.swagger.NestedAPI.class));
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("/toplevel/top", "/toplevel/{param}/nested/list"), swagger.getPaths().keySet());
        org.apache.ambari.swagger.AmbariSwaggerReaderTest.assertPathParamsExist(swagger, "/toplevel/{param}/nested/list", "param");
    }

    @org.junit.Test
    public void swaggerConflictingNestedApis() {
        org.apache.ambari.swagger.AmbariSwaggerReader asr = new org.apache.ambari.swagger.AmbariSwaggerReader(null, EasyMock.createMock(org.apache.maven.plugin.logging.Log.class));
        java.util.Set<java.lang.Class<?>> classes = new java.util.LinkedHashSet<>(java.util.Arrays.asList(org.apache.ambari.swagger.TopLevelAPI.class, org.apache.ambari.swagger.AnotherTopLevelAPI.class, org.apache.ambari.swagger.NestedAPI.class));
        io.swagger.models.Swagger swagger = asr.read(classes);
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("/toplevel/top", "/toplevel/{param}/nested/list", "/toplevel2/anotherTop"), swagger.getPaths().keySet());
        org.apache.ambari.swagger.AmbariSwaggerReaderTest.assertPathParamsExist(swagger, "/toplevel/{param}/nested/list", "param");
    }

    @org.junit.Test
    public void swaggerConflictingNestedApisWithPreferredParent() {
        org.apache.ambari.swagger.AmbariSwaggerReader asr = new org.apache.ambari.swagger.AmbariSwaggerReader(null, EasyMock.createMock(org.apache.maven.plugin.logging.Log.class));
        java.util.Set<java.lang.Class<?>> classes = new java.util.LinkedHashSet<>(java.util.Arrays.asList(org.apache.ambari.swagger.TopLevelAPI.class, org.apache.ambari.swagger.AnotherTopLevelAPI.class, org.apache.ambari.swagger.NestedWithPreferredParentAPI.class));
        io.swagger.models.Swagger swagger = asr.read(classes);
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("/toplevel/top", "/toplevel2/{param}/nestedWithPreferredParent/list", "/toplevel2/anotherTop"), swagger.getPaths().keySet());
        org.apache.ambari.swagger.AmbariSwaggerReaderTest.assertPathParamsExist(swagger, "/toplevel2/{param}/nestedWithPreferredParent/list", "param");
    }

    @org.junit.Test
    public void swaggerConflictingNestedApisWithSamePreferredParent() {
        org.apache.ambari.swagger.AmbariSwaggerReader asr = new org.apache.ambari.swagger.AmbariSwaggerReader(null, EasyMock.createMock(org.apache.maven.plugin.logging.Log.class));
        java.util.Set<java.lang.Class<?>> classes = new java.util.LinkedHashSet<>(java.util.Arrays.asList(org.apache.ambari.swagger.TopLevelAPI.class, org.apache.ambari.swagger.AnotherTopLevelAPI.class, org.apache.ambari.swagger.NestedWithSamePreferredParentAPI.class));
        io.swagger.models.Swagger swagger = asr.read(classes);
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("/toplevel/top", "/toplevel/{param}/nestedWithSamePreferredParent/list", "/toplevel2/anotherTop"), swagger.getPaths().keySet());
        org.apache.ambari.swagger.AmbariSwaggerReaderTest.assertPathParamsExist(swagger, "/toplevel/{param}/nestedWithSamePreferredParent/list", "param");
    }

    @org.junit.Test
    public void swaggerConflictingNestedApisWithBadPreferredParent() {
        org.apache.ambari.swagger.AmbariSwaggerReader asr = new org.apache.ambari.swagger.AmbariSwaggerReader(null, EasyMock.createMock(org.apache.maven.plugin.logging.Log.class));
        java.util.Set<java.lang.Class<?>> classes = new java.util.LinkedHashSet<>(java.util.Arrays.asList(org.apache.ambari.swagger.TopLevelAPI.class, org.apache.ambari.swagger.AnotherTopLevelAPI.class, org.apache.ambari.swagger.NestedWithBadPreferredParentAPI.class));
        io.swagger.models.Swagger swagger = asr.read(classes);
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("/toplevel/top", "/toplevel2/{param}/nestedWithBadPreferredParent/list", "/toplevel2/anotherTop"), swagger.getPaths().keySet());
        org.apache.ambari.swagger.AmbariSwaggerReaderTest.assertPathParamsExist(swagger, "/toplevel2/{param}/nestedWithBadPreferredParent/list", "param");
    }

    @org.junit.Test
    public void swaggerNestedApisWithOverwrite() {
        org.apache.ambari.swagger.AmbariSwaggerReader asr = new org.apache.ambari.swagger.AmbariSwaggerReader(null, EasyMock.createMock(org.apache.maven.plugin.logging.Log.class));
        java.util.Set<java.lang.Class<?>> classes = new java.util.LinkedHashSet<>(java.util.Arrays.asList(org.apache.ambari.swagger.NestedWithOverwrite.class, org.apache.ambari.swagger.TopLevel4API.class));
        io.swagger.models.Swagger swagger = asr.read(classes);
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("/toplevel3/{foo}/bar/list", "/toplevel4/top"), swagger.getPaths().keySet());
        org.apache.ambari.swagger.AmbariSwaggerReaderTest.assertPathParamsExist(swagger, "/toplevel3/{foo}/bar/list", "foo");
    }

    @org.junit.Test
    public void swaggerApiThatIsBothTopLevelAndNestedIsCountedAsTopLevel() {
        org.apache.ambari.swagger.AmbariSwaggerReader asr = new org.apache.ambari.swagger.AmbariSwaggerReader(null, EasyMock.createMock(org.apache.maven.plugin.logging.Log.class));
        io.swagger.models.Swagger swagger = asr.read(com.google.common.collect.ImmutableSet.of(org.apache.ambari.swagger.YetAnotherTopLevelAPI.class, org.apache.ambari.swagger.NestedAndTopLevelAPI.class));
        org.junit.Assert.assertEquals(com.google.common.collect.ImmutableSet.of("/toplevel3/yetAnotherTop", "/canBeReachedFromTopToo/list"), swagger.getPaths().keySet());
    }

    private static void assertPathParamsExist(io.swagger.models.Swagger swagger, java.lang.String path, java.lang.String... expectedPathParams) {
        java.util.List<io.swagger.models.parameters.Parameter> parameters = swagger.getPath(path).getGet().getParameters();
        org.junit.Assert.assertNotNull("No path parameters for path: " + path, parameters);
        java.util.Set<java.lang.String> pathParamNames = new java.util.HashSet<>();
        for (io.swagger.models.parameters.Parameter param : parameters) {
            if (param instanceof io.swagger.models.parameters.PathParameter) {
                pathParamNames.add(param.getName());
            }
        }
        java.util.Set<java.lang.String> missingPathParams = com.google.common.collect.Sets.difference(com.google.common.collect.ImmutableSet.copyOf(expectedPathParams), pathParamNames);
        org.junit.Assert.assertTrue((("Expected path params for [" + path) + "] are missing: ") + missingPathParams, missingPathParams.isEmpty());
    }
}