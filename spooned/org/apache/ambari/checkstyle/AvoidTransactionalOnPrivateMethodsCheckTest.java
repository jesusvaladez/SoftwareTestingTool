package org.apache.ambari.checkstyle;
import com.puppycrawl.tools.checkstyle.AbstractModuleTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import static org.apache.ambari.checkstyle.AvoidTransactionalOnPrivateMethodsCheck.MSG_TRANSACTIONAL_ON_PRIVATE_METHOD;
public class AvoidTransactionalOnPrivateMethodsCheckTest extends com.puppycrawl.tools.checkstyle.AbstractModuleTestSupport {
    @java.lang.Override
    protected java.lang.String getPackageLocation() {
        return "org/apache/ambari/checkstyle";
    }

    @org.junit.Test
    public void transactionalOnPrivateMethod() throws java.lang.Exception {
        final com.puppycrawl.tools.checkstyle.DefaultConfiguration config = createModuleConfig(org.apache.ambari.checkstyle.AvoidTransactionalOnPrivateMethodsCheck.class);
        final java.lang.String[] expected = new java.lang.String[]{ "32: " + org.apache.ambari.checkstyle.AvoidTransactionalOnPrivateMethodsCheck.MSG_TRANSACTIONAL_ON_PRIVATE_METHOD, "41: " + org.apache.ambari.checkstyle.AvoidTransactionalOnPrivateMethodsCheck.MSG_TRANSACTIONAL_ON_PRIVATE_METHOD };
        verify(config, getPath("InputTransactionalOnPrivateMethods.java"), expected);
    }
}