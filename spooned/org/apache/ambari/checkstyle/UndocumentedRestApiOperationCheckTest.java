package org.apache.ambari.checkstyle;
import com.puppycrawl.tools.checkstyle.AbstractModuleTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import static org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.MESSAGE;
public class UndocumentedRestApiOperationCheckTest extends com.puppycrawl.tools.checkstyle.AbstractModuleTestSupport {
    @java.lang.Override
    protected java.lang.String getPackageLocation() {
        return "org/apache/ambari/checkstyle";
    }

    @org.junit.Test
    @org.junit.Ignore("Fix later: NoSuchField: WHITESPACE error exception")
    public void test() throws java.lang.Exception {
        final com.puppycrawl.tools.checkstyle.DefaultConfiguration config = createModuleConfig(org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.class);
        final java.lang.String[] expected = new java.lang.String[]{ "36: " + org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.MESSAGE, "53: " + org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.MESSAGE, "70: " + org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.MESSAGE, "87: " + org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.MESSAGE, "104: " + org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.MESSAGE, "121: " + org.apache.ambari.checkstyle.UndocumentedRestApiOperationCheck.MESSAGE };
        verify(config, getPath("InputRestApiOperation.java"), expected);
    }
}