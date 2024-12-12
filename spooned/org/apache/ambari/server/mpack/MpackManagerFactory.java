package org.apache.ambari.server.mpack;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
public interface MpackManagerFactory {
    org.apache.ambari.server.mpack.MpackManager create(@com.google.inject.assistedinject.Assisted("mpacksv2Staging")
    java.io.File mpackStaging, @com.google.inject.assistedinject.Assisted("stackRoot")
    java.io.File stackRoot);
}