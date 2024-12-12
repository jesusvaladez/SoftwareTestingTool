package org.apache.ambari.server.agent;
public class AgentEnv {
    private org.apache.ambari.server.agent.AgentEnv.Directory[] stackFoldersAndFiles = new org.apache.ambari.server.agent.AgentEnv.Directory[0];

    private org.apache.ambari.server.agent.AgentEnv.Alternative[] alternatives = new org.apache.ambari.server.agent.AgentEnv.Alternative[0];

    private org.apache.ambari.server.agent.AgentEnv.ExistingUser[] existingUsers = new org.apache.ambari.server.agent.AgentEnv.ExistingUser[0];

    private java.lang.String[] existingRepos = new java.lang.String[0];

    private org.apache.ambari.server.agent.AgentEnv.PackageDetail[] installedPackages = new org.apache.ambari.server.agent.AgentEnv.PackageDetail[0];

    private org.apache.ambari.server.agent.AgentEnv.HostHealth hostHealth = new org.apache.ambari.server.agent.AgentEnv.HostHealth();

    private java.lang.Integer umask;

    private java.lang.String transparentHugePage;

    private java.lang.Boolean firewallRunning;

    private java.lang.String firewallName;

    private java.lang.Boolean hasUnlimitedJcePolicy;

    private java.lang.Boolean reverseLookup;

    public java.lang.Boolean getReverseLookup() {
        return reverseLookup;
    }

    public void setReverseLookup(java.lang.Boolean reverseLookup) {
        this.reverseLookup = reverseLookup;
    }

    public java.lang.Integer getUmask() {
        return umask;
    }

    public void setUmask(java.lang.Integer umask) {
        this.umask = umask;
    }

    public java.lang.String getTransparentHugePage() {
        return transparentHugePage;
    }

    public void setTransparentHugePage(java.lang.String transparentHugePage) {
        this.transparentHugePage = transparentHugePage;
    }

    public org.apache.ambari.server.agent.AgentEnv.Directory[] getStackFoldersAndFiles() {
        return stackFoldersAndFiles;
    }

    public void setStackFoldersAndFiles(org.apache.ambari.server.agent.AgentEnv.Directory[] dirs) {
        stackFoldersAndFiles = dirs;
    }

    public void setExistingUsers(org.apache.ambari.server.agent.AgentEnv.ExistingUser[] users) {
        existingUsers = users;
    }

    public org.apache.ambari.server.agent.AgentEnv.ExistingUser[] getExistingUsers() {
        return existingUsers;
    }

    public void setAlternatives(org.apache.ambari.server.agent.AgentEnv.Alternative[] dirs) {
        alternatives = dirs;
    }

    public org.apache.ambari.server.agent.AgentEnv.Alternative[] getAlternatives() {
        return alternatives;
    }

    public void setExistingRepos(java.lang.String[] repos) {
        existingRepos = repos;
    }

    public java.lang.String[] getExistingRepos() {
        return existingRepos;
    }

    public void setInstalledPackages(org.apache.ambari.server.agent.AgentEnv.PackageDetail[] packages) {
        installedPackages = packages;
    }

    public org.apache.ambari.server.agent.AgentEnv.PackageDetail[] getInstalledPackages() {
        return installedPackages;
    }

    public void setHostHealth(org.apache.ambari.server.agent.AgentEnv.HostHealth healthReport) {
        hostHealth = healthReport;
    }

    public org.apache.ambari.server.agent.AgentEnv.HostHealth getHostHealth() {
        return hostHealth;
    }

    public java.lang.Boolean getFirewallRunning() {
        return firewallRunning;
    }

    public void setFirewallRunning(java.lang.Boolean firewallRunning) {
        this.firewallRunning = firewallRunning;
    }

    public java.lang.String getFirewallName() {
        return firewallName;
    }

    public void setFirewallName(java.lang.String firewallName) {
        this.firewallName = firewallName;
    }

    public java.lang.Boolean getHasUnlimitedJcePolicy() {
        return hasUnlimitedJcePolicy;
    }

    public static class HostHealth {
        @com.google.gson.annotations.SerializedName("activeJavaProcs")
        @com.fasterxml.jackson.annotation.JsonProperty("activeJavaProcs")
        private org.apache.ambari.server.agent.AgentEnv.JavaProc[] activeJavaProcs = new org.apache.ambari.server.agent.AgentEnv.JavaProc[0];

        @com.google.gson.annotations.SerializedName("agentTimeStampAtReporting")
        @com.fasterxml.jackson.annotation.JsonProperty("agentTimeStampAtReporting")
        private long agentTimeStampAtReporting = 0;

        @com.google.gson.annotations.SerializedName("serverTimeStampAtReporting")
        @com.fasterxml.jackson.annotation.JsonProperty("serverTimeStampAtReporting")
        private long serverTimeStampAtReporting = 0;

        @com.google.gson.annotations.SerializedName("liveServices")
        @com.fasterxml.jackson.annotation.JsonProperty("liveServices")
        private org.apache.ambari.server.agent.AgentEnv.LiveService[] liveServices = new org.apache.ambari.server.agent.AgentEnv.LiveService[0];

        public void setAgentTimeStampAtReporting(long currentTime) {
            agentTimeStampAtReporting = currentTime;
        }

        public long getAgentTimeStampAtReporting() {
            return agentTimeStampAtReporting;
        }

        public void setServerTimeStampAtReporting(long currentTime) {
            serverTimeStampAtReporting = currentTime;
        }

        public long getServerTimeStampAtReporting() {
            return serverTimeStampAtReporting;
        }

        public void setActiveJavaProcs(org.apache.ambari.server.agent.AgentEnv.JavaProc[] procs) {
            activeJavaProcs = procs;
        }

