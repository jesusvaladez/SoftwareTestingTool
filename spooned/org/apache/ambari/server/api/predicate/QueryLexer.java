package org.apache.ambari.server.api.predicate;
public class QueryLexer {
    public static final java.lang.String QUERY_FIELDS = "fields";

    public static final java.lang.String QUERY_FORMAT = "format";

    public static final java.lang.String QUERY_PAGE_SIZE = "page_size";

    public static final java.lang.String QUERY_TO = "to";

    public static final java.lang.String QUERY_FROM = "from";

    public static final java.lang.String QUERY_MINIMAL = "minimal_response";

    public static final java.lang.String QUERY_SORT = "sortBy";

    public static final java.lang.String QUERY_DOAS = "doAs";

    private static final java.lang.String[] ALL_DELIMS = new java.lang.String[]{ ".matches\\(", ".in\\(", ".isEmpty\\(", "<=", ">=", "!=", "=", "<", ">", "&", "|", "!", "(", ")" };

    private static final java.util.Map<org.apache.ambari.server.api.predicate.Token.TYPE, java.util.List<org.apache.ambari.server.api.predicate.QueryLexer.TokenHandler>> TOKEN_HANDLERS = new java.util.HashMap<>();

    private static final java.util.Set<java.lang.String> SET_IGNORE = new java.util.HashSet<>();

