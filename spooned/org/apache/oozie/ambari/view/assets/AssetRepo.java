package org.apache.oozie.ambari.view.assets;
public class AssetRepo extends org.apache.oozie.ambari.view.repo.BaseRepo<org.apache.oozie.ambari.view.assets.model.ActionAsset> {
    public AssetRepo(org.apache.ambari.view.DataStore dataStore) {
        super(org.apache.oozie.ambari.view.assets.model.ActionAsset.class, dataStore);
    }

    public java.util.Collection<org.apache.oozie.ambari.view.assets.model.ActionAsset> getMyAsets(java.lang.String userName) {
        try {
            return dataStore.findAll(org.apache.oozie.ambari.view.assets.model.ActionAsset.class, (" owner='" + userName) + "'");
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public boolean assetNameAvailable(java.lang.String name) {
        try {
            java.util.Collection<org.apache.oozie.ambari.view.assets.model.ActionAsset> assets = dataStore.findAll(org.apache.oozie.ambari.view.assets.model.ActionAsset.class, (" name='" + name) + "'");
            boolean assetExists = (assets != null) && (!assets.isEmpty());
            return !assetExists;
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new java.lang.RuntimeException(e);
        }
    }
}