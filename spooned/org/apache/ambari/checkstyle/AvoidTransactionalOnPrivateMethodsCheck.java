package org.apache.ambari.checkstyle;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
public class AvoidTransactionalOnPrivateMethodsCheck extends com.puppycrawl.tools.checkstyle.api.AbstractCheck {
    private static final java.lang.String ANNOTATION_NAME = "Transactional";

    public static final java.lang.String MSG_TRANSACTIONAL_ON_PRIVATE_METHOD = ("@" + org.apache.ambari.checkstyle.AvoidTransactionalOnPrivateMethodsCheck.ANNOTATION_NAME) + " should not be used on private methods";

    private static final int[] TOKENS = new int[]{ com.puppycrawl.tools.checkstyle.api.TokenTypes.METHOD_DEF };

    @java.lang.Override
    public int[] getAcceptableTokens() {
        return org.apache.ambari.checkstyle.AvoidTransactionalOnPrivateMethodsCheck.TOKENS;
    }

    @java.lang.Override
    public int[] getDefaultTokens() {
        return org.apache.ambari.checkstyle.AvoidTransactionalOnPrivateMethodsCheck.TOKENS;
    }

    @java.lang.Override
    public int[] getRequiredTokens() {
        return org.apache.ambari.checkstyle.AvoidTransactionalOnPrivateMethodsCheck.TOKENS;
    }

    @java.lang.Override
    public void visitToken(com.puppycrawl.tools.checkstyle.api.DetailAST ast) {
        com.puppycrawl.tools.checkstyle.api.DetailAST modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);
        if (modifiers.findFirstToken(TokenTypes.LITERAL_PRIVATE) != null) {
            com.puppycrawl.tools.checkstyle.api.DetailAST annotation = modifiers.findFirstToken(TokenTypes.ANNOTATION);
            while (annotation != null) {
                com.puppycrawl.tools.checkstyle.api.DetailAST name = annotation.findFirstToken(TokenTypes.IDENT);
                if ((name != null) && org.apache.ambari.checkstyle.AvoidTransactionalOnPrivateMethodsCheck.ANNOTATION_NAME.equals(name.getText())) {
                    log(ast.getLineNo(), org.apache.ambari.checkstyle.AvoidTransactionalOnPrivateMethodsCheck.MSG_TRANSACTIONAL_ON_PRIVATE_METHOD);
                    break;
                }
                annotation = annotation.getNextSibling();
            } 
        }
    }
}