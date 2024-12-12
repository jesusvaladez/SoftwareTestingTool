package org.apache.oozie.ambari.view.assets;
public class AssetService {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.oozie.ambari.view.assets.AssetService.class);

    private final org.apache.oozie.ambari.view.assets.AssetRepo assetRepo;

    private final org.apache.oozie.ambari.view.assets.AssetDefinitionRepo assetDefinitionRepo;

    private final org.apache.ambari.view.ViewContext viewContext;

    public AssetService(org.apache.ambari.view.ViewContext viewContext) {
        super();
        this.viewContext = viewContext;
        this.assetDefinitionRepo = new org.apache.oozie.ambari.view.assets.AssetDefinitionRepo(viewContext.getDataStore());
        this.assetRepo = new org.apache.oozie.ambari.view.assets.AssetRepo(viewContext.getDataStore());
    }

    public java.util.Collection<org.apache.oozie.ambari.view.assets.model.ActionAsset> getAssets() {
        return assetRepo.findAll();
    }

    public java.util.Collection<org.apache.oozie.ambari.view.assets.model.ActionAsset> getPrioritizedAssets() {
        java.util.Collection<org.apache.oozie.ambari.view.assets.model.ActionAsset> assets = getAssets();
        return assets;
    }

    public void saveAsset(java.lang.String assetId, java.lang.String userName, org.apache.oozie.ambari.view.assets.model.AssetDefintion assetDefinition) {
        if (assetId != null) {
            org.apache.oozie.ambari.view.assets.model.ActionAsset actionAsset = assetRepo.findById(assetId);
            if (actionAsset == null) {
                throw new java.lang.RuntimeException("could not find asset with id :" + assetId);
            }
            actionAsset.setDescription(assetDefinition.getDescription());
            actionAsset.setName(assetDefinition.getName());
            actionAsset.setType(assetDefinition.getType());
            org.apache.oozie.ambari.view.assets.model.ActionAssetDefinition assetDefinintion = assetDefinitionRepo.findById(actionAsset.getDefinitionRef());
            assetDefinintion.setData(assetDefinintion.getData());
            assetDefinitionRepo.update(assetDefinintion);
            assetRepo.update(actionAsset);
        } else {
            org.apache.oozie.ambari.view.assets.model.ActionAsset actionAsset = new org.apache.oozie.ambari.view.assets.model.ActionAsset();
            actionAsset.setOwner(userName);
            org.apache.oozie.ambari.view.assets.model.ActionAssetDefinition definition = new org.apache.oozie.ambari.view.assets.model.ActionAssetDefinition();
            definition.setData(assetDefinition.getDefinition());
            org.apache.oozie.ambari.view.assets.model.ActionAssetDefinition createdDefinition = assetDefinitionRepo.create(definition);
            actionAsset.setDefinitionRef(createdDefinition.getId());
            actionAsset.setDescription(assetDefinition.getDescription());
            actionAsset.setName(assetDefinition.getName());
            actionAsset.setType(assetDefinition.getType());
            assetRepo.create(actionAsset);
        }
    }

    public void deleteAsset(java.lang.String id) {
        assetRepo.deleteById(id);
    }

    public org.apache.oozie.ambari.view.assets.model.AssetDefintion getAssetDetail(java.lang.String assetId) {
        org.apache.oozie.ambari.view.assets.model.AssetDefintion ad = new org.apache.oozie.ambari.view.assets.model.AssetDefintion();
        org.apache.oozie.ambari.view.assets.model.ActionAsset actionAsset = assetRepo.findById(assetId);
        org.apache.oozie.ambari.view.assets.model.ActionAssetDefinition actionDefinition = assetDefinitionRepo.findById(actionAsset.getDefinitionRef());
        ad.setDefinition(actionDefinition.getData());
        ad.setDescription(actionAsset.getDescription());
        ad.setName(actionAsset.getName());
        return ad;
    }

    public org.apache.oozie.ambari.view.assets.model.ActionAssetDefinition getAssetDefinition(java.lang.String assetDefintionId) {
        return assetDefinitionRepo.findById(assetDefintionId);
    }

    public org.apache.oozie.ambari.view.assets.model.ActionAsset getAsset(java.lang.String id) {
        return assetRepo.findById(id);
    }

    public java.util.Collection<org.apache.oozie.ambari.view.assets.model.ActionAsset> getMyAssets() {
        return assetRepo.getMyAsets(viewContext.getUsername());
    }

    public boolean isAssetNameAvailable(java.lang.String name) {
        return assetRepo.assetNameAvailable(name);
    }
}