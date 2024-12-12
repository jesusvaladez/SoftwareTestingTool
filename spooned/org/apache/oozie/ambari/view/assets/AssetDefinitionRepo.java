package org.apache.oozie.ambari.view.assets;
public class AssetDefinitionRepo extends org.apache.oozie.ambari.view.repo.BaseRepo<org.apache.oozie.ambari.view.assets.model.ActionAssetDefinition> {
    public AssetDefinitionRepo(org.apache.ambari.view.DataStore dataStore) {
        super(org.apache.oozie.ambari.view.assets.model.ActionAssetDefinition.class, dataStore);
    }
}