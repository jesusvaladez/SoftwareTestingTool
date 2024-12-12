package org.apache.ambari.server.mpack;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
public class MpackManager {
    protected java.util.Map<java.lang.Long, org.apache.ambari.server.state.Mpack> mpackMap = new java.util.HashMap<>();

    private java.io.File mpackStaging;

    private org.apache.ambari.server.orm.dao.MpackDAO mpackDAO;

    private org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    private org.apache.ambari.server.state.Mpack mpack;

    private java.io.File stackRoot;

    private static final java.lang.String MPACK_METADATA = "mpack.json";

    private static final java.lang.String MPACK_TAR_LOCATION = "staging";

    private static final java.lang.String MODULES_DIRECTORY = "services";

    private static final java.lang.String MIN_JDK_PROPERTY = "min-jdk";

    private static final java.lang.String MAX_JDK_PROPERTY = "max-jdk";

    private static final java.lang.String DEFAULT_JDK_VALUE = "1.8";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.mpack.MpackManager.class);

    @com.google.inject.assistedinject.AssistedInject
    public MpackManager(@com.google.inject.assistedinject.Assisted("mpacksv2Staging")
    java.io.File mpacksStagingLocation, @com.google.inject.assistedinject.Assisted("stackRoot")
    java.io.File stackRootDir, org.apache.ambari.server.orm.dao.MpackDAO mpackDAOObj, org.apache.ambari.server.orm.dao.StackDAO stackDAOObj) {
        mpackStaging = mpacksStagingLocation;
        mpackDAO = mpackDAOObj;
        stackRoot = stackRootDir;
        stackDAO = stackDAOObj;
        parseMpackDirectories();
    }

    private void parseMpackDirectories() {
        try {
            for (final java.io.File dirEntry : mpackStaging.listFiles()) {
                if (dirEntry.isDirectory()) {
                    java.lang.String mpackName = dirEntry.getName();
                    org.apache.ambari.server.mpack.MpackManager.LOG.info("Reading mpack :" + mpackName);
                    if (!mpackName.equals(org.apache.ambari.server.mpack.MpackManager.MPACK_TAR_LOCATION)) {
                        for (final java.io.File file : dirEntry.listFiles()) {
                            if (file.isDirectory()) {
                                java.lang.String mpackVersion = file.getName();
                                java.util.List resultSet = mpackDAO.findByNameVersion(mpackName, mpackVersion);
                                if (resultSet.size() > 0) {
                                    org.apache.ambari.server.orm.entities.MpackEntity mpackEntity = ((org.apache.ambari.server.orm.entities.MpackEntity) (resultSet.get(0)));
                                    java.lang.String mpackJsonContents = new java.lang.String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get((file + "/") + org.apache.ambari.server.mpack.MpackManager.MPACK_METADATA)), "UTF-8");
                                    com.google.gson.Gson gson = new com.google.gson.Gson();
                                    org.apache.ambari.server.state.Mpack existingMpack = gson.fromJson(mpackJsonContents, org.apache.ambari.server.state.Mpack.class);
                                    existingMpack.setResourceId(mpackEntity.getId());
                                    existingMpack.setMpackUri(mpackEntity.getMpackUri());
                                    existingMpack.setRegistryId(mpackEntity.getRegistryId());
                                    mpackMap.put(mpackEntity.getId(), existingMpack);
                                }
                            }
                        }
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public java.util.Map<java.lang.Long, org.apache.ambari.server.state.Mpack> getMpackMap() {
        return mpackMap;
    }

    public void setMpackMap(java.util.Map<java.lang.Long, org.apache.ambari.server.state.Mpack> mpackMap) {
        this.mpackMap = mpackMap;
    }

    public org.apache.ambari.server.controller.MpackResponse registerMpack(org.apache.ambari.server.controller.MpackRequest mpackRequest) throws java.io.IOException, java.lang.IllegalArgumentException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException {
        java.lang.Long mpackResourceId;
        java.lang.String mpackName = "";
        java.lang.String mpackVersion = "";
        mpack = new org.apache.ambari.server.state.Mpack();
        boolean isValidMetadata;
        java.lang.String mpackDirectory = "";
        java.nio.file.Path mpackTarPath;
        if (mpackRequest.getRegistryId() != null) {
            mpackName = mpackRequest.getMpackName();
            mpackVersion = mpackRequest.getMpackVersion();
            org.apache.ambari.server.mpack.MpackManager.LOG.info("Mpack Registration via Registry :" + mpackName);
            mpack = downloadMpackMetadata(mpackRequest.getMpackUri());
            mpack.setRegistryId(mpackRequest.getRegistryId());
            isValidMetadata = validateMpackInfo(mpackName, mpackVersion, mpack.getName(), mpack.getVersion());
            if (isValidMetadata) {
                mpackTarPath = downloadMpack(mpackRequest.getMpackUri(), mpack.getDefinition());
                createMpackDirectory(mpack);
                mpackDirectory = (((mpackStaging + java.io.File.separator) + mpack.getName()) + java.io.File.separator) + mpack.getVersion();
            } else {
                java.lang.String message = ((((((("Incorrect information : Mismatch in - (" + mpackName) + ",") + mpack.getName()) + ") or (") + mpackVersion) + ",") + mpack.getVersion()) + ")";
                throw new java.lang.IllegalArgumentException(message);
            }
        } else {
            mpack = downloadMpackMetadata(mpackRequest.getMpackUri());
            mpackTarPath = downloadMpack(mpackRequest.getMpackUri(), mpack.getDefinition());
            org.apache.ambari.server.mpack.MpackManager.LOG.info("Custom Mpack Registration :" + mpackRequest.getMpackUri());
            if (createMpackDirectory(mpack)) {
                mpackDirectory = (((mpackStaging + java.io.File.separator) + mpack.getName()) + java.io.File.separator) + mpack.getVersion();
            }
        }
        extractMpackTar(mpack, mpackTarPath, mpackDirectory);
        mpack.setMpackUri(mpackRequest.getMpackUri());
        mpackResourceId = populateDB(mpack);
        if (mpackResourceId != null) {
            mpackMap.put(mpackResourceId, mpack);
            mpack.setResourceId(mpackResourceId);
            populateStackDB(mpack);
            return new org.apache.ambari.server.controller.MpackResponse(mpack);
        }
        java.lang.String message = ((("Mpack :" + mpackRequest.getMpackName()) + " version: ") + mpackRequest.getMpackVersion()) + " already exists in server";
        throw new org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException(message);
    }

    private org.apache.ambari.server.state.Mpack downloadMpackMetadata(java.lang.String mpackURI) throws java.io.IOException {
        java.net.URL url = new java.net.URL(mpackURI);
        java.io.File stagingDir = new java.io.File((mpackStaging.toString() + java.io.File.separator) + org.apache.ambari.server.mpack.MpackManager.MPACK_TAR_LOCATION);
        java.nio.file.Path targetPath = new java.io.File((stagingDir.getPath() + java.io.File.separator) + org.apache.ambari.server.mpack.MpackManager.MPACK_METADATA).toPath();
        org.apache.ambari.server.mpack.MpackManager.LOG.debug("Download mpack.json and store in :" + targetPath);
        if (!stagingDir.exists()) {
            stagingDir.mkdirs();
        }
        java.nio.file.Files.copy(url.openStream(), targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        org.apache.ambari.server.state.Mpack mpack = gson.fromJson(new java.io.FileReader(targetPath.toString()), org.apache.ambari.server.state.Mpack.class);
        return mpack;
    }

    private void extractTar(java.nio.file.Path tarPath, java.io.File untarDirectory) throws java.io.IOException {
        org.apache.commons.compress.archivers.tar.TarArchiveInputStream tarFile = new org.apache.commons.compress.archivers.tar.TarArchiveInputStream(new org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(new java.io.File(java.lang.String.valueOf(tarPath))))));
        org.apache.commons.compress.archivers.tar.TarArchiveEntry entry = null;
        java.io.File outputFile = null;
        org.apache.ambari.server.mpack.MpackManager.LOG.debug("Extracting tar file :" + tarFile);
        while ((entry = tarFile.getNextTarEntry()) != null) {
            outputFile = new java.io.File(untarDirectory, entry.getName());
            if (entry.isDirectory()) {
                if (!outputFile.exists()) {
                    org.apache.ambari.server.mpack.MpackManager.LOG.debug("Creating output directory " + outputFile.getAbsolutePath());
                    if (!outputFile.mkdirs()) {
                        throw new java.lang.IllegalStateException("Couldn't create directory " + outputFile.getAbsolutePath());
                    }
                }
            } else {
                java.io.File parentDir = outputFile.getParentFile();
                if (!parentDir.exists()) {
                    org.apache.ambari.server.mpack.MpackManager.LOG.debug("Attempting to create output directory " + parentDir.getAbsolutePath());
                    if (!parentDir.mkdirs()) {
                        throw new java.lang.IllegalStateException("Couldn't create directory " + parentDir.getAbsolutePath());
                    }
                }
                org.apache.ambari.server.mpack.MpackManager.LOG.debug("Creating output file " + outputFile.getAbsolutePath());
                final java.io.OutputStream outputFileStream = new java.io.FileOutputStream(outputFile);
                org.apache.commons.io.IOUtils.copy(tarFile, outputFileStream);
                outputFileStream.close();
            }
        } 
        tarFile.close();
    }

    private void extractMpackTar(org.apache.ambari.server.state.Mpack mpack, java.nio.file.Path mpackTarPath, java.lang.String mpackDirectory) throws java.io.IOException {
        extractTar(mpackTarPath, mpackStaging);
        java.lang.String mpackTarDirectory = mpackTarPath.toString();
        java.nio.file.Path extractedMpackDirectory = java.nio.file.Files.move(java.nio.file.Paths.get(((mpackStaging + java.io.File.separator) + mpackTarDirectory.substring(mpackTarDirectory.lastIndexOf('/') + 1, mpackTarDirectory.indexOf(".tar"))) + java.io.File.separator), java.nio.file.Paths.get(mpackDirectory), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        org.apache.ambari.server.mpack.MpackManager.LOG.debug("Extracting Mpack definitions into :" + extractedMpackDirectory);
        createServicesDirectory(extractedMpackDirectory, mpack);
        java.io.File metainfoFile = new java.io.File((extractedMpackDirectory + java.io.File.separator) + "metainfo.xml");
        if (!metainfoFile.exists()) {
            generateMetainfo(metainfoFile, mpack);
        }
        createSymLinks(mpack);
    }

    private void generateMetainfo(java.io.File metainfoFile, org.apache.ambari.server.state.Mpack mpack) throws java.io.IOException {
        org.apache.ambari.server.mpack.MpackManager.LOG.info("Generating {} for mpack {}", metainfoFile, mpack.getName());
        org.apache.ambari.server.state.stack.StackMetainfoXml generatedMetainfo = new org.apache.ambari.server.state.stack.StackMetainfoXml();
        org.apache.ambari.server.state.stack.StackMetainfoXml.Version version = new org.apache.ambari.server.state.stack.StackMetainfoXml.Version();
        version.setActive(true);
        generatedMetainfo.setVersion(version);
        java.util.Map<java.lang.String, java.lang.String> prerequisites = mpack.getPrerequisites();
        if ((prerequisites != null) && prerequisites.containsKey(org.apache.ambari.server.mpack.MpackManager.MIN_JDK_PROPERTY)) {
            generatedMetainfo.setMinJdk(mpack.getPrerequisites().get(org.apache.ambari.server.mpack.MpackManager.MIN_JDK_PROPERTY));
        } else {
            org.apache.ambari.server.mpack.MpackManager.LOG.warn("Couldn't detect {} for mpack {}. Using default value {}", org.apache.ambari.server.mpack.MpackManager.MIN_JDK_PROPERTY, mpack.getName(), org.apache.ambari.server.mpack.MpackManager.DEFAULT_JDK_VALUE);
            generatedMetainfo.setMinJdk(org.apache.ambari.server.mpack.MpackManager.DEFAULT_JDK_VALUE);
        }
        if ((prerequisites != null) && prerequisites.containsKey(org.apache.ambari.server.mpack.MpackManager.MAX_JDK_PROPERTY)) {
            generatedMetainfo.setMaxJdk(mpack.getPrerequisites().get(org.apache.ambari.server.mpack.MpackManager.MAX_JDK_PROPERTY));
        } else {
            org.apache.ambari.server.mpack.MpackManager.LOG.warn("Couldn't detect {} for mpack {}. Using default value {}", org.apache.ambari.server.mpack.MpackManager.MAX_JDK_PROPERTY, mpack.getName(), org.apache.ambari.server.mpack.MpackManager.DEFAULT_JDK_VALUE);
            generatedMetainfo.setMaxJdk(org.apache.ambari.server.mpack.MpackManager.DEFAULT_JDK_VALUE);
        }
        try {
            javax.xml.bind.JAXBContext ctx = javax.xml.bind.JAXBContext.newInstance(org.apache.ambari.server.state.stack.StackMetainfoXml.class);
            javax.xml.bind.Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, java.lang.Boolean.TRUE);
            java.io.FileOutputStream stackMetainfoFileStream = new java.io.FileOutputStream(metainfoFile);
            marshaller.marshal(generatedMetainfo, stackMetainfoFileStream);
            stackMetainfoFileStream.flush();
            stackMetainfoFileStream.close();
        } catch (javax.xml.bind.JAXBException e) {
            e.printStackTrace();
        }
    }

    private void createServicesDirectory(java.nio.file.Path extractedMpackDirectory, org.apache.ambari.server.state.Mpack mpack) throws java.io.IOException {
        java.io.File servicesDir = new java.io.File((extractedMpackDirectory.toAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.mpack.MpackManager.MODULES_DIRECTORY);
        if (!servicesDir.exists()) {
            servicesDir.mkdirs();
        }
        java.util.List<org.apache.ambari.server.state.Module> modules = mpack.getModules();
        org.apache.ambari.server.mpack.MpackManager.LOG.info("Creating services directory for mpack :" + mpack.getName());
        for (org.apache.ambari.server.state.Module module : modules) {
            java.lang.String moduleDefinitionLocation = module.getDefinition();
            java.io.File serviceTargetDir = new java.io.File((servicesDir + java.io.File.separator) + module.getName());
            extractTar(java.nio.file.Paths.get((((extractedMpackDirectory + java.io.File.separator) + "modules") + java.io.File.separator) + moduleDefinitionLocation), servicesDir);
            java.nio.file.Path extractedServiceDirectory = java.nio.file.Files.move(java.nio.file.Paths.get((servicesDir + java.io.File.separator) + moduleDefinitionLocation.substring(moduleDefinitionLocation.indexOf("/") + 1, moduleDefinitionLocation.indexOf(".tar.gz"))), serviceTargetDir.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private java.lang.Boolean createMpackDirectory(org.apache.ambari.server.state.Mpack mpack) throws java.io.IOException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException {
        java.util.List<org.apache.ambari.server.orm.entities.MpackEntity> mpackEntities = mpackDAO.findByNameVersion(mpack.getName(), mpack.getVersion());
        if (mpackEntities.size() == 0) {
            java.io.File mpackDirectory = new java.io.File((mpackStaging + java.io.File.separator) + mpack.getName());
            if (!mpackDirectory.exists()) {
                mpackDirectory.mkdirs();
            }
            return true;
        } else {
            java.lang.String message = ((("Mpack: " + mpack.getName()) + " version: ") + mpack.getVersion()) + " already exists in server";
            throw new org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException(message);
        }
    }

    private void createSymLinks(org.apache.ambari.server.state.Mpack mpack) throws java.io.IOException {
        java.lang.String stackName = mpack.getName();
        java.lang.String stackVersion = mpack.getVersion();
        java.io.File stack = new java.io.File((stackRoot + "/") + stackName);
        java.nio.file.Path stackPath = java.nio.file.Paths.get((((stackRoot + "/") + stackName) + "/") + stackVersion);
        java.nio.file.Path mpackPath = java.nio.file.Paths.get((((mpackStaging + "/") + mpack.getName()) + "/") + mpack.getVersion());
        if (java.nio.file.Files.isSymbolicLink(stackPath))
            java.nio.file.Files.delete(stackPath);

        java.nio.file.Files.createSymbolicLink(stackPath, mpackPath);
    }

    public java.nio.file.Path downloadMpack(java.lang.String mpackURI, java.lang.String mpackDefinitionLocation) throws java.io.IOException {
        java.io.File stagingDir = new java.io.File((mpackStaging.toString() + java.io.File.separator) + org.apache.ambari.server.mpack.MpackManager.MPACK_TAR_LOCATION);
        java.nio.file.Path targetPath = new java.io.File((stagingDir.getPath() + java.io.File.separator) + mpackDefinitionLocation).toPath();
        java.lang.String mpackTarURI = (mpackURI.substring(0, mpackURI.lastIndexOf('/')) + java.io.File.separator) + mpackDefinitionLocation;
        java.net.URL url = new java.net.URL(mpackTarURI);
        if (!stagingDir.exists()) {
            stagingDir.mkdirs();
        }
        java.nio.file.Files.copy(url.openStream(), targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        return targetPath;
    }

    protected boolean validateMpackInfo(java.lang.String expectedMpackName, java.lang.String expectedMpackVersion, java.lang.String actualMpackName, java.lang.String actualMpackVersion) {
        if (expectedMpackName.equalsIgnoreCase(actualMpackName) && expectedMpackVersion.equalsIgnoreCase(actualMpackVersion)) {
            return true;
        } else {
            org.apache.ambari.server.mpack.MpackManager.LOG.info(((((((("Incorrect information : Mismatch in - (" + expectedMpackName) + ",") + actualMpackName) + ") or (") + expectedMpackVersion) + ",") + actualMpackVersion) + ")");
            return false;
        }
    }

    protected java.lang.Long populateDB(org.apache.ambari.server.state.Mpack mpacks) throws java.io.IOException {
        java.lang.String mpackName = mpacks.getName();
        java.lang.String mpackVersion = mpacks.getVersion();
        java.util.List resultSet = mpackDAO.findByNameVersion(mpackName, mpackVersion);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(mpackName, mpackVersion);
        if ((resultSet.size() == 0) && (stackEntity == null)) {
            org.apache.ambari.server.mpack.MpackManager.LOG.info("Adding mpack {}-{} to the database", mpackName, mpackVersion);
            org.apache.ambari.server.orm.entities.MpackEntity mpackEntity = new org.apache.ambari.server.orm.entities.MpackEntity();
            mpackEntity.setMpackName(mpackName);
            mpackEntity.setMpackVersion(mpackVersion);
            mpackEntity.setMpackUri(mpack.getMpackUri());
            mpackEntity.setRegistryId(mpack.getRegistryId());
            java.lang.Long mpackId = mpackDAO.create(mpackEntity);
            return mpackId;
        }
        return null;
    }

    protected void populateStackDB(org.apache.ambari.server.state.Mpack mpack) throws java.io.IOException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException {
        java.lang.String stackName = mpack.getName();
        java.lang.String stackVersion = mpack.getVersion();
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(stackName, stackVersion);
        if (stackEntity == null) {
            org.apache.ambari.server.mpack.MpackManager.LOG.info("Adding stack {}-{} to the database", stackName, stackVersion);
            stackEntity = new org.apache.ambari.server.orm.entities.StackEntity();
            stackEntity.setStackName(stackName);
            stackEntity.setStackVersion(stackVersion);
            stackEntity.setMpackId(mpack.getResourceId());
            stackDAO.create(stackEntity);
        } else {
            java.lang.String message = ((("Stack " + stackName) + "-") + stackVersion) + " already exists";
            org.apache.ambari.server.mpack.MpackManager.LOG.error(message);
            throw new org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException(message);
        }
    }

    public java.util.List<org.apache.ambari.server.state.Module> getModules(java.lang.Long mpackId) {
        org.apache.ambari.server.state.Mpack mpack = mpackMap.get(mpackId);
        return mpack.getModules();
    }

    public boolean removeMpack(org.apache.ambari.server.orm.entities.MpackEntity mpackEntity, org.apache.ambari.server.orm.entities.StackEntity stackEntity) throws java.io.IOException {
        boolean stackDelete = false;
        java.io.File mpackDirToDelete = new java.io.File((((mpackStaging + java.io.File.separator) + mpackEntity.getMpackName()) + java.io.File.separator) + mpackEntity.getMpackVersion());
        java.io.File mpackDirectory = new java.io.File((mpackStaging + "/") + mpackEntity.getMpackName());
        java.lang.String mpackName = ((mpackEntity.getMpackName() + "-") + mpackEntity.getMpackVersion()) + ".tar.gz";
        java.nio.file.Path mpackTarFile = java.nio.file.Paths.get((((mpackStaging + java.io.File.separator) + org.apache.ambari.server.mpack.MpackManager.MPACK_TAR_LOCATION) + java.io.File.separator) + mpackName);
        org.apache.ambari.server.mpack.MpackManager.LOG.info("Removing mpack :" + mpackName);
        mpackMap.remove(mpackEntity.getId());
        org.apache.commons.io.FileUtils.deleteDirectory(mpackDirToDelete);
        if (mpackDirectory.isDirectory()) {
            if (mpackDirectory.list().length == 0) {
                java.nio.file.Files.delete(mpackDirectory.toPath());
            }
        }
        if (stackEntity != null) {
            java.nio.file.Path stackPath = java.nio.file.Paths.get((((stackRoot + "/") + stackEntity.getStackName()) + "/") + stackEntity.getStackVersion());
            java.io.File stackDirectory = new java.io.File((stackRoot + "/") + stackEntity.getStackName());
            if (!java.nio.file.Files.exists(stackPath))
                java.nio.file.Files.delete(stackPath);

            if (stackDirectory.isDirectory()) {
                if (stackDirectory.list().length == 0) {
                    java.nio.file.Files.delete(stackDirectory.toPath());
                }
            }
            stackDelete = true;
        }
        if (java.nio.file.Files.exists(mpackTarFile)) {
            java.nio.file.Files.delete(mpackTarFile);
        }
        return stackDelete;
    }
}