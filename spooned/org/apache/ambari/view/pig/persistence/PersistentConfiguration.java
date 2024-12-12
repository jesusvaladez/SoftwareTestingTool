package org.apache.ambari.view.pig.persistence;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
@java.lang.Deprecated
public class PersistentConfiguration extends org.apache.commons.configuration.PropertiesConfiguration {
    public PersistentConfiguration(java.lang.String fileName) throws org.apache.commons.configuration.ConfigurationException {
        super();
        java.io.File config = new java.io.File(fileName);
        setFile(config);
        this.setAutoSave(true);
        this.setReloadingStrategy(new org.apache.commons.configuration.reloading.FileChangedReloadingStrategy());
        this.setDelimiterParsingDisabled(true);
        this.setListDelimiter(((char) (0)));
        if (config.exists()) {
            this.load();
        }
    }
}