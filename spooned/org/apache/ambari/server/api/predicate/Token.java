package org.apache.ambari.server.api.predicate;
public class Token {
    public enum TYPE {

        PROPERTY_OPERAND,
        VALUE_OPERAND,
        RELATIONAL_OPERATOR,
        RELATIONAL_OPERATOR_FUNC,
        LOGICAL_OPERATOR,
        LOGICAL_UNARY_OPERATOR,
        BRACKET_OPEN,
        BRACKET_CLOSE;}

    private org.apache.ambari.server.api.predicate.Token.TYPE m_type;

    private java.lang.String m_value;

    public Token(org.apache.ambari.server.api.predicate.Token.TYPE type, java.lang.String value) {
        m_type = type;
        m_value = value;
    }

    public org.apache.ambari.server.api.predicate.Token.TYPE getType() {
        return m_type;
    }

    public java.lang.String getValue() {
        return m_value;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("Token{ type=" + m_type) + ", value='") + m_value) + "' }";
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.api.predicate.Token token = ((org.apache.ambari.server.api.predicate.Token) (o));
        return (m_type == token.m_type) && (m_value == null ? token.m_value == null : m_value.equals(token.m_value));
    }

    @java.lang.Override
    public int hashCode() {
        int result = m_type.hashCode();
        result = (31 * result) + (m_value != null ? m_value.hashCode() : 0);
        return result;
    }
}