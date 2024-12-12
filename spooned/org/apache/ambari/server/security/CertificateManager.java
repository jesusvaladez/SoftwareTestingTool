package org.apache.ambari.server.security;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
public class CertificateManager {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.CertificateManager.class);

    @com.google.inject.Inject
    org.apache.ambari.server.configuration.Configuration configs;

    private static final java.lang.String GEN_SRVR_KEY = (("openssl genrsa -des3 " + "-passout pass:{0} -out {1}") + java.io.File.separator) + "{2} 4096 ";

    private static final java.lang.String GEN_SRVR_REQ = (((("openssl req -passin pass:{0} " + "-new -key {1}") + java.io.File.separator) + "{2} -out {1}") + java.io.File.separator) + "{5} -batch";

    private static final java.lang.String SIGN_SRVR_CRT = (((((((((("openssl ca -create_serial " + "-out {1}") + java.io.File.separator) + "{3} -days 365 -keyfile {1}") + java.io.File.separator) + "{2} -key {0} -selfsign ") + "-extensions jdk7_ca -config {1}") + java.io.File.separator) + "ca.config -batch ") + "-infiles {1}") + java.io.File.separator) + "{5}";

    private static final java.lang.String EXPRT_KSTR = ((((((((("openssl pkcs12 -export" + " -in {1}") + java.io.File.separator) + "{3} -inkey {1}") + java.io.File.separator) + "{2} -certfile {1}") + java.io.File.separator) + "{3} -out {1}") + java.io.File.separator) + "{4} ") + "-password pass:{0} -passin pass:{0} \n";

    private static final java.lang.String REVOKE_AGENT_CRT = ((((((((("openssl ca " + "-config {0}") + java.io.File.separator) + "ca.config -keyfile {0}") + java.io.File.separator) + "{4} -revoke {0}") + java.io.File.separator) + "{2} -batch ") + "-passin pass:{3} -cert {0}") + java.io.File.separator) + "{5}";

    private static final java.lang.String SIGN_AGENT_CRT = ((((((((((("openssl ca -config " + "{0}") + java.io.File.separator) + "ca.config -in {0}") + java.io.File.separator) + "{1} -out {0}") + java.io.File.separator) + "{2} -batch -passin pass:{3} ") + "-keyfile {0}") + java.io.File.separator) + "{4} -cert {0}") + java.io.File.separator) + "{5}";

    private static final java.lang.String SET_PERMISSIONS = "find %s -type f -exec chmod 700 {} +";

    private static final java.lang.String SET_SERVER_PASS_FILE_PERMISSIONS = "chmod 600 %s";

    public void initRootCert() {
        org.apache.ambari.server.security.CertificateManager.LOG.info("Initialization of root certificate");
        boolean certExists = isCertExists();
        org.apache.ambari.server.security.CertificateManager.LOG.info("Certificate exists:" + certExists);
        if (!certExists) {
            generateServerCertificate();
        }
    }

    private boolean isCertExists() {
        java.util.Map<java.lang.String, java.lang.String> configsMap = configs.getConfigsMap();
        java.lang.String srvrKstrDir = configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey());
        java.lang.String srvrCrtName = configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_NAME.getKey());
        java.io.File certFile = new java.io.File((srvrKstrDir + java.io.File.separator) + srvrCrtName);
        org.apache.ambari.server.security.CertificateManager.LOG.debug("srvrKstrDir = {}", srvrKstrDir);
        org.apache.ambari.server.security.CertificateManager.LOG.debug("srvrCrtName = {}", srvrCrtName);
        org.apache.ambari.server.security.CertificateManager.LOG.debug("certFile = {}", certFile.getAbsolutePath());
        return certFile.exists();
    }

    protected int runCommand(java.lang.String command) {
        java.lang.String line = null;
        java.lang.Process process = null;
        java.io.BufferedReader br = null;
        try {
            process = java.lang.Runtime.getRuntime().exec(command);
            br = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream(), java.nio.charset.Charset.forName("UTF8")));
            while ((line = br.readLine()) != null) {
                org.apache.ambari.server.security.CertificateManager.LOG.info(line);
            } 
            try {
                process.waitFor();
                org.apache.ambari.server.utils.ShellCommandUtil.logOpenSslExitCode(command, process.exitValue());
                return process.exitValue();
            } catch (java.lang.InterruptedException e) {
                e.printStackTrace();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (java.io.IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return -1;
    }

    private void generateServerCertificate() {
        org.apache.ambari.server.security.CertificateManager.LOG.info("Generation of server certificate");
        java.util.Map<java.lang.String, java.lang.String> configsMap = configs.getConfigsMap();
        java.lang.String srvrKstrDir = configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey());
        java.lang.String srvrCrtName = configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_NAME.getKey());
        java.lang.String srvrCsrName = configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_CSR_NAME.getKey());
        java.lang.String srvrKeyName = configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_KEY_NAME.getKey());
        java.lang.String kstrName = configsMap.get(org.apache.ambari.server.configuration.Configuration.KSTR_NAME.getKey());
        java.lang.String srvrCrtPass = configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS.getKey());
        java.lang.String srvrCrtPassFile = configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS_FILE.getKey());
        java.lang.Object[] scriptArgs = new java.lang.Object[]{ srvrCrtPass, srvrKstrDir, srvrKeyName, srvrCrtName, kstrName, srvrCsrName };
        java.lang.String command = java.text.MessageFormat.format(org.apache.ambari.server.security.CertificateManager.GEN_SRVR_KEY, scriptArgs);
        runCommand(command);
        command = java.text.MessageFormat.format(org.apache.ambari.server.security.CertificateManager.GEN_SRVR_REQ, scriptArgs);
        runCommand(command);
        command = java.text.MessageFormat.format(org.apache.ambari.server.security.CertificateManager.SIGN_SRVR_CRT, scriptArgs);
        runCommand(command);
        command = java.text.MessageFormat.format(org.apache.ambari.server.security.CertificateManager.EXPRT_KSTR, scriptArgs);
        runCommand(command);
        command = java.lang.String.format(org.apache.ambari.server.security.CertificateManager.SET_PERMISSIONS, srvrKstrDir);
        runCommand(command);
        command = java.lang.String.format(org.apache.ambari.server.security.CertificateManager.SET_SERVER_PASS_FILE_PERMISSIONS, (srvrKstrDir + java.io.File.separator) + srvrCrtPassFile);
        runCommand(command);
    }

    public java.lang.String getCACertificateChainContent() {
        java.lang.String serverCertDir = configs.getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR);
        java.lang.String serverCertChainName = configs.getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_CHAIN_NAME);
        java.io.File certChainFile = new java.io.File(serverCertDir, serverCertChainName);
        if (certChainFile.exists()) {
            try {
                return new java.lang.String(java.nio.file.Files.readAllBytes(certChainFile.toPath()));
            } catch (java.io.IOException e) {
                org.apache.ambari.server.security.CertificateManager.LOG.error(e.getMessage());
            }
        }
        java.lang.String serverCertName = configs.getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_NAME);
        java.io.File certFile = new java.io.File(serverCertDir, serverCertName);
        if (certFile.canRead()) {
            try {
                return new java.lang.String(java.nio.file.Files.readAllBytes(certFile.toPath()));
            } catch (java.io.IOException e) {
                org.apache.ambari.server.security.CertificateManager.LOG.error(e.getMessage());
            }
        }
        return null;
    }

    public synchronized org.apache.ambari.server.security.SignCertResponse signAgentCrt(java.lang.String agentHostname, java.lang.String agentCrtReqContent, java.lang.String passphraseAgent) {
        org.apache.ambari.server.security.SignCertResponse response = new org.apache.ambari.server.security.SignCertResponse();
        org.apache.ambari.server.security.CertificateManager.LOG.info("Signing agent certificate");
        agentHostname = org.apache.commons.lang.StringUtils.trim(agentHostname);
        if (org.apache.commons.lang.StringUtils.isEmpty(agentHostname)) {
            org.apache.ambari.server.security.CertificateManager.LOG.warn("The agent hostname is missing");
            response.setResult(org.apache.ambari.server.security.SignCertResponse.ERROR_STATUS);
            response.setMessage("The agent hostname is missing");
            return response;
        }
        if (configs.validateAgentHostnames()) {
            org.apache.ambari.server.security.CertificateManager.LOG.info("Validating agent hostname: {}", agentHostname);
            if (!org.apache.ambari.server.utils.HostUtils.isValidHostname(agentHostname)) {
                org.apache.ambari.server.security.CertificateManager.LOG.warn("The agent hostname is not a valid hostname");
                response.setResult(org.apache.ambari.server.security.SignCertResponse.ERROR_STATUS);
                response.setMessage("The agent hostname is not a valid hostname");
                return response;
            }
        } else {
            org.apache.ambari.server.security.CertificateManager.LOG.info("Skipping validation of agent hostname: {}", agentHostname);
        }
        org.apache.ambari.server.security.CertificateManager.LOG.info("Verifying passphrase");
        java.lang.String passphraseSrvr = configs.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.PASSPHRASE.getKey()).trim();
        if (!passphraseSrvr.equals(passphraseAgent.trim())) {
            org.apache.ambari.server.security.CertificateManager.LOG.warn("Incorrect passphrase from the agent");
            response.setResult(org.apache.ambari.server.security.SignCertResponse.ERROR_STATUS);
            response.setMessage("Incorrect passphrase from the agent");
            return response;
        }
        java.util.Map<java.lang.String, java.lang.String> configsMap = configs.getConfigsMap();
        java.lang.String srvrKstrDir = configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey());
        java.lang.String srvrCrtPass = configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS.getKey());
        java.lang.String srvrCrtName = configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_NAME.getKey());
        java.lang.String srvrKeyName = configsMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_KEY_NAME.getKey());
        java.lang.String agentCrtReqName = agentHostname + ".csr";
        java.lang.String agentCrtName = agentHostname + ".crt";
        java.lang.Object[] scriptArgs = new java.lang.Object[]{ srvrKstrDir, agentCrtReqName, agentCrtName, srvrCrtPass, srvrKeyName, srvrCrtName };
        java.io.File agentCrtFile = new java.io.File((srvrKstrDir + java.io.File.separator) + agentCrtName);
        if (agentCrtFile.exists()) {
            org.apache.ambari.server.security.CertificateManager.LOG.info(("Revoking of " + agentHostname) + " certificate.");
            java.lang.String command = java.text.MessageFormat.format(org.apache.ambari.server.security.CertificateManager.REVOKE_AGENT_CRT, scriptArgs);
            int commandExitCode = runCommand(command);
            if (commandExitCode != 0) {
                response.setResult(org.apache.ambari.server.security.SignCertResponse.ERROR_STATUS);
                response.setMessage(org.apache.ambari.server.utils.ShellCommandUtil.getOpenSslCommandResult(command, commandExitCode));
                return response;
            }
        }
        java.io.File agentCrtReqFile = new java.io.File((srvrKstrDir + java.io.File.separator) + agentCrtReqName);
        try {
            org.apache.commons.io.FileUtils.writeStringToFile(agentCrtReqFile, agentCrtReqContent, java.nio.charset.Charset.defaultCharset());
        } catch (java.io.IOException e1) {
            e1.printStackTrace();
        }
        java.lang.String command = java.text.MessageFormat.format(org.apache.ambari.server.security.CertificateManager.SIGN_AGENT_CRT, scriptArgs);
        org.apache.ambari.server.security.CertificateManager.LOG.debug(org.apache.ambari.server.utils.ShellCommandUtil.hideOpenSslPassword(command));
        int commandExitCode = runCommand(command);
        if (commandExitCode != 0) {
            response.setResult(org.apache.ambari.server.security.SignCertResponse.ERROR_STATUS);
            response.setMessage(org.apache.ambari.server.utils.ShellCommandUtil.getOpenSslCommandResult(command, commandExitCode));
            return response;
        }
        java.lang.String agentCrtContent = "";
        try {
            agentCrtContent = org.apache.commons.io.FileUtils.readFileToString(agentCrtFile, java.nio.charset.Charset.defaultCharset());
        } catch (java.io.IOException e) {
            e.printStackTrace();
            org.apache.ambari.server.security.CertificateManager.LOG.error("Error reading signed agent certificate");
            response.setResult(org.apache.ambari.server.security.SignCertResponse.ERROR_STATUS);
            response.setMessage("Error reading signed agent certificate");
            return response;
        }
        response.setResult(org.apache.ambari.server.security.SignCertResponse.OK_STATUS);
        response.setSignedCa(agentCrtContent);
        return response;
    }
}