package org.apache.ambari.view.pig.resources.scripts;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileAlreadyExistsException;
public class ScriptResourceManager extends org.apache.ambari.view.pig.resources.PersonalCRUDResourceManager<org.apache.ambari.view.pig.resources.scripts.models.PigScript> {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.view.pig.resources.scripts.ScriptResourceManager.class);

    public ScriptResourceManager(org.apache.ambari.view.ViewContext context) {
        super(org.apache.ambari.view.pig.resources.scripts.models.PigScript.class, context);
    }

    @java.lang.Override
    public org.apache.ambari.view.pig.resources.scripts.models.PigScript create(org.apache.ambari.view.pig.resources.scripts.models.PigScript object) {
        super.create(object);
        if ((object.getPigScript() == null) || object.getPigScript().isEmpty()) {
            createDefaultScriptFile(object);
        }
        return object;
    }

    private void createDefaultScriptFile(org.apache.ambari.view.pig.resources.scripts.models.PigScript object) {
        java.lang.String userScriptsPath = context.getProperties().get("scripts.dir");
        if (userScriptsPath == null) {
            java.lang.String msg = "scripts.dir is not configured!";
            org.apache.ambari.view.pig.resources.scripts.ScriptResourceManager.LOG.error(msg);
            throw new org.apache.ambari.view.pig.utils.MisconfigurationFormattedException("scripts.dir");
        }
        int checkId = 0;
        boolean fileCreated;
        java.lang.String newFilePath;
        do {
            java.lang.String normalizedName = object.getTitle().replaceAll("[^a-zA-Z0-9 ]+", "").replaceAll(" ", "_").toLowerCase();
            java.lang.String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd_hh-mm").format(new java.util.Date());
            newFilePath = java.lang.String.format(userScriptsPath + "/%s-%s%s.pig", normalizedName, timestamp, checkId == 0 ? "" : "_" + checkId);
            org.apache.ambari.view.pig.resources.scripts.ScriptResourceManager.LOG.debug("Trying to create new file " + newFilePath);
            try {
                org.apache.hadoop.fs.FSDataOutputStream stream = org.apache.ambari.view.pig.utils.UserLocalObjects.getHdfsApi(context).create(newFilePath, false);
                stream.close();
                fileCreated = true;
                org.apache.ambari.view.pig.resources.scripts.ScriptResourceManager.LOG.debug("File created successfully!");
            } catch (org.apache.hadoop.fs.FileAlreadyExistsException e) {
                fileCreated = false;
                org.apache.ambari.view.pig.resources.scripts.ScriptResourceManager.LOG.debug("File already exists. Trying next id");
            } catch (java.io.IOException e) {
                try {
                    delete(object.getId());
                } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
                    throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Error in creation, during clean up: " + itemNotFound.toString(), itemNotFound);
                }
                throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Error in creation: " + e.toString(), e);
            } catch (java.lang.InterruptedException e) {
                try {
                    delete(object.getId());
                } catch (org.apache.ambari.view.pig.persistence.utils.ItemNotFound itemNotFound) {
                    throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Error in creation, during clean up: " + itemNotFound.toString(), itemNotFound);
                }
                throw new org.apache.ambari.view.pig.utils.ServiceFormattedException("Error in creation: " + e.toString(), e);
            }
            checkId += 1;
        } while (!fileCreated );
        object.setPigScript(newFilePath);
        getPigStorage().store(object);
    }
}