    public QueryLexer() {
        java.util.List<org.apache.ambari.server.api.predicate.QueryLexer.TokenHandler> listHandlers = new java.util.ArrayList<>();
        listHandlers.add(new org.apache.ambari.server.api.predicate.QueryLexer.LogicalUnaryOperatorTokenHandler());
        listHandlers.add(new org.apache.ambari.server.api.predicate.QueryLexer.OpenBracketTokenHandler());
        listHandlers.add(new org.apache.ambari.server.api.predicate.QueryLexer.PropertyOperandTokenHandler());
        org.apache.ambari.server.api.predicate.QueryLexer.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_OPEN, listHandlers);
        org.apache.ambari.server.api.predicate.QueryLexer.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, listHandlers);
        org.apache.ambari.server.api.predicate.QueryLexer.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_UNARY_OPERATOR, listHandlers);
        listHandlers = new java.util.ArrayList<>();
        listHandlers.add(new org.apache.ambari.server.api.predicate.QueryLexer.RelationalOperatorTokenHandler());
        listHandlers.add(new org.apache.ambari.server.api.predicate.QueryLexer.RelationalOperatorFuncTokenHandler());
        org.apache.ambari.server.api.predicate.QueryLexer.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, listHandlers);
        listHandlers = new java.util.ArrayList<>();
        listHandlers.add(new org.apache.ambari.server.api.predicate.QueryLexer.ValueOperandTokenHandler());
        org.apache.ambari.server.api.predicate.QueryLexer.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, listHandlers);
        listHandlers = new java.util.ArrayList<>();
        listHandlers.add(new org.apache.ambari.server.api.predicate.QueryLexer.CloseBracketTokenHandler());
        listHandlers.add(new org.apache.ambari.server.api.predicate.QueryLexer.ComplexValueOperandTokenHandler());
        org.apache.ambari.server.api.predicate.QueryLexer.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, listHandlers);
        listHandlers = new java.util.ArrayList<>();
        listHandlers.add(new org.apache.ambari.server.api.predicate.QueryLexer.CloseBracketTokenHandler());
        listHandlers.add(new org.apache.ambari.server.api.predicate.QueryLexer.LogicalOperatorTokenHandler());
        org.apache.ambari.server.api.predicate.QueryLexer.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, listHandlers);
        listHandlers = new java.util.ArrayList<>(listHandlers);
        listHandlers.add(0, new org.apache.ambari.server.api.predicate.QueryLexer.ComplexValueOperandTokenHandler());
        org.apache.ambari.server.api.predicate.QueryLexer.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, listHandlers);
    }

    public org.apache.ambari.server.api.predicate.Token[] tokens(java.lang.String exp) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        return tokens(exp, java.util.Collections.emptySet());
    }

    public org.apache.ambari.server.api.predicate.Token[] tokens(java.lang.String exp, java.util.Collection<java.lang.String> ignoreProperties) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx = new org.apache.ambari.server.api.predicate.QueryLexer.ScanContext();
        ctx.addPropertiesToIgnore(org.apache.ambari.server.api.predicate.QueryLexer.SET_IGNORE);
        ctx.addPropertiesToIgnore(ignoreProperties);
        for (java.lang.String tok : parseStringTokens(exp)) {
            java.util.List<org.apache.ambari.server.api.predicate.QueryLexer.TokenHandler> listHandlers = org.apache.ambari.server.api.predicate.QueryLexer.TOKEN_HANDLERS.get(ctx.getLastTokenType());
            boolean processed = false;
            int idx = 0;
            while ((!processed) && (idx < listHandlers.size())) {
                processed = listHandlers.get(idx++).handleToken(tok, ctx);
            } 
            if (!processed) {
                throw new org.apache.ambari.server.api.predicate.InvalidQueryException((("Invalid Query Token: token='" + tok) + "\', previous token type=") + ctx.getLastTokenType());
            }
        }
        ctx.validateEndState();
        return ctx.getTokenList().toArray(new org.apache.ambari.server.api.predicate.Token[ctx.getTokenList().size()]);
    }

    private java.util.List<java.lang.String> parseStringTokens(java.lang.String exp) {
        java.util.regex.Pattern pattern = generatePattern();
        java.util.regex.Matcher matcher = pattern.matcher(exp);
        java.util.List<java.lang.String> listStrTokens = new java.util.ArrayList<>();
        int pos = 0;
        while (matcher.find()) {
            if (pos != matcher.start()) {
                listStrTokens.add(exp.substring(pos, matcher.start()));
            }
            listStrTokens.add(matcher.group());
            pos = matcher.end();
        } 
        if (pos != exp.length()) {
            listStrTokens.add(exp.substring(pos));
        }
        return listStrTokens;
    }

    private java.util.regex.Pattern generatePattern() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append('(');
        for (java.lang.String delim : org.apache.ambari.server.api.predicate.QueryLexer.ALL_DELIMS) {
            if (sb.length() != 1)
                sb.append('|');

            sb.append('\\');
            sb.append(delim);
        }
        sb.append(')');
        return java.util.regex.Pattern.compile(sb.toString());
    }

    static {
        SET_IGNORE.add(QUERY_FIELDS);
        SET_IGNORE.add(QUERY_FORMAT);
        SET_IGNORE.add(QUERY_PAGE_SIZE);
        SET_IGNORE.add(QUERY_TO);
        SET_IGNORE.add(QUERY_FROM);
        SET_IGNORE.add(QUERY_MINIMAL);
        SET_IGNORE.add(QUERY_SORT);
        SET_IGNORE.add(QUERY_DOAS);
        SET_IGNORE.add("_");
    }

    private class ScanContext {
        private org.apache.ambari.server.api.predicate.Token.TYPE m_lastType;

        private java.lang.String m_propertyName;

        private java.util.List<org.apache.ambari.server.api.predicate.Token> m_listTokens = new java.util.ArrayList<>();

        private org.apache.ambari.server.api.predicate.Token.TYPE m_ignoreSegmentEndToken = null;

        private java.util.Set<java.lang.String> m_propertiesToIgnore = new java.util.HashSet<>();

        private int bracketScore = 0;

        private java.util.Deque<org.apache.ambari.server.api.predicate.Token> m_intermediateTokens = new java.util.ArrayDeque<>();

        private ScanContext() {
            m_lastType = org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR;
        }

        public void setIgnoreSegmentEndToken(org.apache.ambari.server.api.predicate.Token.TYPE type) {
            m_ignoreSegmentEndToken = type;
        }

        public org.apache.ambari.server.api.predicate.Token.TYPE getLastTokenType() {
            return m_lastType;
        }

        public void setLastTokenType(org.apache.ambari.server.api.predicate.Token.TYPE lastType) {
            m_lastType = lastType;
        }

        public java.lang.String getPropertyOperand() {
            return m_propertyName;
        }

        public void setPropertyOperand(java.lang.String prop) {
            m_propertyName = prop;
        }

        public void addToken(org.apache.ambari.server.api.predicate.Token token) {
            if (m_ignoreSegmentEndToken == null) {
                m_listTokens.add(token);
            } else if (token.getType() == m_ignoreSegmentEndToken) {
                m_ignoreSegmentEndToken = null;
            }
        }

        public java.util.List<org.apache.ambari.server.api.predicate.Token> getTokenList() {
            return m_listTokens;
        }

        public java.util.Set<java.lang.String> getPropertiesToIgnore() {
            return m_propertiesToIgnore;
        }

        public void addPropertiesToIgnore(java.util.Collection<java.lang.String> ignoredProperties) {
            if (ignoredProperties != null) {
                m_propertiesToIgnore.addAll(ignoredProperties);
            }
        }

        public void pushIntermediateToken(org.apache.ambari.server.api.predicate.Token token) {
            if (m_ignoreSegmentEndToken == null) {
                m_intermediateTokens.add(token);
            } else if (token.getType() == m_ignoreSegmentEndToken) {
                m_ignoreSegmentEndToken = null;
            }
        }

        public java.util.Deque<org.apache.ambari.server.api.predicate.Token> getIntermediateTokens() {
            return m_intermediateTokens;
        }

        public void addIntermediateTokens() {
            m_listTokens.addAll(m_intermediateTokens);
            m_intermediateTokens.clear();
        }

        public int getBracketScore() {
            return bracketScore;
        }

        public int incrementBracketScore(int n) {
            return bracketScore += n;
        }

        public int decrementBracketScore(int decValue) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            bracketScore -= decValue;
            if (bracketScore < 0) {
                throw new org.apache.ambari.server.api.predicate.InvalidQueryException((((("Unexpected closing bracket.  Last token type: " + getLastTokenType()) + ", Current property operand: ") + getPropertyOperand()) + ", tokens: ") + getTokenList());
            }
            return bracketScore;
        }

        public void validateEndState() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            for (org.apache.ambari.server.api.predicate.QueryLexer.TokenHandler handler : org.apache.ambari.server.api.predicate.QueryLexer.TOKEN_HANDLERS.get(getLastTokenType())) {
                handler.validateEndState(this);
            }
        }
    }

    private abstract class TokenHandler {
        public boolean handleToken(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            if (handles(token, ctx)) {
                _handleToken(token, ctx);
                ctx.setLastTokenType(getType());
                return true;
            } else {
                return false;
            }
        }

        public void validateEndState(org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            if (!ctx.getIntermediateTokens().isEmpty()) {
                throw new org.apache.ambari.server.api.predicate.InvalidQueryException("Unexpected end of expression.");
            }
        }

        public abstract void _handleToken(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException;

        public abstract org.apache.ambari.server.api.predicate.Token.TYPE getType();

        public abstract boolean handles(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx);
    }

    private class PropertyOperandTokenHandler extends org.apache.ambari.server.api.predicate.QueryLexer.TokenHandler {
        @java.lang.Override
        public void _handleToken(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            if (!ctx.getPropertiesToIgnore().contains(token)) {
                ctx.setPropertyOperand(token);
            } else if (!ctx.getTokenList().isEmpty()) {
                ctx.setIgnoreSegmentEndToken(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND);
                ctx.getTokenList().remove(ctx.getTokenList().size() - 1);
            } else {
                ctx.setIgnoreSegmentEndToken(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR);
            }
        }

        @java.lang.Override
        public org.apache.ambari.server.api.predicate.Token.TYPE getType() {
            return org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND;
        }

        @java.lang.Override
        public boolean handles(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) {
            return token.matches("[^!&\\|<=|>=|!=|=|<|>\\(\\)]+");
        }
    }

    private class ValueOperandTokenHandler extends org.apache.ambari.server.api.predicate.QueryLexer.TokenHandler {
        @java.lang.Override
        public void _handleToken(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            ctx.addToken(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, token));
        }

        @java.lang.Override
        public org.apache.ambari.server.api.predicate.Token.TYPE getType() {
            return org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND;
        }

        @java.lang.Override
        public boolean handles(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) {
            return token.matches("[^!&\\|<=|>=|!=|=|<|>]+");
        }
    }

    private class OpenBracketTokenHandler extends org.apache.ambari.server.api.predicate.QueryLexer.TokenHandler {
        @java.lang.Override
        public void _handleToken(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            ctx.addToken(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_OPEN, token));
        }

        @java.lang.Override
        public org.apache.ambari.server.api.predicate.Token.TYPE getType() {
            return org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_OPEN;
        }

        @java.lang.Override
        public boolean handles(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) {
            return token.matches("\\(");
        }
    }

    private class CloseBracketTokenHandler extends org.apache.ambari.server.api.predicate.QueryLexer.TokenHandler {
        @java.lang.Override
        public void _handleToken(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            ctx.addToken(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, token));
        }

        @java.lang.Override
        public org.apache.ambari.server.api.predicate.Token.TYPE getType() {
            return org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE;
        }

        @java.lang.Override
        public boolean handles(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) {
            return token.matches("\\)");
        }
    }

    private class RelationalOperatorTokenHandler extends org.apache.ambari.server.api.predicate.QueryLexer.TokenHandler {
        @java.lang.Override
        public void _handleToken(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            ctx.addToken(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, token));
            ctx.addToken(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, ctx.getPropertyOperand()));
        }

        @java.lang.Override
        public org.apache.ambari.server.api.predicate.Token.TYPE getType() {
            return org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR;
        }

        @java.lang.Override
        public boolean handles(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) {
            return token.matches("<=|>=|!=|=|<|>");
        }
    }

    private class RelationalOperatorFuncTokenHandler extends org.apache.ambari.server.api.predicate.QueryLexer.TokenHandler {
        @java.lang.Override
        public void _handleToken(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            ctx.addToken(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, token));
            ctx.addToken(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, ctx.getPropertyOperand()));
        }

        @java.lang.Override
        public org.apache.ambari.server.api.predicate.Token.TYPE getType() {
            return org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC;
        }

        @java.lang.Override
        public boolean handles(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) {
            return token.matches("\\.[a-zA-Z]+\\(");
        }
    }

    private class ComplexValueOperandTokenHandler extends org.apache.ambari.server.api.predicate.QueryLexer.TokenHandler {
        @java.lang.Override
        public void _handleToken(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            if (token.equals(")")) {
                ctx.decrementBracketScore(1);
            } else if (token.endsWith("(")) {
                ctx.incrementBracketScore(1);
            }
            java.lang.String tokenValue = token;
            if (ctx.getBracketScore() > 0) {
                java.util.Deque<org.apache.ambari.server.api.predicate.Token> intermediateTokens = ctx.getIntermediateTokens();
                if ((intermediateTokens != null) && (!intermediateTokens.isEmpty())) {
                    org.apache.ambari.server.api.predicate.Token lastToken = intermediateTokens.peek();
                    if (lastToken.getType() == org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND) {
                        intermediateTokens.pop();
                        tokenValue = lastToken.getValue() + token;
                    }
                }
                ctx.pushIntermediateToken(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, tokenValue));
            }
            if (ctx.getBracketScore() == 0) {
                ctx.addIntermediateTokens();
                ctx.addToken(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, ")"));
            }
        }

        @java.lang.Override
        public org.apache.ambari.server.api.predicate.Token.TYPE getType() {
            return org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND;
        }

        @java.lang.Override
        public boolean handles(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) {
            org.apache.ambari.server.api.predicate.Token.TYPE lastTokenType = ctx.getLastTokenType();
            if (lastTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC) {
                ctx.incrementBracketScore(1);
                return true;
            } else {
                return ctx.getBracketScore() > 0;
            }
        }

        @java.lang.Override
        public void validateEndState(org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            if (ctx.getBracketScore() > 0) {
                throw new org.apache.ambari.server.api.predicate.InvalidQueryException("Missing closing bracket for function: " + ctx.getTokenList());
            }
        }
    }

    private class LogicalOperatorTokenHandler extends org.apache.ambari.server.api.predicate.QueryLexer.TokenHandler {
        @java.lang.Override
        public void _handleToken(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            ctx.addToken(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, token));
        }

        @java.lang.Override
        public org.apache.ambari.server.api.predicate.Token.TYPE getType() {
            return org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR;
        }

        @java.lang.Override
        public boolean handles(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) {
            return token.matches("[!&\\|]");
        }
    }

    private class LogicalUnaryOperatorTokenHandler extends org.apache.ambari.server.api.predicate.QueryLexer.TokenHandler {
        @java.lang.Override
        public void _handleToken(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            ctx.addToken(new org.apache.ambari.server.api.predicate.Token(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_UNARY_OPERATOR, token));
        }

        @java.lang.Override
        public org.apache.ambari.server.api.predicate.Token.TYPE getType() {
            return org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_UNARY_OPERATOR;
        }

        @java.lang.Override
        public boolean handles(java.lang.String token, org.apache.ambari.server.api.predicate.QueryLexer.ScanContext ctx) {
            return "!".equals(token);
        }
    }
}