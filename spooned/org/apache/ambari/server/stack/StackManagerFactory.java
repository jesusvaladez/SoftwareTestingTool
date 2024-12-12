package org.apache.ambari.server.stack;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javax.annotation.Nullable;
public interface StackManagerFactory {
    org.apache.ambari.server.stack.StackManager create(@com.google.inject.assistedinject.Assisted("stackRoot")
    java.io.File stackRoot, @javax.annotation.Nullable
    @com.google.inject.assistedinject.Assisted("commonServicesRoot")
    java.io.File commonServicesRoot, @com.google.inject.assistedinject.Assisted("extensionRoot")
    @javax.annotation.Nullable
    java.io.File extensionRoot, org.apache.ambari.server.state.stack.OsFamily osFamily, boolean validate);
}