        public org.apache.ambari.server.agent.AgentEnv.JavaProc[] getActiveJavaProcs() {
            return activeJavaProcs;
        }

        public void setLiveServices(org.apache.ambari.server.agent.AgentEnv.LiveService[] services) {
            liveServices = services;
        }

        public org.apache.ambari.server.agent.AgentEnv.LiveService[] getLiveServices() {
            return liveServices;
        }
    }

    public static class PackageDetail {
        @com.google.gson.annotations.SerializedName("name")
        @com.fasterxml.jackson.annotation.JsonProperty("name")
        private java.lang.String pkgName;

        @com.google.gson.annotations.SerializedName("version")
        @com.fasterxml.jackson.annotation.JsonProperty("version")
        private java.lang.String pkgVersion;

        @com.google.gson.annotations.SerializedName("repoName")
        @com.fasterxml.jackson.annotation.JsonProperty("repoName")
        private java.lang.String pkgRepoName;

        public void setName(java.lang.String name) {
            pkgName = name;
        }

        public java.lang.String getName() {
            return pkgName;
        }

        public void setVersion(java.lang.String version) {
            pkgVersion = version;
        }

        public java.lang.String getVersion() {
            return pkgVersion;
        }

        public void setRepoName(java.lang.String repoName) {
            pkgRepoName = repoName;
        }

        public java.lang.String getRepoName() {
            return pkgRepoName;
        }
    }

    public static class Directory {
        @com.google.gson.annotations.SerializedName("name")
        @com.fasterxml.jackson.annotation.JsonProperty("name")
        private java.lang.String dirName;

        @com.google.gson.annotations.SerializedName("type")
        @com.fasterxml.jackson.annotation.JsonProperty("type")
        private java.lang.String dirType;

        public void setName(java.lang.String name) {
            dirName = name;
        }

        public java.lang.String getName() {
            return dirName;
        }

        public void setType(java.lang.String type) {
            dirType = type;
        }

        public java.lang.String getType() {
            return dirType;
        }
    }

    public static class JavaProc {
        @com.google.gson.annotations.SerializedName("user")
        @com.fasterxml.jackson.annotation.JsonProperty("user")
        private java.lang.String user;

        @com.google.gson.annotations.SerializedName("pid")
        @com.fasterxml.jackson.annotation.JsonProperty("pid")
        private int pid = 0;

        @com.google.gson.annotations.SerializedName("hadoop")
        @com.fasterxml.jackson.annotation.JsonProperty("hadoop")
        private boolean is_hadoop = false;

        @com.google.gson.annotations.SerializedName("command")
        @com.fasterxml.jackson.annotation.JsonProperty("command")
        private java.lang.String command;

        public void setUser(java.lang.String user) {
            this.user = user;
        }

        public java.lang.String getUser() {
            return user;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getPid() {
            return pid;
        }

        public void setHadoop(boolean hadoop) {
            is_hadoop = hadoop;
        }

        public boolean isHadoop() {
            return is_hadoop;
        }

        public void setCommand(java.lang.String cmd) {
            command = cmd;
        }

        public java.lang.String getCommand() {
            return command;
        }
    }

    public static class Alternative {
        @com.google.gson.annotations.SerializedName("name")
        @com.fasterxml.jackson.annotation.JsonProperty("name")
        private java.lang.String altName;

        @com.google.gson.annotations.SerializedName("target")
        @com.fasterxml.jackson.annotation.JsonProperty("target")
        private java.lang.String altTarget;

        public void setName(java.lang.String name) {
            altName = name;
        }

        public java.lang.String getName() {
            return altName;
        }

        public void setTarget(java.lang.String target) {
            altTarget = target;
        }

        public java.lang.String getTarget() {
            return altTarget;
        }
    }

    public static class LiveService {
        @com.google.gson.annotations.SerializedName("name")
        @com.fasterxml.jackson.annotation.JsonProperty("name")
        private java.lang.String svcName;

        @com.google.gson.annotations.SerializedName("status")
        @com.fasterxml.jackson.annotation.JsonProperty("status")
        private java.lang.String svcStatus;

        @com.google.gson.annotations.SerializedName("desc")
        @com.fasterxml.jackson.annotation.JsonProperty("desc")
        private java.lang.String svcDesc;

        public void setName(java.lang.String name) {
            svcName = name;
        }

        public java.lang.String getName() {
            return svcName;
        }

        public void setStatus(java.lang.String status) {
            svcStatus = status;
        }

        public java.lang.String getStatus() {
            return svcStatus;
        }

        public void setDesc(java.lang.String desc) {
            svcDesc = desc;
        }

        public java.lang.String getDesc() {
            return svcDesc;
        }
    }

    public static class ExistingUser {
        @com.google.gson.annotations.SerializedName("name")
        @com.fasterxml.jackson.annotation.JsonProperty("name")
        private java.lang.String name;

        @com.google.gson.annotations.SerializedName("homeDir")
        @com.fasterxml.jackson.annotation.JsonProperty("homeDir")
        private java.lang.String homeDir;

        @com.google.gson.annotations.SerializedName("status")
        @com.fasterxml.jackson.annotation.JsonProperty("status")
        private java.lang.String status;

        public void setUserName(java.lang.String userName) {
            name = userName;
        }

        public java.lang.String getUserName() {
            return name;
        }

        public void setUserHomeDir(java.lang.String userHomeDir) {
            homeDir = userHomeDir;
        }

        public java.lang.String getUserHomeDir() {
            return homeDir;
        }

        public void setUserStatus(java.lang.String userStatus) {
            status = userStatus;
        }

        public java.lang.String getUserStatus() {
            return status;
        }
    }
}