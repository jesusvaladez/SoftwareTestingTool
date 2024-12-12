package org.apache.ambari.server.stack;
import com.google.inject.persist.Transactional;
@org.apache.ambari.annotations.Experimental(feature = org.apache.ambari.annotations.ExperimentalFeature.CUSTOM_SERVICE_REPOS, comment = "Remove logic for handling custom service repos after enabling multi-mpack cluster deployment")
public class UpdateActiveRepoVersionOnStartup {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.stack.UpdateActiveRepoVersionOnStartup.class);

    org.apache.ambari.server.orm.dao.ClusterDAO clusterDao;

    org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDao;

    org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper repositoryVersionHelper;

    org.apache.ambari.server.stack.StackManager stackManager;

    @com.google.inject.Inject
    public UpdateActiveRepoVersionOnStartup(org.apache.ambari.server.orm.dao.ClusterDAO clusterDao, org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDao, org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper repositoryVersionHelper, org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo) {
        this.clusterDao = clusterDao;
        this.repositoryVersionDao = repositoryVersionDao;
        this.repositoryVersionHelper = repositoryVersionHelper;
        this.stackManager = metaInfo.getStackManager();
    }

    @com.google.inject.persist.Transactional
    public void process() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.stack.UpdateActiveRepoVersionOnStartup.LOG.info("Updating existing repo versions with service repos.");
        try {
            java.util.List<org.apache.ambari.server.orm.entities.ClusterEntity> clusters = clusterDao.findAll();
            for (org.apache.ambari.server.orm.entities.ClusterEntity cluster : clusters) {
                for (org.apache.ambari.server.orm.entities.ClusterServiceEntity service : cluster.getClusterServiceEntities()) {
                    org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = service.getServiceDesiredStateEntity().getDesiredRepositoryVersion();
                    org.apache.ambari.server.state.StackId stackId = repositoryVersion.getStackId();
                    org.apache.ambari.server.state.StackInfo stack = stackManager.getStack(stackId.getStackName(), stackId.getStackVersion());
                    if (stack != null) {
                        if (updateRepoVersion(stack, repositoryVersion)) {
                            repositoryVersionDao.merge(repositoryVersion);
                        }
                    } else {
                        throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Stack %s %s was not found on the file system. In the event that it was removed, " + "please ensure that it exists before starting Ambari Server.", stackId.getStackName(), stackId.getStackVersion()));
                    }
                }
            }
        } catch (java.lang.Exception ex) {
            throw new org.apache.ambari.server.AmbariException("An error occured during updating current repository versions with stack repositories.", ex);
        }
    }

    private boolean updateRepoVersion(org.apache.ambari.server.state.StackInfo stackInfo, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion) throws java.lang.Exception {
        com.google.common.collect.ListMultimap<java.lang.String, org.apache.ambari.server.state.RepositoryInfo> serviceReposByOs = stackInfo.getRepositoriesByOs();
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystems = repoVersion.getRepoOsEntities();
        boolean changed = org.apache.ambari.server.stack.RepoUtil.addServiceReposToOperatingSystemEntities(operatingSystems, serviceReposByOs);
        if (changed) {
            repoVersion.addRepoOsEntities(operatingSystems);
        }
        return changed;
    }
}