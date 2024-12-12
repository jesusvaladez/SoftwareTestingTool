package org.apache.ambari.server.api.resources;
public class SimpleResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Stage, "stage", "stages", org.apache.ambari.server.controller.spi.Resource.Type.Task);
        org.junit.Assert.assertEquals("stages", resourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Stage, "stage", "stages", org.apache.ambari.server.controller.spi.Resource.Type.Task);
        org.junit.Assert.assertEquals("stage", resourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testDirectives() {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition;
        resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Stage, "stage", "stages", org.apache.ambari.server.controller.spi.Resource.Type.Task);
        validateDirectives(java.util.Collections.emptySet(), resourceDefinition.getCreateDirectives());
        validateDirectives(java.util.Collections.emptySet(), resourceDefinition.getReadDirectives());
        validateDirectives(java.util.Collections.emptySet(), resourceDefinition.getUpdateDirectives());
        validateDirectives(java.util.Collections.emptySet(), resourceDefinition.getDeleteDirectives());
        java.util.HashMap<org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType, java.util.Collection<java.lang.String>> directives = new java.util.HashMap<>();
        directives.put(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.CREATE, java.util.Arrays.asList("POST1", "POST2"));
        directives.put(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.READ, java.util.Arrays.asList("GET1", "GET2"));
        directives.put(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.UPDATE, java.util.Arrays.asList("PUT1", "PUT2"));
        directives.put(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.DELETE, java.util.Arrays.asList("DEL1", "DEL2"));
        resourceDefinition = new org.apache.ambari.server.api.resources.SimpleResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.Stage, "stage", "stages", java.util.Collections.singleton(org.apache.ambari.server.controller.spi.Resource.Type.Task), directives);
        validateDirectives(directives.get(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.CREATE), resourceDefinition.getCreateDirectives());
        validateDirectives(directives.get(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.READ), resourceDefinition.getReadDirectives());
        validateDirectives(directives.get(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.UPDATE), resourceDefinition.getUpdateDirectives());
        validateDirectives(directives.get(org.apache.ambari.server.api.resources.BaseResourceDefinition.DirectiveType.DELETE), resourceDefinition.getDeleteDirectives());
    }

    private void validateDirectives(java.util.Collection<java.lang.String> expected, java.util.Collection<java.lang.String> actual) {
        int actualSize = actual.size();
        org.junit.Assert.assertEquals(expected.size(), actual.size());
        for (java.lang.String actualItem : actual) {
            org.junit.Assert.assertTrue(expected.contains(actualItem));
        }
        org.junit.Assert.assertTrue(actual.add("DIRECTIVE"));
        org.junit.Assert.assertEquals(actualSize + 1, actual.size());
    }
}