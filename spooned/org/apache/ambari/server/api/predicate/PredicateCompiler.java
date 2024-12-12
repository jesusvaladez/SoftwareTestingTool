package org.apache.ambari.server.api.predicate;
public class PredicateCompiler {
    private org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();

    private org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();

    public org.apache.ambari.server.controller.spi.Predicate compile(java.lang.String exp) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        return parser.parse(lexer.tokens(exp));
    }

    public org.apache.ambari.server.controller.spi.Predicate compile(java.lang.String exp, java.util.Collection<java.lang.String> ignoredProperties) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        return parser.parse(lexer.tokens(exp, ignoredProperties));
    }
}