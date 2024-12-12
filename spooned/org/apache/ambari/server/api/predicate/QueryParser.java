package org.apache.ambari.server.api.predicate;
public class QueryParser {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.predicate.QueryParser.class);

    private static final java.util.Map<org.apache.ambari.server.api.predicate.Token.TYPE, org.apache.ambari.server.api.predicate.QueryParser.TokenHandler> TOKEN_HANDLERS = new java.util.HashMap<>();

    public QueryParser() {
        org.apache.ambari.server.api.predicate.QueryParser.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_OPEN, new org.apache.ambari.server.api.predicate.QueryParser.BracketOpenTokenHandler());
        org.apache.ambari.server.api.predicate.QueryParser.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE, new org.apache.ambari.server.api.predicate.QueryParser.BracketCloseTokenHandler());
        org.apache.ambari.server.api.predicate.QueryParser.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR, new org.apache.ambari.server.api.predicate.QueryParser.RelationalOperatorTokenHandler());
        org.apache.ambari.server.api.predicate.QueryParser.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR, new org.apache.ambari.server.api.predicate.QueryParser.LogicalOperatorTokenHandler());
        org.apache.ambari.server.api.predicate.QueryParser.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_UNARY_OPERATOR, new org.apache.ambari.server.api.predicate.QueryParser.LogicalUnaryOperatorTokenHandler());
        org.apache.ambari.server.api.predicate.QueryParser.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND, new org.apache.ambari.server.api.predicate.QueryParser.PropertyOperandTokenHandler());
        org.apache.ambari.server.api.predicate.QueryParser.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND, new org.apache.ambari.server.api.predicate.QueryParser.ValueOperandTokenHandler());
        org.apache.ambari.server.api.predicate.QueryParser.TOKEN_HANDLERS.put(org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC, new org.apache.ambari.server.api.predicate.QueryParser.RelationalOperatorFuncTokenHandler());
    }

    public org.apache.ambari.server.controller.spi.Predicate parse(org.apache.ambari.server.api.predicate.Token[] tokens) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        org.apache.ambari.server.api.predicate.QueryParser.ParseContext ctx = parseExpressions(tokens);
        java.util.List<org.apache.ambari.server.api.predicate.expressions.Expression> listExpressions = ctx.getExpressions();
        changeHostNameToLowerCase(listExpressions);
        java.util.List<org.apache.ambari.server.api.predicate.expressions.Expression> listMergedExpressions = mergeExpressions(listExpressions, ctx.getMaxPrecedence());
        return listMergedExpressions.isEmpty() ? null : listMergedExpressions.get(0).toPredicate();
    }

    private void changeHostNameToLowerCase(java.util.List<org.apache.ambari.server.api.predicate.expressions.Expression> listExpressions) {
        try {
            for (org.apache.ambari.server.api.predicate.expressions.Expression expression : listExpressions) {
                java.lang.Object keyObject = expression.getLeftOperand();
                if (keyObject != null) {
                    java.lang.String key = keyObject.toString();
                    if (key.endsWith("/host_name")) {
                        if (expression.getRightOperand() != null) {
                            expression.setRightOperand(expression.getRightOperand().toString().toLowerCase());
                        }
                    }
                }
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.api.predicate.QueryParser.LOG.error("Lowercase host_name value in expression failed with error:" + e);
        }
    }

    private org.apache.ambari.server.api.predicate.QueryParser.ParseContext parseExpressions(org.apache.ambari.server.api.predicate.Token[] tokens) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        org.apache.ambari.server.api.predicate.QueryParser.ParseContext ctx = new org.apache.ambari.server.api.predicate.QueryParser.ParseContext(tokens);
        while (ctx.getCurrentTokensIndex() < tokens.length) {
            org.apache.ambari.server.api.predicate.QueryParser.TOKEN_HANDLERS.get(tokens[ctx.getCurrentTokensIndex()].getType()).handleToken(ctx);
        } 
        if (ctx.getPrecedenceLevel() != 0) {
            throw new org.apache.ambari.server.api.predicate.InvalidQueryException("Invalid query string: mismatched parentheses.");
        }
        return ctx;
    }

    private java.util.List<org.apache.ambari.server.api.predicate.expressions.Expression> mergeExpressions(java.util.List<org.apache.ambari.server.api.predicate.expressions.Expression> listExpressions, int precedenceLevel) {
        if (listExpressions.size() > 1) {
            java.util.Stack<org.apache.ambari.server.api.predicate.expressions.Expression> stack = new java.util.Stack<>();
            stack.push(listExpressions.remove(0));
            while (!listExpressions.isEmpty()) {
                org.apache.ambari.server.api.predicate.expressions.Expression exp = stack.pop();
                org.apache.ambari.server.api.predicate.expressions.Expression left = (stack.empty()) ? null : stack.pop();
                org.apache.ambari.server.api.predicate.expressions.Expression right = listExpressions.remove(0);
                stack.addAll(exp.merge(left, right, precedenceLevel));
            } 
            return mergeExpressions(new java.util.ArrayList<>(stack), precedenceLevel - 1);
        }
        return listExpressions;
    }

    private class ParseContext {
        private int m_precedence = 0;

        private int m_tokensIdx = 0;

        private org.apache.ambari.server.api.predicate.Token[] m_tokens;

        private org.apache.ambari.server.api.predicate.Token.TYPE m_previousTokenType = null;

        private java.util.List<org.apache.ambari.server.api.predicate.expressions.Expression> m_listExpressions = new java.util.ArrayList<>();

        int m_maxPrecedence = 0;

        public ParseContext(org.apache.ambari.server.api.predicate.Token[] tokens) {
            m_tokens = tokens;
        }

        public org.apache.ambari.server.api.predicate.Token[] getTokens() {
            return m_tokens;
        }

        public int getCurrentTokensIndex() {
            return m_tokensIdx;
        }

        public void setCurrentTokensIndex(int idx) {
            m_tokensIdx = idx;
        }

        public void incPrecedenceLevel(int val) {
            m_precedence += val;
        }

        public void decPrecedenceLevel(int val) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            m_precedence -= val;
            if (m_precedence < 0) {
                throw new org.apache.ambari.server.api.predicate.InvalidQueryException("Invalid query string: mismatched parentheses.");
            }
        }

        public int getPrecedenceLevel() {
            return m_precedence;
        }

        public java.util.List<org.apache.ambari.server.api.predicate.expressions.Expression> getExpressions() {
            return m_listExpressions;
        }

        public org.apache.ambari.server.api.predicate.expressions.Expression getPrecedingExpression() {
            return m_listExpressions == null ? null : m_listExpressions.get(m_listExpressions.size() - 1);
        }

        public int getMaxPrecedence() {
            return m_maxPrecedence;
        }

        public void updateMaxPrecedence(int precedenceLevel) {
            if (precedenceLevel > m_maxPrecedence) {
                m_maxPrecedence = precedenceLevel;
            }
        }

        public void addExpression(org.apache.ambari.server.api.predicate.expressions.Expression exp) {
            m_listExpressions.add(exp);
        }

        private void setTokenType(org.apache.ambari.server.api.predicate.Token.TYPE type) {
            m_previousTokenType = type;
        }

        public org.apache.ambari.server.api.predicate.Token.TYPE getPreviousTokenType() {
            return m_previousTokenType;
        }
    }

    private abstract class TokenHandler {
        public void handleToken(org.apache.ambari.server.api.predicate.QueryParser.ParseContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            org.apache.ambari.server.api.predicate.Token token = ctx.getTokens()[ctx.getCurrentTokensIndex()];
            if (!validate(ctx.getPreviousTokenType())) {
                throw new org.apache.ambari.server.api.predicate.InvalidQueryException(((((("Unexpected token encountered in query string. Last Token Type=" + ctx.getPreviousTokenType()) + ", Current Token[type=") + token.getType()) + ", value='") + token.getValue()) + "']");
            }
            ctx.setTokenType(token.getType());
            int idxIncrement = _handleToken(ctx);
            ctx.setCurrentTokensIndex(ctx.getCurrentTokensIndex() + idxIncrement);
        }

        public abstract int _handleToken(org.apache.ambari.server.api.predicate.QueryParser.ParseContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException;

        public abstract boolean validate(org.apache.ambari.server.api.predicate.Token.TYPE previousTokenType);
    }

    private class BracketOpenTokenHandler extends org.apache.ambari.server.api.predicate.QueryParser.TokenHandler {
        @java.lang.Override
        public int _handleToken(org.apache.ambari.server.api.predicate.QueryParser.ParseContext ctx) {
            ctx.incPrecedenceLevel(org.apache.ambari.server.api.predicate.operators.Operator.MAX_OP_PRECEDENCE);
            return 1;
        }

        @java.lang.Override
        public boolean validate(org.apache.ambari.server.api.predicate.Token.TYPE previousTokenType) {
            return (((previousTokenType == null) || (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_OPEN)) || (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR)) || (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_UNARY_OPERATOR);
        }
    }

    private class BracketCloseTokenHandler extends org.apache.ambari.server.api.predicate.QueryParser.TokenHandler {
        @java.lang.Override
        public int _handleToken(org.apache.ambari.server.api.predicate.QueryParser.ParseContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            ctx.decPrecedenceLevel(org.apache.ambari.server.api.predicate.operators.Operator.MAX_OP_PRECEDENCE);
            return 1;
        }

        @java.lang.Override
        public boolean validate(org.apache.ambari.server.api.predicate.Token.TYPE previousTokenType) {
            return (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND) || (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE);
        }
    }

    private class RelationalOperatorTokenHandler extends org.apache.ambari.server.api.predicate.QueryParser.TokenHandler {
        @java.lang.Override
        public int _handleToken(org.apache.ambari.server.api.predicate.QueryParser.ParseContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            org.apache.ambari.server.api.predicate.Token token = ctx.getTokens()[ctx.getCurrentTokensIndex()];
            org.apache.ambari.server.api.predicate.operators.RelationalOperator relationalOp = org.apache.ambari.server.api.predicate.operators.RelationalOperatorFactory.createOperator(token.getValue());
            ctx.addExpression(new org.apache.ambari.server.api.predicate.expressions.RelationalExpression(relationalOp));
            return 1;
        }

        @java.lang.Override
        public boolean validate(org.apache.ambari.server.api.predicate.Token.TYPE previousTokenType) {
            return (((previousTokenType == null) || (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_OPEN)) || (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR)) || (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_UNARY_OPERATOR);
        }
    }

    private class RelationalOperatorFuncTokenHandler extends org.apache.ambari.server.api.predicate.QueryParser.TokenHandler {
        @java.lang.Override
        public int _handleToken(org.apache.ambari.server.api.predicate.QueryParser.ParseContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            org.apache.ambari.server.api.predicate.Token[] tokens = ctx.getTokens();
            int idx = ctx.getCurrentTokensIndex();
            org.apache.ambari.server.api.predicate.Token token = tokens[idx];
            org.apache.ambari.server.api.predicate.operators.RelationalOperator relationalOp = org.apache.ambari.server.api.predicate.operators.RelationalOperatorFactory.createOperator(token.getValue());
            ctx.addExpression(new org.apache.ambari.server.api.predicate.expressions.RelationalExpression(relationalOp));
            ctx.setCurrentTokensIndex(++idx);
            org.apache.ambari.server.api.predicate.QueryParser.TokenHandler propertyHandler = new org.apache.ambari.server.api.predicate.QueryParser.PropertyOperandTokenHandler();
            propertyHandler.handleToken(ctx);
            idx = ctx.getCurrentTokensIndex();
            if ((ctx.getCurrentTokensIndex() < tokens.length) && tokens[idx].getType().equals(org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND)) {
                org.apache.ambari.server.api.predicate.QueryParser.TokenHandler valueHandler = new org.apache.ambari.server.api.predicate.QueryParser.ValueOperandTokenHandler();
                valueHandler.handleToken(ctx);
            }
            idx = ctx.getCurrentTokensIndex();
            if ((idx >= tokens.length) || (tokens[idx].getType() != org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE)) {
                throw new org.apache.ambari.server.api.predicate.InvalidQueryException("Missing closing bracket for in expression.");
            }
            return 1;
        }

        @java.lang.Override
        public boolean validate(org.apache.ambari.server.api.predicate.Token.TYPE previousTokenType) {
            return (((previousTokenType == null) || (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_OPEN)) || (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR)) || (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_UNARY_OPERATOR);
        }
    }

    private class LogicalOperatorTokenHandler extends org.apache.ambari.server.api.predicate.QueryParser.TokenHandler {
        @java.lang.Override
        public int _handleToken(org.apache.ambari.server.api.predicate.QueryParser.ParseContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            org.apache.ambari.server.api.predicate.Token token = ctx.getTokens()[ctx.getCurrentTokensIndex()];
            org.apache.ambari.server.api.predicate.operators.LogicalOperator logicalOp = org.apache.ambari.server.api.predicate.operators.LogicalOperatorFactory.createOperator(token.getValue(), ctx.getPrecedenceLevel());
            ctx.updateMaxPrecedence(logicalOp.getPrecedence());
            ctx.addExpression(org.apache.ambari.server.api.predicate.expressions.LogicalExpressionFactory.createLogicalExpression(logicalOp));
            return 1;
        }

        @java.lang.Override
        public boolean validate(org.apache.ambari.server.api.predicate.Token.TYPE previousTokenType) {
            return (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.VALUE_OPERAND) || (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_CLOSE);
        }
    }

    private class LogicalUnaryOperatorTokenHandler extends org.apache.ambari.server.api.predicate.QueryParser.LogicalOperatorTokenHandler {
        @java.lang.Override
        public boolean validate(org.apache.ambari.server.api.predicate.Token.TYPE previousTokenType) {
            return ((previousTokenType == null) || (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.BRACKET_OPEN)) || (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.LOGICAL_OPERATOR);
        }
    }

    private class PropertyOperandTokenHandler extends org.apache.ambari.server.api.predicate.QueryParser.TokenHandler {
        @java.lang.Override
        public int _handleToken(org.apache.ambari.server.api.predicate.QueryParser.ParseContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            org.apache.ambari.server.api.predicate.Token token = ctx.getTokens()[ctx.getCurrentTokensIndex()];
            ctx.getPrecedingExpression().setLeftOperand(token.getValue());
            return 1;
        }

        @java.lang.Override
        public boolean validate(org.apache.ambari.server.api.predicate.Token.TYPE previousTokenType) {
            return (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR) || (previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.RELATIONAL_OPERATOR_FUNC);
        }
    }

    private class ValueOperandTokenHandler extends org.apache.ambari.server.api.predicate.QueryParser.TokenHandler {
        @java.lang.Override
        public int _handleToken(org.apache.ambari.server.api.predicate.QueryParser.ParseContext ctx) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
            org.apache.ambari.server.api.predicate.Token token = ctx.getTokens()[ctx.getCurrentTokensIndex()];
            if (ctx.getPrecedingExpression() != null) {
                ctx.getPrecedingExpression().setRightOperand(token.getValue());
            }
            return 1;
        }

        @java.lang.Override
        public boolean validate(org.apache.ambari.server.api.predicate.Token.TYPE previousTokenType) {
            return previousTokenType == org.apache.ambari.server.api.predicate.Token.TYPE.PROPERTY_OPERAND;
        }
    }
}