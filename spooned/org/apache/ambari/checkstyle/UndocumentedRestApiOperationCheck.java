package org.apache.ambari.checkstyle;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtility;
public class UndocumentedRestApiOperationCheck extends com.puppycrawl.tools.checkstyle.api.AbstractCheck {
    private static final java.util.Set<java.lang.String> API_ANNOTATIONS = com.google.common.collect.ImmutableSet.of("DELETE", "GET", "HEAD", "OPTIONS", "PUT", "POST");

    private static final java.lang.String API_OPERATION = "ApiOperation";

    private static final java.lang.String API_IGNORE = "ApiIgnore";

    public static final java.lang.String MESSAGE = "REST API operation should be documented";

    private static final int[] TOKENS = new int[]{ com.puppycrawl.tools.checkstyle.api.TokenTypes.METHOD_DEF };

    @java.lang.Override
    public int[] getAcceptableTokens() {
        return org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.TOKENS;
    }

    @java.lang.Override
    public int[] getDefaultTokens() {
        return org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.TOKENS;
    }

    @java.lang.Override
    public int[] getRequiredTokens() {
        return org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.TOKENS;
    }

    @java.lang.Override
    public void visitToken(com.puppycrawl.tools.checkstyle.api.DetailAST ast) {
        if ((org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.isApiOperation(ast) && (!org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.isDocumentedApiOperation(ast))) && (!org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.isIgnoredApi(ast))) {
            log(ast.getLineNo(), org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.MESSAGE);
        }
    }

    private static boolean isApiOperation(com.puppycrawl.tools.checkstyle.api.DetailAST ast) {
        com.puppycrawl.tools.checkstyle.api.DetailAST modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);
        if (modifiers.findFirstToken(TokenTypes.LITERAL_PRIVATE) != null) {
            return false;
        }
        com.puppycrawl.tools.checkstyle.api.DetailAST annotation = modifiers.findFirstToken(TokenTypes.ANNOTATION);
        while (annotation != null) {
            com.puppycrawl.tools.checkstyle.api.DetailAST name = annotation.findFirstToken(TokenTypes.IDENT);
            if ((name != null) && org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.API_ANNOTATIONS.contains(name.getText())) {
                return true;
            }
            annotation = annotation.getNextSibling();
        } 
        return false;
    }

    private static boolean isDocumentedApiOperation(com.puppycrawl.tools.checkstyle.api.DetailAST ast) {
        return com.puppycrawl.tools.checkstyle.utils.AnnotationUtility.containsAnnotation(ast, org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.API_OPERATION);
    }

    private static boolean isIgnoredApi(com.puppycrawl.tools.checkstyle.api.DetailAST ast) {
        return com.puppycrawl.tools.checkstyle.utils.AnnotationUtility.containsAnnotation(ast, org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.API_IGNORE);
    }
}