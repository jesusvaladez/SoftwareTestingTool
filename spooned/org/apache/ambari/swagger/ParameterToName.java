package org.apache.ambari.swagger;
class ParameterToName implements com.google.common.base.Function<io.swagger.models.parameters.Parameter, java.lang.String> {
    @java.lang.Override
    public java.lang.String apply(io.swagger.models.parameters.Parameter input) {
        return input.getName();
    }
}