package org.apache.ambari.server.api.predicate;
public class QueryParserTest {
    @org.junit.Test
    public void testParse_simple() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "a"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "b"));
        org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();
        org.apache.ambari.server.controller.spi.Predicate p = parser.parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("a", "b"), p);
    }

    @org.junit.Test
    public void testParse() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "bar"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "&"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_OPEN, "("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "<"));
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
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "&"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, ">="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "d"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "100"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "|"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "!="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "e"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "5"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "&"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_UNARY_OPERATOR, "!"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_OPEN, "("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "f"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "6"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "|"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "g"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "7"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();
        org.apache.ambari.server.controller.spi.Predicate p = parser.parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
        org.apache.ambari.server.controller.predicate.EqualsPredicate<java.lang.String> fooPred = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("foo", "bar");
        org.apache.ambari.server.controller.predicate.LessPredicate<java.lang.String> aPred = new org.apache.ambari.server.controller.predicate.LessPredicate<>("a", "1");
        org.apache.ambari.server.controller.predicate.LessEqualsPredicate<java.lang.String> bPred = new org.apache.ambari.server.controller.predicate.LessEqualsPredicate<>("b", "2");
        org.apache.ambari.server.controller.predicate.GreaterEqualsPredicate<java.lang.String> cPred = new org.apache.ambari.server.controller.predicate.GreaterEqualsPredicate<>("c", "3");
        org.apache.ambari.server.controller.predicate.GreaterEqualsPredicate<java.lang.String> dPred = new org.apache.ambari.server.controller.predicate.GreaterEqualsPredicate<>("d", "100");
        org.apache.ambari.server.controller.predicate.NotPredicate ePred = new org.apache.ambari.server.controller.predicate.NotPredicate(new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("e", "5"));
        org.apache.ambari.server.controller.predicate.EqualsPredicate fPred = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("f", "6");
        org.apache.ambari.server.controller.predicate.EqualsPredicate gPRed = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("g", "7");
        org.apache.ambari.server.controller.predicate.OrPredicate bORcPred = new org.apache.ambari.server.controller.predicate.OrPredicate(bPred, cPred);
        org.apache.ambari.server.controller.predicate.AndPredicate aANDbORcPred = new org.apache.ambari.server.controller.predicate.AndPredicate(aPred, bORcPred);
        org.apache.ambari.server.controller.predicate.AndPredicate aANDbORcANDdPred = new org.apache.ambari.server.controller.predicate.AndPredicate(aANDbORcPred, dPred);
        org.apache.ambari.server.controller.predicate.AndPredicate fooANDaANDbORcANDdPred = new org.apache.ambari.server.controller.predicate.AndPredicate(fooPred, aANDbORcANDdPred);
        org.apache.ambari.server.controller.predicate.OrPredicate fORgPred = new org.apache.ambari.server.controller.predicate.OrPredicate(fPred, gPRed);
        org.apache.ambari.server.controller.predicate.NotPredicate NOTfORgPred = new org.apache.ambari.server.controller.predicate.NotPredicate(fORgPred);
        org.apache.ambari.server.controller.predicate.AndPredicate eANDNOTfORgPred = new org.apache.ambari.server.controller.predicate.AndPredicate(ePred, NOTfORgPred);
        org.apache.ambari.server.controller.predicate.OrPredicate rootPredicate = new org.apache.ambari.server.controller.predicate.OrPredicate(fooANDaANDbORcANDdPred, eANDNOTfORgPred);
        org.junit.Assert.assertEquals(rootPredicate, p);
    }

    @org.junit.Test
    public void testParse_NotOp__simple() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_UNARY_OPERATOR, "!"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "a"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "b"));
        org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();
        org.apache.ambari.server.controller.spi.Predicate p = parser.parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.predicate.NotPredicate(new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("a", "b")), p);
    }

    @org.junit.Test
    public void testParse_NotOp() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "a"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, "&"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_UNARY_OPERATOR, "!"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "b"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "2"));
        org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();
        org.apache.ambari.server.controller.spi.Predicate p = parser.parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
        org.apache.ambari.server.controller.predicate.EqualsPredicate aPred = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("a", "1");
        org.apache.ambari.server.controller.predicate.EqualsPredicate bPred = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("b", "2");
        org.apache.ambari.server.controller.predicate.NotPredicate notPred = new org.apache.ambari.server.controller.predicate.NotPredicate(bPred);
        org.apache.ambari.server.controller.predicate.AndPredicate andPred = new org.apache.ambari.server.controller.predicate.AndPredicate(aPred, notPred);
        org.junit.Assert.assertEquals(andPred, p);
    }

    @org.junit.Test
    public void testParse_InOp__simple() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, ".in("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "one,two,3"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();
        org.apache.ambari.server.controller.spi.Predicate p = parser.parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
        org.apache.ambari.server.controller.predicate.EqualsPredicate ep1 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("foo", "one");
        org.apache.ambari.server.controller.predicate.EqualsPredicate ep2 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("foo", "two");
        org.apache.ambari.server.controller.predicate.EqualsPredicate ep3 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("foo", "3");
        org.apache.ambari.server.controller.predicate.OrPredicate orPredicate = new org.apache.ambari.server.controller.predicate.OrPredicate(ep1, ep2, ep3);
        org.junit.Assert.assertEquals(orPredicate, p);
    }

    @org.junit.Test
    public void testParse_InOp__HostName() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, ".in("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "HostRoles/host_name"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "Host1,HOST2,HoSt3"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();
        org.apache.ambari.server.controller.spi.Predicate p = parser.parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
        org.apache.ambari.server.controller.predicate.EqualsPredicate ep1 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("HostRoles/host_name", "host1");
        org.apache.ambari.server.controller.predicate.EqualsPredicate ep2 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("HostRoles/host_name", "host2");
        org.apache.ambari.server.controller.predicate.EqualsPredicate ep3 = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("HostRoles/host_name", "host3");
        org.apache.ambari.server.controller.predicate.OrPredicate orPredicate = new org.apache.ambari.server.controller.predicate.OrPredicate(ep1, ep2, ep3);
        org.junit.Assert.assertEquals(orPredicate, p);
    }

    @org.junit.Test
    public void testParse_InOp__HostName_Empty() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, ".in("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "Hosts/host_name"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();
        try {
            org.apache.ambari.server.controller.spi.Predicate p = parser.parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
            org.junit.Assert.fail();
        } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
            org.junit.Assert.assertEquals(e.getMessage(), "IN operator is missing a required right operand for property Hosts/host_name");
        }
    }

    @org.junit.Test
    public void testParse_EquOp_HostName() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "HostRoles/host_name"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "HOST1"));
        org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();
        org.apache.ambari.server.controller.spi.Predicate p = parser.parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
        org.apache.ambari.server.controller.predicate.EqualsPredicate equalsPred = new org.apache.ambari.server.controller.predicate.EqualsPredicate<>("HostRoles/host_name", "host1");
        org.junit.Assert.assertEquals(equalsPred, p);
    }

    @org.junit.Test
    public void testParse_InOp__exception() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, ".in("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();
        try {
            parser.parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
            org.junit.Assert.fail("Expected InvalidQueryException due to missing right operand");
        } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
        }
    }

    @org.junit.Test
    public void testParse_FilterOp() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, ".matches("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, ".*"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();
        org.apache.ambari.server.controller.spi.Predicate p = parser.parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
        org.apache.ambari.server.controller.predicate.FilterPredicate fp = new org.apache.ambari.server.controller.predicate.FilterPredicate("foo", ".*");
        org.junit.Assert.assertEquals(fp, p);
    }

    @org.junit.Test
    public void testParse_FilterOp_exception() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, ".matches("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "foo"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();
        try {
            org.apache.ambari.server.controller.spi.Predicate p = parser.parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
            org.junit.Assert.fail("Filter operator is missing a required right operand.");
        } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
        }
    }

    @org.junit.Test
    public void testParse_isEmptyOp__simple() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, ".isEmpty("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "category1"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();
        org.apache.ambari.server.controller.spi.Predicate p = parser.parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
        org.junit.Assert.assertEquals(new org.apache.ambari.server.controller.predicate.CategoryIsEmptyPredicate("category1"), p);
    }

    @org.junit.Test
    public void testParse_isEmptyOp__exception() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, ".isEmpty("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "category1"));
        org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();
        try {
            parser.parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
            org.junit.Assert.fail("Expected InvalidQueryException due to missing closing bracket");
        } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
        }
    }

    @org.junit.Test
    public void testParse_isEmptyOp__exception2() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, ".isEmpty("));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "category1"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "one,two,3"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
        org.apache.ambari.server.api.predicate.QueryParser parser = new org.apache.ambari.server.api.predicate.QueryParser();
        try {
            parser.parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
            org.junit.Assert.fail("Expected InvalidQueryException due to existence of right operand");
        } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
        }
    }

    @org.junit.Test
    public void testParse_noTokens() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        org.junit.Assert.assertNull(new org.apache.ambari.server.api.predicate.QueryParser().parse(new org.apache.ambari.server.api.predicate.Token[0]));
    }

    @org.junit.Test
    public void testParse_mismatchedBrackets() {
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
        try {
            new org.apache.ambari.server.api.predicate.QueryParser().parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
            org.junit.Assert.fail("Expected InvalidQueryException due to missing closing bracket");
        } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
        }
    }

    @org.junit.Test
    public void testParse_outOfOrderTokens() {
        java.util.List<org.apache.ambari.server.api.predicate.Token> listTokens = new java.util.ArrayList<>();
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, "a"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, "1"));
        listTokens.add(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, "="));
        try {
            new org.apache.ambari.server.api.predicate.QueryParser().parse(listTokens.toArray(new org.apache.ambari.server.api.predicate.Token[listTokens.size()]));
            org.junit.Assert.fail("Expected InvalidQueryException due to invalid last token");
        } catch (org.apache.ambari.server.api.predicate.InvalidQueryException e) {
        }
    }
}