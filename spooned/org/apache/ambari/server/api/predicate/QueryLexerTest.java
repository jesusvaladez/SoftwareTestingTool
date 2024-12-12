package org.apache.ambari.server.api.predicate;
public class QueryLexerTest {
    @org.junit.Test
    public void testTokens_simple() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "a"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "&"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_OPEN, "("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "<="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "b"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "2"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "|"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, ">"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "c"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "3"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("a=1&(b<=2|c>3)");
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_multipleBrackets() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "<"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "a"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "&"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_OPEN, "("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "<="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "b"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "2"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "&"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_OPEN, "("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, ">="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "c"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "3"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "|"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "!="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "d"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "4"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("a<1&(b<=2&(c>=3|d!=4))");
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testUnaryNot() throws java.lang.Exception {
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("!foo<5");
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_UNARY_OPERATOR, "!"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "<"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "5"));
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testInOperator() throws java.lang.Exception {
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("foo.in(one, two, 3)");
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, ".in("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "one, two, 3"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testIsEmptyOperator() throws java.lang.Exception {
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("category1.isEmpty()");
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, ".isEmpty("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "category1"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_ignoreFieldsSyntax___noPredicate() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("fields=foo,bar");
        org.junit.Assert.assertEquals(0, tokens.length);
    }

    @org.junit.Test
    public void testTokens_ignoreFieldsSyntax___fieldsFirst() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("fields=foo,bar&foo=1");
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_ignoreFieldsSyntax___fieldsLast() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("foo=1&fields=foo,bar");
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_ignoreFormatSyntax___noPredicate() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("format=default");
        org.junit.Assert.assertEquals(0, tokens.length);
    }

    @org.junit.Test
    public void testTokens_ignoreFormatSyntax___formatFirst() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("format=default&foo=1");
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_ignoreFormatSyntax___formatLast() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("foo=1&format=foo");
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_ignoreUnderscoreSyntax___noPredicate() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("_=1");
        org.junit.Assert.assertEquals(0, tokens.length);
    }

    @org.junit.Test
    public void testTokens_ignoreUnderscoreSyntax___fieldsFirst() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("_=111111&foo=1");
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_ignoreUnderscoreSyntax___fieldsLast() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("foo=1&_=11111");
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_ignore__multipleIgnoreFields() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("fields=a/b&foo=1&_=5555555");
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_ignore__multipleConsecutiveIgnoreFields() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("foo=1&fields=a/b&_=5555555");
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_ignore__multipleConsecutiveIgnoreFields2() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("fields=a/b&_=5555555&foo=1");
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_ignore__fieldsMiddle() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "&"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "bar"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "2"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("foo=1&fields=a/b&bar=2");
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_ignore__fieldsMiddle2() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "&"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "bar"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "2"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("foo=1&fields=a/b,c&_=123&bar=2");
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_ignore__userDefined() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "&"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "bar"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "2"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        java.util.Set<java.lang.String> propertiesToIgnore = new java.util.HashSet<>();
        propertiesToIgnore.add("ignore1");
        propertiesToIgnore.add("otherIgnore");
        propertiesToIgnore.add("ba");
        propertiesToIgnore.add("ple");
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("ba=gone&foo=1&ignore1=pleaseIgnoreMe&fields=a/b&bar=2&otherIgnore=byebye", propertiesToIgnore);
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_invalidRelationalOp() {
        try {
            new org.apache.ambari.server.api.predicate.QueryLexer().tokens("foo=1&bar|5");
            org.junit.Assert.fail("Expected InvalidQueryException due to invalid relational op");
        } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
        }
    }

    @org.junit.Test
    public void testTokens_invalidLogicalOp() {
        try {
            new org.apache.ambari.server.api.predicate.QueryLexer().tokens("foo=1<5=2");
            org.junit.Assert.fail("Expected InvalidQueryException due to invalid logical op");
        } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
        }
    }

    @org.junit.Test
    public void testTokens_invalidLogicalOp2() {
        try {
            new org.apache.ambari.server.api.predicate.QueryLexer().tokens("foo=1&&5=2");
            org.junit.Assert.fail("Expected InvalidQueryException due to invalid logical op");
        } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
        }
    }

    @org.junit.Test
    public void testTokens_matchesRegexp_simple() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, ".matches("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "StackConfigurations/property_type"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "(.*USER.*)|(.*GROUP.*)"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("StackConfigurations/property_type.matches((.*USER.*)|(.*GROUP.*))");
        org.junit.Assert.assertArrayEquals(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test
    public void testTokens_matchesRegexp() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_OPEN, "("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, ".matches("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "StackConfigurations/property_type"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "(([^=])|=|!=),.in(&).*USER.*.isEmpty(a).matches(b)"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "|"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, ".matches("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "StackConfigurations/property_type"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "fields format to from .*GROUP.*"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        org.apache.ambari.server.api.predicate.Token[] tokens = lexer.tokens("(StackConfigurations/property_type.matches((([^=])|=|!=),.in(&).*USER.*" + ".isEmpty(a).matches(b))|StackConfigurations/property_type.matches(fields format to from .*GROUP.*))");
        org.junit.Assert.assertArrayEquals("All characters between \".matches(\" and corresponding closing \")\" bracket should " + "come to VALUE_OPERAND.", listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]), tokens);
    }

    @org.junit.Test(expected = org.apache.ambari.server.api.predicate.InvalidQueryException.class)
    public void testTokens_matchesRegexpInvalidQuery() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        lexer.tokens("StackConfigurations/property_type.matches((.*USER.*)|(.*GROUP.*)");
    }

    @org.junit.Test(expected = org.apache.ambari.server.api.predicate.InvalidQueryException.class)
    public void testTokens_matchesRegexpInvalidQuery2() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        org.apache.ambari.server.api.predicate.QueryLexer lexer = new org.apache.ambari.server.api.predicate.QueryLexer();
        lexer.tokens("StackConfigurations/property_type.matches((.*USER.*)|(.*GROUP.*)|StackConfigurations/property_type.matches(.*GROUP.*)");
    }
}